package com.github.pcimcioch.protobuf.io;

import com.github.pcimcioch.protobuf.dto.ByteArray;
import com.github.pcimcioch.protobuf.io.exception.UnknownWireTypeException;
import com.github.pcimcioch.protobuf.io.exception.UnsupportedWireTypeException;

import java.io.IOException;

import static com.github.pcimcioch.protobuf.io.WireType.I32;
import static com.github.pcimcioch.protobuf.io.WireType.I64;
import static com.github.pcimcioch.protobuf.io.WireType.LEN;
import static com.github.pcimcioch.protobuf.io.WireType.VARINT;

/**
 * Unknown field
 */
public sealed interface UnknownField {

    /**
     * Write to the output
     *
     * @param output output
     * @throws IOException exception
     */
    void writeTo(ProtobufOutput output) throws IOException;

    /**
     * Size in protobuf format
     *
     * @return size
     */
    int protobufSize();

    /**
     * Skips given tag
     *
     * @param tag   tag to skip
     * @param input input
     * @throws IOException in case of any read errors
     */
    static void skip(int tag, ProtobufInput input) throws IOException {
        try {
            switch (WireType.fromTag(tag)) {
                case VARINT -> input.readVarint64();
                case I64 -> input.skip(8);
                case LEN -> input.skip(input.readVarint32());
                case SGROUP -> throw new UnsupportedWireTypeException("SGROUP");
                case EGROUP -> throw new UnsupportedWireTypeException("EGROUP");
                case I32 -> input.skip(4);
            }
        } catch (IllegalArgumentException ex) {
            throw new UnknownWireTypeException();
        }
    }

    /**
     * Reads unknown field
     *
     * @param tag   tag
     * @param input input
     * @return unknown type field
     * @throws IOException in case of any read errors
     */
    @SuppressWarnings("deprecation")
    static UnknownField read(int tag, ProtobufInput input) throws IOException {
        try {
            return switch (WireType.fromTag(tag)) {
                case VARINT -> new VarintField(WireType.numberFrom(tag), input.readVarint64());
                case I64 -> new I64Field(WireType.numberFrom(tag), input.readFixedLong());
                case LEN -> new BytesField(WireType.numberFrom(tag), ByteArray.unsafeFromByteArray(input.readBytes()));
                case SGROUP -> throw new UnsupportedWireTypeException("SGROUP");
                case EGROUP -> throw new UnsupportedWireTypeException("EGROUP");
                case I32 -> new I32Field(WireType.numberFrom(tag), input.readFixedInt());
            };
        } catch (IllegalArgumentException ex) {
            throw new UnknownWireTypeException();
        }
    }

    /**
     * Unknown I32 field
     *
     * @param number number
     * @param value  value
     */
    record I32Field(int number, int value) implements UnknownField {

        @Override
        public void writeTo(ProtobufOutput output) throws IOException {
            output.writeVarint32(I32.tagFrom(number));
            output.writeFixedInt(value);
        }

        @Override
        public int protobufSize() {
            return Size.tagSize(number) + 4;
        }
    }

    /**
     * Unknown I64 field
     *
     * @param number number
     * @param value  value
     */
    record I64Field(int number, long value) implements UnknownField {

        @Override
        public void writeTo(ProtobufOutput output) throws IOException {
            output.writeVarint32(I64.tagFrom(number));
            output.writeFixedLong(value);
        }

        @Override
        public int protobufSize() {
            return Size.tagSize(number) + 8;
        }
    }

    /**
     * Unknown Varint field
     *
     * @param number number
     * @param value  value
     */
    record VarintField(int number, long value) implements UnknownField {

        @Override
        public void writeTo(ProtobufOutput output) throws IOException {
            output.writeVarint32(VARINT.tagFrom(number));
            output.writeVarint64(value);
        }

        @Override
        public int protobufSize() {
            return Size.tagSize(number) + Size.varint64Size(value);
        }
    }

    /**
     * Unknown bytes field
     *
     * @param number number
     * @param value  value
     */
    record BytesField(int number, ByteArray value) implements UnknownField {

        @Override
        @SuppressWarnings("deprecation")
        public void writeTo(ProtobufOutput output) throws IOException {
            output.writeVarint32(LEN.tagFrom(number));
            output.writeBytes(value.internalData());
        }

        @Override
        public int protobufSize() {
            return Size.tagSize(number) + Size.varint32Size(value.length()) + value.length();
        }
    }
}
