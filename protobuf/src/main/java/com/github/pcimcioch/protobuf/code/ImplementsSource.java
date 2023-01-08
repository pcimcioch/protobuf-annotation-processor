package com.github.pcimcioch.protobuf.code;

public final class ImplementsSource {
    private final String value;

    private ImplementsSource(String value) {
        this.value = value;
    }

    public static ImplementsSource implementz(String value) {
        return new ImplementsSource(value);
    }

    public static ImplementsSource implementz(Class<?> clazz) {
        return implementz(clazz.getCanonicalName());
    }

    @Override
    public String toString() {
        return value;
    }
}
