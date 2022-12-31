package com.github.pcimcioch.protobuf.model;

import com.github.pcimcioch.protobuf.model.message.EnumerationDefinition;
import com.github.pcimcioch.protobuf.model.message.MessageDefinition;
import com.github.pcimcioch.protobuf.model.type.TypeName;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.github.pcimcioch.protobuf.model.validation.Assertions.assertContainsNoNulls;
import static com.github.pcimcioch.protobuf.model.validation.Assertions.assertNonNull;

/**
 * Full protobuf model definitions
 */
public class ProtoDefinitions {
    private final List<MessageDefinition> messages;
    private final List<EnumerationDefinition> enumerations;

    /**
     * Constructor
     *
     * @param messages all messages in this model
     * @param enumerations all enumerations in this model
     */
    public ProtoDefinitions(List<MessageDefinition> messages, List<EnumerationDefinition> enumerations) {
        this.messages = Valid.messages(messages);
        this.enumerations = Valid.enumerations(enumerations);
        Valid.names(messages, enumerations);
    }

    /**
     * Returns all messages in this model
     *
     * @return messages
     */
    public List<MessageDefinition> messages() {
        return messages;
    }

    /**
     * Returns all enumerations in this model
     *
     * @return enumerations
     */
    public List<EnumerationDefinition> enumerations() {
        return enumerations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProtoDefinitions that = (ProtoDefinitions) o;
        return messages.equals(that.messages) && enumerations.equals(that.enumerations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messages, enumerations);
    }

    private static final class Valid {

        private static List<MessageDefinition> messages(List<MessageDefinition> messages) {
            assertNonNull(messages, "Messages can be empty, but not null");
            assertContainsNoNulls(messages, "Null message");

            return messages;
        }

        private static List<EnumerationDefinition> enumerations(List<EnumerationDefinition> enumerations) {
            assertNonNull(enumerations, "Enumerations can be empty, but not null");
            assertContainsNoNulls(enumerations, "Null enumeration");

            return enumerations;
        }

        private static void names(List<MessageDefinition> messages, List<EnumerationDefinition> enumerations) {
            Set<TypeName> names = new HashSet<>();
            for (MessageDefinition message : messages) {
                if (!names.add(message.name())) {
                    throw new IllegalArgumentException("Duplicated message name: " + message.name());
                }
            }
            for (EnumerationDefinition enumeration : enumerations) {
                if (!names.add(enumeration.name())) {
                    throw new IllegalArgumentException("Duplicated enumeration name: " + enumeration.name());
                }
            }
        }
    }
}
