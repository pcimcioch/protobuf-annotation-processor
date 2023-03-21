package com.github.pcimcioch.protobuf.code;

/**
 * Initializer source
 */
public final class InitializerSource {
    private final String value;

    private InitializerSource(String value) {
        this.value = value;
    }

    /**
     * Create new initializer source
     *
     * @param value initializer
     * @return initializer source
     */
    public static InitializerSource initializer(String value) {
        return new InitializerSource(value);
    }

    /**
     * Create new initializer source
     *
     * @param body initializer body
     * @return initializer source
     */
    public static InitializerSource initializer(CodeBody body) {
        return new InitializerSource(body.toString());
    }

    @Override
    public String toString() {
        return value;
    }
}
