package com.github.pcimcioch.protobuf.io;

import com.github.pcimcioch.protobuf.dto.ByteArray;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Reads protobuf types from raw data input.
 */
@SuppressWarnings("PointlessBitwiseExpression")
public class ProtobufInput {
    private static final byte PAYLOAD_MASK = 0b01111111;

    private final InputStream in;

    /**
     * Constructor. Given input stream will not be closed by this class in any way
     *
     * @param in input stream to read data from
     */
    public ProtobufInput(InputStream in) {
        this.in = in;
    }

    /**
     * Reads 4 bytes as an int
     *
     * @return int
     * @throws IOException in case of any data read error
     */
    public int readFixedInt() throws IOException {
        return readInt();
    }

    /**
     * Reads 8 bytes as a long
     *
     * @return long
     * @throws IOException in case of any data read error
     */
    public long readFixedLong() throws IOException {
        return readLong();
    }

    /**
     * Reads 8 bytes as a double
     *
     * @return double
     * @throws IOException in case of any data read error
     */
    public double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }

    /**
     * Reads 4 bytes as a float
     *
     * @return float
     * @throws IOException in case of any data read error
     */
    public float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    /**
     * Reads one byte as a boolean
     *
     * @return boolean
     * @throws IOException in case of any data read error
     */
    public boolean readBoolean() throws IOException {
        int ch = in.read();
        if (ch < 0) {
            throw new EOFException();
        }
        return (ch != 0);
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
        int length = readVarint32();
        byte[] bytes = new byte[length];

        for (int i = 0; i < length; i++) {
            bytes[i] = readByte();
        }

        return bytes;
    }

    /**
     * Reads undefined number of bytes as a {@link ByteArray}
     *
     * @return byte array
     * @throws IOException in case of any data read error
     */
    public ByteArray readByteArray() throws IOException {
        int length = readVarint32();
        ByteArray.Builder builder = ByteArray.builder(length);

        for (int i = 0; i < length; i++) {
            builder.set(i, readByte());
        }

        return builder.build();
    }

    /**
     * Reads undefined number of bytes as a signed integer from zig-zag format
     *
     * @return long
     * @throws IOException in case of any data read error
     */
    public long readZigZag() throws IOException {
        long encoded = readVarint64();
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
            read = readByte();
            significantBytes = read & PAYLOAD_MASK;
            result |= significantBytes << shift;
            shift += 7;
        } while (read < 0);

        return result;
    }

    /**
     * Reads undefined number of bytes as a variant length integer
     *
     * @return long
     * @throws IOException in case of any data read error
     */
    public long readVarint64() throws IOException {
        long result = 0L;
        byte read;
        long significantBytes;
        int shift = 0;

        do {
            read = readByte();
            significantBytes = read & PAYLOAD_MASK;
            result |= significantBytes << shift;
            shift += 7;
        } while (read < 0);

        return result;
    }

    /**
     * Reads undefined number of bytes as a variant length integer
     *
     * @return int
     * @throws IOException in case of any data read error
     */
    public int readVarint32() throws IOException {
        return (int) readVarint64();
    }

    /**
     * Skips given number of bytes
     *
     * @param bytes number of bytes to skip
     * @throws IOException in case of any data read error
     */
    public void skip(long bytes) throws IOException {
        for (long i = 0; i < bytes; i++) {
            readByte();
        }
    }

    private byte readByte() throws IOException {
        int ch = in.read();
        if (ch < 0) {
            throw new EOFException();
        }
        return (byte) (ch);
    }

    private int readInt() throws IOException {
        int ch4 = in.read();
        int ch3 = in.read();
        int ch2 = in.read();
        int ch1 = in.read();

        if ((ch1 | ch2 | ch3 | ch4) < 0) {
            throw new EOFException();
        }

        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
    }

    private long readLong() throws IOException {
        int ch8 = in.read();
        int ch7 = in.read();
        int ch6 = in.read();
        int ch5 = in.read();
        int ch4 = in.read();
        int ch3 = in.read();
        int ch2 = in.read();
        int ch1 = in.read();

        if ((ch1 | ch2 | ch3 | ch4 | ch5 | ch6 | ch7 | ch8) < 0) {
            throw new EOFException();
        }

        return (((long) ch1 << 56) +
                ((long) (ch2 & 255) << 48) +
                ((long) (ch3 & 255) << 40) +
                ((long) (ch4 & 255) << 32) +
                ((long) (ch5 & 255) << 24) +
                ((ch6 & 255) << 16) +
                ((ch7 & 255) << 8) +
                ((ch8 & 255) << 0));
    }
}
