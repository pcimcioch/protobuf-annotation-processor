package com.github.pcimcioch.protobuf.io;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static java.nio.charset.StandardCharsets.UTF_8;

abstract class ProtobufOutput implements AutoCloseable {
    private static final VarHandle LONG = MethodHandles.byteArrayViewVarHandle(long[].class, LITTLE_ENDIAN);
    private static final VarHandle INT = MethodHandles.byteArrayViewVarHandle(int[].class, LITTLE_ENDIAN);
    private static final VarHandle DOUBLE = MethodHandles.byteArrayViewVarHandle(double[].class, LITTLE_ENDIAN);
    private static final VarHandle FLOAT = MethodHandles.byteArrayViewVarHandle(float[].class, LITTLE_ENDIAN);

    protected final byte[] buffer;
    protected int currentPosition;

    protected ProtobufOutput(byte[] buffer) {
        this.buffer = buffer;
        this.currentPosition = 0;
    }

    static ProtobufOutput from(OutputStream outputStream, int bufferSize) {
        return new StreamProtobufOutput(outputStream, bufferSize);
    }

    static ProtobufOutput from(byte[] data) {
        return new ArrayProtobufOutput(data);
    }

    void writeFixedInt(int value) throws IOException {
        ensureAvailable(4);
        INT.set(buffer, currentPosition, value);
        currentPosition += 4;
    }

    void writeFixedLong(long value) throws IOException {
        ensureAvailable(8);
        LONG.set(buffer, currentPosition, value);
        currentPosition += 8;
    }

    void writeDouble(double value) throws IOException {
        ensureAvailable(8);
        DOUBLE.set(buffer, currentPosition, value);
        currentPosition += 8;
    }

    void writeFloat(float value) throws IOException {
        ensureAvailable(4);
        FLOAT.set(buffer, currentPosition, value);
        currentPosition += 4;
    }

    void writeBoolean(boolean value) throws IOException {
        ensureAvailable(1);
        buffer[currentPosition++] = value ? (byte) 1 : (byte) 0;
    }

    void writeString(String value) throws IOException {
        writeBytes(value.getBytes(UTF_8));
    }

    void writeBytes(byte[] value) throws IOException {
        writeVarint32(value.length);
        writeRawBytes(value);
    }

    void writeZigZag32(int value) throws IOException {
        writeVarint32((value << 1) ^ (value >> 31));
    }

    void writeZigZag64(long value) throws IOException {
        writeVarint64((value << 1) ^ (value >> 63));
    }

    void writeVarint32(int value) throws IOException {
        if (value < 0) {
            writeVarint64(value);
            return;
        }

        ensureAvailable(5);
        while (true) {
            if ((value & ~0x7F) == 0) {
                buffer[currentPosition++] = (byte) value;
                return;
            } else {
                buffer[currentPosition++] = (byte) ((value & 0x7F) | 0x80);
                value >>>= 7;
            }
        }
    }

    void writeVarint64(long value) throws IOException {
        ensureAvailable(10);
        while (true) {
            if ((value & ~0x7FL) == 0) {
                buffer[currentPosition++] = (byte) value;
                return;
            } else {
                buffer[currentPosition++] = (byte) (((int) value & 0x7F) | 0x80);
                value >>>= 7;
            }
        }
    }

    void writeRawByte(byte value) throws IOException {
        ensureAvailable(1);
        buffer[currentPosition++] = value;
    }

    protected int available() {
        return buffer.length - currentPosition;
    }

    protected abstract void ensureAvailable(int size) throws IOException;

    protected abstract void writeRawBytes(byte[] value) throws IOException;

    @Override
    public abstract void close() throws IOException;

    private static final class ArrayProtobufOutput extends ProtobufOutput {
        private ArrayProtobufOutput(byte[] data) {
            super(data);
        }

        @Override
        public void close() {
            // Do Nothing
        }

        @Override
        protected void ensureAvailable(int size) {
            // Do Nothing
        }

        @Override
        protected void writeRawBytes(byte[] value) {
            System.arraycopy(value, 0, buffer, currentPosition, value.length);
            currentPosition += value.length;
        }
    }

    private static final class StreamProtobufOutput extends ProtobufOutput {
        private final OutputStream output;

        private StreamProtobufOutput(OutputStream output, int bufferSize) {
            super(new byte[bufferSize]);
            this.output = output;
        }

        @Override
        public void close() throws IOException {
            flush();
        }

        @Override
        protected void ensureAvailable(int size) throws IOException {
            if (available() < size) {
                flush();
            }
        }

        @Override
        protected void writeRawBytes(byte[] value) throws IOException {
            if (available() >= value.length) {
                System.arraycopy(value, 0, buffer, currentPosition, value.length);
                currentPosition += value.length;
                return;
            }

            int srcPosition = 0;
            while (true) {
                flush();

                int toWrite = value.length - srcPosition;
                if (available() >= toWrite) {
                    System.arraycopy(value, srcPosition, buffer, currentPosition, toWrite);
                    currentPosition += toWrite;
                    return;
                }

                System.arraycopy(value, srcPosition, buffer, 0, buffer.length);
                currentPosition = buffer.length;
                srcPosition += buffer.length;
            }
        }

        private void flush() throws IOException {
            output.write(buffer, 0, currentPosition);
            currentPosition = 0;
        }
    }
}
