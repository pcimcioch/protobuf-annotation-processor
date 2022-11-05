package com.github.pcimcioch.protobuf.annotation;

import com.github.pcimcioch.protobuf.model.FieldDefinition;
import com.github.pcimcioch.protobuf.model.MessageDefinition;
import com.github.pcimcioch.protobuf.model.ProtoDefinitions;
import com.github.pcimcioch.protobuf.model.ScalarFieldType;
import com.github.pcimcioch.protobuf.model.TypeName;

import java.util.Arrays;
import java.util.List;

/**
 * Create model from annotations
 */
public class ModelFactory {

    /**
     * Create model from annotations
     *
     * @param messageAnnotations all annotations
     * @return model
     */
    public ProtoDefinitions buildProtoDefinitions(List<Message> messageAnnotations) {
        List<MessageDefinition> messages = messageAnnotations.stream()
                .map(this::buildMessage)
                .toList();

        return new ProtoDefinitions(messages);
    }

    private MessageDefinition buildMessage(Message message) {
        return new MessageDefinition(
                new TypeName(message.name()),
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
