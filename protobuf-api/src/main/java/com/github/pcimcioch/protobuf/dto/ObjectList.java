package com.github.pcimcioch.protobuf.dto;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.RandomAccess;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Immutable list of objects
 */
@SuppressWarnings("unchecked")
public final class ObjectList<T> extends AbstractList<T> implements RandomAccess {
    private static final ObjectList<?> EMPTY = new ObjectList<>(new Object[0], 0);

    private final Object[] values;
    private final int size;

    private ObjectList(Object[] values, int size) {
        this.values = values;
        this.size = size;
    }

    private ObjectList(Object[] values) {
        this(values, values.length);
    }

    @Override
    public T get(int index) {
        return (T) values[rangeCheck(index)];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int hashCode() {
        int result = 1;

        for (int i = 0; i < size; i++) {
            Object element = values[i];
            result = 31 * result + (element == null ? 0 : element.hashCode());
        }

        return result;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ObjectList<?> other && other.size == size
                ? Arrays.equals(values, 0, size, other.values, 0, size)
                : super.equals(o);
    }

    /**
     * Returns new builder
     *
     * @param <T> element type
     * @return new builder
     */
    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    /**
     * Create new list from given elements
     *
     * @param elements elements
     * @param <E>      element type
     * @return new list
     */
    @SafeVarargs
    public static <E> ObjectList<E> of(E... elements) {
        return elements.length == 0 ? (ObjectList<E>) EMPTY : new ObjectList<>(elements);
    }

    /**
     * Return immutable copy of given collection
     *
     * @param elements elements
     * @param <E>      element type
     * @return object list
     */
    public static <E> ObjectList<E> copyOf(Collection<E> elements) {
        if (elements.isEmpty()) {
            return (ObjectList<E>) EMPTY;
        }
        Object[] data = new Object[elements.size()];
        int i = 0;
        for (E element : elements) {
            data[i++] = element;
        }
        return new ObjectList<>(data);
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
    public static final class Builder<V> {
        private Object[] values = new Object[8];
        private int size = 0;

        /**
         * Add element
         *
         * @param element element to add
         */
        public void add(V element) {
            if (element == null) {
                throw new NullPointerException();
            }
            grow(1);
            values[size++] = element;
        }

        /**
         * Add all elements
         *
         * @param elements elements to add
         */
        public void addAll(Collection<V> elements) {
            grow(elements.size());

            for (V element : elements) {
                if (element == null) {
                    throw new NullPointerException();
                }
                values[size++] = element;
            }
        }

        /**
         * Clear all elements
         */
        public void clear() {
            size = 0;
            values = new Object[8];
        }

        /**
         * Build list
         *
         * @return new immutable list
         */
        public ObjectList<V> build() {
            ObjectList<V> list = size == 0 ? (ObjectList<V>) EMPTY : new ObjectList<>(values, size);
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
