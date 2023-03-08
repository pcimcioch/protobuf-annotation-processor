package com.github.pcimcioch.protobuf.io;

import com.github.pcimcioch.protobuf.dto.ByteArray;
import com.github.pcimcioch.protobuf.dto.ProtobufMessage;

import java.util.IdentityHashMap;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Utils to compute size of given value
 */
public class Size {
    private static final Size INSTANCE = new Size();

    /**
     * Constructor
     */
    protected Size() {
    }

    /**
     * Returns double size
     *
     * @param number tag number
     * @param value  value
     * @return size
     */
    public int double_(int number, double value) {
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
    public int double_Unpacked(int number, List<Double> values) {
        return values.size() * (tagSize(number) + 8);
    }

    /**
     * Returns float size
     *
     * @param number tag number
     * @param value  value
     * @return size
     */
    public int float_(int number, float value) {
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
    public int float_Unpacked(int number, List<Float> values) {
        return values.size() * (tagSize(number) + 4);
    }

    /**
     * Returns int32 size
     *
     * @param number tag number
     * @param value  value
     * @return size
     */
    public int int32(int number, int value) {
        if (value == 0) {
            return 0;
        }

        return tagSize(number) + varint32Size(value);
    }

    /**
     * Returns list of int32 size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public int int32Unpacked(int number, List<Integer> values) {
        int size = tagSize(number) * values.size();
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
    public int int64(int number, long value) {
        if (value == 0L) {
            return 0;
        }

        return tagSize(number) + varint64Size(value);
    }

    /**
     * Returns list of int64 size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public int int64Unpacked(int number, List<Long> values) {
        int size = tagSize(number) * values.size();
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
    public int uint32(int number, int value) {
        if (value == 0) {
            return 0;
        }

        return tagSize(number) + varint32Size(value);
    }

    /**
     * Returns list of yint32 size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public int uint32Unpacked(int number, List<Integer> values) {
        int size = tagSize(number) * values.size();
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
    public int uint64(int number, long value) {
        if (value == 0L) {
            return 0;
        }

        return tagSize(number) + varint64Size(value);
    }

    /**
     * Returns list of uint64 size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public int uint64Unpacked(int number, List<Long> values) {
        int size = tagSize(number) * values.size();
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
    public int sint32(int number, int value) {
        if (value == 0) {
            return 0;
        }

        return tagSize(number) + zigzag32Size(value);
    }

    /**
     * Returns sint32 size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public int sint32Unpacked(int number, List<Integer> values) {
        int size = tagSize(number) * values.size();
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
    public int sint64(int number, long value) {
        if (value == 0L) {
            return 0;
        }

        return tagSize(number) + zigzag64Size(value);
    }

    /**
     * Returns list of sint64 size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public int sint64Unpacked(int number, List<Long> values) {
        int size = tagSize(number) * values.size();
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
    public int fixed32(int number, int value) {
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
    public int fixed32Unpacked(int number, List<Integer> values) {
        return values.size() * (tagSize(number) + 4);
    }

    /**
     * Returns fixed64 size
     *
     * @param number tag number
     * @param value  value
     * @return size
     */
    public int fixed64(int number, long value) {
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
    public int fixed64Unpacked(int number, List<Long> values) {
        return values.size() * (tagSize(number) + 8);
    }

    /**
     * Returns sfixed32 size
     *
     * @param number tag number
     * @param value  value
     * @return size
     */
    public int sfixed32(int number, int value) {
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
    public int sfixed32Unpacked(int number, List<Integer> values) {
        return values.size() * (tagSize(number) + 4);
    }

    /**
     * Returns sfixed64 size
     *
     * @param number tag number
     * @param value  value
     * @return size
     */
    public int sfixed64(int number, long value) {
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
    public int sfixed64Unpacked(int number, List<Long> values) {
        return values.size() * (tagSize(number) + 8);
    }

    /**
     * Returns bool size
     *
     * @param number tag number
     * @param value  value
     * @return size
     */
    public int bool(int number, boolean value) {
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
    public int boolUnpacked(int number, List<Boolean> values) {
        return values.size() * (tagSize(number) + 1);
    }

    /**
     * Returns bytes size
     *
     * @param number tag number
     * @param value  value
     * @return size
     */
    public int bytes(int number, ByteArray value) {
        if (value.isEmpty()) {
            return 0;
        }

        return tagSize(number) + varint32Size(value.length()) + value.length();
    }

    /**
     * Returns list of bytes size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public int bytesUnpacked(int number, List<ByteArray> values) {
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
    public int string(int number, String value) {
        if ("".equals(value)) {
            return 0;
        }
        int valueSize = stringSize(value);

        return tagSize(number) + varint32Size(valueSize) + valueSize;
    }

    /**
     * Returns list of string size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public int stringUnpacked(int number, List<String> values) {
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
    public int message(int number, ProtobufMessage<?> value) {
        if (value == null) {
            return 0;
        }

        int valueSize = messageSize(value);
        return tagSize(number) + varint32Size(valueSize) + valueSize;
    }

    /**
     * Returns list of message size
     *
     * @param number tag number
     * @param values values
     * @return size
     */
    public int messageUnpacked(int number, List<? extends ProtobufMessage<?>> values) {
        int size = tagSize(number) * values.size();
        for (ProtobufMessage<?> value : values) {
            int valueSize = messageSize(value);
            size += varint32Size(valueSize) + valueSize;
        }

        return size;
    }

    /**
     * Returns tag size
     *
     * @param number number
     * @return size
     */
    protected int tagSize(int number) {
        return varint32Size(number << 3);
    }

    /**
     * Returns varint32 size
     *
     * @param value value
     * @return size
     */
    protected int varint32Size(int value) {
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

    /**
     * Returns varint64 size
     *
     * @param value value
     * @return size
     */
    protected int varint64Size(long value) {
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

    /**
     * Returns zigzag32 size
     *
     * @param value value
     * @return size
     */
    protected int zigzag32Size(int value) {
        return varint32Size((value << 1) ^ (value >> 31));
    }

    /**
     * Returns zigzag64 size
     *
     * @param value value
     * @return size
     */
    protected int zigzag64Size(long value) {
        return varint64Size((value << 1) ^ (value >> 63));
    }

    /**
     * Returns string size
     *
     * @param value value
     * @return size
     */
    protected int stringSize(String value) {
        // TODO [performance] this is quite slow and high-resource use
        return value.getBytes(UTF_8).length;
    }

    /**
     * Returns message size
     *
     * @param value value
     * @return size
     */
    protected int messageSize(ProtobufMessage<?> value) {
        return value.protobufSize(this);
    }

    /**
     * Returns size computer that will always recompute size of the whole message including its nested messages
     *
     * @return size computer
     */
    public static Size inPlace() {
        return INSTANCE;
    }

    /**
     * Returns size computer that will cache nested messages sizes for performance improvement with the cost of additional space requirements
     *
     * @return size computer
     */
    public static Size cached() {
        return new Cached();
    }

    private static final class Cached extends Size {
        private final IdentityHashMap<ProtobufMessage<?>, Integer> sizes = new IdentityHashMap<>();

        @Override
        protected int messageSize(ProtobufMessage<?> value) {
            // TODO [performance] does using lambda here costs us performance?
            return sizes.computeIfAbsent(value, super::messageSize);
        }
    }
}
