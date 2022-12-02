package com.github.pcimcioch.protobuf.model;

import java.util.regex.Pattern;

/**
 * Field definition
 */
public class FieldDefinition {
    private final String name;
    private final FieldType type;
    private final int number;

    /**
     * Constructor
     *
     * @param name   name of the field
     * @param type   type of the field
     * @param number number of the field
     */
    public FieldDefinition(String name, FieldType type, int number) {
        this.name = Valid.name(name);
        this.type = Valid.type(type);
        this.number = Valid.number(number);
    }

    /**
     * Returns name of the field
     *
     * @return name
     */
    public String name() {
        return name;
    }

    /**
     * Returns number of the field
     *
     * @return number
     */
    public int number() {
        return number;
    }

    /**
     * Returns java type name
     *
     * @return java type name
     */
    public TypeName typeName() {
        return type.fieldJavaType();
    }

    /**
     * Returns name of java method from {@link com.github.pcimcioch.protobuf.io.ProtobufWriter} and
     * {@link com.github.pcimcioch.protobuf.io.ProtobufReader} used to write and read this field
     *
     * @return java method name
     */
    public String ioMethod() {
        return type.ioMethod();
    }

    /**
     * Returns default value for this field
     *
     * @return default value
     */
    public String defaultValue() {
        return type.defaultValue();
    }

    /**
     * Returns whether this field should be required to be non-null
     *
     * @return whether this field should be required to be non-null
     */
    public boolean requireNonNull() {
        return type.requireNonNull();
    }

    private static final class Valid {
        private static final Pattern namePattern = Pattern.compile("^[a-zA-z_][a-zA-Z0-9_]*$");

        private static FieldType type(FieldType type) {
            if (type == null) {
                throw new IllegalArgumentException("Must provide field type");
            }
            return type;
        }

        private static int number(int number) {
            if (number <= 0) {
                throw new IllegalArgumentException("Number must be positive, but was: " + number);
            }
            return number;
        }

        private static String name(String name) {
            if (name == null) {
                throw new IllegalArgumentException("Incorrect field name: <null>");
            }
            if (!namePattern.matcher(name).matches()) {
                throw new IllegalArgumentException("Incorrect field name: " + name);
            }
            return name;
        }
    }
}
