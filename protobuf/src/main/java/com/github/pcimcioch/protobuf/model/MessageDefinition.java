package com.github.pcimcioch.protobuf.model;

import java.util.List;

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
     * Returns java simple name of the message (the part without package)
     *
     * @return simple name
     */
    public String messageTypeSimpleName() {
        return name.simpleName();
    }

    /**
     * Returns java package of this message
     *
     * @return package
     */
    public String messageTypePackage() {
        return name.packageName();
    }

    /**
     * Returns java simple name of the builder for this message
     *
     * @return simple name of the builder
     */
    public String builderSimpleName() {
        return name.simpleName() + "Builder";
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
