package org.github.pcimcioch.protobuf.annotation;

import org.github.pcimcioch.protobuf.model.FieldDefinition;
import org.github.pcimcioch.protobuf.model.MessageDefinition;
import org.github.pcimcioch.protobuf.model.ProtoDefinitions;
import org.github.pcimcioch.protobuf.model.ScalarFieldType;
import org.github.pcimcioch.protobuf.model.TypeName;

import java.util.Arrays;
import java.util.List;

public class ModelFactory {
    public ProtoDefinitions buildProtoDefinitions(List<Message> messageAnnotations) {
        List<MessageDefinition> messages = messageAnnotations.stream()
                .map(this::buildMessage)
                .toList();

        return new ProtoDefinitions(messages);
    }

    private MessageDefinition buildMessage(Message message) {
        return new MessageDefinition(
                new TypeName(message.packageName(), message.name()),
                Arrays.stream(message.fields()).map(this::buildField).toList()
        );
    }

    private FieldDefinition buildField(Field field) {
        return new FieldDefinition(
                field.name(),
                buildFieldType(field.type()),
                field.number()
        );
    }

    private ScalarFieldType buildFieldType(String type) {
        return ScalarFieldType.fromProtoType(type).orElse(null);
    }

}
