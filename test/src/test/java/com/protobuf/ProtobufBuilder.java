package com.protobuf;

import com.github.pcimcioch.protobuf.io.ProtobufOutput;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ProtobufBuilder {

    private final ByteArrayOutputStream data;
    private final ProtobufOutput output;

    private ProtobufBuilder() {
        this.data = new ByteArrayOutputStream();
        this.output = new ProtobufOutput(data);
    }

    public ProtobufBuilder _double(long number, double value) throws IOException {
        long tag = number << 3 | 1;

        output.writeVarint(tag);
        output.writeDouble(value);

        return this;
    }

    public ProtobufBuilder _float(long number, float value) throws IOException {
        long tag = number << 3 | 5;

        output.writeVarint(tag);
        output.writeFloat(value);

        return this;
    }

    public ProtobufBuilder int32(long number, int value) throws IOException {
        long tag = number << 3;

        output.writeVarint(tag);
        output.writeVarint(value);

        return this;
    }

    public ProtobufBuilder int64(long number, long value) throws IOException {
        long tag = number << 3;

        output.writeVarint(tag);
        output.writeVarint(value);

        return this;
    }

    public ProtobufBuilder uint32(long number, int value) throws IOException {
        long tag = number << 3;

        output.writeVarint(tag);
        output.writeVarint(value);

        return this;
    }

    public ProtobufBuilder uint64(long number, long value) throws IOException {
        long tag = number << 3;

        output.writeVarint(tag);
        output.writeVarint(value);

        return this;
    }

    public ProtobufBuilder sint32(long number, int value) throws IOException {
        long tag = number << 3;

        output.writeVarint(tag);
        output.writeZigZag(value);

        return this;
    }

    public ProtobufBuilder sint64(long number, long value) throws IOException {
        long tag = number << 3;

        output.writeVarint(tag);
        output.writeZigZag(value);

        return this;
    }

    public ProtobufBuilder fixed32(long number, int value) throws IOException {
        long tag = number << 3 | 5;

        output.writeVarint(tag);
        output.writeFixedInt(value);

        return this;
    }

    public ProtobufBuilder fixed64(long number, long value) throws IOException {
        long tag = number << 3 | 1;

        output.writeVarint(tag);
        output.writeFixedLong(value);

        return this;
    }

    public ProtobufBuilder sfixed32(long number, int value) throws IOException {
        long tag = number << 3 | 5;

        output.writeVarint(tag);
        output.writeFixedInt(value);

        return this;
    }

    public ProtobufBuilder sfixed64(long number, long value) throws IOException {
        long tag = number << 3 | 1;

        output.writeVarint(tag);
        output.writeFixedLong(value);

        return this;
    }

    public ProtobufBuilder bool(long number, boolean value) throws IOException {
        long tag = number << 3;

        output.writeVarint(tag);
        output.writeBoolean(value);

        return this;
    }

    public ProtobufBuilder string(long number, String value) throws IOException {
        long tag = number << 3 | 2;

        output.writeVarint(tag);
        output.writeString(value);

        return this;
    }

    public ProtobufBuilder bytes(long number, byte[] value) throws IOException {
        long tag = number << 3 | 2;

        output.writeVarint(tag);
        output.writeBytes(value);

        return this;
    }

    public byte[] data() {
        return data.toByteArray();
    }

    public static ProtobufBuilder proto() {
        return new ProtobufBuilder();
    }
}
