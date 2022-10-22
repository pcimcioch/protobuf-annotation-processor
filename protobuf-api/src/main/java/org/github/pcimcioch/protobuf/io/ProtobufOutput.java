package org.github.pcimcioch.protobuf.io;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ProtobufOutput {
    private static final long PAYLOAD_MASK = 0b01111111;
    private static final int CONTINUATION_MASK = 0b10000000;

    private final DataOutputStream output;

    public ProtobufOutput(OutputStream output) {
        this.output = new DataOutputStream(output);
    }

    public void writeFixedInt(int value) throws IOException {
        output.writeInt(value);
    }

    public void writeFixedLong(long value) throws IOException {
        output.writeLong(value);
    }

    public void writeDouble(double value) throws IOException {
        output.writeDouble(value);
    }

    public void writeFloat(float value) throws IOException {
        output.writeFloat(value);
    }

    public void writeBoolean(boolean value) throws IOException {
        output.writeBoolean(value);
    }

    public void writeString(String value) throws IOException {
        writeBytes(value.getBytes(StandardCharsets.UTF_8));
    }

    public void writeBytes(byte[] value) throws IOException {
        writeVarint(value.length);
        output.write(value);
    }

    public void writeZigZag(long value) throws IOException {
        if (value < 0) {
            writeVarint(((-value - 1) << 1) + 1);
        } else {
            writeVarint(value << 1);
        }
    }

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
