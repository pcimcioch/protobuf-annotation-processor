package com.github.pcimcioch.protobuf.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Writes protobuf types to the raw data stream
 */
@SuppressWarnings("PointlessBitwiseExpression")
// TODO [performance] Replace with ProtoOutput similar to ProtoInput + ReadBuffer
public class ProtobufOutput {
    private static final long LONG_PAYLOAD_MASK = 0b01111111;
    private static final int CONTINUATION_MASK = 0b10000000;

    private final OutputStream out;

    /**
     * Constructor. Given output stream will not be closed by this class in any way
     *
     * @param out output stream to save data to
     */
    public ProtobufOutput(OutputStream out) {
        this.out = out;
    }

    /**
     * Writes 4 bytes to the stream
     *
     * @param value int value
     * @throws IOException in case of any data write error
     */
    public void writeFixedInt(int value) throws IOException {
        writeInt(value);
    }

    /**
     * Writes 8 bytes to the stream
     *
     * @param value long value
     * @throws IOException in case of any data write error
     */
    public void writeFixedLong(long value) throws IOException {
        writeLong(value);
    }

    /**
     * Writes 8 bytes to the stream
     *
     * @param value double value
     * @throws IOException in case of any data write error
     */
    public void writeDouble(double value) throws IOException {
        writeLong(Double.doubleToLongBits(value));
    }

    /**
     * Writes 4 bytes to the stream
     *
     * @param value float value
     * @throws IOException in case of any data write error
     */
    public void writeFloat(float value) throws IOException {
        writeInt(Float.floatToIntBits(value));
    }

    /**
     * Writes one byte to the stream
     *
     * @param value boolean value
     * @throws IOException in case of any data write error
     */
    public void writeBoolean(boolean value) throws IOException {
        out.write(value ? 1 : 0);
    }

    /**
     * Writes undefined number of bytes to the stream
     *
     * @param value String value
     * @throws IOException in case of any data write error
     */
    public void writeString(String value) throws IOException {
        writeBytes(value.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Writes undefined number of bytes to the stream
     *
     * @param value bytes array
     * @throws IOException in case of any data write error
     */
    public void writeBytes(byte[] value) throws IOException {
        writeVarint32(value.length);

        for (byte b : value) {
            out.write(b);
        }
    }

    /**
     * Writes undefined number of bytes to encode given signed integer in a zig-zag format
     *
     * @param value long value
     * @throws IOException in case of any data write error
     */
    public void writeZigZag64(long value) throws IOException {
        if (value < 0) {
            writeVarint64(((-value - 1) << 1) + 1);
        } else {
            writeVarint64(value << 1);
        }
    }

    /**
     * Writes undefined number of bytes to encode given signed integer in a zig-zag format
     *
     * @param value int value
     * @throws IOException in case of any data write error
     */
    public void writeZigZag32(int value) throws IOException {
        writeZigZag64(value);
    }

    /**
     * Writes undefined number of bytes to encode given integer in a variant integer format
     *
     * @param value long value
     * @throws IOException in case of any data write error
     */
    public void writeVarint64(long value) throws IOException {
        int toWrite;

        do {
            toWrite = (int) (value & LONG_PAYLOAD_MASK);
            value >>>= 7;
            if (value != 0) {
                toWrite |= CONTINUATION_MASK;
            }
            out.write(toWrite);
        } while (value != 0);
    }

    /**
     * Writes undefined number of bytes to encode given integer in a variant integer format
     *
     * @param value int value
     * @throws IOException in case of any data write error
     */
    public void writeVarint32(int value) throws IOException {
        writeVarint64(value);
    }

    private void writeInt(int value) throws IOException {
        out.write(value >>> 0);
        out.write(value >>> 8);
        out.write(value >>> 16);
        out.write(value >>> 24);
    }

    private void writeLong(long value) throws IOException {
        out.write((int) (value >>> 0));
        out.write((int) (value >>> 8));
        out.write((int) (value >>> 16));
        out.write((int) (value >>> 24));
        out.write((int) (value >>> 32));
        out.write((int) (value >>> 40));
        out.write((int) (value >>> 48));
        out.write((int) (value >>> 56));
    }
}
