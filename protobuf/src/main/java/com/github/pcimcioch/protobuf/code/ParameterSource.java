package com.github.pcimcioch.protobuf.code;

import static com.github.pcimcioch.protobuf.code.CodeBody.body;
import static com.github.pcimcioch.protobuf.code.CodeBody.param;

public final class ParameterSource {
    private final String type;
    private final String name;

    private ParameterSource(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public static ParameterSource parameter(TypeName type, String name) {
        return new ParameterSource(type.canonicalName(), name);
    }

    public static ParameterSource parameter(Class<?> clazz, String name) {
        return new ParameterSource(clazz.getCanonicalName(), name);
    }

    @Override
    public String toString() {
        return body("$type $name",
                param("type", type),
                param("name", name)
        ).toString();
    }
}
