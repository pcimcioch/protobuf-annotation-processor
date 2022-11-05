package com.github.pcimcioch.protobuf.model;

/**
 * Enum representing protobuf wire types
 */
public enum WireType {
    /**
     * Variant integer
     */
    VARINT(0),

    /**
     * Fixed 64 bits
     */
    I64(1),

    /**
     * Variable length
     */
    LEN(2),

    /**
     * Group start
     */
    SGROUP(3),

    /**
     * Group end
     */
    EGROUP(4),

    /**
     * Fixed 32 bits
     */
    I32(5);

    private final int id;

    WireType(int id) {
        this.id = id;
    }

    /**
     * Returns identifier of the wire type
     *
     * @return identifier
     */
    public int id() {
        return id;
    }
}
