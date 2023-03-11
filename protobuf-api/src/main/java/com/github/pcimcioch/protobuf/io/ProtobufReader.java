package com.github.pcimcioch.protobuf.io;

import com.github.pcimcioch.protobuf.dto.ByteArray;
import com.github.pcimcioch.protobuf.io.exception.UnknownWireTypeException;
import com.github.pcimcioch.protobuf.io.exception.UnsupportedWireTypeException;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;

/**
 * Reads protobuf data
 */
public class ProtobufReader {
    private static final int DEFAULT_BUFFER_SIZE = 4096;

    private final ProtobufInput input;

    /**
     * Constructor. Given input stream will not be closed by this class in any way
     *
     * @param inputStream input stream to read data from
     */
    public ProtobufReader(InputStream inputStream) {
        this.input = ProtobufInput.from(inputStream, DEFAULT_BUFFER_SIZE);
    }

    /**
     * Constructor
     *
     * @param bytes array to read data from
     */
    public ProtobufReader(byte[] bytes) {
        this.input = ProtobufInput.from(bytes);
    }

    /**
     * Reads tag
     *
     * @return tag or null if end of input reached
     * @throws IOException in case of any data read error
     */
    public int tag() throws IOException {
        if (input.isEnded()) {
            return -1;
        }

        return input.readVarint32();
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
     * Reads unpacked list of double
     *
     * @param consumer consumer to take values
     * @throws IOException in case of any data read error
     */
    public void double_Unpacked(DoubleConsumer consumer) throws IOException {
        int size = input.readVarint32();
        int oldLimit = input.setLimit(size);

        while (!input.isEnded()) {
            consumer.accept(double_());
        }

        input.setLimit(oldLimit - size);
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
     * Reads unpacked list of float
     *
     * @param consumer consumer to take values
     * @throws IOException in case of any data read error
     */
    public void float_Unpacked(FloatConsumer consumer) throws IOException {
        int size = input.readVarint32();
        int oldLimit = input.setLimit(size);

        while (!input.isEnded()) {
            consumer.accept(float_());
        }

        input.setLimit(oldLimit - size);
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
     * Reads unpacked list of int32
     *
     * @param consumer consumer to take values
     * @throws IOException in case of any data read error
     */
    public void int32Unpacked(IntConsumer consumer) throws IOException {
        int size = input.readVarint32();
        int oldLimit = input.setLimit(size);

        while (!input.isEnded()) {
            consumer.accept(int32());
        }

        input.setLimit(oldLimit - size);
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
     * Reads unpacked list of int64
     *
     * @param consumer consumer to take values
     * @throws IOException in case of any data read error
     */
    public void int64Unpacked(LongConsumer consumer) throws IOException {
        int size = input.readVarint32();
        int oldLimit = input.setLimit(size);

        while (!input.isEnded()) {
            consumer.accept(int64());
        }

        input.setLimit(oldLimit - size);
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
     * Reads unpacked list of uint32
     *
     * @param consumer consumer to take values
     * @throws IOException in case of any data read error
     */
    public void uint32Unpacked(IntConsumer consumer) throws IOException {
        int size = input.readVarint32();
        int oldLimit = input.setLimit(size);

        while (!input.isEnded()) {
            consumer.accept(uint32());
        }

        input.setLimit(oldLimit - size);
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
     * Reads unpacked list of uint64
     *
     * @param consumer consumer to take values
     * @throws IOException in case of any data read error
     */
    public void uint64Unpacked(LongConsumer consumer) throws IOException {
        int size = input.readVarint32();
        int oldLimit = input.setLimit(size);

        while (!input.isEnded()) {
            consumer.accept(uint64());
        }

        input.setLimit(oldLimit - size);
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
     * Reads unpacked list of sint32
     *
     * @param consumer consumer to take values
     * @throws IOException in case of any data read error
     */
    public void sint32Unpacked(IntConsumer consumer) throws IOException {
        int size = input.readVarint32();
        int oldLimit = input.setLimit(size);

        while (!input.isEnded()) {
            consumer.accept(sint32());
        }

        input.setLimit(oldLimit - size);
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
     * Reads unpacked list of sint64
     *
     * @param consumer consumer to take values
     * @throws IOException in case of any data read error
     */
    public void sint64Unpacked(LongConsumer consumer) throws IOException {
        int size = input.readVarint32();
        int oldLimit = input.setLimit(size);

        while (!input.isEnded()) {
            consumer.accept(sint64());
        }

        input.setLimit(oldLimit - size);
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
     * Reads unpacked list of fixed32
     *
     * @param consumer consumer to take values
     * @throws IOException in case of any data read error
     */
    public void fixed32Unpacked(IntConsumer consumer) throws IOException {
        int size = input.readVarint32();
        int oldLimit = input.setLimit(size);

        while (!input.isEnded()) {
            consumer.accept(fixed32());
        }

        input.setLimit(oldLimit - size);
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
     * Reads unpacked list of fixed64
     *
     * @param consumer consumer to take values
     * @throws IOException in case of any data read error
     */
    public void fixed64Unpacked(LongConsumer consumer) throws IOException {
        int size = input.readVarint32();
        int oldLimit = input.setLimit(size);

        while (!input.isEnded()) {
            consumer.accept(fixed64());
        }

        input.setLimit(oldLimit - size);
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
     * Reads unpacked list of sfixed32
     *
     * @param consumer consumer to take values
     * @throws IOException in case of any data read error
     */
    public void sfixed32Unpacked(IntConsumer consumer) throws IOException {
        int size = input.readVarint32();
        int oldLimit = input.setLimit(size);

        while (!input.isEnded()) {
            consumer.accept(sfixed32());
        }

        input.setLimit(oldLimit - size);
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
     * Reads unpacked list of sfixed64
     *
     * @param consumer consumer to take values
     * @throws IOException in case of any data read error
     */
    public void sfixed64Unpacked(LongConsumer consumer) throws IOException {
        int size = input.readVarint32();
        int oldLimit = input.setLimit(size);

        while (!input.isEnded()) {
            consumer.accept(sfixed64());
        }

        input.setLimit(oldLimit - size);
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
     * Reads unpacked list of bool
     *
     * @param consumer consumer to take values
     * @throws IOException in case of any data read error
     */
    public void boolUnpacked(BooleanConsumer consumer) throws IOException {
        int size = input.readVarint32();
        int oldLimit = input.setLimit(size);

        while (!input.isEnded()) {
            consumer.accept(bool());
        }

        input.setLimit(oldLimit - size);
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
    @SuppressWarnings("deprecation")
    public ByteArray bytes() throws IOException {
        return ByteArray.unsafeFromByteArray(input.readBytes());
    }

    /**
     * Reads message
     *
     * @param factory message from bytes factory
     * @param <T>     type of message
     * @return message
     * @throws IOException in case of any data read error
     */
    public <T> T message(MessageFactory<T> factory) throws IOException {
        int size = input.readVarint32();
        int oldLimit = input.setLimit(size);

        T message = factory.parse(this);
        input.setLimit(oldLimit - size);

        return message;
    }

    /**
     * Skips unknown value
     *
     * @param tag tag
     * @throws IOException in case of any data read error
     */
    public void skip(int tag) throws IOException {
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
     * Factory that creates message from byte array
     *
     * @param <T> type of the message
     */
    @FunctionalInterface
    public interface MessageFactory<T> {

        /**
         * Creates message from reader
         *
         * @param reader reader
         * @return new message
         * @throws IOException in case of any data read error
         */
        T parse(ProtobufReader reader) throws IOException;
    }

    /**
     * Represents an operation that accepts a single {@code float}-valued argument and
     * returns no result.  This is the primitive type specialization of
     * {@link Consumer} for {@code double}.  Unlike most other functional interfaces,
     * {@code FloatConsumer} is expected to operate via side-effects.
     * <p>This is a <a href="package-summary.html">functional interface</a>
     * whose functional method is {@link #accept(float)}.
     */
    @FunctionalInterface
    public interface FloatConsumer {

        /**
         * Performs this operation on the given argument.
         *
         * @param value the input argument
         */
        void accept(float value);
    }

    /**
     * Represents an operation that accepts a single {@code boolean}-valued argument and
     * returns no result.  This is the primitive type specialization of
     * {@link Consumer} for {@code double}.  Unlike most other functional interfaces,
     * {@code BooleanConsumer} is expected to operate via side-effects.
     * <p>This is a <a href="package-summary.html">functional interface</a>
     * whose functional method is {@link #accept(boolean)}.
     */
    @FunctionalInterface
    public interface BooleanConsumer {

        /**
         * Performs this operation on the given argument.
         *
         * @param value the input argument
         */
        void accept(boolean value);
    }
}
