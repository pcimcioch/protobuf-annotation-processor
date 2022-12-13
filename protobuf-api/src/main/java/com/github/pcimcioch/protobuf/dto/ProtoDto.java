package com.github.pcimcioch.protobuf.dto;

import java.util.Objects;

public final class ProtoDto {
    private ProtoDto() {
    }

    public static int copy(int value) {
        return value;
    }

    public static long copy(long value) {
        return value;
    }

    public static double copy(double value) {
        return value;
    }

    public static float copy(float value) {
        return value;
    }

    public static boolean copy(boolean value) {
        return value;
    }

    public static String copy(String value) {
        return Objects.requireNonNull(value);
    }

    public static ByteArray copy(ByteArray value) {
        return Objects.requireNonNull(value);
    }

    public static Object copy(Object value) {
        return value;
    }
}
