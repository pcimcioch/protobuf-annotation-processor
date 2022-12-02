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
     * Returns name of java method from {@link com.github.pcimcioch.protobuf.io.ProtobufWriter} and
     * {@link com.github.pcimcioch.protobuf.io.ProtobufReader} used to write and read this field
     *
     * @return java method name
     */
    String ioMethod();
}
