package com.github.pcimcioch.protobuf.dto;

import java.util.Arrays;

import static java.util.Objects.requireNonNull;

/**
 * Wrapper for {@code byte[]} that provides equals and hashcode comparing by array content, not array identity.
 * This structure is immutable.
 *
 * @param data byte array to wrap
 */
public record ByteArray(byte[] data) {

    /**
     * Empty array. More efficient than creating new empty byte array
     */
    public static final ByteArray EMPTY = new ByteArray(new byte[0]);


    /**
     * Constructor
     *
     * @param data byte array
     */
    public ByteArray(byte[] data) {
        requireNonNull(data, "Data cannot be null");
        this.data = Arrays.copyOf(data, data.length);
    }

    /**
     * Returns byte array
     *
     * @return byte array
     */
    @Override
    public byte[] data() {
        return Arrays.copyOf(data, data.length);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ByteArray byteArray = (ByteArray) o;
        return Arrays.equals(data, byteArray.data);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }
}
