package com.github.pcimcioch.protobuf.model;

import java.util.List;

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
        this.messages = messages;
    }

    /**
     * Returns all messages in this model
     *
     * @return messages
     */
    public List<MessageDefinition> messages() {
        return messages;
    }
}
