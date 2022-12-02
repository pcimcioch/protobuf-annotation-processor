package com.github.pcimcioch.protobuf.io;

import com.github.pcimcioch.protobuf.dto.ByteArray;

import java.io.IOException;
import java.io.OutputStream;

// TODO this should be used by the message record
public class ProtobufWriter {

    private final ProtobufOutput output;

    public ProtobufWriter(OutputStream outputStream) {
        this.output = new ProtobufOutput(outputStream);
    }

    public void _double(long number, double value) throws IOException {
        long tag = number << 3 | WireType.I64.id();

        output.writeVarint(tag);
        output.writeDouble(value);
    }

    public void _float(long number, float value) throws IOException {
        long tag = number << 3 | WireType.I32.id();

        output.writeVarint(tag);
        output.writeFloat(value);
    }

    public void int32(long number, int value) throws IOException {
        long tag = number << 3 | WireType.VARINT.id();

        output.writeVarint(tag);
        output.writeVarint(value);
    }

    public void int64(long number, long value) throws IOException {
        long tag = number << 3 | WireType.VARINT.id();

        output.writeVarint(tag);
        output.writeVarint(value);
    }

    public void uint32(long number, int value) throws IOException {
        long tag = number << 3 | WireType.VARINT.id();

        output.writeVarint(tag);
        output.writeVarint(value);
    }

    public void uint64(long number, long value) throws IOException {
        long tag = number << 3 | WireType.VARINT.id();

        output.writeVarint(tag);
        output.writeVarint(value);
    }

    public void sint32(long number, int value) throws IOException {
        long tag = number << 3 | WireType.VARINT.id();

        output.writeVarint(tag);
        output.writeZigZag(value);
    }

    public void sint64(long number, long value) throws IOException {
        long tag = number << 3 | WireType.VARINT.id();

        output.writeVarint(tag);
        output.writeZigZag(value);
    }

    public void fixed32(long number, int value) throws IOException {
        long tag = number << 3 | WireType.I32.id();

        output.writeVarint(tag);
        output.writeFixedInt(value);
    }

    public void fixed64(long number, long value) throws IOException {
        long tag = number << 3 | WireType.I64.id();

        output.writeVarint(tag);
        output.writeFixedLong(value);
    }

    public void sfixed32(long number, int value) throws IOException {
        long tag = number << 3 | WireType.I32.id();

        output.writeVarint(tag);
        output.writeFixedInt(value);
    }

    public void sfixed64(long number, long value) throws IOException {
        long tag = number << 3 | WireType.I64.id();

        output.writeVarint(tag);
        output.writeFixedLong(value);
    }

    public void bool(long number, boolean value) throws IOException {
        long tag = number << 3 | WireType.VARINT.id();

        output.writeVarint(tag);
        output.writeBoolean(value);
    }

    public void string(long number, String value) throws IOException {
        long tag = number << 3 | WireType.LEN.id();

        output.writeVarint(tag);
        output.writeString(value);
    }

    public void bytes(long number, ByteArray value) throws IOException {
        long tag = number << 3 | WireType.LEN.id();

        output.writeVarint(tag);
        output.writeBytes(value.data());
    }
}
