package com.github.pcimcioch.protobuf.io;

import com.github.pcimcioch.protobuf.io.exception.InputEndedException;
import com.github.pcimcioch.protobuf.io.exception.LimitExceededException;
import com.github.pcimcioch.protobuf.io.exception.MalformedVarintException;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static java.nio.charset.StandardCharsets.UTF_8;

// TODO [performance] Reading varints can be faster if 10 bytes (max varint) are already in the buffer. No loops are needed and no calls to ensureAvailable()
abstract class ProtobufInput {
    private static final VarHandle LONG = MethodHandles.byteArrayViewVarHandle(long[].class, LITTLE_ENDIAN);
    private static final VarHandle INT = MethodHandles.byteArrayViewVarHandle(int[].class, LITTLE_ENDIAN);
    private static final VarHandle DOUBLE = MethodHandles.byteArrayViewVarHandle(double[].class, LITTLE_ENDIAN);
    private static final VarHandle FLOAT = MethodHandles.byteArrayViewVarHandle(float[].class, LITTLE_ENDIAN);

    protected final byte[] buffer;
    protected int currentPosition;
    protected int endPosition;

    protected ProtobufInput(byte[] buffer, int endPosition) {
        this.buffer = buffer;
        this.currentPosition = 0;
        this.endPosition = endPosition;
    }

    static ProtobufInput from(InputStream input, int bufferSize) {
        return new StreamProtobufInput(input, bufferSize);
    }

    static ProtobufInput from(byte[] bytes) {
        return new ArrayProtobufInput(bytes);
    }

    int readFixedInt() throws IOException {
        ensureAvailable(4);
        int value = (int) INT.get(buffer, currentPosition);
        currentPosition += 4;
        return value;
    }

    long readFixedLong() throws IOException {
        ensureAvailable(8);
        long value = (long) LONG.get(buffer, currentPosition);
        currentPosition += 8;
        return value;
    }

    double readDouble() throws IOException {
        ensureAvailable(8);
        double value = (double) DOUBLE.get(buffer, currentPosition);
        currentPosition += 8;
        return value;
    }

    float readFloat() throws IOException {
        ensureAvailable(4);
        float value = (float) FLOAT.get(buffer, currentPosition);
        currentPosition += 4;
        return value;
    }

    boolean readBoolean() throws IOException {
        return readVarint32() != 0;
    }

    int readVarint32() throws IOException {
        return (int) readVarint64();
    }

    long readVarint64() throws IOException {
        long result = 0L;

        for (int shift = 0; shift < 64; shift += 7) {
            ensureAvailable(1);
            byte b = buffer[currentPosition++];

            result |= (b & 0b01111111L) << shift;
            if ((b & 0b10000000) == 0) {
                return result;
            }
        }

        throw new MalformedVarintException();
    }

    int readZigZag32() throws IOException {
        int encoded = readVarint32();
        return (encoded >>> 1) ^ -(encoded & 1);
    }

    long readZigZag64() throws IOException {
        long encoded = readVarint64();
        return (encoded >>> 1) ^ -(encoded & 1);
    }

    byte[] readBytes() throws IOException {
        return readRawBytes(readVarint32());
    }

    String readString() throws IOException {
        return readRawString(readVarint32());
    }

    abstract long setLimit(long limit) throws IOException;

    abstract void skip(long size) throws IOException;

    abstract boolean isEnded() throws IOException;

    protected int available() {
        return endPosition - currentPosition;
    }

    protected byte readRawByte() throws IOException {
        ensureAvailable(1);
        return buffer[currentPosition++];
    }

    protected abstract String readRawString(int size) throws IOException;

    protected abstract byte[] readRawBytes(int size) throws IOException;

    protected abstract void ensureAvailable(int size) throws IOException;

    private static final class ArrayProtobufInput extends ProtobufInput {
        private ArrayProtobufInput(byte[] data) {
            super(data, data.length);
        }

        @Override
        long setLimit(long limit) throws LimitExceededException {
            long oldLimit = available();
            if (limit + currentPosition > buffer.length) {
                throw new LimitExceededException();
            }
            endPosition = currentPosition + (int) limit;
            return oldLimit;
        }

        @Override
        void skip(long size) throws IOException {
            ensureAvailable((int) size);
            currentPosition += size;
        }

        @Override
        boolean isEnded() {
            return available() == 0;
        }

        @Override
        protected byte[] readRawBytes(int size) throws IOException {
            ensureAvailable(size);

            byte[] data = new byte[size];
            System.arraycopy(buffer, currentPosition, data, 0, size);
            currentPosition += size;

            return data;
        }

        @Override
        protected String readRawString(int size) throws IOException {
            ensureAvailable(size);

            String result = new String(buffer, currentPosition, size, UTF_8);
            currentPosition += size;

            return result;
        }

        @Override
        protected void ensureAvailable(int size) throws IOException {
            if (currentPosition + size > buffer.length) {
                throw new InputEndedException();
            }
            if (available() < size) {
                throw new LimitExceededException();
            }
        }
    }

    private static final class StreamProtobufInput extends ProtobufInput {
        private final InputStream input;
        private boolean inputEnded;
        private long limit;

        private StreamProtobufInput(InputStream input, int bufferSize) {
            super(new byte[bufferSize], 0);

            this.input = input;
            this.inputEnded = false;
            this.limit = Long.MAX_VALUE;
        }

        @Override
        long setLimit(long limit) {
            long oldLimit = this.limit;
            this.limit = limit;
            return oldLimit;
        }

        @Override
        void skip(long size) throws IOException {
            consumeLimit(size);

            int available = available();
            if (size <= available) {
                currentPosition += size;
                return;
            }

            try {
                input.skipNBytes(size - available);
            } catch (EOFException ex) {
                throw new InputEndedException();
            }

            currentPosition = 0;
            endPosition = 0;
        }

        @Override
        boolean isEnded() throws IOException {
            return limit == 0 || (available() == 0 && (inputEnded || fillBuffer() == 0));
        }

        @Override
        protected byte[] readRawBytes(int size) throws IOException {
            consumeLimit(size);
            return getBytes(size);
        }

        @Override
        protected String readRawString(int size) throws IOException {
            consumeLimit(size);

            if (available() >= size) {
                String result = new String(buffer, currentPosition, size, UTF_8);
                currentPosition += size;
                return result;
            }

            return new String(getBytes(size), UTF_8);
        }

        @Override
        protected void ensureAvailable(int size) throws IOException {
            consumeLimit(size);
            if (available() >= size) {
                return;
            }
            if (fillBuffer() < size) {
                throw new InputEndedException();
            }
        }

        private void consumeLimit(long size) throws LimitExceededException {
            if (size > limit) {
                throw new LimitExceededException();
            }

            limit -= size;
        }

        private int fillBuffer() throws IOException {
            int currentSize = available();
            int toRead = buffer.length - currentSize;

            // Move existing bytes to the beginning of the buffer
            System.arraycopy(buffer, currentPosition, buffer, 0, currentSize);
            currentPosition = 0;
            endPosition = currentSize;

            // Read data blocks until buffered is filled or nothing more in the input
            while (toRead > 0) {
                int read = input.read(buffer, currentSize, toRead);
                if (read < 0) {
                    inputEnded = true;
                    break;
                }

                toRead -= read;
                endPosition += read;
            }

            return endPosition;
        }

        private byte[] getBytes(int size) throws IOException {
            byte[] result = new byte[size];
            int resultPosition = 0;
            int available = available();

            while (size > 0) {
                if (size <= available) {
                    System.arraycopy(buffer, currentPosition, result, resultPosition, size);
                    currentPosition += size;
                    return result;
                }

                if (inputEnded) {
                    throw new InputEndedException();
                }

                System.arraycopy(buffer, currentPosition, result, resultPosition, available);
                currentPosition += available;
                resultPosition += available;
                size -= available;

                available = fillBuffer();
            }

            return result;
        }
    }
}