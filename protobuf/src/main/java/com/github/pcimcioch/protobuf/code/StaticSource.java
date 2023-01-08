package com.github.pcimcioch.protobuf.code;

public final class StaticSource {
    private final String value;

    private StaticSource(String value) {
        this.value = value;
    }

    public static StaticSource staticModifier() {
        return new StaticSource("static");
    }

    @Override
    public String toString() {
        return value;
    }
}
