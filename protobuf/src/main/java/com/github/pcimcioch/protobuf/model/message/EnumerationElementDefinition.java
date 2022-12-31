package com.github.pcimcioch.protobuf.model.message;

import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

import static com.github.pcimcioch.protobuf.model.validation.Assertions.assertFalse;
import static com.github.pcimcioch.protobuf.model.validation.Assertions.assertNonNull;
import static com.github.pcimcioch.protobuf.model.validation.Assertions.assertTrue;

/**
 * Enumeration element definition
 */
public class EnumerationElementDefinition {
    /**
     * Name of the unrecognized enumeration element
     */
    public static final String UNRECOGNIZED_ELEMENT_NAME = "UNRECOGNIZED";

    private final String name;
    private final int number;

    /**
     * Constructor
     *
     * @param name   name of the element
     * @param number number of the element
     */
    public EnumerationElementDefinition(String name, int number) {
        this.name = Valid.name(name);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnumerationElementDefinition that = (EnumerationElementDefinition) o;
        return number == that.number && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, number);
    }

    private static final class Valid {
        private static final Pattern namePattern = Pattern.compile("^[a-zA-z_][a-zA-Z0-9_]*$");
        private static final Set<String> reserved = Set.of(UNRECOGNIZED_ELEMENT_NAME);

        private static String name(String name) {
            assertNonNull(name, "Incorrect enum name: <null>");
            assertTrue(namePattern.matcher(name).matches(), "Incorrect enum name: " + name);
            assertFalse(reserved.contains(name), "Used restricted enum name: " + name);

            return name;
        }
    }
}
