package com.github.pcimcioch.protobuf.code;

public final class VisibilitySource {
    private final String value;

    private VisibilitySource(String value) {
        this.value = value;
    }

    public static VisibilitySource publicVisibility() {
        return new VisibilitySource("public");
    }

    public static VisibilitySource privateVisibility() {
        return new VisibilitySource("private");
    }

    public static VisibilitySource protectedVisibility() {
        return new VisibilitySource("protected");
    }

    @Override
    public String toString() {
        return value;
    }
}
