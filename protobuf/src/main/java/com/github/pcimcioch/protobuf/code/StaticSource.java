package com.github.pcimcioch.protobuf.code;

/**
 * Static modifier source
 */
public final class StaticSource {
    private final String value;

    private StaticSource(String value) {
        this.value = value;
    }

    /**
     * Create new static modifier source
     *
     * @return static modifier source
     */
    public static StaticSource staticModifier() {
        return new StaticSource("static");
    }

    @Override
    public String toString() {
        return value;
    }
}
