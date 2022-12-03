package com.github.pcimcioch.protobuf.model;

/**
 * Enumeration element definition
 */
// TODO add validation
// TODO add tests
public class EnumerationElementDefinition {
    private final String name;
    private final int number;

    /**
     * Constructor
     *
     * @param name   name of the element
     * @param number numer of the element
     */
    public EnumerationElementDefinition(String name, int number) {
        this.name = name;
        this.number = number;
    }

    /**
     * Returns name of the element
     *
     * @return name
     */
    public String name() {
        return name;
    }

    /**
     * Returns number of the element
     *
     * @return number
     */
    public int number() {
        return number;
    }
}
