package com.github.pcimcioch.protobuf.model.message;

import java.util.Set;
import java.util.regex.Pattern;

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

    private static final class Valid {
        private static final Pattern namePattern = Pattern.compile("^[a-zA-z_][a-zA-Z0-9_]*$");
        private static final Set<String> reserved = Set.of(UNRECOGNIZED_ELEMENT_NAME);

        private static String name(String name) {
            if (name == null) {
                throw new IllegalArgumentException("Incorrect enum name: <null>");
            }
            if (!namePattern.matcher(name).matches()) {
                throw new IllegalArgumentException("Incorrect enum name: " + name);
            }
            if (reserved.contains(name)) {
                throw new IllegalArgumentException("Used restricted enum name: " + name);
            }

            return name;
        }
    }
}
