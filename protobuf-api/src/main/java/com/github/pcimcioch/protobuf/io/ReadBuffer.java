package com.github.pcimcioch.protobuf.io;

import com.github.pcimcioch.protobuf.io.exception.InputEndedException;
import com.github.pcimcioch.protobuf.io.exception.LimitExceededException;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import static java.nio.charset.StandardCharsets.UTF_8;

final class ReadBuffer {
    private final InputStream input;
    private long limit;
    private boolean inputEnded;

    private final byte[] buffer;
    private int currentPosition;
    private int endPosition;

    private ReadBuffer(InputStream input, int bufferSize, long limit) {
        this.input = input;
        this.limit = limit;
        this.inputEnded = false;

        this.buffer = new byte[bufferSize];
        this.currentPosition = 0;
        this.endPosition = 0;
    }

    static ReadBuffer from(InputStream input, int bufferSize) {
        return new ReadBuffer(input, bufferSize, Long.MAX_VALUE);
    }

    static ReadBuffer from(byte[] bytes, int bufferSize) {
        // TODO [performance] We could have dedicated implementation of ReadBuffer that operates on bytes directly
        return new ReadBuffer(new ByteArrayInputStream(bytes), bufferSize, Long.MAX_VALUE);
    }

    long setLimit(long limit) {
        long oldLimit = this.limit;
        this.limit = limit;
        return oldLimit;
    }

    void ensureAvailable(int size) throws IOException {
        assertLimit(size);
        limit -= size;
        if (areAvailable(size)) {
            return;
        }

        if (fillBuffer() < size) {
            throw new InputEndedException();
        }
    }

    byte read() {
        return buffer[currentPosition++];
    }

    byte[] read(int size) throws IOException {
        assertLimit(size);
        limit -= size;

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

    String readString(int size) throws IOException {
        assertLimit(size);

        if (areAvailable(size)) {
            String result = new String(buffer, currentPosition, size, UTF_8);
            limit -= size;
            currentPosition += size;
            return result;
        }

        return new String(read(size), UTF_8);
    }

    void skip(long size) throws IOException {
        assertLimit(size);
        limit -= size;

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

    private int available() {
        return endPosition - currentPosition;
    }

    private boolean areAvailable(int size) {
        return currentPosition + size <= endPosition;
    }

    private void assertLimit(long size) throws LimitExceededException {
        if (size > limit) {
            throw new LimitExceededException();
        }
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
