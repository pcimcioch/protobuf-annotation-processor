package com.github.pcimcioch.protobuf.code;

public final class FinalSource {
    private final String value;

    private FinalSource(String value) {
        this.value = value;
    }

    public static FinalSource finalModifier() {
        return new FinalSource("final");
    }

    @Override
    public String toString() {
        return value;
    }
}
