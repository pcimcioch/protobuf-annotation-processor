package com.github.pcimcioch.protobuf.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Enumeration Definition
 */
public class EnumerationDefinition {
    private final TypeName name;
    private final List<EnumerationElementDefinition> elements;

    /**
     * Constructor
     *
     * @param name       name of the enumeration
     * @param allowAlias whether to allow aliases
     * @param elements   elements of the enumeration
     */
    public EnumerationDefinition(TypeName name, boolean allowAlias, List<EnumerationElementDefinition> elements) {
        this.name = Valid.name(name);
        this.elements = Valid.elements(elements, allowAlias);
    }

    /**
     * Returns name of the enumeration
     *
     * @return name of the enumeration
     */
    public TypeName name() {
        return name;
    }

    /**
     * Returns elements of this enumeration
     *
     * @return elements
     */
    public List<EnumerationElementDefinition> elements() {
        return elements;
    }

    private static final class Valid {

        private static TypeName name(TypeName name) {
            if (name == null) {
                throw new IllegalArgumentException("Enum name cannot be null");
            }

            return name;
        }

        private static List<EnumerationElementDefinition> elements(List<EnumerationElementDefinition> elements, boolean allowAlias) {
            if (elements == null || elements.isEmpty()) {
                throw new IllegalArgumentException("Enum must have at least one element");
            }

            Set<String> names = new HashSet<>();
            Set<Integer> numbers = new HashSet<>();

            for (EnumerationElementDefinition element : elements) {
                if (!names.add(element.name())) {
                    throw new IllegalArgumentException("Duplicated element name: " + element.name());
                }
                if (!numbers.add(element.number())) {
                    if (!allowAlias) {
                        throw new IllegalArgumentException("Duplicated element number: " + element.number());
                    }
                }
            }

            if (!numbers.contains(0)) {
                throw new IllegalArgumentException("Enum must contain element with number 0");
            }

            return elements;
        }
    }
}
