package com.github.pcimcioch.protobuf.io;

import com.github.pcimcioch.protobuf.dto.ByteArray;
import com.github.pcimcioch.protobuf.dto.ProtobufMessage;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import static com.github.pcimcioch.protobuf.io.WireType.I32;
import static com.github.pcimcioch.protobuf.io.WireType.I64;
import static com.github.pcimcioch.protobuf.io.WireType.LEN;
import static com.github.pcimcioch.protobuf.io.WireType.VARINT;

/**
 * Writes protobuf data
 */
// TODO rename all ProtobufReader and ProtobufWriter methods to contain read and write. Get rid of those underscores
public class ProtobufWriter implements AutoCloseable {
    private static final int DEFAULT_BUFFER_SIZE = 4096;

    private final ProtobufOutput output;

    /**
     * Constructor. Given output stream will not be closed by this class in any way
     *
     * @param outputStream output stream to save data to
     */
    public ProtobufWriter(OutputStream outputStream) {
        this.output = ProtobufOutput.from(outputStream, DEFAULT_BUFFER_SIZE);
    }

    /**
     * Constructor. Given data array must be big enough for write operations
     *
     * @param data output array
     */
    public ProtobufWriter(byte[] data) {
        this.output = ProtobufOutput.from(data);
    }

    /**
     * Writes double
     *
     * @param number field number
     * @param value  value to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter double_(int number, double value) throws IOException {
        if (value != 0d) {
            output.writeVarint32(I64.tagFrom(number));
            output.writeDouble(value);
        }

        return this;
    }

    /**
     * Writes unpacked list of double
     *
     * @param number field number
     * @param values values to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter double_Unpacked(int number, List<Double> values) throws IOException {
        for (double value : values) {
            output.writeVarint32(I64.tagFrom(number));
            output.writeDouble(value);
        }

        return this;
    }

    /**
     * Writes packed list of double
     *
     * @param number field number
     * @param values values to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter double_Packed(int number, List<Double> values) throws IOException {
        if (values.isEmpty()) {
            return this;
        }

        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.double_Packed(values));
        for (double value : values) {
            output.writeDouble(value);
        }

        return this;
    }

    /**
     * Writes float
     *
     * @param number field number
     * @param value  value to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter float_(int number, float value) throws IOException {
        if (value != 0f) {
            output.writeVarint32(I32.tagFrom(number));
            output.writeFloat(value);
        }

        return this;
    }

    /**
     * Writes unpacked list of float
     *
     * @param number field number
     * @param values values to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter float_Unpacked(int number, List<Float> values) throws IOException {
        for (float value : values) {
            output.writeVarint32(I32.tagFrom(number));
            output.writeFloat(value);
        }

        return this;
    }

    /**
     * Writes packed list of float
     *
     * @param number field number
     * @param values values to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter float_Packed(int number, List<Float> values) throws IOException {
        if (values.isEmpty()) {
            return this;
        }

        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.float_Packed(values));
        for (float value : values) {
            output.writeFloat(value);
        }

        return this;
    }

    /**
     * Writes int32
     *
     * @param number field number
     * @param value  value to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter int32(int number, int value) throws IOException {
        if (value != 0) {
            output.writeVarint32(VARINT.tagFrom(number));
            output.writeVarint32(value);
        }

        return this;
    }

    /**
     * Writes unpacked list of int32
     *
     * @param number field number
     * @param values values to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter int32Unpacked(int number, List<Integer> values) throws IOException {
        for (int value : values) {
            output.writeVarint32(VARINT.tagFrom(number));
            output.writeVarint32(value);
        }

        return this;
    }

    /**
     * Writes packed list of int32
     *
     * @param number field number
     * @param values values to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter int32Packed(int number, List<Integer> values) throws IOException {
        if (values.isEmpty()) {
            return this;
        }

        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.int32Packed(values));
        for (int value : values) {
            output.writeVarint32(value);
        }

        return this;
    }

    /**
     * Writes int64
     *
     * @param number field number
     * @param value  value to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter int64(int number, long value) throws IOException {
        if (value != 0L) {
            output.writeVarint32(VARINT.tagFrom(number));
            output.writeVarint64(value);
        }

        return this;
    }

    /**
     * Writes unpacked list of int64
     *
     * @param number field number
     * @param values values to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter int64Unpacked(int number, List<Long> values) throws IOException {
        for (long value : values) {
            output.writeVarint32(VARINT.tagFrom(number));
            output.writeVarint64(value);
        }

        return this;
    }

    /**
     * Writes packed list of int64
     *
     * @param number field number
     * @param values values to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter int64Packed(int number, List<Long> values) throws IOException {
        if (values.isEmpty()) {
            return this;
        }

        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.int64Packed(values));
        for (long value : values) {
            output.writeVarint64(value);
        }

        return this;
    }

    /**
     * Writes uint32
     *
     * @param number field number
     * @param value  value to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter uint32(int number, int value) throws IOException {
        if (value != 0) {
            output.writeVarint32(VARINT.tagFrom(number));
            output.writeVarint32(value);
        }

        return this;
    }

    /**
     * Writes unpacked list of uint32
     *
     * @param number field number
     * @param values values to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter uint32Unpacked(int number, List<Integer> values) throws IOException {
        for (int value : values) {
            output.writeVarint32(VARINT.tagFrom(number));
            output.writeVarint32(value);
        }

        return this;
    }

    /**
     * Writes packed list of uint32
     *
     * @param number field number
     * @param values values to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter uint32Packed(int number, List<Integer> values) throws IOException {
        if (values.isEmpty()) {
            return this;
        }

        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.uint32Packed(values));
        for (int value : values) {
            output.writeVarint32(value);
        }

        return this;
    }

    /**
     * Writes uint64
     *
     * @param number field number
     * @param value  value to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter uint64(int number, long value) throws IOException {
        if (value != 0L) {
            output.writeVarint32(VARINT.tagFrom(number));
            output.writeVarint64(value);
        }

        return this;
    }

    /**
     * Writes unpacked list of uint64
     *
     * @param number field number
     * @param values values to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter uint64Unpacked(int number, List<Long> values) throws IOException {
        for (long value : values) {
            output.writeVarint32(VARINT.tagFrom(number));
            output.writeVarint64(value);
        }

        return this;
    }

    /**
     * Writes packed list of uint64
     *
     * @param number field number
     * @param values values to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter uint64Packed(int number, List<Long> values) throws IOException {
        if (values.isEmpty()) {
            return this;
        }

        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.uint64Packed(values));
        for (long value : values) {
            output.writeVarint64(value);
        }

        return this;
    }

    /**
     * Writes sint32
     *
     * @param number field number
     * @param value  value to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter sint32(int number, int value) throws IOException {
        if (value != 0) {
            output.writeVarint32(VARINT.tagFrom(number));
            output.writeZigZag32(value);
        }

        return this;
    }

    /**
     * Writes unpacked list of sint32
     *
     * @param number field number
     * @param values values to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter sint32Unpacked(int number, List<Integer> values) throws IOException {
        for (int value : values) {
            output.writeVarint32(VARINT.tagFrom(number));
            output.writeZigZag32(value);
        }

        return this;
    }

    /**
     * Writes packed list of sint32
     *
     * @param number field number
     * @param values values to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter sint32Packed(int number, List<Integer> values) throws IOException {
        if (values.isEmpty()) {
            return this;
        }

        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.sint32Packed(values));
        for (int value : values) {
            output.writeZigZag32(value);
        }

        return this;
    }

    /**
     * Writes sint64
     *
     * @param number field number
     * @param value  value to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter sint64(int number, long value) throws IOException {
        if (value != 0L) {
            output.writeVarint32(VARINT.tagFrom(number));
            output.writeZigZag64(value);
        }

        return this;
    }

    /**
     * Writes unpacked list of sint64
     *
     * @param number field number
     * @param values values to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter sint64Unpacked(int number, List<Long> values) throws IOException {
        for (long value : values) {
            output.writeVarint32(VARINT.tagFrom(number));
            output.writeZigZag64(value);
        }

        return this;
    }

    /**
     * Writes packed list of sint64
     *
     * @param number field number
     * @param values values to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter sint64Packed(int number, List<Long> values) throws IOException {
        if (values.isEmpty()) {
            return this;
        }

        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.sint64Packed(values));
        for (long value : values) {
            output.writeZigZag64(value);
        }

        return this;
    }

    /**
     * Writes fixed32
     *
     * @param number field number
     * @param value  value to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter fixed32(int number, int value) throws IOException {
        if (value != 0) {
            output.writeVarint32(I32.tagFrom(number));
            output.writeFixedInt(value);
        }

        return this;
    }

    /**
     * Writes unpacked list of fixed32
     *
     * @param number field number
     * @param values values to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter fixed32Unpacked(int number, List<Integer> values) throws IOException {
        for (int value : values) {
            output.writeVarint32(I32.tagFrom(number));
            output.writeFixedInt(value);
        }

        return this;
    }

    /**
     * Writes packed list of fixed32
     *
     * @param number field number
     * @param values values to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter fixed32Packed(int number, List<Integer> values) throws IOException {
        if (values.isEmpty()) {
            return this;
        }

        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.fixed32Packed(values));
        for (int value : values) {
            output.writeFixedInt(value);
        }

        return this;
    }

    /**
     * Writes fixed64
     *
     * @param number field number
     * @param value  value to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter fixed64(int number, long value) throws IOException {
        if (value != 0L) {
            output.writeVarint32(I64.tagFrom(number));
            output.writeFixedLong(value);
        }

        return this;
    }

    /**
     * Writes unpacked list of fixed64
     *
     * @param number field number
     * @param values values to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter fixed64Unpacked(int number, List<Long> values) throws IOException {
        for (long value : values) {
            output.writeVarint32(I64.tagFrom(number));
            output.writeFixedLong(value);
        }

        return this;
    }

    /**
     * Writes packed list of fixed64
     *
     * @param number field number
     * @param values values to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter fixed64Packed(int number, List<Long> values) throws IOException {
        if (values.isEmpty()) {
            return this;
        }

        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.fixed64Packed(values));
        for (long value : values) {
            output.writeFixedLong(value);
        }

        return this;
    }

    /**
     * Writes sfixed32
     *
     * @param number field number
     * @param value  value to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter sfixed32(int number, int value) throws IOException {
        if (value != 0) {
            output.writeVarint32(I32.tagFrom(number));
            output.writeFixedInt(value);
        }

        return this;
    }

    /**
     * Writes unpacked list of sfixed32
     *
     * @param number field number
     * @param values values to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter sfixed32Unpacked(int number, List<Integer> values) throws IOException {
        for (int value : values) {
            output.writeVarint32(I32.tagFrom(number));
            output.writeFixedInt(value);
        }

        return this;
    }

    /**
     * Writes packed list of sfixed32
     *
     * @param number field number
     * @param values values to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter sfixed32Packed(int number, List<Integer> values) throws IOException {
        if (values.isEmpty()) {
            return this;
        }

        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.sfixed32Packed(values));
        for (int value : values) {
            output.writeFixedInt(value);
        }

        return this;
    }

    /**
     * Writes sfixed64
     *
     * @param number field number
     * @param value  value to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter sfixed64(int number, long value) throws IOException {
        if (value != 0L) {
            output.writeVarint32(I64.tagFrom(number));
            output.writeFixedLong(value);
        }

        return this;
    }

    /**
     * Writes unpacked list of sfixed64
     *
     * @param number field number
     * @param values values to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter sfixed64Unpacked(int number, List<Long> values) throws IOException {
        for (long value : values) {
            output.writeVarint32(I64.tagFrom(number));
            output.writeFixedLong(value);
        }

        return this;
    }

    /**
     * Writes packed list of sfixed64
     *
     * @param number field number
     * @param values values to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter sfixed64Packed(int number, List<Long> values) throws IOException {
        if (values.isEmpty()) {
            return this;
        }

        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.sfixed64Packed(values));
        for (long value : values) {
            output.writeFixedLong(value);
        }

        return this;
    }

    /**
     * Writes bool
     *
     * @param number field number
     * @param value  value to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter bool(int number, boolean value) throws IOException {
        if (value) {
            output.writeVarint32(VARINT.tagFrom(number));
            output.writeBoolean(true);
        }

        return this;
    }

    /**
     * Writes unpacked list of bool
     *
     * @param number field number
     * @param values values to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter boolUnpacked(int number, List<Boolean> values) throws IOException {
        for (boolean value : values) {
            output.writeVarint32(VARINT.tagFrom(number));
            output.writeBoolean(value);
        }

        return this;
    }

    /**
     * Writes packed list of bool
     *
     * @param number field number
     * @param values values to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter boolPacked(int number, List<Boolean> values) throws IOException {
        if (values.isEmpty()) {
            return this;
        }

        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.boolPacked(values));
        for (boolean value : values) {
            output.writeBoolean(value);
        }

        return this;
    }

    /**
     * Writes string
     *
     * @param number field number
     * @param value  value to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter string(int number, String value) throws IOException {
        if (!"".equals(value)) {
            output.writeVarint32(LEN.tagFrom(number));
            output.writeString(value);
        }

        return this;
    }

    /**
     * Writes list of string
     *
     * @param number field number
     * @param values values to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter stringUnpacked(int number, List<String> values) throws IOException {
        for (String value : values) {
            output.writeVarint32(LEN.tagFrom(number));
            output.writeString(value);
        }

        return this;
    }

    /**
     * Writes bytes
     *
     * @param number field number
     * @param value  value to write
     * @return this
     * @throws IOException in case of any data write error
     */
    @SuppressWarnings("deprecation")
    public ProtobufWriter bytes(int number, ByteArray value) throws IOException {
        if (!value.isEmpty()) {
            output.writeVarint32(LEN.tagFrom(number));
            output.writeBytes(value.internalData());
        }

        return this;
    }

    /**
     * Writes list of bytes
     *
     * @param number field number
     * @param values values to write
     * @return this
     * @throws IOException in case of any data write error
     */
    @SuppressWarnings("deprecation")
    public ProtobufWriter bytesUnpacked(int number, List<ByteArray> values) throws IOException {
        for (ByteArray value : values) {
            output.writeVarint32(LEN.tagFrom(number));
            output.writeBytes(value.internalData());
        }

        return this;
    }

    /**
     * Write message
     *
     * @param number field number
     * @param value  message to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter message(int number, ProtobufMessage<?> value) throws IOException {
        if (value != null) {
            output.writeVarint32(LEN.tagFrom(number));
            output.writeVarint32(value.protobufSize());
            value.writeTo(this);
        }

        return this;
    }

    /**
     * Write list of messages
     *
     * @param number field number
     * @param values messages to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter messageUnpacked(int number, List<? extends ProtobufMessage<?>> values) throws IOException {
        for (ProtobufMessage<?> value : values) {
            output.writeVarint32(LEN.tagFrom(number));
            output.writeVarint32(value.protobufSize());
            value.writeTo(this);
        }

        return this;
    }

    @Override
    public void close() throws IOException {
        output.close();
    }
}
