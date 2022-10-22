package org.github.pcimcioch.protobuf.model;

import java.util.List;

public class ProtoDefinitions{
    private final List<MessageDefinition> messages;

    public ProtoDefinitions(List<MessageDefinition> messages) {
        this.messages = messages;
    }

    public List<MessageDefinition> messages() {
        return messages;
    }
}
