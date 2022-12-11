package com.github.pcimcioch.protobuf.model;

import com.github.pcimcioch.protobuf.model.message.EnumerationDefinition;
import com.github.pcimcioch.protobuf.model.message.MessageDefinition;
import com.github.pcimcioch.protobuf.model.type.TypeName;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    private static final class Valid {

        private static List<MessageDefinition> messages(List<MessageDefinition> messages) {
            if (messages == null) {
                throw new IllegalArgumentException("Messages can be empty, but not null");
            }
            for (MessageDefinition message : messages) {
                if (message == null) {
                    throw new IllegalArgumentException("Null message");
                }
            }

            return messages;
        }

        private static List<EnumerationDefinition> enumerations(List<EnumerationDefinition> enumerations) {
            if (enumerations == null) {
                throw new IllegalArgumentException("Enumerations can be empty, but not null");
            }
            for (EnumerationDefinition enumeration : enumerations) {
                if (enumeration == null) {
                    throw new IllegalArgumentException("Null enumeration");
                }
            }

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
