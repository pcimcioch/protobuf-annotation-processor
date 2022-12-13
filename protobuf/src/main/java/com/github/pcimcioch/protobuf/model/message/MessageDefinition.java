package com.github.pcimcioch.protobuf.model.message;

import com.github.pcimcioch.protobuf.model.field.FieldDefinition;
import com.github.pcimcioch.protobuf.model.type.TypeName;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.github.pcimcioch.protobuf.model.type.TypeName.canonicalName;

/**
 * Message definition
 */
public class MessageDefinition {
    private final TypeName name;
    private final List<FieldDefinition> fields;

    /**
     * Constructor
     *
     * @param name     name of the message
     * @param fields   fields of the message
     * @param reserved reserved fields
     */
    public MessageDefinition(TypeName name, List<FieldDefinition> fields, ReservedDefinition reserved) {
        this.name = Valid.name(name);
        this.fields = Valid.fields(fields, reserved);
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
        return canonicalName(name.canonicalName() + ".Builder");
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

        private static List<FieldDefinition> fields(List<FieldDefinition> fields, ReservedDefinition reserved) {
            if (fields == null || fields.isEmpty()) {
                throw new IllegalArgumentException("Message must have at least one field");
            }

            Set<String> names = new HashSet<>();
            Set<Integer> numbers = new HashSet<>();

            for (FieldDefinition field : fields) {
                if (field == null) {
                    throw new IllegalArgumentException("Null field");
                }

                if (!names.add(field.name())) {
                    throw new IllegalArgumentException("Duplicated field name: " + field.name());
                }
                if (!numbers.add(field.number())) {
                    throw new IllegalArgumentException("Duplicated field number: " + field.number());
                }
            }

            return reserved.validFields(fields);
        }
    }
}
