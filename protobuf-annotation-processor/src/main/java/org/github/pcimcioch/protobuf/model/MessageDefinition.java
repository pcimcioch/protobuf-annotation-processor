package org.github.pcimcioch.protobuf.model;

import java.util.List;

public class MessageDefinition {
    private final TypeName name;
    private final List<FieldDefinition> fields;

    public MessageDefinition(TypeName name, List<FieldDefinition> fields) {
        this.name = name;
        this.fields = fields;
    }

    public String messageTypeSimpleName() {
        return name.simpleName();
    }

    public String messageTypePackage() {
        return name.packageName();
    }

    public String builderSimpleName() {
        return name.simpleName() + "Builder";
    }

    public List<FieldDefinition> fields() {
        return fields;
    }
}
