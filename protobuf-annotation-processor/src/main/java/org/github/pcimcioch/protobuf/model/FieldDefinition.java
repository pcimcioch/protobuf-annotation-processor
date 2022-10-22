package org.github.pcimcioch.protobuf.model;

public class FieldDefinition {
    private final String name;
    private final ScalarFieldType type;
    private final int number;

    public FieldDefinition(String name, ScalarFieldType type, int number) {
        this.name = name;
        this.type = type;
        this.number = number;
    }

    public String name() {
        return name;
    }

    public int number() {
        return number;
    }

    public WireType wireType() {
        return type.wireType();
    }

    public String typeName() {
        return type.fieldClass();
    }

    public String protobufWriteMethod(String outputName, String parameterName) {
        return type.writeMethod(outputName, parameterName);
    }

    public String protobufReadMethod(String inputName) {
        return type.readMethod(inputName);
    }

    public String defaultValue() {
        return type.defaultValue();
    }

    public int tag() {
        return (number << 3) | type.wireType().id();
    }
}
