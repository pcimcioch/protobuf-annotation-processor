package com.github.pcimcioch.protobuf.dto;

import java.util.Arrays;

import static java.util.Objects.requireNonNull;

/**
 * Wrapper for {@code byte[]} that provides equals and hashcode comparing by array content, not array identity.
 * This structure is immutable.
 */
public final class ByteArray {

    /**
     * Empty array. More efficient than creating new empty byte array
     */
    public static final ByteArray EMPTY = new ByteArray(new byte[0]);

    /**
     * Byte array
     */
    private final byte[] data;

    private ByteArray(byte[] data) {
        requireNonNull(data, "Data cannot be null");
        this.data = data;
    }

    /**
     * Returns new byte array
     *
     * @return byte array copy
     */
    public byte[] toByteArray() {
        return Arrays.copyOf(data, data.length);
    }

    /**
     * Returns whether this structure is empty
     *
     * @return whether it is empty
     */
    public boolean isEmpty() {
        return data.length == 0;
    }

    /**
     * Returns data length
     *
     * @return data length
     */
    public int length() {
        return data.length;
    }

    /**
     * Returns byte at given position
     *
     * @param index position
     * @return byte
     */
    public byte get(int index) {
        return data[index];
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

    /**
     * Creates ByteArray from byte[]. Data will be copied
     *
     * @param data byte array
     * @return ByteArray
     */
    public static ByteArray fromByteArray(byte[] data) {
        return new ByteArray(Arrays.copyOf(data, data.length));
    }

    /**
     * Returns builder that can be used to initialize ByteArray in efficient way without data copying
     *
     * @param length data length
     * @return ByteArray builder
     */
    public static Builder builder(int length) {
        return new Builder(length);
    }

    /**
     * Builder that can be used to initialize ByteArray in efficient way without data copying
     */
    public static final class Builder {
        private byte[] data;

        private Builder(int length) {
            this.data = new byte[length];
        }

        /**
         * Set byte at given position
         *
         * @param index position
         * @param value byte to set
         * @return builder
         */
        public Builder set(int index, byte value) {
            data[index] = value;
            return this;
        }

        /**
         * Builds a ByteArray. Builder cannot be used after this operation
         *
         * @return ByteArray
         */
        public ByteArray build() {
            byte[] toPass = this.data;
            this.data = null;
            return new ByteArray(toPass);
        }
    }
}
