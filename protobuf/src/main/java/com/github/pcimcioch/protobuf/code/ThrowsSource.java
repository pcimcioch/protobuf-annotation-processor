package com.github.pcimcioch.protobuf.code;

public final class ThrowsSource {
    private final String value;

    private ThrowsSource(String value) {
        this.value = value;
    }

    public static ThrowsSource throwsEx(Class<? extends Throwable> throwable) {
        return new ThrowsSource(throwable.getCanonicalName());
    }

    @Override
    public String toString() {
        return value;
    }
}
