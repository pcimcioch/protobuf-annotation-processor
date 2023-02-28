package com.github.pcimcioch.protobuf.io;

import com.github.pcimcioch.protobuf.io.exception.InputEndedException;
import com.github.pcimcioch.protobuf.io.exception.LimitExceededException;
import com.github.pcimcioch.protobuf.io.exception.MalformedVarintException;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import static java.lang.Double.longBitsToDouble;
import static java.lang.Float.intBitsToFloat;
import static java.nio.charset.StandardCharsets.UTF_8;

@SuppressWarnings("PointlessBitwiseExpression")
class ProtobufInput {
    private final InputStream input;
    private boolean inputEnded;

    private final byte[] buffer;
    private int currentPosition;
    private int endPosition;
    private long limit;

    private ProtobufInput(InputStream input, int bufferSize) {
        this.input = input;
        this.inputEnded = false;

        this.buffer = new byte[bufferSize];
        this.currentPosition = 0;
        this.endPosition = 0;
        this.limit = Long.MAX_VALUE;
    }

    static ProtobufInput from(InputStream input, int bufferSize) {
        return new ProtobufInput(input, bufferSize);
    }

    static ProtobufInput from(byte[] bytes) {
        // TODO [performance] We could have dedicated implementation of ReadBuffer that operates on bytes directly
        return new ProtobufInput(new ByteArrayInputStream(bytes), 4096);
    }

    long setLimit(long limit) {
        long oldLimit = this.limit;
        this.limit = limit;
        return oldLimit;
    }

    int readFixedInt() throws IOException {
        ensureAvailable(4);

        byte ch4 = buffer[currentPosition++];
        byte ch3 = buffer[currentPosition++];
        byte ch2 = buffer[currentPosition++];
        byte ch1 = buffer[currentPosition++];

        return (((ch1 & 0xff) << 24) |
                ((ch2 & 0xff) << 16) |
                ((ch3 & 0xff) << 8) |
                ((ch4 & 0xff) << 0));
    }

    long readFixedLong() throws IOException {
        ensureAvailable(8);

        byte ch8 = buffer[currentPosition++];
        byte ch7 = buffer[currentPosition++];
        byte ch6 = buffer[currentPosition++];
        byte ch5 = buffer[currentPosition++];
        byte ch4 = buffer[currentPosition++];
        byte ch3 = buffer[currentPosition++];
        byte ch2 = buffer[currentPosition++];
        byte ch1 = buffer[currentPosition++];

        return (((ch1 & 0xffL) << 56) |
                ((ch2 & 0xffL) << 48) |
                ((ch3 & 0xffL) << 40) |
                ((ch4 & 0xffL) << 32) |
                ((ch5 & 0xffL) << 24) |
                ((ch6 & 0xffL) << 16) |
                ((ch7 & 0xffL) << 8) |
                ((ch8 & 0xffL) << 0));
    }

    double readDouble() throws IOException {
        return longBitsToDouble(readFixedLong());
    }

    float readFloat() throws IOException {
        return intBitsToFloat(readFixedInt());
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
        return readBytes(readVarint32());
    }

    String readString() throws IOException {
        return readString(readVarint32());
    }

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

    byte readByte() throws IOException {
        ensureAvailable(1);
        return buffer[currentPosition++];
    }

    byte[] readBytes(int size) throws IOException {
        consumeLimit(size);
        return getBytes(size);
    }

    String readString(int size) throws IOException {
        consumeLimit(size);

        if (available() >= size) {
            String result = new String(buffer, currentPosition, size, UTF_8);
            currentPosition += size;
            return result;
        }

        return new String(getBytes(size), UTF_8);
    }

    boolean isEnded() throws IOException {
        return limit == 0 || (available() == 0 && (inputEnded || fillBuffer() == 0));
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

    private void ensureAvailable(int size) throws IOException {
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

    private int available() {
        return endPosition - currentPosition;
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
}
