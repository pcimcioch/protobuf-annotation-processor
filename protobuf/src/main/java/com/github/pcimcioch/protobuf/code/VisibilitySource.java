package com.github.pcimcioch.protobuf.code;

/**
 * Visibility modifier source
 */
public final class VisibilitySource {
    private final String value;

    private VisibilitySource(String value) {
        this.value = value;
    }

    /**
     * Create new public visibility modifier source
     *
     * @return public visibility modifier source
     */
    public static VisibilitySource publicVisibility() {
        return new VisibilitySource("public");
    }

    /**
     * Create new private visibility modifier source
     *
     * @return private visibility modifier source
     */
    public static VisibilitySource privateVisibility() {
        return new VisibilitySource("private");
    }

    @Override
    public String toString() {
        return value;
    }
}
