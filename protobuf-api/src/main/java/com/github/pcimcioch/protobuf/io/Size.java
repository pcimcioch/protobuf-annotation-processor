package com.github.pcimcioch.protobuf.io;

import com.github.pcimcioch.protobuf.dto.ByteArray;

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
     * Returns list of doubles size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public static int double_(int number, List<Double> values) {
        return values.size() * (tagSize(number) + 8);
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
     * Returns list of floats size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public static int float_(int number, List<Float> values) {
        return values.size() * (tagSize(number) + 4);
    }

    /**
     * Returns int32 size
     *
     * @param number tag number
     * @param value  value
     * @return size
     */
    // TODO add tests
    public static int int32(int number, int value) {
        if (value == 0) {
            return 0;
        }

        return tagSize(number) + varint32(value);
    }

    /**
     * Returns list of int32 size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    // TODO add tests
    public static int int32(int number, List<Integer> values) {
        int size = tagSize(number) * values.size();
        for (Integer value : values) {
            size += varint32(value);
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
    // TODO add tests
    public static int int64(int number, long value) {
        if (value == 0L) {
            return 0;
        }

        return tagSize(number) + varint64(value);
    }

    /**
     * Returns list of int64 size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    // TODO add tests
    public static int int64(int number, List<Long> values) {
        int size = tagSize(number) * values.size();
        for (Long value : values) {
            size += varint64(value);
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
    // TODO add tests
    public static int uint32(int number, int value) {
        if (value == 0) {
            return 0;
        }

        return tagSize(number) + varint32(value);
    }

    /**
     * Returns list of yint32 size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    // TODO add tests
    public static int uint32(int number, List<Integer> values) {
        int size = tagSize(number) * values.size();
        for (Integer value : values) {
            size += varint32(value);
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
    // TODO add tests
    public static int uint64(int number, long value) {
        if (value == 0L) {
            return 0;
        }

        return tagSize(number) + varint64(value);
    }

    /**
     * Returns list of uint64 size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    // TODO add tests
    public static int uint64(int number, List<Long> values) {
        int size = tagSize(number) * values.size();
        for (Long value : values) {
            size += varint64(value);
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
    // TODO add tests
    public static int sint32(int number, int value) {
        if (value == 0) {
            return 0;
        }

        return tagSize(number) + zigzag32(value);
    }

    /**
     * Returns sint32 size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    // TODO add tests
    public static int sint32(int number, List<Integer> values) {
        int size = tagSize(number) * values.size();
        for (Integer value : values) {
            size += zigzag32(value);
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
    // TODO add tests
    public static int sint64(int number, long value) {
        if (value == 0L) {
            return 0;
        }

        return tagSize(number) + zigzag64(value);
    }

    /**
     * Returns list of sint64 size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    // TODO add tests
    public static int sint64(int number, List<Long> values) {
        int size = tagSize(number) * values.size();
        for (Long value : values) {
            size += zigzag64(value);
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
     * Returns list of fixed32 size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public static int fixed32(int number, List<Integer> values) {
        return values.size() * (tagSize(number) + 4);
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
     * Returns list of fixed64 size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public static int fixed64(int number, List<Long> values) {
        return values.size() * (tagSize(number) + 8);
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
     * Returns list of sfixed32 size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public static int sfixed32(int number, List<Integer> values) {
        return values.size() * (tagSize(number) + 4);
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
     * Returns list of sfixed64 size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public static int sfixed64(int number, List<Long> values) {
        return values.size() * (tagSize(number) + 8);
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
     * Returns list of bool size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public static int bool(int number, List<Boolean> values) {
        return values.size() * (tagSize(number) + 1);
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

        return tagSize(number) + varint32(value.length()) + value.length();
    }

    /**
     * Returns list of bytes size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public static int bytes(int number, List<ByteArray> values) {
        int size = tagSize(number) * values.size();
        for (ByteArray value : values) {
            size += varint32(value.length()) + value.length();
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
        // TODO [performance] this is quite slow and high-resource use
        byte[] data = value.getBytes(UTF_8);

        return tagSize(number) + varint32(data.length) + data.length;
    }

    /**
     * Returns list of string size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public static int string(int number, List<String> values) {
        int size = tagSize(number) * values.size();
        for (String value : values) {
            byte[] data = value.getBytes(UTF_8);
            size += varint32(data.length) + data.length;
        }

        return size;
    }

    private static int tagSize(int number) {
        return varint32(number << 3);
    }

    private static int varint32(int value) {
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

    private static int varint64(long value) {
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

    private static int zigzag32(int value) {
        return varint32((value << 1) ^ (value >> 31));
    }

    private static int zigzag64(long value) {
        return varint64((value << 1) ^ (value >> 63));
    }
}
