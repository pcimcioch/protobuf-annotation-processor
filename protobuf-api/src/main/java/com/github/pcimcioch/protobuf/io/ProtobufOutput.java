package com.github.pcimcioch.protobuf.io;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Writes protobuf types to the raw data stream
 */
public class ProtobufOutput {
    private static final long PAYLOAD_MASK = 0b01111111;
    private static final int CONTINUATION_MASK = 0b10000000;

    private final DataOutputStream output;

    /**
     * Constructor. Given output stream will not be closed by this class in any way
     *
     * @param output output stream to save data to
     */
    public ProtobufOutput(OutputStream output) {
        this.output = new DataOutputStream(output);
    }

    /**
     * Writes 4 bytes to the stream
     *
     * @param value int value
     * @throws IOException in case of any data write error
     */
    public void writeFixedInt(int value) throws IOException {
        output.writeInt(value);
    }

    /**
     * Writes 8 bytes to the stream
     *
     * @param value long value
     * @throws IOException in case of any data write error
     */
    public void writeFixedLong(long value) throws IOException {
        output.writeLong(value);
    }

    /**
     * Writes 8 bytes to the stream
     *
     * @param value double value
     * @throws IOException in case of any data write error
     */
    public void writeDouble(double value) throws IOException {
        output.writeDouble(value);
    }

    /**
     * Writes 4 bytes to the stream
     *
     * @param value float value
     * @throws IOException in case of any data write error
     */
    public void writeFloat(float value) throws IOException {
        output.writeFloat(value);
    }

    /**
     * Writes one byte to the stream
     *
     * @param value boolean value
     * @throws IOException in case of any data write error
     */
    public void writeBoolean(boolean value) throws IOException {
        output.writeBoolean(value);
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
        output.write(value);
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
            output.writeByte(toWrite);
        } while (value != 0);
    }
}
