package com.github.pcimcioch.protobuf.io;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Reads protobuf types from raw data input.
 */
public class ProtobufInput {
    private static final byte PAYLOAD_MASK = 0b01111111;

    private final DataInputStream input;

    /**
     * Constructor. Given input stream will not be closed by this class in any way
     *
     * @param input input stream to read data from
     */
    public ProtobufInput(InputStream input) {
        this.input = new DataInputStream(input);
    }

    /**
     * Reads 4 bytes as an int
     *
     * @return int
     * @throws IOException in case of any data read error
     */
    public int readFixedInt() throws IOException {
        return input.readInt();
    }

    /**
     * Reads 8 bytes as a long
     *
     * @return long
     * @throws IOException in case of any data read error
     */
    public long readFixedLong() throws IOException {
        return input.readLong();
    }

    /**
     * Reads 8 bytes as a double
     *
     * @return double
     * @throws IOException in case of any data read error
     */
    public double readDouble() throws IOException {
        return input.readDouble();
    }

    /**
     * Reads 4 bytes as a float
     *
     * @return float
     * @throws IOException in case of any data read error
     */
    public float readFloat() throws IOException {
        return input.readFloat();
    }

    /**
     * Reads one byte as a boolean
     *
     * @return boolean
     * @throws IOException in case of any data read error
     */
    public boolean readBoolean() throws IOException {
        return input.readBoolean();
    }

    /**
     * Reads undefined number of bytes as a String
     *
     * @return String
     * @throws IOException in case of any data read error
     */
    public String readString() throws IOException {
        return new String(readBytes(), StandardCharsets.UTF_8);
    }

    /**
     * Reads undefined number of bytes as a byte array
     *
     * @return byte array
     * @throws IOException in case of any data read error
     */
    public byte[] readBytes() throws IOException {
        int length = (int) readVarint();
        byte[] bytes = new byte[length];
        input.readFully(bytes);
        return bytes;
    }

    /**
     * Reads undefined number of bytes as a signed integer from zig-zag format
     *
     * @return long
     * @throws IOException in case of any data read error
     */
    public long readZigZag() throws IOException {
        long encoded = readVarint();
        if ((encoded & 1) > 0) {
            return -(encoded >>> 1) - 1;
        } else {
            return encoded >>> 1;
        }
    }

    /**
     * Reads undefined number of bytes as a variant length integer
     *
     * @return long
     * @throws IOException in case of any data read error
     */
    public long readVarint() throws IOException {
        long result = 0L;
        byte read;
        long significantBytes;
        int shift = 0;

        do {
            read = input.readByte();
            significantBytes = read & PAYLOAD_MASK;
            result |= significantBytes << shift;
            shift += 7;
        } while (read < 0);

        return result;
    }

    /**
     * Skips given number of bytes
     *
     * @param bytes number of bytes to skip
     * @throws IOException in case of any data read error
     */
    public void skip(long bytes) throws IOException {
        for (long i = 0; i < bytes; i++) {
            input.readByte();
        }
    }
}
