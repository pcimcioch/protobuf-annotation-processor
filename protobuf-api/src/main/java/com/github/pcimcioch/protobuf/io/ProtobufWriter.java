package com.github.pcimcioch.protobuf.io;

import com.github.pcimcioch.protobuf.dto.ByteArray;
import com.github.pcimcioch.protobuf.dto.ProtobufMessage;

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
    public void _double(int number, double value) throws IOException {
        if (value != 0d) {
            Tag tag = new Tag(number, I64);
            output.writeVarint(tag.value());
            output.writeDouble(value);
        }
    }

    /**
     * Writes float
     *
     * @param number field number
     * @param value  value to write
     * @throws IOException in case of any data write error
     */
    public void _float(int number, float value) throws IOException {
        if (value != 0f) {
            Tag tag = new Tag(number, I32);
            output.writeVarint(tag.value());
            output.writeFloat(value);
        }
    }

    /**
     * Writes int32
     *
     * @param number field number
     * @param value  value to write
     * @throws IOException in case of any data write error
     */
    public void int32(int number, int value) throws IOException {
        if (value != 0) {
            Tag tag = new Tag(number, VARINT);
            output.writeVarint(tag.value());
            output.writeVarint(value);
        }
    }

    /**
     * Writes int64
     *
     * @param number field number
     * @param value  value to write
     * @throws IOException in case of any data write error
     */
    public void int64(int number, long value) throws IOException {
        if (value != 0L) {
            Tag tag = new Tag(number, VARINT);
            output.writeVarint(tag.value());
            output.writeVarint(value);
        }
    }

    /**
     * Writes uint32
     *
     * @param number field number
     * @param value  value to write
     * @throws IOException in case of any data write error
     */
    public void uint32(int number, int value) throws IOException {
        if (value != 0) {
            Tag tag = new Tag(number, VARINT);
            output.writeVarint(tag.value());
            output.writeVarint(value);
        }
    }

    /**
     * Writes uint64
     *
     * @param number field number
     * @param value  value to write
     * @throws IOException in case of any data write error
     */
    public void uint64(int number, long value) throws IOException {
        if (value != 0L) {
            Tag tag = new Tag(number, VARINT);
            output.writeVarint(tag.value());
            output.writeVarint(value);
        }
    }

    /**
     * Writes sint32
     *
     * @param number field number
     * @param value  value to write
     * @throws IOException in case of any data write error
     */
    public void sint32(int number, int value) throws IOException {
        if (value != 0) {
            Tag tag = new Tag(number, VARINT);
            output.writeVarint(tag.value());
            output.writeZigZag(value);
        }
    }

    /**
     * Writes sint64
     *
     * @param number field number
     * @param value  value to write
     * @throws IOException in case of any data write error
     */
    public void sint64(int number, long value) throws IOException {
        if (value != 0L) {
            Tag tag = new Tag(number, VARINT);
            output.writeVarint(tag.value());
            output.writeZigZag(value);
        }
    }

    /**
     * Writes fixed32
     *
     * @param number field number
     * @param value  value to write
     * @throws IOException in case of any data write error
     */
    public void fixed32(int number, int value) throws IOException {
        if (value != 0) {
            Tag tag = new Tag(number, I32);
            output.writeVarint(tag.value());
            output.writeFixedInt(value);
        }
    }

    /**
     * Writes fixed64
     *
     * @param number field number
     * @param value  value to write
     * @throws IOException in case of any data write error
     */
    public void fixed64(int number, long value) throws IOException {
        if (value != 0L) {
            Tag tag = new Tag(number, I64);
            output.writeVarint(tag.value());
            output.writeFixedLong(value);
        }
    }

    /**
     * Writes sfixed32
     *
     * @param number field number
     * @param value  value to write
     * @throws IOException in case of any data write error
     */
    public void sfixed32(int number, int value) throws IOException {
        if (value != 0) {
            Tag tag = new Tag(number, I32);
            output.writeVarint(tag.value());
            output.writeFixedInt(value);
        }
    }

    /**
     * Writes sfixed64
     *
     * @param number field number
     * @param value  value to write
     * @throws IOException in case of any data write error
     */
    public void sfixed64(int number, long value) throws IOException {
        if (value != 0L) {
            Tag tag = new Tag(number, I64);
            output.writeVarint(tag.value());
            output.writeFixedLong(value);
        }
    }

    /**
     * Writes bool
     *
     * @param number field number
     * @param value  value to write
     * @throws IOException in case of any data write error
     */
    public void bool(int number, boolean value) throws IOException {
        if (value) {
            Tag tag = new Tag(number, VARINT);
            output.writeVarint(tag.value());
            output.writeBoolean(true);
        }
    }

    /**
     * Writes string
     *
     * @param number field number
     * @param value  value to write
     * @throws IOException in case of any data write error
     */
    public void string(int number, String value) throws IOException {
        if (!"".equals(value)) {
            Tag tag = new Tag(number, LEN);
            output.writeVarint(tag.value());
            output.writeString(value);
        }
    }

    /**
     * Writes bytes
     *
     * @param number field number
     * @param value  value to write
     * @throws IOException in case of any data write error
     */
    public void bytes(int number, ByteArray value) throws IOException {
        if (!value.isEmpty()) {
            Tag tag = new Tag(number, LEN);
            output.writeVarint(tag.value());
            output.writeBytes(value.data());
        }
    }

    /**
     * Write message
     *
     * @param number  field number
     * @param message message to write
     * @throws IOException in case of any data write error
     */
    public void message(int number, ProtobufMessage message) throws IOException {
        if (message != null) {
            Tag tag = new Tag(number, LEN);
            output.writeVarint(tag.value());
            output.writeBytes(message.toByteArray()); //TODO this is very inefficient. But it works
        }
    }
}
