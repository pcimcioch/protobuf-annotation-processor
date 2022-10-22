package org.github.pcimcioch.protobuf.io;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ProtobufInput {
    private static final byte PAYLOAD_MASK = 0b01111111;

    private final DataInputStream input;

    public ProtobufInput(InputStream input) {
        this.input = new DataInputStream(input);
    }

    public int readFixedInt() throws IOException {
        return input.readInt();
    }

    public long readFixedLong() throws IOException {
        return input.readLong();
    }

    public double readDouble() throws IOException {
        return input.readDouble();
    }

    public float readFloat() throws IOException {
        return input.readFloat();
    }

    public boolean readBoolean() throws IOException {
        return input.readBoolean();
    }

    public String readString() throws IOException {
        return new String(readBytes(), StandardCharsets.UTF_8);
    }

    public byte[] readBytes() throws IOException {
        int length = (int) readVarint();
        byte[] bytes = new byte[length];
        input.readFully(bytes);
        return bytes;
    }

    public long readZigZag() throws IOException {
        long encoded = readVarint();
        if ((encoded & 1) > 0) {
            return -(encoded >>> 1) - 1;
        } else {
            return encoded >>> 1;
        }
    }

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
}
