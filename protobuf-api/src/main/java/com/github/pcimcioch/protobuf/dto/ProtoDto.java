package com.github.pcimcioch.protobuf.dto;

import java.util.ArrayList;
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
        if (toMerge == null || toMerge.isEmpty()) {
            return current;
        }
        if (current == null || current.isEmpty()) {
            return toMerge;
        }

        List<T> sum = new ArrayList<>();
        sum.addAll(current);
        sum.addAll(toMerge);

        return List.copyOf(sum);
    }
}
