package com.github.pcimcioch.protobuf.model.message;

import com.github.pcimcioch.protobuf.code.TypeName;
import com.github.pcimcioch.protobuf.model.field.FieldDefinition;

import java.util.List;
import java.util.Objects;

import static com.github.pcimcioch.protobuf.code.TypeName.canonicalName;
import static com.github.pcimcioch.protobuf.model.validation.Assertions.assertAllMatches;
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
    private final List<MessageDefinition> messages;
    private final List<EnumerationDefinition> enumerations;

    /**
     * Constructor
     *
     * @param name         name of the message
     * @param fields       fields of the message
     * @param reserved     reserved fields
     * @param messages     nested messages
     * @param enumerations nested enumerations
     */
    public MessageDefinition(TypeName name, List<FieldDefinition> fields, ReservedDefinition reserved,
                             List<MessageDefinition> messages, List<EnumerationDefinition> enumerations) {
        this.name = Valid.name(name);
        this.fields = Valid.fields(fields, reserved);
        this.messages = Valid.messages(name, messages);
        this.enumerations = Valid.enumerations(name, enumerations);
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

    /**
     * Returns all nested messages
     *
     * @return nested messages
     */
    public List<MessageDefinition> messages() {
        return messages;
    }

    /**
     * Returns all nested enumerations
     *
     * @return nested enumerations
     */
    public List<EnumerationDefinition> enumerations() {
        return enumerations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageDefinition that = (MessageDefinition) o;
        return name.equals(that.name) && fields.equals(that.fields) && messages.equals(that.messages) && enumerations.equals(that.enumerations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, fields, messages, enumerations);
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

        private static List<MessageDefinition> messages(TypeName name, List<MessageDefinition> messages) {
            assertNonNull(messages, "Nested messages cannot be null");
            assertContainsNoNulls(messages, "Null nested message");
            assertAllMatches(messages, message -> message.name().isDirectChildOf(name), "Nested message has non-nested type");

            return messages;
        }

        private static List<EnumerationDefinition> enumerations(TypeName name, List<EnumerationDefinition> enumerations) {
            assertNonNull(enumerations, "Nested enumerations cannot be null");
            assertContainsNoNulls(enumerations, "Null nested enumeration");
            assertAllMatches(enumerations, enumeration -> enumeration.name().isDirectChildOf(name), "Nested enumeration has non-nested type");

            return enumerations;
        }
    }
}
