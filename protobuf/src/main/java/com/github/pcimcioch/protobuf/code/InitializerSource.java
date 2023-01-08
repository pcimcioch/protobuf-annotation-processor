package com.github.pcimcioch.protobuf.code;

public final class InitializerSource {
    private final String value;

    private InitializerSource(String value) {
        this.value = value;
    }

    public static InitializerSource initializer(String value) {
        return new InitializerSource(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
