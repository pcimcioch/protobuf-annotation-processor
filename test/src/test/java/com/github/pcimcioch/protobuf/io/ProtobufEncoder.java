package com.github.pcimcioch.protobuf.io;

import com.github.pcimcioch.protobuf.dto.BooleanList;
import com.github.pcimcioch.protobuf.dto.DoubleList;
import com.github.pcimcioch.protobuf.dto.FloatList;
import com.github.pcimcioch.protobuf.dto.IntList;
import com.github.pcimcioch.protobuf.dto.LongList;
import com.github.pcimcioch.protobuf.dto.ProtobufMessage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.github.pcimcioch.protobuf.io.WireType.I32;
import static com.github.pcimcioch.protobuf.io.WireType.I64;
import static com.github.pcimcioch.protobuf.io.WireType.LEN;
import static com.github.pcimcioch.protobuf.io.WireType.VARINT;

public class ProtobufEncoder {
    private final ByteArrayOutputStream stream;
    private final ProtobufOutput output;

    public ProtobufEncoder() {
        this.stream = new ByteArrayOutputStream();
        this.output = ProtobufOutput.from(stream, 4096);
    }

    public byte[] data() throws IOException {
        output.close();
        return stream.toByteArray();
    }

    public ProtobufEncoder writeDouble(int number, double value) throws IOException {
        output.writeVarint32(I64.tagFrom(number));
        output.writeDouble(value);

        return this;
    }

    public ProtobufEncoder writeDoublePacked(int number, double... values) throws IOException {
        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.ofDoublePacked(DoubleList.of(values)));
        for (double value : values) {
            output.writeDouble(value);
        }

        return this;
    }

    public ProtobufEncoder writeFloat(int number, float value) throws IOException {
        output.writeVarint32(I32.tagFrom(number));
        output.writeFloat(value);

        return this;
    }

    public ProtobufEncoder writeFloatPacked(int number, float... values) throws IOException {
        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.ofFloatPacked(FloatList.of(values)));
        for (float value : values) {
            output.writeFloat(value);
        }

        return this;
    }

    public ProtobufEncoder writeInt32(int number, int value) throws IOException {
        output.writeVarint32(VARINT.tagFrom(number));
        output.writeVarint32(value);

        return this;
    }

    public ProtobufEncoder writeInt32Packed(int number, int... values) throws IOException {
        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.ofInt32Packed(IntList.of(values)));
        for (int value : values) {
            output.writeVarint32(value);
        }

        return this;
    }

    public ProtobufEncoder writeInt64(int number, long value) throws IOException {
        output.writeVarint32(VARINT.tagFrom(number));
        output.writeVarint64(value);

        return this;
    }

    public ProtobufEncoder writeInt64Packed(int number, long... values) throws IOException {
        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.ofInt64Packed(LongList.of(values)));
        for (long value : values) {
            output.writeVarint64(value);
        }

        return this;
    }

    public ProtobufEncoder writeUint32(int number, int value) throws IOException {
        output.writeVarint32(VARINT.tagFrom(number));
        output.writeVarint32(value);

        return this;
    }

    public ProtobufEncoder writeUint32Packed(int number, int... values) throws IOException {
        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.ofUint32Packed(IntList.of(values)));
        for (int value : values) {
            output.writeVarint32(value);
        }

        return this;
    }

    public ProtobufEncoder writeUint64(int number, long value) throws IOException {
        output.writeVarint32(VARINT.tagFrom(number));
        output.writeVarint64(value);

        return this;
    }

    public ProtobufEncoder writeUint64Packed(int number, long... values) throws IOException {
        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.ofUint64Packed(LongList.of(values)));
        for (long value : values) {
            output.writeVarint64(value);
        }

        return this;
    }

    public ProtobufEncoder writeSint32(int number, int value) throws IOException {
        output.writeVarint32(VARINT.tagFrom(number));
        output.writeZigZag32(value);

        return this;
    }

    public ProtobufEncoder writeSint32Packed(int number, int... values) throws IOException {
        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.ofSint32Packed(IntList.of(values)));
        for (int value : values) {
            output.writeZigZag32(value);
        }

        return this;
    }

    public ProtobufEncoder writeSint64(int number, long value) throws IOException {
        output.writeVarint32(VARINT.tagFrom(number));
        output.writeZigZag64(value);

        return this;
    }

    public ProtobufEncoder writeSint64Packed(int number, long... values) throws IOException {
        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.ofSint64Packed(LongList.of(values)));
        for (long value : values) {
            output.writeZigZag64(value);
        }

        return this;
    }

    public ProtobufEncoder writeFixed32(int number, int value) throws IOException {
        output.writeVarint32(I32.tagFrom(number));
        output.writeFixedInt(value);

        return this;
    }

    public ProtobufEncoder writeFixed32Packed(int number, int... values) throws IOException {
        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.ofFixed32Packed(IntList.of(values)));
        for (int value : values) {
            output.writeFixedInt(value);
        }

        return this;
    }

    public ProtobufEncoder writeFixed64(int number, long value) throws IOException {
        output.writeVarint32(I64.tagFrom(number));
        output.writeFixedLong(value);

        return this;
    }

    public ProtobufEncoder writeFixed64Packed(int number, long... values) throws IOException {
        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.ofFixed64Packed(LongList.of(values)));
        for (long value : values) {
            output.writeFixedLong(value);
        }

        return this;
    }

    public ProtobufEncoder writeSfixed32(int number, int value) throws IOException {
        output.writeVarint32(I32.tagFrom(number));
        output.writeFixedInt(value);

        return this;
    }

    public ProtobufEncoder writeSfixed32Packed(int number, int... values) throws IOException {
        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.ofSfixed32Packed(IntList.of(values)));
        for (int value : values) {
            output.writeFixedInt(value);
        }

        return this;
    }

    public ProtobufEncoder writeSfixed64(int number, long value) throws IOException {
        output.writeVarint32(I64.tagFrom(number));
        output.writeFixedLong(value);

        return this;
    }

    public ProtobufEncoder writeSfixed64Packed(int number, long... values) throws IOException {
        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.ofSfixed64Packed(LongList.of(values)));
        for (long value : values) {
            output.writeFixedLong(value);
        }

        return this;
    }

    public ProtobufEncoder writeBool(int number, boolean value) throws IOException {
        output.writeVarint32(VARINT.tagFrom(number));
        output.writeBoolean(value);

        return this;
    }

    public ProtobufEncoder writeBoolPacked(int number, boolean... values) throws IOException {
        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.ofBoolPacked(BooleanList.of(values)));
        for (boolean value : values) {
            output.writeBoolean(value);
        }

        return this;
    }

    public ProtobufEncoder writeString(int number, String value) throws IOException {
        output.writeVarint32(LEN.tagFrom(number));
        output.writeString(value);

        return this;
    }

    public ProtobufEncoder writeBytes(int number, byte[] value) throws IOException {
        output.writeVarint32(LEN.tagFrom(number));
        output.writeBytes(value);

        return this;
    }

    public ProtobufEncoder writeMessage(int number, ProtobufMessage<?> value) throws IOException {
        return writeBytes(number, value.toByteArray());
    }
}
