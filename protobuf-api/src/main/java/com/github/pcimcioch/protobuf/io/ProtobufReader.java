package com.github.pcimcioch.protobuf.io;

import com.github.pcimcioch.protobuf.dto.ByteArray;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import static com.github.pcimcioch.protobuf.io.WireType.I32;
import static com.github.pcimcioch.protobuf.io.WireType.I64;
import static com.github.pcimcioch.protobuf.io.WireType.LEN;
import static com.github.pcimcioch.protobuf.io.WireType.VARINT;
import static com.github.pcimcioch.protobuf.io.WireType.SGROUP;
import static com.github.pcimcioch.protobuf.io.WireType.EGROUP;
import static com.github.pcimcioch.protobuf.io.WireType.wireTypeFrom;

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
    public int tag() throws IOException {
        try {
            return input.readVarint32();
        } catch (EOFException ex) {
            return -1;
        }
    }

    /**
     * Reads double
     *
     * @return double
     * @throws IOException in case of any data read error
     */
    public double double_() throws IOException {
        return input.readDouble();
    }

    /**
     * Reads float
     *
     * @return float
     * @throws IOException in case of any data read error
     */
    public float float_() throws IOException {
        return input.readFloat();
    }

    /**
     * Reads int32
     *
     * @return int32
     * @throws IOException in case of any data read error
     */
    public int int32() throws IOException {
        return input.readVarint32();
    }

    /**
     * Reads int64
     *
     * @return int64
     * @throws IOException in case of any data read error
     */
    public long int64() throws IOException {
        return input.readVarint64();
    }

    /**
     * Reads uint32
     *
     * @return uint32
     * @throws IOException in case of any data read error
     */
    public int uint32() throws IOException {
        return input.readVarint32();
    }

    /**
     * Reads uint64
     *
     * @return uint64
     * @throws IOException in case of any data read error
     */
    public long uint64() throws IOException {
        return input.readVarint64();
    }

    /**
     * Reads sint32
     *
     * @return sint32
     * @throws IOException in case of any data read error
     */
    public int sint32() throws IOException {
        return input.readZigZag32();
    }

    /**
     * Reads sint64
     *
     * @return sint64
     * @throws IOException in case of any data read error
     */
    public long sint64() throws IOException {
        return input.readZigZag64();
    }

    /**
     * Reads fixed32
     *
     * @return fixed32
     * @throws IOException in case of any data read error
     */
    public int fixed32() throws IOException {
        return input.readFixedInt();
    }

    /**
     * Reads fixed64
     *
     * @return fixed64
     * @throws IOException in case of any data read error
     */
    public long fixed64() throws IOException {
        return input.readFixedLong();
    }

    /**
     * Reads sfixed32
     *
     * @return sfixed32
     * @throws IOException in case of any data read error
     */
    public int sfixed32() throws IOException {
        return input.readFixedInt();
    }

    /**
     * Reads sfixed64
     *
     * @return sfixed64
     * @throws IOException in case of any data read error
     */
    public long sfixed64() throws IOException {
        return input.readFixedLong();
    }

    /**
     * Reads bool
     *
     * @return bool
     * @throws IOException in case of any data read error
     */
    public boolean bool() throws IOException {
        return input.readBoolean();
    }

    /**
     * Reads string
     *
     * @return string
     * @throws IOException in case of any data read error
     */
    public String string() throws IOException {
        return input.readString();
    }

    /**
     * Reads bytes
     *
     * @return bytes
     * @throws IOException in case of any data read error
     */
    public ByteArray bytes() throws IOException {
        return input.readByteArray();
    }

    /**
     * Reads message
     *
     * @param factory   message from bytes factory
     * @param <T>       type of message
     * @return message
     * @throws IOException in case of any data read error
     */
    public <T> T message(MessageFactory<T> factory) throws IOException {
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
    public void skip(int tag) throws IOException {
        switch (wireTypeFrom(tag)) {
            case VARINT -> input.readVarint64();
            case I64 -> input.skip(8);
            case LEN -> input.skip(input.readVarint64());
            case SGROUP -> throw new ProtobufParseException("Wire Type SGROUP is not supported");
            case EGROUP -> throw new ProtobufParseException("Wire Type EGROUP is not supported");
            case I32 -> input.skip(4);
            default -> throw new ProtobufParseException("Unknown wire type %d", wireTypeFrom(tag));
        }
    }
}
