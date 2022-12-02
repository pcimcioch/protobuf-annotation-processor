package com.github.pcimcioch.protobuf.model;

/**
 * Describes type of field
 */
public interface FieldType {

    /**
     * Returns java type of the field
     *
     * @return java type
     */
    TypeName fieldJavaType();

    /**
     * Returns wrapper java type of the field. For primitives this will be their boxing type, in other case it is the same as
     * a {@link #fieldJavaType()}. Wrapper type must be nullable and usable in generic types.
     *
     * @return java type
     */
    default TypeName wrapperJavaType() {
        return fieldJavaType();
    }

    /**
     * Returns default java value for this type
     *
     * @return default value
     */
    String defaultValue();

    /**
     * Returns wire type for this type
     *
     * @return wire type
     */
    WireType wireType();

    /**
     * Returns java code to write this field to given output
     *
     * @param outputName    output name
     * @param number        number of the field
     * @param parameterName name of the parameter storing this scalar type
     * @return java code
     */
    String writeMethod(String outputName, int number, String parameterName);

    /**
     * Returns java code to read this field from given input
     *
     * @param inputName input name
     * @return java code
     */
    String readMethod(String inputName);
}
