package com.github.pcimcioch.protobuf.dto;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.RandomAccess;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Immutable list of floats
 */
public final class FloatList extends AbstractList<Float> implements RandomAccess {
    private static final FloatList EMPTY = new FloatList(new float[0], 0);

    private final float[] values;
    private final int size;

    private FloatList(float[] values, int size) {
        this.values = values;
        this.size = size;
    }

    private FloatList(float[] values) {
        this(values, values.length);
    }

    @Override
    @Deprecated
    public Float get(int index) {
        return values[rangeCheck(index)];
    }

    /**
     * Return primitive value
     *
     * @param index index
     * @return primitive value
     */
    public float getFloat(int index) {
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
    public static FloatList of(float... elements) {
        return elements.length == 0 ? EMPTY : new FloatList(elements);
    }

    /**
     * Return immutable copy of given collection
     *
     * @param elements elements
     * @return float list
     */
    public static FloatList copyOf(Collection<Float> elements) {
        if (elements.isEmpty()) {
            return EMPTY;
        }
        float[] data = new float[elements.size()];
        int i = 0;
        for (float element : elements) {
            data[i++] = element;
        }
        return new FloatList(data);
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
        private float[] values = new float[8];
        private int size = 0;

        /**
         * Add element
         *
         * @param element element to add
         */
        public void add(float element) {
            grow(1);
            values[size++] = element;
        }

        /**
         * Add all elements
         *
         * @param elements elements to add
         */
        public void addAll(Collection<Float> elements) {
            grow(elements.size());

            if (elements instanceof FloatList our) {
                System.arraycopy(our.values, 0, values, size, our.size);
                size += our.size;
            } else {
                for (float element : elements) {
                    values[size++] = element;
                }
            }
        }

        /**
         * Clear all elements
         */
        public void clear() {
            size = 0;
            values = new float[8];
        }

        /**
         * Build list
         *
         * @return new immutable list
         */
        public FloatList build() {
            FloatList list = size == 0 ? EMPTY : new FloatList(values, size);
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
