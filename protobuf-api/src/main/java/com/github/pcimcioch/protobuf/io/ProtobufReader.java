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
    public int readTag() throws IOException {
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
    public double readDouble() throws IOException {
        return input.readDouble();
    }

    /**
     * Reads packed list of double
     *
     * @param consumer consumer to take values
     * @throws IOException in case of any data read error
     */
    public void readDoublePacked(DoubleConsumer consumer) throws IOException {
        int size = input.readVarint32();
        int oldLimit = input.setLimit(size);

        while (!input.isEnded()) {
            consumer.accept(readDouble());
        }

        input.setLimit(oldLimit - size);
    }

    /**
     * Reads float
     *
     * @return float
     * @throws IOException in case of any data read error
     */
    public float readFloat() throws IOException {
        return input.readFloat();
    }

    /**
     * Reads packed list of float
     *
     * @param consumer consumer to take values
     * @throws IOException in case of any data read error
     */
    public void readFloatPacked(FloatConsumer consumer) throws IOException {
        int size = input.readVarint32();
        int oldLimit = input.setLimit(size);

        while (!input.isEnded()) {
            consumer.accept(readFloat());
        }

        input.setLimit(oldLimit - size);
    }

    /**
     * Reads int32
     *
     * @return int32
     * @throws IOException in case of any data read error
     */
    public int readInt32() throws IOException {
        return input.readVarint32();
    }

    /**
     * Reads packed list of int32
     *
     * @param consumer consumer to take values
     * @throws IOException in case of any data read error
     */
    public void readInt32Packed(IntConsumer consumer) throws IOException {
        int size = input.readVarint32();
        int oldLimit = input.setLimit(size);

        while (!input.isEnded()) {
            consumer.accept(readInt32());
        }

        input.setLimit(oldLimit - size);
    }

    /**
     * Reads int64
     *
     * @return int64
     * @throws IOException in case of any data read error
     */
    public long readInt64() throws IOException {
        return input.readVarint64();
    }

    /**
     * Reads packed list of int64
     *
     * @param consumer consumer to take values
     * @throws IOException in case of any data read error
     */
    public void readInt64Packed(LongConsumer consumer) throws IOException {
        int size = input.readVarint32();
        int oldLimit = input.setLimit(size);

        while (!input.isEnded()) {
            consumer.accept(readInt64());
        }

        input.setLimit(oldLimit - size);
    }

    /**
     * Reads uint32
     *
     * @return uint32
     * @throws IOException in case of any data read error
     */
    public int readUint32() throws IOException {
        return input.readVarint32();
    }

    /**
     * Reads packed list of uint32
     *
     * @param consumer consumer to take values
     * @throws IOException in case of any data read error
     */
    public void readUint32Packed(IntConsumer consumer) throws IOException {
        int size = input.readVarint32();
        int oldLimit = input.setLimit(size);

        while (!input.isEnded()) {
            consumer.accept(readUint32());
        }

        input.setLimit(oldLimit - size);
    }

    /**
     * Reads uint64
     *
     * @return uint64
     * @throws IOException in case of any data read error
     */
    public long readUint64() throws IOException {
        return input.readVarint64();
    }

    /**
     * Reads packed list of uint64
     *
     * @param consumer consumer to take values
     * @throws IOException in case of any data read error
     */
    public void readUint64Packed(LongConsumer consumer) throws IOException {
        int size = input.readVarint32();
        int oldLimit = input.setLimit(size);

        while (!input.isEnded()) {
            consumer.accept(readUint64());
        }

        input.setLimit(oldLimit - size);
    }

    /**
     * Reads sint32
     *
     * @return sint32
     * @throws IOException in case of any data read error
     */
    public int readSint32() throws IOException {
        return input.readZigZag32();
    }

    /**
     * Reads packed list of sint32
     *
     * @param consumer consumer to take values
     * @throws IOException in case of any data read error
     */
    public void readSint32Packed(IntConsumer consumer) throws IOException {
        int size = input.readVarint32();
        int oldLimit = input.setLimit(size);

        while (!input.isEnded()) {
            consumer.accept(readSint32());
        }

        input.setLimit(oldLimit - size);
    }

    /**
     * Reads sint64
     *
     * @return sint64
     * @throws IOException in case of any data read error
     */
    public long readSint64() throws IOException {
        return input.readZigZag64();
    }

    /**
     * Reads packed list of sint64
     *
     * @param consumer consumer to take values
     * @throws IOException in case of any data read error
     */
    public void readSint64Packed(LongConsumer consumer) throws IOException {
        int size = input.readVarint32();
        int oldLimit = input.setLimit(size);

        while (!input.isEnded()) {
            consumer.accept(readSint64());
        }

        input.setLimit(oldLimit - size);
    }

    /**
     * Reads fixed32
     *
     * @return fixed32
     * @throws IOException in case of any data read error
     */
    public int readFixed32() throws IOException {
        return input.readFixedInt();
    }

    /**
     * Reads packed list of fixed32
     *
     * @param consumer consumer to take values
     * @throws IOException in case of any data read error
     */
    public void readFixed32Packed(IntConsumer consumer) throws IOException {
        int size = input.readVarint32();
        int oldLimit = input.setLimit(size);

        while (!input.isEnded()) {
            consumer.accept(readFixed32());
        }

        input.setLimit(oldLimit - size);
    }

    /**
     * Reads fixed64
     *
     * @return fixed64
     * @throws IOException in case of any data read error
     */
    public long readFixed64() throws IOException {
        return input.readFixedLong();
    }

    /**
     * Reads packed list of fixed64
     *
     * @param consumer consumer to take values
     * @throws IOException in case of any data read error
     */
    public void readFixed64Packed(LongConsumer consumer) throws IOException {
        int size = input.readVarint32();
        int oldLimit = input.setLimit(size);

        while (!input.isEnded()) {
            consumer.accept(readFixed64());
        }

        input.setLimit(oldLimit - size);
    }

    /**
     * Reads sfixed32
     *
     * @return sfixed32
     * @throws IOException in case of any data read error
     */
    public int readSfixed32() throws IOException {
        return input.readFixedInt();
    }

    /**
     * Reads packed list of sfixed32
     *
     * @param consumer consumer to take values
     * @throws IOException in case of any data read error
     */
    public void readSfixed32Packed(IntConsumer consumer) throws IOException {
        int size = input.readVarint32();
        int oldLimit = input.setLimit(size);

        while (!input.isEnded()) {
            consumer.accept(readSfixed32());
        }

        input.setLimit(oldLimit - size);
    }

    /**
     * Reads sfixed64
     *
     * @return sfixed64
     * @throws IOException in case of any data read error
     */
    public long readSfixed64() throws IOException {
        return input.readFixedLong();
    }

    /**
     * Reads packed list of sfixed64
     *
     * @param consumer consumer to take values
     * @throws IOException in case of any data read error
     */
    public void readSfixed64Packed(LongConsumer consumer) throws IOException {
        int size = input.readVarint32();
        int oldLimit = input.setLimit(size);

        while (!input.isEnded()) {
            consumer.accept(readSfixed64());
        }

        input.setLimit(oldLimit - size);
    }

    /**
     * Reads bool
     *
     * @return bool
     * @throws IOException in case of any data read error
     */
    public boolean readBool() throws IOException {
        return input.readBoolean();
    }

    /**
     * Reads packed list of bool
     *
     * @param consumer consumer to take values
     * @throws IOException in case of any data read error
     */
    public void readBoolPacked(BooleanConsumer consumer) throws IOException {
        int size = input.readVarint32();
        int oldLimit = input.setLimit(size);

        while (!input.isEnded()) {
            consumer.accept(readBool());
        }

        input.setLimit(oldLimit - size);
    }

    /**
     * Reads string
     *
     * @return string
     * @throws IOException in case of any data read error
     */
    public String readString() throws IOException {
        return input.readString();
    }

    /**
     * Reads bytes
     *
     * @return bytes
     * @throws IOException in case of any data read error
     */
    @SuppressWarnings("deprecation")
    public ByteArray readBytes() throws IOException {
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
    public <T> T readMessage(MessageFactory<T> factory) throws IOException {
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
