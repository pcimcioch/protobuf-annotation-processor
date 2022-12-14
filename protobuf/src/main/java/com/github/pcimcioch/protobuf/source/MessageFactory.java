package com.github.pcimcioch.protobuf.source;

import com.github.pcimcioch.protobuf.code.MethodBody;
import com.github.pcimcioch.protobuf.dto.ProtoDto;
import com.github.pcimcioch.protobuf.dto.ProtobufMessage;
import com.github.pcimcioch.protobuf.model.field.FieldDefinition;
import com.github.pcimcioch.protobuf.model.message.MessageDefinition;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaRecordComponentSource;
import org.jboss.forge.roaster.model.source.JavaRecordSource;
import org.jboss.forge.roaster.model.source.MethodSource;

import static com.github.pcimcioch.protobuf.code.MethodBody.body;
import static com.github.pcimcioch.protobuf.code.MethodBody.param;
import static com.github.pcimcioch.protobuf.model.field.FieldDefinition.ProtoKind.ENUM;

final class MessageFactory {
    private final EncodingFactory encodingFactory = new EncodingFactory();
    private final DecodingFactory decodingFactory = new DecodingFactory();
    private final BuilderFactory builderFactory = new BuilderFactory();

    JavaRecordSource buildMessageRecord(MessageDefinition message) {
        JavaRecordSource source = buildSourceFile(message);

        for (FieldDefinition field : message.fields()) {
            addField(source, field);
            if (field.protoKind() == ENUM) {
                addEnumGetter(source, field);
            }
        }
        addConstructor(source, message);
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

    private void addField(JavaRecordSource source, FieldDefinition field) {
        JavaRecordComponentSource component = source.addRecordComponent(field.javaFieldType().canonicalName(), field.javaFieldName());
        field.handleDeprecated(component);
    }

    private void addEnumGetter(JavaRecordSource source, FieldDefinition field) {
        MethodBody body = body("return $enumType.forNumber($valueName);",
                param("enumType", field.type()),
                param("valueName", field.javaFieldName())
        );

        MethodSource<JavaRecordSource> method = source.addMethod()
                .setPublic()
                .setReturnType(field.type().canonicalName())
                .setName(field.name())
                .setBody(body.toString());
        field.handleDeprecated(method);
    }

    // TODO [improvement] it would be better to use compact constructor here. Waiting for https://github.com/forge/roaster/issues/275
    private void addConstructor(JavaRecordSource source, MessageDefinition message) {
        MethodBody body = body();

        for (FieldDefinition field : message.fields()) {
            body.append("this.$fieldName = $ProtoDto.copy($fieldName);",
                    param("fieldName", field.javaFieldName()),
                    param("ProtoDto", ProtoDto.class));
        }

        MethodSource<JavaRecordSource> constructor = source.addMethod()
                .setPublic()
                .setConstructor(true)
                .setBody(body.toString());

        for (FieldDefinition field : message.fields()) {
            constructor.addParameter(field.javaFieldType().canonicalName(), field.javaFieldName());
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
