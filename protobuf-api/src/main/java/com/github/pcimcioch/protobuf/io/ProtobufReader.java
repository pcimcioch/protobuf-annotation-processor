package com.github.pcimcioch.protobuf.io;

import com.github.pcimcioch.protobuf.dto.ByteArray;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import static com.github.pcimcioch.protobuf.io.WireType.I32;
import static com.github.pcimcioch.protobuf.io.WireType.I64;
import static com.github.pcimcioch.protobuf.io.WireType.LEN;
import static com.github.pcimcioch.protobuf.io.WireType.VARINT;

/**
 * Reads protobuf data
 */
public class ProtobufReader {

    private final ProtobufInput input;

    /**
     * Constructor. Given input stream will not be closed by this class in any way
     *
     * @param inputStream input stream to read data from
     */
    public ProtobufReader(InputStream inputStream) {
        this.input = new ProtobufInput(inputStream);
    }

    /**
     * Reads tag
     *
     * @return tag or null if end of input reached
     * @throws IOException in case of any data read error
     */
    public Tag tag() throws IOException {
        try {
            return new Tag(input.readVarint());
        } catch (EOFException ex) {
            return null;
        }
    }

    /**
     * Reads double
     *
     * @param tag       tag
     * @param fieldName field name
     * @return double
     * @throws IOException in case of any data read error
     */
    public double double_(Tag tag, String fieldName) throws IOException {
        assertWireType(tag, fieldName, I64);
        return input.readDouble();
    }

    /**
     * Reads float
     *
     * @param tag       tag
     * @param fieldName field name
     * @return float
     * @throws IOException in case of any data read error
     */
    public float float_(Tag tag, String fieldName) throws IOException {
        assertWireType(tag, fieldName, I32);
        return input.readFloat();
    }

    /**
     * Reads int32
     *
     * @param tag       tag
     * @param fieldName field name
     * @return int32
     * @throws IOException in case of any data read error
     */
    public int int32(Tag tag, String fieldName) throws IOException {
        assertWireType(tag, fieldName, VARINT);
        return (int) input.readVarint();
    }

    /**
     * Reads int64
     *
     * @param tag       tag
     * @param fieldName field name
     * @return int64
     * @throws IOException in case of any data read error
     */
    public long int64(Tag tag, String fieldName) throws IOException {
        assertWireType(tag, fieldName, VARINT);
        return input.readVarint();
    }

    /**
     * Reads uint32
     *
     * @param tag       tag
     * @param fieldName field name
     * @return uint32
     * @throws IOException in case of any data read error
     */
    public int uint32(Tag tag, String fieldName) throws IOException {
        assertWireType(tag, fieldName, VARINT);
        return (int) input.readVarint();
    }

    /**
     * Reads uint64
     *
     * @param tag       tag
     * @param fieldName field name
     * @return uint64
     * @throws IOException in case of any data read error
     */
    public long uint64(Tag tag, String fieldName) throws IOException {
        assertWireType(tag, fieldName, VARINT);
        return input.readVarint();
    }

    /**
     * Reads sint32
     *
     * @param tag       tag
     * @param fieldName field name
     * @return sint32
     * @throws IOException in case of any data read error
     */
    public int sint32(Tag tag, String fieldName) throws IOException {
        assertWireType(tag, fieldName, VARINT);
        return (int) input.readZigZag();
    }

    /**
     * Reads sint64
     *
     * @param tag       tag
     * @param fieldName field name
     * @return sint64
     * @throws IOException in case of any data read error
     */
    public long sint64(Tag tag, String fieldName) throws IOException {
        assertWireType(tag, fieldName, VARINT);
        return input.readZigZag();
    }

    /**
     * Reads fixed32
     *
     * @param tag       tag
     * @param fieldName field name
     * @return fixed32
     * @throws IOException in case of any data read error
     */
    public int fixed32(Tag tag, String fieldName) throws IOException {
        assertWireType(tag, fieldName, I32);
        return input.readFixedInt();
    }

    /**
     * Reads fixed64
     *
     * @param tag       tag
     * @param fieldName field name
     * @return fixed64
     * @throws IOException in case of any data read error
     */
    public long fixed64(Tag tag, String fieldName) throws IOException {
        assertWireType(tag, fieldName, I64);
        return input.readFixedLong();
    }

    /**
     * Reads sfixed32
     *
     * @param tag       tag
     * @param fieldName field name
     * @return sfixed32
     * @throws IOException in case of any data read error
     */
    public int sfixed32(Tag tag, String fieldName) throws IOException {
        assertWireType(tag, fieldName, I32);
        return input.readFixedInt();
    }

    /**
     * Reads sfixed64
     *
     * @param tag       tag
     * @param fieldName field name
     * @return sfixed64
     * @throws IOException in case of any data read error
     */
    public long sfixed64(Tag tag, String fieldName) throws IOException {
        assertWireType(tag, fieldName, I64);
        return input.readFixedLong();
    }

    /**
     * Reads bool
     *
     * @param tag       tag
     * @param fieldName field name
     * @return bool
     * @throws IOException in case of any data read error
     */
    public boolean bool(Tag tag, String fieldName) throws IOException {
        assertWireType(tag, fieldName, VARINT);
        return input.readBoolean();
    }

    /**
     * Reads string
     *
     * @param tag       tag
     * @param fieldName field name
     * @return string
     * @throws IOException in case of any data read error
     */
    public String string(Tag tag, String fieldName) throws IOException {
        assertWireType(tag, fieldName, LEN);
        return input.readString();
    }

    /**
     * Reads bytes
     *
     * @param tag       tag
     * @param fieldName field name
     * @return bytes
     * @throws IOException in case of any data read error
     */
    public ByteArray bytes(Tag tag, String fieldName) throws IOException {
        assertWireType(tag, fieldName, LEN);
        return input.readByteArray();
    }

    /**
     * Reads message
     *
     * @param tag       tag
     * @param fieldName field name
     * @param factory   message from bytes factory
     * @param <T>       type of message
     * @return message
     * @throws IOException in case of any data read error
     */
    public <T> T message(Tag tag, String fieldName, MessageFactory<T> factory) throws IOException {
        assertWireType(tag, fieldName, LEN);
        return factory.parse(input.readBytes());
    }

    /**
     * Factory that creates message from byte array
     *
     * @param <T> type of the message
     */
    @FunctionalInterface
    public interface MessageFactory<T> {

        /**
         * Creates message from byte array
         *
         * @param data bytes
         * @return new message
         * @throws IOException in case of any data read error
         */
        T parse(byte[] data) throws IOException;
    }

    /**
     * Skips unknown value
     *
     * @param tag tag
     * @throws IOException in case of any data read error
     */
    public void skip(Tag tag) throws IOException {
        switch (tag.wireType()) {
            case VARINT -> input.readVarint();
            case I64 -> input.skip(8);
            case LEN -> input.skip(input.readVarint());
            case WireType.SGROUP -> throw new ProtobufParseException("Wire Type SGROUP is not supported");
            case WireType.EGROUP -> throw new ProtobufParseException("Wire Type EGROUP is not supported");
            case WireType.I32 -> input.skip(4);
            default -> throw new ProtobufParseException("Unknown wire type %d", tag.wireType());
        }
    }

    private static void assertWireType(Tag tag, String fieldName, int expectedWireType) {
        if (tag.wireType() != expectedWireType) {
            throw new ProtobufParseException("Field [name=%s, number=%d] incorrect wire type. Expected %d, got %d",
                    fieldName, tag.number(), expectedWireType, tag.wireType());
        }
    }
}
