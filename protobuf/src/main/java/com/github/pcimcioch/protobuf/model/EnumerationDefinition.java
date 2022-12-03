package com.github.pcimcioch.protobuf.model;

import java.util.List;

/**
 * Enumeration Definition
 */
// TODO add validation
// TODO add tests
public class EnumerationDefinition {
    private final TypeName name;
    private final List<EnumerationElementDefinition> elements;
    private final boolean allowAlias;

    /**
     * Constructor
     *
     * @param name       name of the enumeration
     * @param allowAlias whether to allow aliases
     * @param elements   elements of the enumeration
     */
    public EnumerationDefinition(TypeName name, boolean allowAlias, List<EnumerationElementDefinition> elements) {
        this.name = name;
        this.elements = elements;
        this.allowAlias = allowAlias;
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
     * Returns whether aliases are allowed
     *
     * @return whether aliases are allowed
     */
    public boolean allowAlias() {
        return allowAlias;
    }

    /**
     * Returns elements of this enumeration
     *
     * @return elements
     */
    public List<EnumerationElementDefinition> elements() {
        return elements;
    }
}
