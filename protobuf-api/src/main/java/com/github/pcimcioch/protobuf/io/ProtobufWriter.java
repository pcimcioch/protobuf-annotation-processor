package com.github.pcimcioch.protobuf.io;

import com.github.pcimcioch.protobuf.dto.ByteArray;

import java.io.IOException;
import java.io.OutputStream;

import static com.github.pcimcioch.protobuf.io.WireType.I32;
import static com.github.pcimcioch.protobuf.io.WireType.I64;
import static com.github.pcimcioch.protobuf.io.WireType.LEN;
import static com.github.pcimcioch.protobuf.io.WireType.VARINT;

/**
 * Writes protobuf data
 */
public class ProtobufWriter {

    private final ProtobufOutput output;

    /**
     * Constructor. Given output stream will not be closed by this class in any way
     *
     * @param outputStream output stream to save data to
     */
    public ProtobufWriter(OutputStream outputStream) {
        this.output = new ProtobufOutput(outputStream);
    }

    /**
     * Writes double
     *
     * @param number field number
     * @param value  value to write
     * @throws IOException in case of any data write error
     */
    public void _double(long number, double value) throws IOException {
        Tag tag = new Tag(number, I64);
        output.writeVarint(tag.value());
        output.writeDouble(value);
    }

    /**
     * Writes float
     *
     * @param number field number
     * @param value  value to write
     * @throws IOException in case of any data write error
     */
    public void _float(long number, float value) throws IOException {
        Tag tag = new Tag(number, I32);
        output.writeVarint(tag.value());
        output.writeFloat(value);
    }

    /**
     * Writes int32
     *
     * @param number field number
     * @param value  value to write
     * @throws IOException in case of any data write error
     */
    public void int32(long number, int value) throws IOException {
        Tag tag = new Tag(number, VARINT);
        output.writeVarint(tag.value());
        output.writeVarint(value);
    }

    /**
     * Writes int64
     *
     * @param number field number
     * @param value  value to write
     * @throws IOException in case of any data write error
     */
    public void int64(long number, long value) throws IOException {
        Tag tag = new Tag(number, VARINT);
        output.writeVarint(tag.value());
        output.writeVarint(value);
    }

    /**
     * Writes uint32
     *
     * @param number field number
     * @param value  value to write
     * @throws IOException in case of any data write error
     */
    public void uint32(long number, int value) throws IOException {
        Tag tag = new Tag(number, VARINT);
        output.writeVarint(tag.value());
        output.writeVarint(value);
    }

    /**
     * Writes uint64
     *
     * @param number field number
     * @param value  value to write
     * @throws IOException in case of any data write error
     */
    public void uint64(long number, long value) throws IOException {
        Tag tag = new Tag(number, VARINT);
        output.writeVarint(tag.value());
        output.writeVarint(value);
    }

    /**
     * Writes sint32
     *
     * @param number field number
     * @param value  value to write
     * @throws IOException in case of any data write error
     */
    public void sint32(long number, int value) throws IOException {
        Tag tag = new Tag(number, VARINT);
        output.writeVarint(tag.value());
        output.writeZigZag(value);
    }

    /**
     * Writes sint64
     *
     * @param number field number
     * @param value  value to write
     * @throws IOException in case of any data write error
     */
    public void sint64(long number, long value) throws IOException {
        Tag tag = new Tag(number, VARINT);
        output.writeVarint(tag.value());
        output.writeZigZag(value);
    }

    /**
     * Writes fixed32
     *
     * @param number field number
     * @param value  value to write
     * @throws IOException in case of any data write error
     */
    public void fixed32(long number, int value) throws IOException {
        Tag tag = new Tag(number, I32);
        output.writeVarint(tag.value());
        output.writeFixedInt(value);
    }

    /**
     * Writes fixed64
     *
     * @param number field number
     * @param value  value to write
     * @throws IOException in case of any data write error
     */
    public void fixed64(long number, long value) throws IOException {
        Tag tag = new Tag(number, I64);
        output.writeVarint(tag.value());
        output.writeFixedLong(value);
    }

    /**
     * Writes sfixed32
     *
     * @param number field number
     * @param value  value to write
     * @throws IOException in case of any data write error
     */
    public void sfixed32(long number, int value) throws IOException {
        Tag tag = new Tag(number, I32);
        output.writeVarint(tag.value());
        output.writeFixedInt(value);
    }

    /**
     * Writes sfixed64
     *
     * @param number field number
     * @param value  value to write
     * @throws IOException in case of any data write error
     */
    public void sfixed64(long number, long value) throws IOException {
        Tag tag = new Tag(number, I64);
        output.writeVarint(tag.value());
        output.writeFixedLong(value);
    }

    /**
     * Writes bool
     *
     * @param number field number
     * @param value  value to write
     * @throws IOException in case of any data write error
     */
    public void bool(long number, boolean value) throws IOException {
        Tag tag = new Tag(number, VARINT);
        output.writeVarint(tag.value());
        output.writeBoolean(value);
    }

    /**
     * Writes string
     *
     * @param number field number
     * @param value  value to write
     * @throws IOException in case of any data write error
     */
    public void string(long number, String value) throws IOException {
        Tag tag = new Tag(number, LEN);
        output.writeVarint(tag.value());
        output.writeString(value);
    }

    /**
     * Writes bytes
     *
     * @param number field number
     * @param value  value to write
     * @throws IOException in case of any data write error
     */
    public void bytes(long number, ByteArray value) throws IOException {
        Tag tag = new Tag(number, LEN);
        output.writeVarint(tag.value());
        output.writeBytes(value.data());
    }
}
