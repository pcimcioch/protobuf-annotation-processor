package com.github.pcimcioch.protobuf.annotation;

import com.github.pcimcioch.protobuf.model.FieldDefinition;
import com.github.pcimcioch.protobuf.model.MessageDefinition;
import com.github.pcimcioch.protobuf.model.ProtoDefinitions;
import com.github.pcimcioch.protobuf.model.ScalarFieldType;
import com.github.pcimcioch.protobuf.model.TypeName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.github.pcimcioch.protobuf.model.TypeName.canonicalName;

/**
 * Create model from annotations
 */
public class ModelFactory {

    /**
     * Create model from protobuf files
     *
     * @param protoFiles protobuf files
     * @return model
     */
    public ProtoDefinitions buildProtoDefinitions(List<ProtoFile> protoFiles) {
        List<MessageDefinition> messages = protoFiles.stream()
                .map(this::buildMessages)
                .flatMap(List::stream)
                .toList();

        return new ProtoDefinitions(messages);
    }

    private List<MessageDefinition> buildMessages(ProtoFile file) {
        List<MessageDefinition> messages = new ArrayList<>();
        for (Message message : file.messages()) {
            messages.add(new MessageDefinition(
                    buildMessageName(file, message),
                    buildFields(message)
            ));
        }

        return messages;
    }

    private static TypeName buildMessageName(ProtoFile file, Message message) {
        String annotationPackageName = file.getOption(Option.javaPackage).orElse("");
        String messageName = message.name();

        if (messageName == null) {
            return canonicalName("");
        }
        if (messageName.startsWith(".")) {
            return canonicalName(annotationPackageName + messageName);
        }
        if (messageName.contains(".")) {
            return canonicalName(messageName);
        }
        return canonicalName(annotationPackageName + "." + messageName);
    }

    private List<FieldDefinition> buildFields(Message message) {
        return Arrays.stream(message.fields()).map(this::buildField).toList();
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
