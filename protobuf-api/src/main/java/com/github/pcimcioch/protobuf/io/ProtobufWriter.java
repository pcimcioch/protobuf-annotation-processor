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
     * @throws IOException in case of any data write error
     */
    public void writeDouble(int number, double value) throws IOException {
        if (value != 0d) {
            output.writeVarint32(I64.tagFrom(number));
            output.writeDouble(value);
        }
    }

    /**
     * Writes unpacked list of double
     *
     * @param number field number
     * @param values values to write
     * @throws IOException in case of any data write error
     */
    public void writeDoubleUnpacked(int number, DoubleList values) throws IOException {
        for (int i = 0; i < values.size(); i++) {
            output.writeVarint32(I64.tagFrom(number));
            output.writeDouble(values.getDouble(i));
        }
    }

    /**
     * Writes packed list of double
     *
     * @param number field number
     * @param values values to write
     * @throws IOException in case of any data write error
     */
    public void writeDoublePacked(int number, DoubleList values) throws IOException {
        if (values.isEmpty()) {
            return;
        }

        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.ofDoublePacked(values));
        for (int i = 0; i < values.size(); i++) {
            output.writeDouble(values.getDouble(i));
        }
    }

    /**
     * Writes float
     *
     * @param number field number
     * @param value  value to write
     * @throws IOException in case of any data write error
     */
    public void writeFloat(int number, float value) throws IOException {
        if (value != 0f) {
            output.writeVarint32(I32.tagFrom(number));
            output.writeFloat(value);
        }
    }

    /**
     * Writes unpacked list of float
     *
     * @param number field number
     * @param values values to write
     * @throws IOException in case of any data write error
     */
    public void writeFloatUnpacked(int number, FloatList values) throws IOException {
        for (int i = 0; i < values.size(); i++) {
            output.writeVarint32(I32.tagFrom(number));
            output.writeFloat(values.getFloat(i));
        }
    }

    /**
     * Writes packed list of float
     *
     * @param number field number
     * @param values values to write
     * @throws IOException in case of any data write error
     */
    public void writeFloatPacked(int number, FloatList values) throws IOException {
        if (values.isEmpty()) {
            return;
        }

        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.ofFloatPacked(values));
        for (int i = 0; i < values.size(); i++) {
            output.writeFloat(values.getFloat(i));
        }
    }

    /**
     * Writes int32
     *
     * @param number field number
     * @param value  value to write
     * @throws IOException in case of any data write error
     */
    public void writeInt32(int number, int value) throws IOException {
        if (value != 0) {
            output.writeVarint32(VARINT.tagFrom(number));
            output.writeVarint32(value);
        }
    }

    /**
     * Writes unpacked list of int32
     *
     * @param number field number
     * @param values values to write
     * @throws IOException in case of any data write error
     */
    public void writeInt32Unpacked(int number, IntList values) throws IOException {
        for (int i = 0; i < values.size(); i++) {
            output.writeVarint32(VARINT.tagFrom(number));
            output.writeVarint32(values.getInt(i));
        }
    }

    /**
     * Writes packed list of int32
     *
     * @param number field number
     * @param values values to write
     * @throws IOException in case of any data write error
     */
    public void writeInt32Packed(int number, IntList values) throws IOException {
        if (values.isEmpty()) {
            return ;
        }

        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.ofInt32Packed(values));
        for (int i = 0; i < values.size(); i++) {
            output.writeVarint32(values.getInt(i));
        }
    }

    /**
     * Writes int64
     *
     * @param number field number
     * @param value  value to write
     * @throws IOException in case of any data write error
     */
    public void writeInt64(int number, long value) throws IOException {
        if (value != 0L) {
            output.writeVarint32(VARINT.tagFrom(number));
            output.writeVarint64(value);
        }
    }

    /**
     * Writes unpacked list of int64
     *
     * @param number field number
     * @param values values to write
     * @throws IOException in case of any data write error
     */
    public void writeInt64Unpacked(int number, LongList values) throws IOException {
        for (int i = 0; i < values.size(); i++) {
            output.writeVarint32(VARINT.tagFrom(number));
            output.writeVarint64(values.getLong(i));
        }
    }

    /**
     * Writes packed list of int64
     *
     * @param number field number
     * @param values values to write
     * @throws IOException in case of any data write error
     */
    public void writeInt64Packed(int number, LongList values) throws IOException {
        if (values.isEmpty()) {
            return;
        }

        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.ofInt64Packed(values));
        for (int i = 0; i < values.size(); i++) {
            output.writeVarint64(values.getLong(i));
        }
    }

    /**
     * Writes uint32
     *
     * @param number field number
     * @param value  value to write
     * @throws IOException in case of any data write error
     */
    public void writeUint32(int number, int value) throws IOException {
        if (value != 0) {
            output.writeVarint32(VARINT.tagFrom(number));
            output.writeVarint32(value);
        }
    }

    /**
     * Writes unpacked list of uint32
     *
     * @param number field number
     * @param values values to write
     * @throws IOException in case of any data write error
     */
    public void writeUint32Unpacked(int number, IntList values) throws IOException {
        for (int i = 0; i < values.size(); i++) {
            output.writeVarint32(VARINT.tagFrom(number));
            output.writeVarint32(values.getInt(i));
        }
    }

    /**
     * Writes packed list of uint32
     *
     * @param number field number
     * @param values values to write
     * @throws IOException in case of any data write error
     */
    public void writeUint32Packed(int number, IntList values) throws IOException {
        if (values.isEmpty()) {
            return;
        }

        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.ofUint32Packed(values));
        for (int i = 0; i < values.size(); i++) {
            output.writeVarint32(values.getInt(i));
        }
    }

    /**
     * Writes uint64
     *
     * @param number field number
     * @param value  value to write
     * @throws IOException in case of any data write error
     */
    public void writeUint64(int number, long value) throws IOException {
        if (value != 0L) {
            output.writeVarint32(VARINT.tagFrom(number));
            output.writeVarint64(value);
        }
    }

    /**
     * Writes unpacked list of uint64
     *
     * @param number field number
     * @param values values to write
     * @throws IOException in case of any data write error
     */
    public void writeUint64Unpacked(int number, LongList values) throws IOException {
        for (int i = 0; i < values.size(); i++) {
            output.writeVarint32(VARINT.tagFrom(number));
            output.writeVarint64(values.getLong(i));
        }
    }

    /**
     * Writes packed list of uint64
     *
     * @param number field number
     * @param values values to write
     * @throws IOException in case of any data write error
     */
    public void writeUint64Packed(int number, LongList values) throws IOException {
        if (values.isEmpty()) {
            return;
        }

        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.ofUint64Packed(values));
        for (int i = 0; i < values.size(); i++) {
            output.writeVarint64(values.getLong(i));
        }
    }

    /**
     * Writes sint32
     *
     * @param number field number
     * @param value  value to write
     * @throws IOException in case of any data write error
     */
    public void writeSint32(int number, int value) throws IOException {
        if (value != 0) {
            output.writeVarint32(VARINT.tagFrom(number));
            output.writeZigZag32(value);
        }
    }

    /**
     * Writes unpacked list of sint32
     *
     * @param number field number
     * @param values values to write
     * @throws IOException in case of any data write error
     */
    public void writeSint32Unpacked(int number, IntList values) throws IOException {
        for (int i = 0; i < values.size(); i++) {
            output.writeVarint32(VARINT.tagFrom(number));
            output.writeZigZag32(values.getInt(i));
        }
    }

    /**
     * Writes packed list of sint32
     *
     * @param number field number
     * @param values values to write
     * @throws IOException in case of any data write error
     */
    public void writeSint32Packed(int number, IntList values) throws IOException {
        if (values.isEmpty()) {
            return;
        }

        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.ofSint32Packed(values));
        for (int i = 0; i < values.size(); i++) {
            output.writeZigZag32(values.getInt(i));
        }
    }

    /**
     * Writes sint64
     *
     * @param number field number
     * @param value  value to write
     * @throws IOException in case of any data write error
     */
    public void writeSint64(int number, long value) throws IOException {
        if (value != 0L) {
            output.writeVarint32(VARINT.tagFrom(number));
            output.writeZigZag64(value);
        }
    }

    /**
     * Writes unpacked list of sint64
     *
     * @param number field number
     * @param values values to write
     * @throws IOException in case of any data write error
     */
    public void writeSint64Unpacked(int number, LongList values) throws IOException {
        for (int i = 0; i < values.size(); i++) {
            output.writeVarint32(VARINT.tagFrom(number));
            output.writeZigZag64(values.getLong(i));
        }
    }

    /**
     * Writes packed list of sint64
     *
     * @param number field number
     * @param values values to write
     * @throws IOException in case of any data write error
     */
    public void writeSint64Packed(int number, LongList values) throws IOException {
        if (values.isEmpty()) {
            return;
        }

        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.ofSint64Packed(values));
        for (int i = 0; i < values.size(); i++) {
            output.writeZigZag64(values.getLong(i));
        }
    }

    /**
     * Writes fixed32
     *
     * @param number field number
     * @param value  value to write
     * @throws IOException in case of any data write error
     */
    public void writeFixed32(int number, int value) throws IOException {
        if (value != 0) {
            output.writeVarint32(I32.tagFrom(number));
            output.writeFixedInt(value);
        }
    }

    /**
     * Writes unpacked list of fixed32
     *
     * @param number field number
     * @param values values to write
     * @throws IOException in case of any data write error
     */
    public void writeFixed32Unpacked(int number, IntList values) throws IOException {
        for (int i = 0; i < values.size(); i++) {
            output.writeVarint32(I32.tagFrom(number));
            output.writeFixedInt(values.getInt(i));
        }
    }

    /**
     * Writes packed list of fixed32
     *
     * @param number field number
     * @param values values to write
     * @throws IOException in case of any data write error
     */
    public void writeFixed32Packed(int number, IntList values) throws IOException {
        if (values.isEmpty()) {
            return;
        }

        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.ofFixed32Packed(values));
        for (int i = 0; i < values.size(); i++) {
            output.writeFixedInt(values.getInt(i));
        }
    }

    /**
     * Writes fixed64
     *
     * @param number field number
     * @param value  value to write
     * @throws IOException in case of any data write error
     */
    public void writeFixed64(int number, long value) throws IOException {
        if (value != 0L) {
            output.writeVarint32(I64.tagFrom(number));
            output.writeFixedLong(value);
        }
    }

    /**
     * Writes unpacked list of fixed64
     *
     * @param number field number
     * @param values values to write
     * @throws IOException in case of any data write error
     */
    public void writeFixed64Unpacked(int number, LongList values) throws IOException {
        for (int i = 0; i < values.size(); i++) {
            output.writeVarint32(I64.tagFrom(number));
            output.writeFixedLong(values.getLong(i));
        }
    }

    /**
     * Writes packed list of fixed64
     *
     * @param number field number
     * @param values values to write
     * @throws IOException in case of any data write error
     */
    public void writeFixed64Packed(int number, LongList values) throws IOException {
        if (values.isEmpty()) {
            return;
        }

        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.ofFixed64Packed(values));
        for (int i = 0; i < values.size(); i++) {
            output.writeFixedLong(values.getLong(i));
        }
    }

    /**
     * Writes sfixed32
     *
     * @param number field number
     * @param value  value to write
     * @throws IOException in case of any data write error
     */
    public void writeSfixed32(int number, int value) throws IOException {
        if (value != 0) {
            output.writeVarint32(I32.tagFrom(number));
            output.writeFixedInt(value);
        }
    }

    /**
     * Writes unpacked list of sfixed32
     *
     * @param number field number
     * @param values values to write
     * @throws IOException in case of any data write error
     */
    public void writeSfixed32Unpacked(int number, IntList values) throws IOException {
        for (int i = 0; i < values.size(); i++) {
            output.writeVarint32(I32.tagFrom(number));
            output.writeFixedInt(values.getInt(i));
        }
    }

    /**
     * Writes packed list of sfixed32
     *
     * @param number field number
     * @param values values to write
     * @throws IOException in case of any data write error
     */
    public void writeSfixed32Packed(int number, IntList values) throws IOException {
        if (values.isEmpty()) {
            return;
        }

        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.ofSfixed32Packed(values));
        for (int i = 0; i < values.size(); i++) {
            output.writeFixedInt(values.getInt(i));
        }
    }

    /**
     * Writes sfixed64
     *
     * @param number field number
     * @param value  value to write
     * @throws IOException in case of any data write error
     */
    public void writeSfixed64(int number, long value) throws IOException {
        if (value != 0L) {
            output.writeVarint32(I64.tagFrom(number));
            output.writeFixedLong(value);
        }
    }

    /**
     * Writes unpacked list of sfixed64
     *
     * @param number field number
     * @param values values to write
     * @throws IOException in case of any data write error
     */
    public void writeSfixed64Unpacked(int number, LongList values) throws IOException {
        for (int i = 0; i < values.size(); i++) {
            output.writeVarint32(I64.tagFrom(number));
            output.writeFixedLong(values.getLong(i));
        }
    }

    /**
     * Writes packed list of sfixed64
     *
     * @param number field number
     * @param values values to write
     * @throws IOException in case of any data write error
     */
    public void writeSfixed64Packed(int number, LongList values) throws IOException {
        if (values.isEmpty()) {
            return;
        }

        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.ofSfixed64Packed(values));
        for (int i = 0; i < values.size(); i++) {
            output.writeFixedLong(values.getLong(i));
        }
    }

    /**
     * Writes bool
     *
     * @param number field number
     * @param value  value to write
     * @throws IOException in case of any data write error
     */
    public void writeBool(int number, boolean value) throws IOException {
        if (value) {
            output.writeVarint32(VARINT.tagFrom(number));
            output.writeBoolean(true);
        }
    }

    /**
     * Writes unpacked list of bool
     *
     * @param number field number
     * @param values values to write
     * @throws IOException in case of any data write error
     */
    public void writeBoolUnpacked(int number, BooleanList values) throws IOException {
        for (int i = 0; i < values.size(); i++) {
            output.writeVarint32(VARINT.tagFrom(number));
            output.writeBoolean(values.getBoolean(i));
        }
    }

    /**
     * Writes packed list of bool
     *
     * @param number field number
     * @param values values to write
     * @throws IOException in case of any data write error
     */
    public void writeBoolPacked(int number, BooleanList values) throws IOException {
        if (values.isEmpty()) {
            return;
        }

        output.writeVarint32(LEN.tagFrom(number));
        output.writeVarint32(Size.ofBoolPacked(values));
        for (int i = 0; i < values.size(); i++) {
            output.writeBoolean(values.getBoolean(i));
        }
    }

    /**
     * Writes string
     *
     * @param number field number
     * @param value  value to write
     * @throws IOException in case of any data write error
     */
    public void writeString(int number, String value) throws IOException {
        if (!"".equals(value)) {
            output.writeVarint32(LEN.tagFrom(number));
            output.writeString(value);
        }
    }

    /**
     * Writes unpacked list of string
     *
     * @param number field number
     * @param values values to write
     * @throws IOException in case of any data write error
     */
    public void writeStringUnpacked(int number, ObjectList<String> values) throws IOException {
        for (String value : values) {
            output.writeVarint32(LEN.tagFrom(number));
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
    @SuppressWarnings("deprecation")
    public void writeBytes(int number, ByteArray value) throws IOException {
        if (!value.isEmpty()) {
            output.writeVarint32(LEN.tagFrom(number));
            output.writeBytes(value.internalData());
        }
    }

    /**
     * Writes unpacked list of bytes
     *
     * @param number field number
     * @param values values to write
     * @throws IOException in case of any data write error
     */
    @SuppressWarnings("deprecation")
    public void writeBytesUnpacked(int number, ObjectList<ByteArray> values) throws IOException {
        for (ByteArray value : values) {
            output.writeVarint32(LEN.tagFrom(number));
            output.writeBytes(value.internalData());
        }
    }

    /**
     * Write message
     *
     * @param number field number
     * @param value  message to write
     * @throws IOException in case of any data write error
     */
    public void writeMessage(int number, ProtobufMessage<?> value) throws IOException {
        if (value != null) {
            output.writeVarint32(LEN.tagFrom(number));
            output.writeVarint32(value.protobufSize());
            value.writeTo(this);
        }
    }

    /**
     * Write unpacked list of messages
     *
     * @param number field number
     * @param values messages to write
     * @throws IOException in case of any data write error
     */
    public void writeMessageUnpacked(int number, ObjectList<? extends ProtobufMessage<?>> values) throws IOException {
        for (ProtobufMessage<?> value : values) {
            output.writeVarint32(LEN.tagFrom(number));
            output.writeVarint32(value.protobufSize());
            value.writeTo(this);
        }
    }

    /**
     * Write unknown fields
     *
     * @param values fields
     * @throws IOException in case of any data write error
     */
    public void writeUnknownFieldsUnpacked(ObjectList<UnknownField> values) throws IOException {
        for (UnknownField value : values) {
            value.writeTo(output);
        }
    }

    /**
     * Write unpacked list of enums
     *
     * @param number field number
     * @param values enums to write
     * @throws IOException in case of any data write error
     */
    public void writeEnumUnpacked(int number, EnumList<?> values) throws IOException {
        writeInt32Unpacked(number, values.valuesList());
    }

    /**
     * Write packed list of enums
     *
     * @param number field number
     * @param values enums to write
     * @throws IOException in case of any data write error
     */
    public void writeEnumPacked(int number, EnumList<?> values) throws IOException {
        writeInt32Packed(number, values.valuesList());
    }

    @Override
    public void close() throws IOException {
        output.close();
    }
}
