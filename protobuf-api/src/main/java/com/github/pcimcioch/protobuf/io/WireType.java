package com.github.pcimcioch.protobuf.io;

/**
 * Wire type
 */
public enum WireType {
    /**
     * VARINT
     */
    VARINT(0),
    /**
     * I64
     */
    I64(1),
    /**
     * LEN
     */
    LEN(2),
    /**
     * SGROUP
     */
    SGROUP(3),
    /**
     * EGROUP
     */
    EGROUP(4),
    /**
     * I32
     */
    I32(5);

    private static final int WIRE_BITS = 3;
    private static final int WIRE_MASK = (1 << WIRE_BITS) - 1;

    private final int wireType;

    WireType(int wireType) {
        this.wireType = wireType;
    }

    /**
     * Create tag for this wire type from given number
     *
     * @param number number
     * @return tag
     */
    public int tagFrom(int number) {
        return number << WIRE_BITS | wireType;
    }

    /**
     * Create wire type from given tag
     *
     * @param tag tag
     * @return wire type
     */
    public static WireType fromTag(int tag) {
        int wireType = tag & WIRE_MASK;
        return switch (wireType) {
            case 0 -> VARINT;
            case 1 -> I64;
            case 2 -> LEN;
            case 3 -> SGROUP;
            case 4 -> EGROUP;
            case 5 -> I32;
            default -> throw new ProtobufParseException("Unknown wire type %d", wireType);
        };
    }
}
