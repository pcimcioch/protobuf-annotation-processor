package com.github.pcimcioch.protobuf.model;

import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaRecordSource;

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

    /**
     * Adds builder methods to set up field of this type
     *
     * @param builderClass builder class source
     * @param field        field name
     */
    void addBuilderMethods(JavaClassSource builderClass, String field);

    /**
     * Adds additional methods to the message record containing field of this type
     *
     * @param messageRecord message record source
     * @param field         field name
     */
    void addMessageMethods(JavaRecordSource messageRecord, String field);
}
