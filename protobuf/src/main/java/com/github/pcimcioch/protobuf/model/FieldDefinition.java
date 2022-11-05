package com.github.pcimcioch.protobuf.model;

/**
 * Field definition
 */
public class FieldDefinition {
    private final String name;
    private final ScalarFieldType type;
    private final int number;

    /**
     * Constructor
     *
     * @param name   name of the field
     * @param type   type of the field
     * @param number number of the field
     */
    public FieldDefinition(String name, ScalarFieldType type, int number) {
        this.name = name;
        this.type = type;
        this.number = number;
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
     * Returns wire type of the field
     *
     * @return wire type
     */
    public WireType wireType() {
        return type.wireType();
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
     * Returns java code to write this field to given output
     *
     * @param outputName output name
     * @return java code
     */
    public String protobufWriteMethod(String outputName) {
        return type.writeMethod(outputName, name);
    }

    /**
     * Returns java code to read this field from given input
     *
     * @param inputName input name
     * @return java code
     */
    public String protobufReadMethod(String inputName) {
        return type.readMethod(inputName);
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
     * Returns protobuf tag for this field
     *
     * @return tag
     */
    public int tag() {
        return (number << 3) | type.wireType().id();
    }
}
