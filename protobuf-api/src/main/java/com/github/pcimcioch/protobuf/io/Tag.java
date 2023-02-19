package com.github.pcimcioch.protobuf.io;

/**
 * Protobuf tag
 *
 * @param number   field number
 * @param wireType field wire type
 */
public record Tag(
        int number,
        int wireType
) {

    /**
     * Creates tag from full integer representation
     *
     * @param value value
     */
    public Tag(int value) {
        this(value >>> WireType.WIRE_BITS, value & WireType.WIRE_MASK);
    }

    /**
     * Returns tag as full integer representation
     *
     * @return integer representation
     */
    public int value() {
        return number << WireType.WIRE_BITS | wireType;
    }
}
