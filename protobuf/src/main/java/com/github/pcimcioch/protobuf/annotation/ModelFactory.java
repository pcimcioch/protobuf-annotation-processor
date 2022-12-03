package com.github.pcimcioch.protobuf.annotation;

import com.github.pcimcioch.protobuf.model.EnumerationDefinition;
import com.github.pcimcioch.protobuf.model.EnumerationElementDefinition;
import com.github.pcimcioch.protobuf.model.FieldDefinition;
import com.github.pcimcioch.protobuf.model.MessageDefinition;
import com.github.pcimcioch.protobuf.model.ProtoDefinitions;
import com.github.pcimcioch.protobuf.model.ScalarFieldType;
import com.github.pcimcioch.protobuf.model.TypeName;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

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
                .flatMap(this::buildMessages)
                .toList();
        List<EnumerationDefinition> enumerations = protoFiles.stream()
                .flatMap(this::buildEnumerations)
                .toList();

        return new ProtoDefinitions(messages, enumerations);
    }

    private Stream<MessageDefinition> buildMessages(ProtoFile file) {
        return file.messages().stream()
                .map(message -> new MessageDefinition(
                        buildName(file.javaPackage(), message.name()),
                        buildFields(message)
                ));
    }

    private static TypeName buildName(String annotationPackageName, String messageName) {
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
        return Arrays.stream(message.fields())
                .map(this::buildField)
                .toList();
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

    private Stream<EnumerationDefinition> buildEnumerations(ProtoFile file) {
        return file.enumerations().stream()
                .map(enumeration -> new EnumerationDefinition(
                        buildName(file.javaPackage(), enumeration.name()),
                        enumeration.allowAlias(),
                        buildEnumerationElements(enumeration)
                ));
    }

    private List<EnumerationElementDefinition> buildEnumerationElements(Enumeration enumeration) {
        return Arrays.stream(enumeration.elements())
                .map(this::buildEnumerationElement)
                .toList();
    }

    private EnumerationElementDefinition buildEnumerationElement(EnumerationElement element) {
        return new EnumerationElementDefinition(element.name(), element.number());
    }
}
