package com.github.pcimcioch.protobuf.dto;

import java.util.Collection;
import java.util.List;

/**
 * Utils for Protobuf Data Transfer Objects
 */
public final class ProtoDto {

    private ProtoDto() {
    }

    /**
     * Copy int value
     *
     * @param value value
     * @return value copy
     */
    public static int copy(int value) {
        return value;
    }

    /**
     * Copy long value
     *
     * @param value value
     * @return value copy
     */
    public static long copy(long value) {
        return value;
    }

    /**
     * Copy double value
     *
     * @param value value
     * @return value copy
     */
    public static double copy(double value) {
        return value;
    }

    /**
     * Copy float value
     *
     * @param value value
     * @return value copy
     */
    public static float copy(float value) {
        return value;
    }

    /**
     * Copy boolean value
     *
     * @param value value
     * @return value copy
     */
    public static boolean copy(boolean value) {
        return value;
    }

    /**
     * Copy String value
     *
     * @param value value
     * @return value copy
     */
    public static String copy(String value) {
        return value == null ? "" : value;
    }

    /**
     * Copy ByteArray value
     *
     * @param value value
     * @return value copy
     */
    public static ByteArray copy(ByteArray value) {
        return value == null || value.isEmpty() ? ByteArray.empty() : value;
    }

    /**
     * Copy list of doubles. Returned list is unmodifiable
     *
     * @param value list to copy
     * @return list copy
     */
    public static DoubleList copy(DoubleList value) {
        return value == null ? DoubleList.of() : value;
    }

    /**
     * Copy list of floats. Returned list is unmodifiable
     *
     * @param value list to copy
     * @return list copy
     */
    public static FloatList copy(FloatList value) {
        return value == null ? FloatList.of() : value;
    }

    /**
     * Copy list of longs. Returned list is unmodifiable
     *
     * @param value list to copy
     * @return list copy
     */
    public static LongList copy(LongList value) {
        return value == null ? LongList.of() : value;
    }

    /**
     * Copy list of ints. Returned list is unmodifiable
     *
     * @param value list to copy
     * @return list copy
     */
    public static IntList copy(IntList value) {
        return value == null ? IntList.of() : value;
    }

    /**
     * Copy list of boolean. Returned list is unmodifiable
     *
     * @param value list to copy
     * @return list copy
     */
    public static BooleanList copy(BooleanList value) {
        return value == null ? BooleanList.of() : value;
    }

    /**
     * Copy list of objects. Returned list is unmodifiable
     *
     * @param value list to copy
     * @param <T> element type
     * @return list copy
     */
    public static <T> ObjectList<T> copy(ObjectList<T> value) {
        return value == null ? ObjectList.of() : value;
    }

    /**
     * Copy message value
     *
     * @param value value
     * @param <T>   type of message
     * @return value copy
     */
    public static <T extends ProtobufMessage<T>> T copy(T value) {
        return value == null || value.isEmpty() ? null : value;
    }

    /**
     * Copy list of elements. Returned list is unmodifiable
     *
     * @param value list to copy
     * @param <T>   type of the list element
     * @return list copy
     */
    public static <T> List<T> copy(Collection<? extends T> value) {
        // TODO [performance] if we get rid of this copying it would be faster
        return value == null ? List.of() : List.copyOf(value);
    }

    /**
     * Merge two values. Uses toMerge if it is not default, uses current otherwise
     *
     * @param current current value
     * @param toMerge value to merge
     * @return current if toMerge is default, toMerge otherwise
     */
    public static int merge(int current, int toMerge) {
        return toMerge == 0 ? current : toMerge;
    }

    /**
     * Merge two values. Uses toMerge if it is not default, uses current otherwise
     *
     * @param current current value
     * @param toMerge value to merge
     * @return current if toMerge is default, toMerge otherwise
     */
    public static long merge(long current, long toMerge) {
        return toMerge == 0L ? current : toMerge;
    }

    /**
     * Merge two values. Uses toMerge if it is not default, uses current otherwise
     *
     * @param current current value
     * @param toMerge value to merge
     * @return current if toMerge is default, toMerge otherwise
     */
    public static float merge(float current, float toMerge) {
        return toMerge == 0f ? current : toMerge;
    }

    /**
     * Merge two values. Uses toMerge if it is not default, uses current otherwise
     *
     * @param current current value
     * @param toMerge value to merge
     * @return current if toMerge is default, toMerge otherwise
     */
    public static double merge(double current, double toMerge) {
        return toMerge == 0d ? current : toMerge;
    }

    /**
     * Merge two values. Uses toMerge if it is not default, uses current otherwise
     *
     * @param current current value
     * @param toMerge value to merge
     * @return current if toMerge is default, toMerge otherwise
     */
    public static boolean merge(boolean current, boolean toMerge) {
        return toMerge || current;
    }

    /**
     * Merge two values. Uses toMerge if it is not default, uses current otherwise
     *
     * @param current current value
     * @param toMerge value to merge
     * @return current if toMerge is default, toMerge otherwise
     */
    public static String merge(String current, String toMerge) {
        return toMerge == null || "".equals(toMerge) ? current : toMerge;
    }

    /**
     * Merge two values. Uses toMerge if it is not default, uses current otherwise
     *
     * @param current current value
     * @param toMerge value to merge
     * @return current if toMerge is default, toMerge otherwise
     */
    public static ByteArray merge(ByteArray current, ByteArray toMerge) {
        return toMerge == null || toMerge.isEmpty() ? current : toMerge;
    }

    /**
     * Merge two values
     *
     * @param current current value
     * @param toMerge value to merge
     * @param <T>     type of the message
     * @return merged message
     */
    public static <T extends ProtobufMessage<T>> T merge(T current, T toMerge) {
        return current == null ? toMerge : current.merge(toMerge);
    }

    /**
     * Merge two lists
     *
     * @param current current value
     * @param toMerge value to merge
     * @param <T>     type of the message
     * @return merged lists
     */
    public static <T> List<T> merge(List<T> current, List<T> toMerge) {
        if (toMerge != null && !toMerge.isEmpty()) {
            current.addAll(toMerge);
        }

        return current;
    }

    /**
     * Merge two lists of doubles
     *
     * @param current current value
     * @param toMerge value to merge
     * @return merged lists
     */
    public static DoubleList.Builder merge(DoubleList.Builder current, DoubleList toMerge) {
        if (toMerge != null && !toMerge.isEmpty()) {
            current.addAll(toMerge);
        }

        return current;
    }

    /**
     * Merge two lists of floats
     *
     * @param current current value
     * @param toMerge value to merge
     * @return merged lists
     */
    public static FloatList.Builder merge(FloatList.Builder current, FloatList toMerge) {
        if (toMerge != null && !toMerge.isEmpty()) {
            current.addAll(toMerge);
        }

        return current;
    }

    /**
     * Merge two lists of longs
     *
     * @param current current value
     * @param toMerge value to merge
     * @return merged lists
     */
    public static LongList.Builder merge(LongList.Builder current, LongList toMerge) {
        if (toMerge != null && !toMerge.isEmpty()) {
            current.addAll(toMerge);
        }

        return current;
    }

    /**
     * Merge two lists of ints
     *
     * @param current current value
     * @param toMerge value to merge
     * @return merged lists
     */
    public static IntList.Builder merge(IntList.Builder current, IntList toMerge) {
        if (toMerge != null && !toMerge.isEmpty()) {
            current.addAll(toMerge);
        }

        return current;
    }

    /**
     * Merge two lists of boolean
     *
     * @param current current value
     * @param toMerge value to merge
     * @return merged lists
     */
    public static BooleanList.Builder merge(BooleanList.Builder current, BooleanList toMerge) {
        if (toMerge != null && !toMerge.isEmpty()) {
            current.addAll(toMerge);
        }

        return current;
    }

    /**
     * Merge two lists of objects
     *
     * @param current current value
     * @param toMerge value to merge
     * @param <T> element type
     * @return merged lists
     */
    public static <T> ObjectList.Builder<T> merge(ObjectList.Builder<T> current, ObjectList<T> toMerge) {
        if (toMerge != null && !toMerge.isEmpty()) {
            current.addAll(toMerge);
        }

        return current;
    }
}
