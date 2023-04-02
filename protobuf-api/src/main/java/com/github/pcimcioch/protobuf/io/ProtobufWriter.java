package com.github.pcimcioch.protobuf.io;

import com.github.pcimcioch.protobuf.dto.BooleanList;
import com.github.pcimcioch.protobuf.dto.ByteArray;
import com.github.pcimcioch.protobuf.dto.DoubleList;
import com.github.pcimcioch.protobuf.dto.EnumList;
import com.github.pcimcioch.protobuf.dto.FloatList;
import com.github.pcimcioch.protobuf.dto.IntList;
import com.github.pcimcioch.protobuf.dto.LongList;
import com.github.pcimcioch.protobuf.dto.ObjectList;
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
    public ProtobufWriter writeDouble(int number, double value) throws IOException {
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
    public ProtobufWriter writeDoubleUnpacked(int number, DoubleList values) throws IOException {
        for (int i = 0; i < values.size(); i++) {
            output.writeVarint32(I64.tagFrom(number));
            output.writeDouble(values.getDouble(i));
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
    public ProtobufWriter writeDoublePacked(int number, DoubleList values) throws IOException {
        if (values.isEmpty()) {
            return this;
        }

        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.ofDoublePacked(values));
        for (int i = 0; i < values.size(); i++) {
            output.writeDouble(values.getDouble(i));
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
    public ProtobufWriter writeFloat(int number, float value) throws IOException {
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
    public ProtobufWriter writeFloatUnpacked(int number, FloatList values) throws IOException {
        for (int i = 0; i < values.size(); i++) {
            output.writeVarint32(I32.tagFrom(number));
            output.writeFloat(values.getFloat(i));
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
    public ProtobufWriter writeFloatPacked(int number, FloatList values) throws IOException {
        if (values.isEmpty()) {
            return this;
        }

        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.ofFloatPacked(values));
        for (int i = 0; i < values.size(); i++) {
            output.writeFloat(values.getFloat(i));
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
    public ProtobufWriter writeInt32(int number, int value) throws IOException {
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
    public ProtobufWriter writeInt32Unpacked(int number, IntList values) throws IOException {
        for (int i = 0; i < values.size(); i++) {
            output.writeVarint32(VARINT.tagFrom(number));
            output.writeVarint32(values.getInt(i));
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
    public ProtobufWriter writeInt32Packed(int number, IntList values) throws IOException {
        if (values.isEmpty()) {
            return this;
        }

        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.ofInt32Packed(values));
        for (int i = 0; i < values.size(); i++) {
            output.writeVarint32(values.getInt(i));
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
    public ProtobufWriter writeInt64(int number, long value) throws IOException {
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
    public ProtobufWriter writeInt64Unpacked(int number, LongList values) throws IOException {
        for (int i = 0; i < values.size(); i++) {
            output.writeVarint32(VARINT.tagFrom(number));
            output.writeVarint64(values.getLong(i));
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
    public ProtobufWriter writeInt64Packed(int number, LongList values) throws IOException {
        if (values.isEmpty()) {
            return this;
        }

        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.ofInt64Packed(values));
        for (int i = 0; i < values.size(); i++) {
            output.writeVarint64(values.getLong(i));
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
    public ProtobufWriter writeUint32(int number, int value) throws IOException {
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
    public ProtobufWriter writeUint32Unpacked(int number, IntList values) throws IOException {
        for (int i = 0; i < values.size(); i++) {
            output.writeVarint32(VARINT.tagFrom(number));
            output.writeVarint32(values.getInt(i));
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
    public ProtobufWriter writeUint32Packed(int number, IntList values) throws IOException {
        if (values.isEmpty()) {
            return this;
        }

        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.ofUint32Packed(values));
        for (int i = 0; i < values.size(); i++) {
            output.writeVarint32(values.getInt(i));
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
    public ProtobufWriter writeUint64(int number, long value) throws IOException {
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
    public ProtobufWriter writeUint64Unpacked(int number, LongList values) throws IOException {
        for (int i = 0; i < values.size(); i++) {
            output.writeVarint32(VARINT.tagFrom(number));
            output.writeVarint64(values.getLong(i));
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
    public ProtobufWriter writeUint64Packed(int number, LongList values) throws IOException {
        if (values.isEmpty()) {
            return this;
        }

        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.ofUint64Packed(values));
        for (int i = 0; i < values.size(); i++) {
            output.writeVarint64(values.getLong(i));
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
    public ProtobufWriter writeSint32(int number, int value) throws IOException {
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
    public ProtobufWriter writeSint32Unpacked(int number, IntList values) throws IOException {
        for (int i = 0; i < values.size(); i++) {
            output.writeVarint32(VARINT.tagFrom(number));
            output.writeZigZag32(values.getInt(i));
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
    public ProtobufWriter writeSint32Packed(int number, IntList values) throws IOException {
        if (values.isEmpty()) {
            return this;
        }

        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.ofSint32Packed(values));
        for (int i = 0; i < values.size(); i++) {
            output.writeZigZag32(values.getInt(i));
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
    public ProtobufWriter writeSint64(int number, long value) throws IOException {
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
    public ProtobufWriter writeSint64Unpacked(int number, LongList values) throws IOException {
        for (int i = 0; i < values.size(); i++) {
            output.writeVarint32(VARINT.tagFrom(number));
            output.writeZigZag64(values.getLong(i));
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
    public ProtobufWriter writeSint64Packed(int number, LongList values) throws IOException {
        if (values.isEmpty()) {
            return this;
        }

        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.ofSint64Packed(values));
        for (int i = 0; i < values.size(); i++) {
            output.writeZigZag64(values.getLong(i));
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
    public ProtobufWriter writeFixed32(int number, int value) throws IOException {
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
    public ProtobufWriter writeFixed32Unpacked(int number, IntList values) throws IOException {
        for (int i = 0; i < values.size(); i++) {
            output.writeVarint32(I32.tagFrom(number));
            output.writeFixedInt(values.getInt(i));
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
    public ProtobufWriter writeFixed32Packed(int number, IntList values) throws IOException {
        if (values.isEmpty()) {
            return this;
        }

        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.ofFixed32Packed(values));
        for (int i = 0; i < values.size(); i++) {
            output.writeFixedInt(values.getInt(i));
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
    public ProtobufWriter writeFixed64(int number, long value) throws IOException {
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
    public ProtobufWriter writeFixed64Unpacked(int number, LongList values) throws IOException {
        for (int i = 0; i < values.size(); i++) {
            output.writeVarint32(I64.tagFrom(number));
            output.writeFixedLong(values.getLong(i));
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
    public ProtobufWriter writeFixed64Packed(int number, LongList values) throws IOException {
        if (values.isEmpty()) {
            return this;
        }

        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.ofFixed64Packed(values));
        for (int i = 0; i < values.size(); i++) {
            output.writeFixedLong(values.getLong(i));
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
    public ProtobufWriter writeSfixed32(int number, int value) throws IOException {
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
    public ProtobufWriter writeSfixed32Unpacked(int number, IntList values) throws IOException {
        for (int i = 0; i < values.size(); i++) {
            output.writeVarint32(I32.tagFrom(number));
            output.writeFixedInt(values.getInt(i));
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
    public ProtobufWriter writeSfixed32Packed(int number, IntList values) throws IOException {
        if (values.isEmpty()) {
            return this;
        }

        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.ofSfixed32Packed(values));
        for (int i = 0; i < values.size(); i++) {
            output.writeFixedInt(values.getInt(i));
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
    public ProtobufWriter writeSfixed64(int number, long value) throws IOException {
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
    public ProtobufWriter writeSfixed64Unpacked(int number, LongList values) throws IOException {
        for (int i = 0; i < values.size(); i++) {
            output.writeVarint32(I64.tagFrom(number));
            output.writeFixedLong(values.getLong(i));
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
    public ProtobufWriter writeSfixed64Packed(int number, LongList values) throws IOException {
        if (values.isEmpty()) {
            return this;
        }

        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.ofSfixed64Packed(values));
        for (int i = 0; i < values.size(); i++) {
            output.writeFixedLong(values.getLong(i));
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
    public ProtobufWriter writeBool(int number, boolean value) throws IOException {
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
    public ProtobufWriter writeBoolUnpacked(int number, BooleanList values) throws IOException {
        for (int i = 0; i < values.size(); i++) {
            output.writeVarint32(VARINT.tagFrom(number));
            output.writeBoolean(values.getBoolean(i));
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
    public ProtobufWriter writeBoolPacked(int number, BooleanList values) throws IOException {
        if (values.isEmpty()) {
            return this;
        }

        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.ofBoolPacked(values));
        for (int i = 0; i < values.size(); i++) {
            output.writeBoolean(values.getBoolean(i));
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
    public ProtobufWriter writeString(int number, String value) throws IOException {
        if (!"".equals(value)) {
            output.writeVarint32(LEN.tagFrom(number));
            output.writeString(value);
        }

        return this;
    }

    /**
     * Writes unpacked list of string
     *
     * @param number field number
     * @param values values to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter writeStringUnpacked(int number, ObjectList<String> values) throws IOException {
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
    public ProtobufWriter writeBytes(int number, ByteArray value) throws IOException {
        if (!value.isEmpty()) {
            output.writeVarint32(LEN.tagFrom(number));
            output.writeBytes(value.internalData());
        }

        return this;
    }

    /**
     * Writes unpacked list of bytes
     *
     * @param number field number
     * @param values values to write
     * @return this
     * @throws IOException in case of any data write error
     */
    @SuppressWarnings("deprecation")
    public ProtobufWriter writeBytesUnpacked(int number, ObjectList<ByteArray> values) throws IOException {
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
    public ProtobufWriter writeMessage(int number, ProtobufMessage<?> value) throws IOException {
        if (value != null) {
            output.writeVarint32(LEN.tagFrom(number));
            output.writeVarint32(value.protobufSize());
            value.writeTo(this);
        }

        return this;
    }

    /**
     * Write unpacked list of messages
     *
     * @param number field number
     * @param values messages to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter writeMessageUnpacked(int number, ObjectList<? extends ProtobufMessage<?>> values) throws IOException {
        for (ProtobufMessage<?> value : values) {
            output.writeVarint32(LEN.tagFrom(number));
            output.writeVarint32(value.protobufSize());
            value.writeTo(this);
        }

        return this;
    }

    /**
     * Write unknown fields
     *
     * @param values fields
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter writeUnknownFields(ObjectList<UnknownField> values) throws IOException {
        for (UnknownField value : values) {
            value.writeTo(output);
        }

        return this;
    }

    /**
     * Write unpacked list of enums
     *
     * @param number field number
     * @param values enums to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter writeEnumUnpacked(int number, EnumList<?> values) throws IOException {
        return writeInt32Unpacked(number, values.valuesList());
    }

    /**
     * Write packed list of enums
     *
     * @param number field number
     * @param values enums to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter writeEnumPacked(int number, EnumList<?> values) throws IOException {
        return writeInt32Packed(number, values.valuesList());
    }

    @Override
    public void close() throws IOException {
        output.close();
    }
}
