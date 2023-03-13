package com.github.pcimcioch.protobuf.dto;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.RandomAccess;

import static java.lang.Math.max;
import static java.lang.Math.min;

public final class DoubleList extends AbstractList<Double> implements RandomAccess {
    private final double[] values;
    private final int size;

    private DoubleList(double[] values, int size) {
        this.values = values;
        this.size = size;
    }

    @Override
    public Double get(int index) {
        return values[rangeCheck(index)];
    }

    public double getDouble(int index) {
        return values[rangeCheck(index)];
    }

    @Override
    public int size() {
        return size;
    }

    public static Builder builder() {
        return new Builder();
    }

    private int rangeCheck(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        return index;
    }

    public static final class Builder {
        private static final DoubleList EMPTY = new DoubleList(new double[0], 0);

        private double[] values = new double[8];
        private int size = 0;

        public void add(double element) {
            grow(1);
            values[size++] = element;
        }

        public void addAll(Collection<Double> elements) {
            grow(elements.size());

            if (elements instanceof DoubleList doubleElements) {
                System.arraycopy(doubleElements.values, 0, values, size, doubleElements.size);
                size += doubleElements.size;
            } else {
                for (Double element : elements) {
                    values[size++] = element;
                }
            }
        }

        public void clear() {
            size = 0;
            values = new double[8];
        }

        public DoubleList build() {
            DoubleList list = size == 0 ? EMPTY : new DoubleList(values, size);
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
