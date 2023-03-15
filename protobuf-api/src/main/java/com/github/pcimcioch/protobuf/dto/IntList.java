package com.github.pcimcioch.protobuf.dto;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.RandomAccess;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Immutable list of ints
 */
public final class IntList extends AbstractList<Integer> implements RandomAccess {
    private static final IntList EMPTY = new IntList(new int[0], 0);

    private final int[] values;
    private final int size;

    private IntList(int[] values, int size) {
        this.values = values;
        this.size = size;
    }

    private IntList(int[] values) {
        this(values, values.length);
    }

    @Override
    @Deprecated
    public Integer get(int index) {
        return values[rangeCheck(index)];
    }

    /**
     * Return primitive value
     *
     * @param index index
     * @return primitive value
     */
    public int getInt(int index) {
        return values[rangeCheck(index)];
    }

    @Override
    public int size() {
        return size;
    }

    // TODO add better hashcode and equals

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
    public static IntList of(int... elements) {
        return elements.length == 0 ? EMPTY : new IntList(elements);
    }

    /**
     * Return immutable copy of given collection
     *
     * @param elements elements
     * @return int list
     */
    public static IntList copyOf(Collection<Integer> elements) {
        if (elements.isEmpty()) {
            return EMPTY;
        }
        int[] data = new int[elements.size()];
        int i = 0;
        for (int element : elements) {
            data[i++] = element;
        }
        return new IntList(data);
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
        private int[] values = new int[8];
        private int size = 0;

        /**
         * Add element
         *
         * @param element element to add
         */
        public void add(int element) {
            grow(1);
            values[size++] = element;
        }

        /**
         * Add all elements
         *
         * @param elements elements to add
         */
        public void addAll(Collection<Integer> elements) {
            grow(elements.size());

            if (elements instanceof IntList our) {
                System.arraycopy(our.values, 0, values, size, our.size);
                size += our.size;
            } else {
                for (int element : elements) {
                    values[size++] = element;
                }
            }
        }

        /**
         * Clear all elements
         */
        public void clear() {
            size = 0;
            values = new int[8];
        }

        /**
         * Build list
         *
         * @return new immutable list
         */
        public IntList build() {
            IntList list = size == 0 ? EMPTY : new IntList(values, size);
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
