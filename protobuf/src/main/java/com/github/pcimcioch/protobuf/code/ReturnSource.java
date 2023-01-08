package com.github.pcimcioch.protobuf.code;

/**
 * Return source
 */
public final class ReturnSource {
    private final String type;

    private ReturnSource(String type) {
        this.type = type;
    }

    /**
     * Create new return source
     *
     * @param type type
     * @return return source
     */
    public static ReturnSource returns(TypeName type) {
        return new ReturnSource(type.canonicalName());
    }

    /**
     * Create new return source
     *
     * @param clazz type
     * @return return source
     */
    public static ReturnSource returns(Class<?> clazz) {
        return new ReturnSource(clazz.getCanonicalName());
    }

    @Override
    public String toString() {
        return type;
    }
}
