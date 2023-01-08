package com.github.pcimcioch.protobuf.code;

import java.util.List;

import static com.github.pcimcioch.protobuf.code.CodeBody.body;
import static com.github.pcimcioch.protobuf.code.CodeBody.param;
import static java.util.Arrays.stream;

/**
 * Enum element source
 */
public final class EnumElementSource {
    private final String name;
    private final List<String> constructorParameters;

    private EnumElementSource(String name, Object... constructorParameters) {
        this.name = name;
        this.constructorParameters = stream(constructorParameters)
                .map(Object::toString)
                .toList();
    }

    /**
     * Create enum element source
     *
     * @param name                  name
     * @param constructorParameters parameters
     * @return enum element source
     */
    public static EnumElementSource element(String name, Object... constructorParameters) {
        return new EnumElementSource(name, constructorParameters);
    }

    @Override
    public String toString() {
        return body("$name$parameters",
                param("name", name),
                param("parameters", constructorParameters, ", ", "(", ")", "")
        ).toString();
    }
}
