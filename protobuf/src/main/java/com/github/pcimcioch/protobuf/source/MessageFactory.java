package com.github.pcimcioch.protobuf.source;

import com.github.pcimcioch.protobuf.code.MethodBody;
import com.github.pcimcioch.protobuf.dto.ProtobufMessage;
import com.github.pcimcioch.protobuf.model.FieldDefinition;
import com.github.pcimcioch.protobuf.model.MessageDefinition;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaRecordSource;
import org.jboss.forge.roaster.model.source.MethodSource;

import java.util.Objects;

import static com.github.pcimcioch.protobuf.code.MethodBody.body;
import static com.github.pcimcioch.protobuf.code.MethodBody.param;

final class MessageFactory {
    private final EncodingFactory encodingFactory = new EncodingFactory();
    private final DecodingFactory decodingFactory = new DecodingFactory();
    private final BuilderFactory builderFactory = new BuilderFactory();

    JavaRecordSource buildMessageRecord(MessageDefinition message) {
        JavaRecordSource source = buildSourceFile(message);
        addConstructor(source, message);
        addRecordComponents(source, message);
        addEncodingMethods(source, message);
        addDecodingMethods(source, message);
        addBuilderMethod(source, message);
        addBuilderClass(source, message);

        return source;
    }

    private JavaRecordSource buildSourceFile(MessageDefinition message) {
        return Roaster.create(JavaRecordSource.class)
                .setPackage(message.name().packageName())
                .setName(message.name().simpleName())
                .addInterface(ProtobufMessage.class);
    }

    // TODO it would be better to use compact constructor here. Waiting for https://github.com/forge/roaster/issues/275
    private void addConstructor(JavaRecordSource source, MessageDefinition message) {
        MethodBody body = body();
        for (FieldDefinition field : message.fields()) {
            if (field.requireNonNull()) {
                body.append("this.$fieldName = $Objects.requireNonNull($fieldName, \"Field $fieldName cannot be null\");",
                        param("fieldName", field),
                        param("Objects", Objects.class));
            } else {
                body.append("this.$fieldName = $fieldName;",
                        param("fieldName", field));
            }
        }

        MethodSource<JavaRecordSource> constructor = source.addMethod()
                .setPublic()
                .setConstructor(true)
                .setBody(body.toString());

        for (FieldDefinition field : message.fields()) {
            constructor.addParameter(field.typeName().canonicalName(), field.name());
        }
    }

    private void addRecordComponents(JavaRecordSource source, MessageDefinition message) {
        for (FieldDefinition field : message.fields()) {
            source.addRecordComponent(field.typeName().canonicalName(), field.name());
        }
    }

    private void addEncodingMethods(JavaRecordSource source, MessageDefinition message) {
        encodingFactory.addEncodingMethods(source, message);
    }

    private void addDecodingMethods(JavaRecordSource source, MessageDefinition message) {
        decodingFactory.addDecodingMethods(source, message);
    }

    private void addBuilderMethod(JavaRecordSource source, MessageDefinition message) {
        MethodBody body = body(
                "return new $BuilderType();",
                param("BuilderType", message.builderName()));

        source.addMethod()
                .setPublic()
                .setStatic(true)
                .setReturnType(message.builderName().canonicalName())
                .setName("builder")
                .setBody(body.toString());
    }

    private void addBuilderClass(JavaRecordSource source, MessageDefinition message) {
        source.addNestedType(builderFactory.buildBuilder(message));
    }
}
