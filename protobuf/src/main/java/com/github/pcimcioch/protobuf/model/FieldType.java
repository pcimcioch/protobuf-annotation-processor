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
     * Returns default java value for this type
     *
     * @return default value
     */
    String defaultValue();

    /**
     * Returns name of java method from {@link com.github.pcimcioch.protobuf.io.ProtobufWriter} and
     * {@link com.github.pcimcioch.protobuf.io.ProtobufReader} used to write and read this field
     *
     * @return java method name
     */
    String ioMethod();

    /**
     * Returns whether this type should be required to be non-null
     *
     * @return whether this type should be required to be non-null
     */
    boolean requireNonNull();
}
