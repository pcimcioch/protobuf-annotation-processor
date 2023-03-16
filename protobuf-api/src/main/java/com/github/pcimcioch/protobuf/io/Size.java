package com.github.pcimcioch.protobuf.io;

import com.github.pcimcioch.protobuf.dto.ByteArray;
import com.github.pcimcioch.protobuf.dto.DoubleList;
import com.github.pcimcioch.protobuf.dto.FloatList;
import com.github.pcimcioch.protobuf.dto.IntList;
import com.github.pcimcioch.protobuf.dto.LongList;
import com.github.pcimcioch.protobuf.dto.ProtobufMessage;

import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Utils to compute size of given value
 */
public final class Size {

    private Size() {
    }

    /**
     * Returns double size
     *
     * @param number tag number
     * @param value  value
     * @return size
     */
    public static int ofDouble(int number, double value) {
        if (value == 0d) {
            return 0;
        }

        return tagSize(number) + 8;
    }

    /**
     * Returns unpacked list of doubles size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public static int ofDoubleUnpacked(int number, DoubleList values) {
        return values.size() * (tagSize(number) + 8);
    }

    /**
     * Returns packed list of doubles size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public static int ofDoublePacked(int number, DoubleList values) {
        if (values.isEmpty()) {
            return 0;
        }
        int size = values.size() * 8;
        return tagSize(number) + varint32Size(size) + size;
    }

    /**
     * Returns packed list of doubles size without tag
     *
     * @param values values
     * @return size
     */
    public static int ofDoublePacked(DoubleList values) {
        if (values.isEmpty()) {
            return 0;
        }

        return values.size() * 8;
    }

    /**
     * Returns float size
     *
     * @param number tag number
     * @param value  value
     * @return size
     */
    public static int ofFloat(int number, float value) {
        if (value == 0f) {
            return 0;
        }

        return tagSize(number) + 4;
    }

    /**
     * Returns unpacked list of floats size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public static int ofFloatUnpacked(int number, FloatList values) {
        return values.size() * (tagSize(number) + 4);
    }

    /**
     * Returns packed list of floats size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public static int ofFloatPacked(int number, FloatList values) {
        if (values.isEmpty()) {
            return 0;
        }
        int size = values.size() * 4;
        return tagSize(number) + varint32Size(size) + size;
    }

    /**
     * Returns packed list of floats size without tag
     *
     * @param values values
     * @return size
     */
    public static int ofFloatPacked(FloatList values) {
        return values.size() * 4;
    }



    /**
     * Returns int32 size
     *
     * @param number tag number
     * @param value  value
     * @return size
     */
    public static int ofInt32(int number, int value) {
        if (value == 0) {
            return 0;
        }

        return tagSize(number) + varint32Size(value);
    }

    /**
     * Returns unpacked list of int32 size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public static int ofInt32Unpacked(int number, IntList values) {
        int size = tagSize(number) * values.size();
        for (int i = 0; i < values.size(); i++) {
            size += varint32Size(values.getInt(i));
        }

        return size;
    }

    /**
     * Returns packed list of int32 size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public static int ofInt32Packed(int number, IntList values) {
        if (values.isEmpty()) {
            return 0;
        }

        int size = 0;
        for (int i = 0; i < values.size(); i++) {
            size += varint32Size(values.getInt(i));
        }

        return tagSize(number) + varint32Size(size) + size;
    }

    /**
     * Returns packed list of int32 size without tag
     *
     * @param values values
     * @return size
     */
    public static int ofInt32Packed(IntList values) {
        if (values.isEmpty()) {
            return 0;
        }

        int size = 0;
        for (int i = 0; i < values.size(); i++) {
            size += varint32Size(values.getInt(i));
        }

        return size;
    }

    /**
     * Returns int64 size
     *
     * @param number tag number
     * @param value  value
     * @return size
     */
    public static int ofInt64(int number, long value) {
        if (value == 0L) {
            return 0;
        }

        return tagSize(number) + varint64Size(value);
    }

    /**
     * Returns unpacked list of int64 size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public static int ofInt64Unpacked(int number, LongList values) {
        int size = tagSize(number) * values.size();
        for (int i = 0; i < values.size(); i++) {
            size += varint64Size(values.getLong(i));
        }

        return size;
    }

    /**
     * Returns unpacked list of int64 size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public static int ofInt64Packed(int number, LongList values) {
        if (values.isEmpty()) {
            return 0;
        }

        int size = 0;
        for (int i = 0; i < values.size(); i++) {
            size += varint64Size(values.getLong(i));
        }

        return tagSize(number) + varint32Size(size) + size;
    }

    /**
     * Returns unpacked list of int64 size without tag
     *
     * @param values values
     * @return size
     */
    public static int ofInt64Packed(LongList values) {
        if (values.isEmpty()) {
            return 0;
        }

        int size = 0;
        for (int i = 0; i < values.size(); i++) {
            size += varint64Size(values.getLong(i));
        }

        return size;
    }

    /**
     * Returns uint32 size
     *
     * @param number tag number
     * @param value  value
     * @return size
     */
    public static int ofUint32(int number, int value) {
        if (value == 0) {
            return 0;
        }

        return tagSize(number) + varint32Size(value);
    }

    /**
     * Returns unpacked list of yint32 size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public static int ofUint32Unpacked(int number, IntList values) {
        int size = tagSize(number) * values.size();
        for (int i = 0; i < values.size(); i++) {
            size += varint32Size(values.getInt(i));
        }

        return size;
    }

    /**
     * Returns unpacked list of yint32 size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public static int ofUint32Packed(int number, IntList values) {
        if (values.isEmpty()) {
            return 0;
        }

        int size = 0;
        for (int i = 0; i < values.size(); i++) {
            size += varint32Size(values.getInt(i));
        }

        return tagSize(number) + varint32Size(size) + size;
    }

    /**
     * Returns unpacked list of yint32 size without tag
     *
     * @param values values
     * @return size
     */
    public static int ofUint32Packed(IntList values) {
        if (values.isEmpty()) {
            return 0;
        }

        int size = 0;
        for (int i = 0; i < values.size(); i++) {
            size += varint32Size(values.getInt(i));
        }

        return size;
    }

    /**
     * Returns uint64 size
     *
     * @param number tag number
     * @param value  value
     * @return size
     */
    public static int ofUint64(int number, long value) {
        if (value == 0L) {
            return 0;
        }

        return tagSize(number) + varint64Size(value);
    }

    /**
     * Returns unpacked list of uint64 size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public static int ofUint64Unpacked(int number, LongList values) {
        int size = tagSize(number) * values.size();
        for (int i = 0; i < values.size(); i++) {
            size += varint64Size(values.getLong(i));
        }

        return size;
    }

    /**
     * Returns packed list of uint64 size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public static int ofUint64Packed(int number, LongList values) {
        if (values.isEmpty()) {
            return 0;
        }

        int size = 0;
        for (int i = 0; i < values.size(); i++) {
            size += varint64Size(values.getLong(i));
        }

        return tagSize(number) + varint32Size(size) + size;
    }

    /**
     * Returns packed list of uint64 size without tag
     *
     * @param values values
     * @return size
     */
    public static int ofUint64Packed(LongList values) {
        if (values.isEmpty()) {
            return 0;
        }

        int size = 0;
        for (int i = 0; i < values.size(); i++) {
            size += varint64Size(values.getLong(i));
        }

        return size;
    }

    /**
     * Returns sint32 size
     *
     * @param number tag number
     * @param value  value
     * @return size
     */
    public static int ofSint32(int number, int value) {
        if (value == 0) {
            return 0;
        }

        return tagSize(number) + zigzag32Size(value);
    }

    /**
     * Returns unpacked list of sint32 size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public static int ofSint32Unpacked(int number, IntList values) {
        int size = tagSize(number) * values.size();
        for (int i = 0; i < values.size(); i++) {
            size += zigzag32Size(values.getInt(i));
        }

        return size;
    }

    /**
     * Returns packed list of sint32 size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public static int ofSint32Packed(int number, IntList values) {
        if (values.isEmpty()) {
            return 0;
        }

        int size = 0;
        for (int i = 0; i < values.size(); i++) {
            size += zigzag32Size(values.getInt(i));
        }

        return tagSize(number) + varint32Size(size) + size;
    }

    /**
     * Returns packed list of sint32 size without tag
     *
     * @param values values
     * @return size
     */
    public static int ofSint32Packed(IntList values) {
        if (values.isEmpty()) {
            return 0;
        }

        int size = 0;
        for (int i = 0; i < values.size(); i++) {
            size += zigzag32Size(values.getInt(i));
        }

        return size;
    }

    /**
     * Returns sint64 size
     *
     * @param number tag number
     * @param value  value
     * @return size
     */
    public static int ofSint64(int number, long value) {
        if (value == 0L) {
            return 0;
        }

        return tagSize(number) + zigzag64Size(value);
    }

    /**
     * Returns unpacked list of sint64 size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public static int ofSint64Unpacked(int number, LongList values) {
        int size = tagSize(number) * values.size();
        for (int i = 0; i < values.size(); i++) {
            size += zigzag64Size(values.getLong(i));
        }

        return size;
    }

    /**
     * Returns packed list of sint64 size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public static int ofSint64Packed(int number, LongList values) {
        if (values.isEmpty()) {
            return 0;
        }

        int size = 0;
        for (int i = 0; i < values.size(); i++) {
            size += zigzag64Size(values.getLong(i));
        }

        return tagSize(number) + varint32Size(size) + size;
    }

    /**
     * Returns packed list of sint64 size without tag
     *
     * @param values values
     * @return size
     */
    public static int ofSint64Packed(LongList values) {
        if (values.isEmpty()) {
            return 0;
        }

        int size = 0;
        for (int i = 0; i < values.size(); i++) {
            size += zigzag64Size(values.getLong(i));
        }

        return size;
    }

    /**
     * Returns fixed32 size
     *
     * @param number tag number
     * @param value  value
     * @return size
     */
    public static int ofFixed32(int number, int value) {
        if (value == 0) {
            return 0;
        }

        return tagSize(number) + 4;
    }

    /**
     * Returns unpacked list of fixed32 size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public static int ofFixed32Unpacked(int number, IntList values) {
        return values.size() * (tagSize(number) + 4);
    }

    /**
     * Returns packed list of fixed32 size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public static int ofFixed32Packed(int number, IntList values) {
        if (values.isEmpty()) {
            return 0;
        }

        int size = values.size() * 4;
        return tagSize(number) + varint32Size(size) + size;
    }

    /**
     * Returns packed list of fixed32 size without tag
     *
     * @param values values
     * @return size
     */
    public static int ofFixed32Packed(IntList values) {
        return values.size() * 4;
    }

    /**
     * Returns fixed64 size
     *
     * @param number tag number
     * @param value  value
     * @return size
     */
    public static int ofFixed64(int number, long value) {
        if (value == 0L) {
            return 0;
        }

        return tagSize(number) + 8;
    }

    /**
     * Returns unpacked list of fixed64 size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public static int ofFixed64Unpacked(int number, LongList values) {
        return values.size() * (tagSize(number) + 8);
    }

    /**
     * Returns packed list of fixed64 size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public static int ofFixed64Packed(int number, LongList values) {
        if (values.isEmpty()) {
            return 0;
        }

        int size = values.size() * 8;
        return tagSize(number) + varint32Size(size) + size;
    }

    /**
     * Returns packed list of fixed64 size without tag
     *
     * @param values values
     * @return size
     */
    public static int ofFixed64Packed(LongList values) {
        return values.size() * 8;
    }

    /**
     * Returns sfixed32 size
     *
     * @param number tag number
     * @param value  value
     * @return size
     */
    public static int ofSfixed32(int number, int value) {
        if (value == 0) {
            return 0;
        }

        return tagSize(number) + 4;
    }

    /**
     * Returns unpacked list of sfixed32 size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public static int ofSfixed32Unpacked(int number, IntList values) {
        return values.size() * (tagSize(number) + 4);
    }

    /**
     * Returns packed list of sfixed32 size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public static int ofSfixed32Packed(int number, IntList values) {
        if (values.isEmpty()) {
            return 0;
        }

        int size = values.size() * 4;
        return tagSize(number) + varint32Size(size) + size;
    }

    /**
     * Returns packed list of sfixed32 size without tag
     *
     * @param values values
     * @return size
     */
    public static int ofSfixed32Packed(IntList values) {
        return values.size() * 4;
    }

    /**
     * Returns sfixed64 size
     *
     * @param number tag number
     * @param value  value
     * @return size
     */
    public static int ofSfixed64(int number, long value) {
        if (value == 0L) {
            return 0;
        }

        return tagSize(number) + 8;
    }

    /**
     * Returns unpacked list of sfixed64 size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public static int ofSfixed64Unpacked(int number, LongList values) {
        return values.size() * (tagSize(number) + 8);
    }

    /**
     * Returns packed list of sfixed64 size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public static int ofSfixed64Packed(int number, LongList values) {
        if (values.isEmpty()) {
            return 0;
        }

        int size = values.size() * 8;
        return tagSize(number) + varint32Size(size) + size;
    }

    /**
     * Returns packed list of sfixed64 size without tag
     *
     * @param values values
     * @return size
     */
    public static int ofSfixed64Packed(LongList values) {
        return values.size() * 8;
    }

    /**
     * Returns bool size
     *
     * @param number tag number
     * @param value  value
     * @return size
     */
    public static int ofBool(int number, boolean value) {
        if (!value) {
            return 0;
        }

        return tagSize(number) + 1;
    }

    /**
     * Returns unpacked list of bool size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public static int ofBoolUnpacked(int number, List<Boolean> values) {
        return values.size() * (tagSize(number) + 1);
    }

    /**
     * Returns packed list of bool size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public static int ofBoolPacked(int number, List<Boolean> values) {
        if (values.isEmpty()) {
            return 0;
        }

        return tagSize(number) + varint32Size(values.size()) + values.size();
    }

    /**
     * Returns packed list of bool size without tag
     *
     * @param values values
     * @return size
     */
    public static int ofBoolPacked(List<Boolean> values) {
        if (values.isEmpty()) {
            return 0;
        }

        return values.size();
    }

    /**
     * Returns bytes size
     *
     * @param number tag number
     * @param value  value
     * @return size
     */
    public static int ofBytes(int number, ByteArray value) {
        if (value.isEmpty()) {
            return 0;
        }

        return tagSize(number) + varint32Size(value.length()) + value.length();
    }

    /**
     * Returns unpacked list of bytes size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public static int ofBytesUnpacked(int number, List<ByteArray> values) {
        int size = tagSize(number) * values.size();
        for (ByteArray value : values) {
            size += varint32Size(value.length()) + value.length();
        }

        return size;
    }

    /**
     * Returns string size
     *
     * @param number tag number
     * @param value  value
     * @return size
     */
    public static int ofString(int number, String value) {
        if ("".equals(value)) {
            return 0;
        }
        int valueSize = stringSize(value);

        return tagSize(number) + varint32Size(valueSize) + valueSize;
    }

    /**
     * Returns unpacked list of string size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public static int ofStringUnpacked(int number, List<String> values) {
        int size = tagSize(number) * values.size();
        for (String value : values) {
            int valueSize = stringSize(value);
            size += varint32Size(valueSize) + valueSize;
        }

        return size;
    }

    /**
     * Returns message size
     *
     * @param number tag number
     * @param value  value
     * @return size
     */
    public static int ofMessage(int number, ProtobufMessage<?> value) {
        if (value == null) {
            return 0;
        }

        int valueSize = value.protobufSize();
        return tagSize(number) + varint32Size(valueSize) + valueSize;
    }

    /**
     * Returns unpacked list of message size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public static int ofMessageUnpacked(int number, List<? extends ProtobufMessage<?>> values) {
        int size = tagSize(number) * values.size();
        for (ProtobufMessage<?> value : values) {
            int valueSize = value.protobufSize();
            size += varint32Size(valueSize) + valueSize;
        }

        return size;
    }

    private static int tagSize(int number) {
        return varint32Size(number << 3);
    }

    private static int varint32Size(int value) {
        if ((value & (~0 << 7)) == 0) {
            return 1;
        }
        if ((value & (~0 << 14)) == 0) {
            return 2;
        }
        if ((value & (~0 << 21)) == 0) {
            return 3;
        }
        if ((value & (~0 << 28)) == 0) {
            return 4;
        }
        if (value < 0) {
            return 10;
        }
        return 5;
    }

    private static int varint64Size(long value) {
        if ((value & (~0L << 7)) == 0L) {
            return 1;
        }
        if (value < 0L) {
            return 10;
        }

        int bytes = 2;
        if ((value & (~0L << 35)) != 0L) {
            bytes += 4;
            value >>>= 28;
        }
        if ((value & (~0L << 21)) != 0L) {
            bytes += 2;
            value >>>= 14;
        }
        if ((value & (~0L << 14)) != 0L) {
            bytes += 1;
        }
        return bytes;
    }

    private static int zigzag32Size(int value) {
        return varint32Size((value << 1) ^ (value >> 31));
    }

    private static int zigzag64Size(long value) {
        return varint64Size((value << 1) ^ (value >> 63));
    }

    private static int stringSize(String value) {
        // TODO [performance] this is quite slow and high-resource use
        return value.getBytes(UTF_8).length;
    }
}
