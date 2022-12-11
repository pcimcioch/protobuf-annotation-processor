package com.protobuf;

import com.github.pcimcioch.protobuf.dto.ByteArray;
import com.google.protobuf.ByteString;

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
        return new ByteArray(b(bytes));
    }

    public static ByteString bs(int... bytes) {
        return ByteString.copyFrom(b(bytes));
    }
}
