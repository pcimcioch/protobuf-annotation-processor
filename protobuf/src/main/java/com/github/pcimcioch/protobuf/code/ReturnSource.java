package com.github.pcimcioch.protobuf.code;

public final class ReturnSource {
    private final String type;

    private ReturnSource(String type) {
        this.type = type;
    }

    public static ReturnSource returns(TypeName type) {
        return new ReturnSource(type.canonicalName());
    }

    public static ReturnSource returns(Class<?> clazz) {
        return new ReturnSource(clazz.getCanonicalName());
    }

    public static ReturnSource returns(ClassSource clazz) {
       return new ReturnSource(clazz.packageName() + "." + clazz.simpleName());
    }

    @Override
    public String toString() {
        return type;
    }
}
