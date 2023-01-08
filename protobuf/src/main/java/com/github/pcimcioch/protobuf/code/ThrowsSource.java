package com.github.pcimcioch.protobuf.code;

/**
 * Throws source
 */
public final class ThrowsSource {
    private final String value;

    private ThrowsSource(String value) {
        this.value = value;
    }

    /**
     * Create new throws source
     * @param throwable throwable
     * @return throws source
     */
    public static ThrowsSource throwsEx(Class<? extends Throwable> throwable) {
        return new ThrowsSource(throwable.getCanonicalName());
    }

    @Override
    public String toString() {
        return value;
    }
}
