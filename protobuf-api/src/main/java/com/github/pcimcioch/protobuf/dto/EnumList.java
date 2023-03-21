package com.github.pcimcioch.protobuf.dto;

import java.util.AbstractList;
import java.util.Collection;
import java.util.RandomAccess;
import java.util.function.IntFunction;

/**
 * Immutable list of protobuf enums
 */
@SuppressWarnings("unchecked")
public final class EnumList<T extends ProtobufEnumeration> extends AbstractList<T> implements RandomAccess {
    private static final EnumList<?> EMPTY = new EnumList<>(IntList.of(), i -> null);

    private final IntList values;
    private final IntFunction<T> enumMapper;

    private EnumList(IntList values, IntFunction<T> enumMapper) {
        this.values = values;
        this.enumMapper = enumMapper;
    }

    @Override
    public T get(int index) {
        return enumMapper.apply(values.getInt(index));
    }

    @Override
    public int size() {
        return values.size();
    }

    /**
     * Returns immutable list of enum values
     *
     * @return list of enum values
     */
    public IntList valuesList() {
        return values;
    }

    /**
     * Returns new builder
     *
     * @param enumMapper enumeration factory
     * @param <T>        element type
     * @return new builder
     */
    public static <T extends ProtobufEnumeration> EnumList.Builder<T> builder(IntFunction<T> enumMapper) {
        return new EnumList.Builder<>(enumMapper);
    }

    /**
     * Create new list from given elements
     *
     * @param <E> element type
     * @return new list
     */
    public static <E extends ProtobufEnumeration> EnumList<E> of() {
        return (EnumList<E>) EMPTY;
    }

    /**
     * Create new list from given elements
     *
     * @param enumMapper enumeration factory
     * @param elements   elements
     * @param <E>        element type
     * @return new list
     */
    @SafeVarargs
    public static <E extends ProtobufEnumeration> EnumList<E> of(IntFunction<E> enumMapper, E... elements) {
        IntList.Builder intElements = IntList.builder();
        for (E element : elements) {
            intElements.add(element.number());
        }

        return new EnumList<>(intElements.build(), enumMapper);
    }

    /**
     * Return immutable copy of given collection
     *
     * @param enumMapper enumeration factory
     * @param elements   elements
     * @param <E>        element type
     * @return object list
     */
    public static <E extends ProtobufEnumeration> EnumList<E> copyOf(IntFunction<E> enumMapper, Collection<E> elements) {
        if (elements.isEmpty()) {
            return (EnumList<E>) EMPTY;
        }

        IntList.Builder intElements = IntList.builder();
        for (E element : elements) {
            intElements.add(element.number());
        }
        return new EnumList<>(intElements.build(), enumMapper);
    }

    /**
     * Builder
     */
    public static final class Builder<V extends ProtobufEnumeration> {
        private final IntFunction<V> enumMapper;
        private IntList.Builder values = IntList.builder();

        private Builder(IntFunction<V> enumMapper) {
            this.enumMapper = enumMapper;
        }

        /**
         * Add element
         *
         * @param element element to add
         */
        public void add(V element) {
            if (element == null) {
                throw new NullPointerException();
            }
            values.add(element.number());
        }

        /**
         * Add element value
         *
         * @param element element value to add
         */
        public void addValue(int element) {
            values.add(element);
        }

        /**
         * Add all elements
         *
         * @param elements elements to add
         */
        public void addAll(Collection<V> elements) {
            if (elements instanceof EnumList<V> enumElements) {
                addAllValues(enumElements.valuesList());
                return;
            }

            for (V element : elements) {
                if (element == null) {
                    throw new NullPointerException();
                }
                values.add(element.number());
            }
        }

        /**
         * Add all element values
         *
         * @param elements elements to add
         */
        public void addAllValues(Collection<Integer> elements) {
            values.addAll(elements);
        }

        /**
         * Clear all elements
         */
        public void clear() {
            values.clear();
        }

        /**
         * Build list
         *
         * @return new immutable list
         */
        public EnumList<V> build() {
            IntList list = values.build();
            values = null;

            return list.isEmpty() ? (EnumList<V>) EMPTY : new EnumList<>(list, enumMapper);
        }
    }
}
