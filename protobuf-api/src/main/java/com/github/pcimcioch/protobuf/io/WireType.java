package com.github.pcimcioch.protobuf.io;

// TODO replace with enum?
public final class WireType {
    public static final int VARINT = 0;
    public static final int I64 = 1;
    public static final int LEN = 2;
    public static final int SGROUP = 3;
    public static final int EGROUP = 4;
    public static final int I32 = 5;

    private static final int WIRE_BITS = 3;
    private static final int WIRE_MASK = (1 << WIRE_BITS) - 1;

    private WireType() {
    }

    public static int tagFrom(int number, int wireType) {
        return number << WIRE_BITS | wireType;
    }

    static int wireTypeFrom(int tag) {
        return tag & WIRE_MASK;
    }
}
