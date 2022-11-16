package com.github.pcimcioch.protobuf.annotation;

import com.github.pcimcioch.protobuf.model.FieldDefinition;
import com.github.pcimcioch.protobuf.model.MessageDefinition;
import com.github.pcimcioch.protobuf.model.ProtoDefinitions;
import com.github.pcimcioch.protobuf.model.ScalarFieldType;
import com.github.pcimcioch.protobuf.model.TypeName;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static com.github.pcimcioch.protobuf.model.TypeName.canonicalName;

/**
 * Create model from annotations
 */
public class ModelFactory {

    /**
     * Create model from annotated elements
     *
     * @param elements annotated elements
     * @return model
     */
    public ProtoDefinitions buildProtoDefinitions(Set<? extends Element> elements) {
        List<MessageDefinition> messages = elements.stream()
                .map(this::buildMessages)
                .flatMap(List::stream)
                .toList();

        return new ProtoDefinitions(messages);
    }

    private List<MessageDefinition> buildMessages(Element element) {
        List<MessageDefinition> messages = new ArrayList<>();
        for (Message message : element.getAnnotationsByType(Message.class)) {
            messages.add(new MessageDefinition(
                    buildMessageName(element, message),
                    buildFields(message)
            ));
        }

        return messages;
    }

    private static TypeName buildMessageName(Element element, Message message) {
        String annotationPackageName = packageOf(element).getQualifiedName().toString();
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

    private static PackageElement packageOf(Element element) {
        Element enclosing = element;
        while (enclosing.getKind() != ElementKind.PACKAGE) {
            enclosing = enclosing.getEnclosingElement();
        }

        return (PackageElement) enclosing;
    }

}
