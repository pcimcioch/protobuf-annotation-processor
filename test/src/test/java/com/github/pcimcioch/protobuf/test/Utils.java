package com.github.pcimcioch.protobuf.test;

import com.github.pcimcioch.protobuf.dto.ByteArray;

public final class Utils {
    private Utils() {
    }

    public static byte[] b(int... bytes) {
        byte[] data = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            data[i] = (byte) bytes[i];
        }

        return data;
    }

    public static ByteArray ba(int... bytes) {
        return new ByteArray(b(bytes));
    }
}
