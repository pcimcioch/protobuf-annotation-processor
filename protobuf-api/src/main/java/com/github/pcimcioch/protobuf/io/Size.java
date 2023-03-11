package com.github.pcimcioch.protobuf.io;

import com.github.pcimcioch.protobuf.dto.ByteArray;
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
    public static int double_(int number, double value) {
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
    public static int double_Unpacked(int number, List<Double> values) {
        return values.size() * (tagSize(number) + 8);
    }

    /**
     * Returns packed list of doubles size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public static int double_Packed(int number, List<Double> values) {
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
    public static int double_Packed(List<Double> values) {
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
    public static int float_(int number, float value) {
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
    public static int float_Unpacked(int number, List<Float> values) {
        return values.size() * (tagSize(number) + 4);
    }

    /**
     * Returns packed list of floats size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public static int float_Packed(int number, List<Float> values) {
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
    public static int float_Packed(List<Float> values) {
        if (values.isEmpty()) {
            return 0;
        }

        return values.size() * 4;
    }

    /**
     * Returns int32 size
     *
     * @param number tag number
     * @param value  value
     * @return size
     */
    public static int int32(int number, int value) {
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
    public static int int32Unpacked(int number, List<Integer> values) {
        int size = tagSize(number) * values.size();
        for (Integer value : values) {
            size += varint32Size(value);
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
    public static int int32Packed(int number, List<Integer> values) {
        if (values.isEmpty()) {
            return 0;
        }

        int size = 0;
        for (Integer value : values) {
            size += varint32Size(value);
        }

        return tagSize(number) + varint32Size(size) + size;
    }

    /**
     * Returns packed list of int32 size without tag
     *
     * @param values values
     * @return size
     */
    // TODO add tests
    public static int int32Packed(List<Integer> values) {
        if (values.isEmpty()) {
            return 0;
        }

        int size = 0;
        for (Integer value : values) {
            size += varint32Size(value);
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
    public static int int64(int number, long value) {
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
    public static int int64Unpacked(int number, List<Long> values) {
        int size = tagSize(number) * values.size();
        for (Long value : values) {
            size += varint64Size(value);
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
    public static int int64Packed(int number, List<Long> values) {
        if (values.isEmpty()) {
            return 0;
        }

        int size = 0;
        for (Long value : values) {
            size += varint64Size(value);
        }

        return tagSize(number) + varint32Size(size) + size;
    }

    /**
     * Returns unpacked list of int64 size without tag
     *
     * @param values values
     * @return size
     */
    // TODO add tests
    public static int int64Packed(List<Long> values) {
        if (values.isEmpty()) {
            return 0;
        }

        int size = 0;
        for (Long value : values) {
            size += varint64Size(value);
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
    public static int uint32(int number, int value) {
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
    public static int uint32Unpacked(int number, List<Integer> values) {
        int size = tagSize(number) * values.size();
        for (Integer value : values) {
            size += varint32Size(value);
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
    public static int uint32Packed(int number, List<Integer> values) {
        if (values.isEmpty()) {
            return 0;
        }

        int size = 0;
        for (Integer value : values) {
            size += varint32Size(value);
        }

        return tagSize(number) + varint32Size(size) + size;
    }

    /**
     * Returns unpacked list of yint32 size without tag
     *
     * @param values values
     * @return size
     */
    // TODO add tests
    public static int uint32Packed(List<Integer> values) {
        if (values.isEmpty()) {
            return 0;
        }

        int size = 0;
        for (Integer value : values) {
            size += varint32Size(value);
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
    public static int uint64(int number, long value) {
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
    public static int uint64Unpacked(int number, List<Long> values) {
        int size = tagSize(number) * values.size();
        for (Long value : values) {
            size += varint64Size(value);
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
    public static int uint64Packed(int number, List<Long> values) {
        if (values.isEmpty()) {
            return 0;
        }

        int size = 0;
        for (Long value : values) {
            size += varint64Size(value);
        }

        return tagSize(number) + varint32Size(size) + size;
    }

    /**
     * Returns packed list of uint64 size without tag
     *
     * @param values values
     * @return size
     */
    // TODO add tests
    public static int uint64Packed(List<Long> values) {
        if (values.isEmpty()) {
            return 0;
        }

        int size = 0;
        for (Long value : values) {
            size += varint64Size(value);
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
    public static int sint32(int number, int value) {
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
    public static int sint32Unpacked(int number, List<Integer> values) {
        int size = tagSize(number) * values.size();
        for (Integer value : values) {
            size += zigzag32Size(value);
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
    public static int sint32Packed(int number, List<Integer> values) {
        if (values.isEmpty()) {
            return 0;
        }

        int size = 0;
        for (Integer value : values) {
            size += zigzag32Size(value);
        }

        return tagSize(number) + varint32Size(size) + size;
    }

    /**
     * Returns packed list of sint32 size without tag
     *
     * @param values values
     * @return size
     */
    // TODO add tests
    public static int sint32Packed(List<Integer> values) {
        if (values.isEmpty()) {
            return 0;
        }

        int size = 0;
        for (Integer value : values) {
            size += zigzag32Size(value);
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
    public static int sint64(int number, long value) {
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
    public static int sint64Unpacked(int number, List<Long> values) {
        int size = tagSize(number) * values.size();
        for (Long value : values) {
            size += zigzag64Size(value);
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
    public static int sint64Packed(int number, List<Long> values) {
        if (values.isEmpty()) {
            return 0;
        }

        int size = 0;
        for (Long value : values) {
            size += zigzag64Size(value);
        }

        return tagSize(number) + varint32Size(size) + size;
    }

    /**
     * Returns packed list of sint64 size without tag
     *
     * @param values values
     * @return size
     */
    // TODO ad dtest
    public static int sint64Packed(List<Long> values) {
        if (values.isEmpty()) {
            return 0;
        }

        int size = 0;
        for (Long value : values) {
            size += zigzag64Size(value);
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
    public static int fixed32(int number, int value) {
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
    public static int fixed32Unpacked(int number, List<Integer> values) {
        return values.size() * (tagSize(number) + 4);
    }

    /**
     * Returns packed list of fixed32 size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public static int fixed32Packed(int number, List<Integer> values) {
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
    // TODO add tests
    public static int fixed32Packed(List<Integer> values) {
        if (values.isEmpty()) {
            return 0;
        }

        return values.size() * 4;
    }

    /**
     * Returns fixed64 size
     *
     * @param number tag number
     * @param value  value
     * @return size
     */
    public static int fixed64(int number, long value) {
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
    public static int fixed64Unpacked(int number, List<Long> values) {
        return values.size() * (tagSize(number) + 8);
    }

    /**
     * Returns packed list of fixed64 size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public static int fixed64Packed(int number, List<Long> values) {
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
    // TODO add tests
    public static int fixed64Packed(List<Long> values) {
        if (values.isEmpty()) {
            return 0;
        }

        return values.size() * 8;
    }

    /**
     * Returns sfixed32 size
     *
     * @param number tag number
     * @param value  value
     * @return size
     */
    public static int sfixed32(int number, int value) {
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
    public static int sfixed32Unpacked(int number, List<Integer> values) {
        return values.size() * (tagSize(number) + 4);
    }

    /**
     * Returns packed list of sfixed32 size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public static int sfixed32Packed(int number, List<Integer> values) {
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
    // TODO add tests
    public static int sfixed32Packed(List<Integer> values) {
        if (values.isEmpty()) {
            return 0;
        }

        return values.size() * 4;
    }

    /**
     * Returns sfixed64 size
     *
     * @param number tag number
     * @param value  value
     * @return size
     */
    public static int sfixed64(int number, long value) {
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
    public static int sfixed64Unpacked(int number, List<Long> values) {
        return values.size() * (tagSize(number) + 8);
    }

    /**
     * Returns packed list of sfixed64 size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public static int sfixed64Packed(int number, List<Long> values) {
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
    // TODO add tests
    public static int sfixed64Packed(List<Long> values) {
        if (values.isEmpty()) {
            return 0;
        }

        return values.size() * 8;
    }

    /**
     * Returns bool size
     *
     * @param number tag number
     * @param value  value
     * @return size
     */
    public static int bool(int number, boolean value) {
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
    public static int boolUnpacked(int number, List<Boolean> values) {
        return values.size() * (tagSize(number) + 1);
    }

    /**
     * Returns packed list of bool size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public static int boolPacked(int number, List<Boolean> values) {
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
    // TODO add tests
    public static int boolPacked(List<Boolean> values) {
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
    public static int bytes(int number, ByteArray value) {
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
    public static int bytesUnpacked(int number, List<ByteArray> values) {
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
    public static int string(int number, String value) {
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
    public static int stringUnpacked(int number, List<String> values) {
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
    public static int message(int number, ProtobufMessage<?> value) {
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
    public static int messageUnpacked(int number, List<? extends ProtobufMessage<?>> values) {
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
