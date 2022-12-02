package com.github.pcimcioch.protobuf.io;

enum WireType {
    ;

    static final int VARINT = 0;
    static final int I64 = 1;
    static final int LEN = 2;
    static final int SGROUP = 3;
    static final int EGROUP = 4;
    static final int I32 = 5;

    static final int WIRE_BITS = 3;
    static final int WIRE_MASK = (1 << WIRE_BITS) - 1;
}
