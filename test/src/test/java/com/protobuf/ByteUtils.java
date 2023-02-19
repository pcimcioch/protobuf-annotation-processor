package com.protobuf;

import com.github.pcimcioch.protobuf.io.ByteArray;
import com.google.protobuf.ByteString;

import java.util.Arrays;

public final class ByteUtils {
    private ByteUtils() {
    }

    public static byte[] b(int... bytes) {
        byte[] data = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            data[i] = (byte) bytes[i];
        }

        return data;
    }

    public static ByteArray ba(int... bytes) {
        return ByteArray.fromByteArray(b(bytes));
    }

    public static ByteString bs(int... bytes) {
        return ByteString.copyFrom(b(bytes));
    }

    public static byte[] concatenate(byte[] first, byte[] second) {
        byte[] concatenated = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, concatenated, first.length, second.length);
        return concatenated;
    }
}
