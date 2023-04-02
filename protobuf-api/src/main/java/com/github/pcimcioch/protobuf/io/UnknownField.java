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
public abstract sealed class UnknownField {
    final int number;

    UnknownField(int number) {
        this.number = number;
    }

    abstract void writeTo(ProtobufOutput output) throws IOException;

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
     */
    public static final class I32Field extends UnknownField {
        private final int value;

        /**
         * Constructor
         *
         * @param number field number
         * @param value  value
         */
        public I32Field(int number, int value) {
            super(number);
            this.value = value;
        }

        /**
         * Returns value
         *
         * @return value
         */
        public int value() {
            return value;
        }

        @Override
        void writeTo(ProtobufOutput output) throws IOException {
            output.writeVarint32(I32.tagFrom(number));
            output.writeFixedInt(value);
        }
    }

    /**
     * Unknown I64 field
     */
    public static final class I64Field extends UnknownField {
        private final long value;

        /**
         * Constructor
         *
         * @param number field number
         * @param value  value
         */
        public I64Field(int number, long value) {
            super(number);
            this.value = value;
        }

        /**
         * Returns value
         *
         * @return value
         */
        public long value() {
            return value;
        }

        @Override
        void writeTo(ProtobufOutput output) throws IOException {
            output.writeVarint32(I64.tagFrom(number));
            output.writeFixedLong(value);
        }
    }

    /**
     * Unknown Varint field
     */
    public static final class VarintField extends UnknownField {
        private final long value;

        /**
         * Constructor
         *
         * @param number field number
         * @param value  value
         */
        public VarintField(int number, long value) {
            super(number);
            this.value = value;
        }

        /**
         * Returns value
         *
         * @return value
         */
        public long value() {
            return value;
        }

        @Override
        void writeTo(ProtobufOutput output) throws IOException {
            output.writeVarint32(VARINT.tagFrom(number));
            output.writeVarint64(value);
        }
    }

    /**
     * Unknown bytes field
     */
    public static final class BytesField extends UnknownField {
        private final ByteArray value;

        /**
         * Constructor
         *
         * @param number field number
         * @param value  value
         */
        public BytesField(int number, ByteArray value) {
            super(number);
            this.value = value;
        }

        /**
         * Returns value
         *
         * @return value
         */
        public ByteArray value() {
            return value;
        }

        @Override
        @SuppressWarnings("deprecation")
        void writeTo(ProtobufOutput output) throws IOException {
            output.writeVarint32(LEN.tagFrom(number));
            output.writeBytes(value.internalData());
        }
    }
}
