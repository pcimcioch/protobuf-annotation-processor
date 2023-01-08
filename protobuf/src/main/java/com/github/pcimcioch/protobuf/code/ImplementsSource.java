package com.github.pcimcioch.protobuf.code;

/**
 * Implements source
 */
public final class ImplementsSource {
    private final String value;

    private ImplementsSource(String value) {
        this.value = value;
    }

    /**
     * Create new implements source
     *
     * @param value implements
     * @return implements source
     */
    public static ImplementsSource implementz(String value) {
        return new ImplementsSource(value);
    }

    /**
     * Create new implements source
     *
     * @param clazz implements
     * @return implements source
     */
    public static ImplementsSource implementz(Class<?> clazz) {
        return implementz(clazz.getCanonicalName());
    }

    @Override
    public String toString() {
        return value;
    }
}
