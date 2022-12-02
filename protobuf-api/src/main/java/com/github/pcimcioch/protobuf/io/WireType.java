package com.github.pcimcioch.protobuf.io;

enum WireType {
    VARINT(0),
    I64(1),
    LEN(2),
    SGROUP(3),
    EGROUP(4),
    I32(5);

    private final int id;

    WireType(int id) {
        this.id = id;
    }

    int id() {
        return id;
    }
}
