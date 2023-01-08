package com.github.pcimcioch.protobuf.code;

/**
 * Final modifier source
 */
public final class FinalSource {
    private final String value;

    private FinalSource(String value) {
        this.value = value;
    }

    /**
     * Create new final modifier source
     *
     * @return final modifier source
     */
    public static FinalSource finalModifier() {
        return new FinalSource("final");
    }

    @Override
    public String toString() {
        return value;
    }
}
