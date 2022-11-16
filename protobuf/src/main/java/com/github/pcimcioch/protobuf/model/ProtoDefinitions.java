package com.github.pcimcioch.protobuf.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Full protobuf model definitions
 */
public class ProtoDefinitions {
    private final List<MessageDefinition> messages;

    /**
     * Constructor
     *
     * @param messages all messages in this model
     */
    public ProtoDefinitions(List<MessageDefinition> messages) {
        this.messages = Valid.messages(messages);
    }

    /**
     * Returns all messages in this model
     *
     * @return messages
     */
    public List<MessageDefinition> messages() {
        return messages;
    }

    private static final class Valid {

        private static List<MessageDefinition> messages(List<MessageDefinition> messages) {
            if (messages == null) {
                throw new IllegalArgumentException("Message can be empty, but not null");
            }

            Set<TypeName> names = new HashSet<>();
            for (MessageDefinition message : messages) {
                if (!names.add(message.name())) {
                    throw new IllegalArgumentException("Duplicated message name: " + message.name());
                }
            }

            return messages;
        }
    }
}
