package com.github.pcimcioch.protobuf.dto;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.RandomAccess;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Immutable list of booleans
 */
public final class BooleanList extends AbstractList<Boolean> implements RandomAccess {
    private static final BooleanList EMPTY = new BooleanList(new boolean[0], 0);

    private final boolean[] values;
    private final int size;

    private BooleanList(boolean[] values, int size) {
        this.values = values;
        this.size = size;
    }

    private BooleanList(boolean[] values) {
        this(values, values.length);
    }

    @Override
    @Deprecated
    public Boolean get(int index) {
        return values[rangeCheck(index)];
    }

    /**
     * Return primitive value
     *
     * @param index index
     * @return primitive value
     */
    public boolean getBoolean(int index) {
        return values[rangeCheck(index)];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int hashCode() {
        int result = 1;
        for (int i = 0; i < size; i++) {
            result = 31 * result + (values[i] ? 1231 : 1237);
        }

        return result;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof BooleanList other && other.size == size
                ? Arrays.equals(values, 0, size, other.values, 0, size)
                : super.equals(o);
    }

    /**
     * Returns new builder
     *
     * @return new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Create new list from given elements
     *
     * @param elements elements
     * @return new list
     */
    public static BooleanList of(boolean... elements) {
        return elements.length == 0 ? EMPTY : new BooleanList(elements);
    }

    /**
     * Return immutable copy of given collection
     *
     * @param elements elements
     * @return boolean list
     */
    public static BooleanList copyOf(Collection<Boolean> elements) {
        if (elements.isEmpty()) {
            return EMPTY;
        }
        boolean[] data = new boolean[elements.size()];
        int i = 0;
        for (boolean element : elements) {
            data[i++] = element;
        }
        return new BooleanList(data);
    }

    private int rangeCheck(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        return index;
    }

    /**
     * Builder
     */
    public static final class Builder {
        private boolean[] values = new boolean[8];
        private int size = 0;

        /**
         * Add element
         *
         * @param element element to add
         */
        public void add(boolean element) {
            grow(1);
            values[size++] = element;
        }

        /**
         * Add all elements
         *
         * @param elements elements to add
         */
        public void addAll(Collection<Boolean> elements) {
            grow(elements.size());

            if (elements instanceof BooleanList our) {
                System.arraycopy(our.values, 0, values, size, our.size);
                size += our.size;
            } else {
                for (boolean element : elements) {
                    values[size++] = element;
                }
            }
        }

        /**
         * Clear all elements
         */
        public void clear() {
            size = 0;
            values = new boolean[8];
        }

        /**
         * Build list
         *
         * @return new immutable list
         */
        public BooleanList build() {
            BooleanList list = size == 0 ? EMPTY : new BooleanList(values, size);
            values = null;
            return list;
        }

        private void grow(int toAdd) {
            if (size + toAdd <= values.length) {
                return;
            }

            int oldCapacity = values.length;
            int newCapacity = max(min(oldCapacity << 1, 128), oldCapacity + toAdd);

            values = Arrays.copyOf(values, newCapacity);
        }
    }
}
