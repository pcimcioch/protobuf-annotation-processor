package org.github.pcimcioch.protobuf.model;

public enum WireType {
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

    public int id() {
        return id;
    }
}
