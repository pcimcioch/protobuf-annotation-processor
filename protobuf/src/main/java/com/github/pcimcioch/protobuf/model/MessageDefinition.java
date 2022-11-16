package com.github.pcimcioch.protobuf.model;

import java.util.List;

import static com.github.pcimcioch.protobuf.model.TypeName.canonicalName;

/**
 * Message definition
 */
public class MessageDefinition {
    private final TypeName name;
    private final List<FieldDefinition> fields;

    /**
     * Constructor
     *
     * @param name   name of the message
     * @param fields fields of the message
     */
    public MessageDefinition(TypeName name, List<FieldDefinition> fields) {
        this.name = name;
        this.fields = fields;
    }

    /**
     * Returns java type name of this message
     *
     * @return java type name of this message
     */
    public TypeName name() {
        return name;
    }

    /**
     * Returns java type name of the builder for this message
     *
     * @return java type name of the builder
     */
    public TypeName builderName() {
        return canonicalName(name.canonicalName() + "Builder");
    }

    /**
     * Returns all field definitions of this message
     *
     * @return fields
     */
    public List<FieldDefinition> fields() {
        return fields;
    }
}
