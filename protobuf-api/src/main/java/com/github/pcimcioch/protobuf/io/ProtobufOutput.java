package com.github.pcimcioch.protobuf.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Writes protobuf types to the raw data stream
 */
@SuppressWarnings("PointlessBitwiseExpression")
public class ProtobufOutput {
    private static final long PAYLOAD_MASK = 0b01111111;
    private static final int CONTINUATION_MASK = 0b10000000;

    private final byte[] writeBuffer = new byte[8];
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
        writeVarint(value.length);
        write(value);
    }

    /**
     * Writes undefined number of bytes to encode given signed integer in a zig-zag format
     *
     * @param value long value
     * @throws IOException in case of any data write error
     */
    public void writeZigZag(long value) throws IOException {
        if (value < 0) {
            writeVarint(((-value - 1) << 1) + 1);
        } else {
            writeVarint(value << 1);
        }
    }

    /**
     * Writes undefined number of bytes to encode given integer in a variant integer format
     *
     * @param value long value
     * @throws IOException in case of any data write error
     */
    public void writeVarint(long value) throws IOException {
        int toWrite;

        do {
            toWrite = (int) (value & PAYLOAD_MASK);
            value >>>= 7;
            if (value != 0) {
                toWrite |= CONTINUATION_MASK;
            }
            writeByte(toWrite);
        } while (value != 0);
    }

    private void writeByte(int value) throws IOException {
        out.write(value);
    }

    private void writeInt(int value) throws IOException {
        writeBuffer[0] = (byte) (value >>> 0);
        writeBuffer[1] = (byte) (value >>> 8);
        writeBuffer[2] = (byte) (value >>> 16);
        writeBuffer[3] = (byte) (value >>> 24);
        out.write(writeBuffer, 0, 4);
    }

    private void writeLong(long value) throws IOException {
        writeBuffer[0] = (byte) (value >>> 0);
        writeBuffer[1] = (byte) (value >>> 8);
        writeBuffer[2] = (byte) (value >>> 16);
        writeBuffer[3] = (byte) (value >>> 24);
        writeBuffer[4] = (byte) (value >>> 32);
        writeBuffer[5] = (byte) (value >>> 40);
        writeBuffer[6] = (byte) (value >>> 48);
        writeBuffer[7] = (byte) (value >>> 56);
        out.write(writeBuffer, 0, 8);
    }

    private void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    private void write(byte[] b, int off, int len) throws IOException {
        if ((off | len | (b.length - (len + off)) | (off + len)) < 0)
            throw new IndexOutOfBoundsException();

        for (int i = 0 ; i < len ; i++) {
            write(b[off + i]);
        }
    }

    void write(int b) throws IOException {
        out.write(b);
    }
}
