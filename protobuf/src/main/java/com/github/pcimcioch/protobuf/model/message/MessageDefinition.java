package com.github.pcimcioch.protobuf.model.message;

import com.github.pcimcioch.protobuf.model.field.FieldDefinition;
import com.github.pcimcioch.protobuf.model.type.TypeName;
import com.github.pcimcioch.protobuf.model.validation.Assertions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.github.pcimcioch.protobuf.model.type.TypeName.canonicalName;
import static com.github.pcimcioch.protobuf.model.validation.Assertions.assertContainsNoNulls;
import static com.github.pcimcioch.protobuf.model.validation.Assertions.assertNoDuplicates;
import static com.github.pcimcioch.protobuf.model.validation.Assertions.assertNonEmpty;
import static com.github.pcimcioch.protobuf.model.validation.Assertions.assertNonNull;

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
            assertNonNull(name, "Message name cannot be null");
            return name;
        }

        private static List<FieldDefinition> fields(List<FieldDefinition> fields, ReservedDefinition reserved) {
            assertNonEmpty(fields, "Message must have at least one field");
            assertContainsNoNulls(fields, "Null field");
            assertNoDuplicates(fields, FieldDefinition::name, "Duplicated field name: %s");
            assertNoDuplicates(fields, FieldDefinition::number, "Duplicated field number: %s");

            return reserved.validFields(fields);
        }
    }
}
