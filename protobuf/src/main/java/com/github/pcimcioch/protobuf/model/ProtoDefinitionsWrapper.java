package com.github.pcimcioch.protobuf.model;

import com.github.pcimcioch.protobuf.code.TypeName;

import java.util.Objects;

import static com.github.pcimcioch.protobuf.model.validation.Assertions.assertAllMatches;
import static com.github.pcimcioch.protobuf.model.validation.Assertions.assertNonNull;

/**
 * Wrapper class that contains other messages and enumerations
 */
public class ProtoDefinitionsWrapper {
    private final TypeName name;
    private final ProtoDefinitions definitions;

    /**
     * Constructor
     *
     * @param name        name
     * @param definitions nested definitions
     */
    public ProtoDefinitionsWrapper(TypeName name, ProtoDefinitions definitions) {
        this.name = Valid.wrapperName(name);
        this.definitions = Valid.definitions(name, definitions);
    }

    /**
     * Returns wrapper name
     *
     * @return name
     */
    public TypeName name() {
        return name;
    }

    /**
     * Returns wrapped definitions
     *
     * @return definitions
     */
    public ProtoDefinitions definitions() {
        return definitions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProtoDefinitionsWrapper that = (ProtoDefinitionsWrapper) o;
        return name.equals(that.name) && definitions.equals(that.definitions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, definitions);
    }

    private static final class Valid {

        private static TypeName wrapperName(TypeName wrapperName) {
            assertNonNull(wrapperName, "Wrapper name cannot be null");

            return wrapperName;
        }

        private static ProtoDefinitions definitions(TypeName name, ProtoDefinitions definitions) {
            assertNonNull(definitions, "Definitions cannot be null");
            assertAllMatches(definitions.messages(), message -> message.name().isDirectChildOf(name), "Message in wrapper has non-nested type");
            assertAllMatches(definitions.enumerations(), enumeration -> enumeration.name().isDirectChildOf(name), "Enumeration in wrapper has non-nested type");
            assertAllMatches(definitions.wrappers(), wrapper -> wrapper.name().isDirectChildOf(name), "Wrapper in wrapper has non-nested type");

            return definitions;
        }
    }
}
