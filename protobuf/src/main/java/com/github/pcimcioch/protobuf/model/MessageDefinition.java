package com.github.pcimcioch.protobuf.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        this.name = Valid.name(name);
        this.fields = Valid.fields(fields);
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

    private static final class Valid {

        private static TypeName name(TypeName name) {
            if (name == null) {
                throw new IllegalArgumentException("Message name cannot be null");
            }

            return name;
        }

        private static List<FieldDefinition> fields(List<FieldDefinition> fields) {
            if (fields == null || fields.isEmpty()) {
                throw new IllegalArgumentException("Message must have at least one field");
            }

            Set<String> names = new HashSet<>();
            for (FieldDefinition field : fields) {
                if (!names.add(field.name())) {
                    throw new IllegalArgumentException("Duplicated field name: " + field.name());
                }
            }

            Set<Integer> numbers = new HashSet<>();
            for (FieldDefinition field : fields) {
                if (!numbers.add(field.number())) {
                    throw new IllegalArgumentException("Duplicated field number: " + field.number());
                }
            }

            return fields;
        }
    }
}
