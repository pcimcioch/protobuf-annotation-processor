package com.github.pcimcioch.protobuf.dto;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.RandomAccess;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Immutable list of longs
 */
public final class LongList extends AbstractList<Long> implements RandomAccess {
    private static final LongList EMPTY = new LongList(new long[0], 0);

    private final long[] values;
    private final int size;

    private LongList(long[] values, int size) {
        this.values = values;
        this.size = size;
    }

    private LongList(long[] values) {
        this(values, values.length);
    }

    @Override
    @Deprecated
    public Long get(int index) {
        return values[rangeCheck(index)];
    }

    /**
     * Return primitive value
     *
     * @param index index
     * @return primitive value
     */
    public long getLong(int index) {
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
    public static LongList of(long... elements) {
        return elements.length == 0 ? EMPTY : new LongList(elements);
    }

    /**
     * Return immutable copy of given collection
     *
     * @param elements elements
     * @return long list
     */
    public static LongList copyOf(Collection<Long> elements) {
        if (elements.isEmpty()) {
            return EMPTY;
        }
        long[] data = new long[elements.size()];
        int i = 0;
        for (long element : elements) {
            data[i++] = element;
        }
        return new LongList(data);
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
        private long[] values = new long[8];
        private int size = 0;

        /**
         * Add element
         *
         * @param element element to add
         */
        public void add(long element) {
            grow(1);
            values[size++] = element;
        }

        /**
         * Add all elements
         *
         * @param elements elements to add
         */
        public void addAll(Collection<Long> elements) {
            grow(elements.size());

            if (elements instanceof LongList our) {
                System.arraycopy(our.values, 0, values, size, our.size);
                size += our.size;
            } else {
                for (long element : elements) {
                    values[size++] = element;
                }
            }
        }

        /**
         * Clear all elements
         */
        public void clear() {
            size = 0;
            values = new long[8];
        }

        /**
         * Build list
         *
         * @return new immutable list
         */
        public LongList build() {
            LongList list = size == 0 ? EMPTY : new LongList(values, size);
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
