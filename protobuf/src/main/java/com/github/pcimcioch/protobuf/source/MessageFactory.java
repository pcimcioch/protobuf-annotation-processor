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
import static com.github.pcimcioch.protobuf.model.field.FieldDefinition.ProtoKind.MESSAGE;

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
            if (field.protoKind() == MESSAGE) {
                addMessageGetter(source, field);
            }
        }
        addConstructor(source, message);
        addEncodingMethods(source, message);
        addDecodingMethods(source, message);
        addEmptyMethods(source, message);
        addMergeMethod(source, message);
        addBuilderMethods(source, message);
        addBuilderClass(source, message);

        return source;
    }

    private JavaRecordSource buildSourceFile(MessageDefinition message) {
        return Roaster.create(JavaRecordSource.class)
                .setPackage(message.name().packageName())
                .setName(message.name().simpleName())
                .addInterface(ProtobufMessage.class.getCanonicalName() + "<" + message.name().simpleName() + ">");
    }

    private void addField(JavaRecordSource source, FieldDefinition field) {
        JavaRecordComponentSource component = source.addRecordComponent(field.javaFieldType().canonicalName(), field.javaFieldName());
        field.handleDeprecated(component);
    }

    private void addEnumGetter(JavaRecordSource source, FieldDefinition field) {
        MethodBody body = body("return $EnumType.forNumber($valueName);",
                param("EnumType", field.type()),
                param("valueName", field.javaFieldName())
        );

        MethodSource<JavaRecordSource> method = source.addMethod()
                .setPublic()
                .setReturnType(field.type().canonicalName())
                .setName(field.name())
                .setBody(body.toString());
        field.handleDeprecated(method);
    }

    private void addMessageGetter(JavaRecordSource source, FieldDefinition field) {
        MethodBody body = body("return $field == null ? $FieldType.empty() : $field;",
                param("field", field.javaFieldName()),
                param("FieldType", field.javaFieldType())
        );

        MethodSource<JavaRecordSource> method = source.addMethod()
                .setPublic()
                .setReturnType(field.javaFieldType().canonicalName())
                .setName(field.javaFieldName())
                .setBody(body.toString());
        method.addAnnotation(Override.class);
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

    private void addEmptyMethods(JavaRecordSource source, MessageDefinition message) {
        // TODO [improvement] EMPTY should be in this record, not the builder https://github.com/forge/roaster/issues/279
        MethodBody emptyBody = body("return Builder.EMPTY;");
        source.addMethod()
                .setPublic()
                .setStatic(true)
                .setReturnType(message.name().canonicalName())
                .setName("empty")
                .setBody(emptyBody.toString());

        MethodBody isEmptyBody = body("return empty().equals(this);");
        MethodSource<JavaRecordSource> isEmptyMethod = source.addMethod()
                .setPublic()
                .setReturnType(boolean.class)
                .setName("isEmpty")
                .setBody(isEmptyBody.toString());
        isEmptyMethod.addAnnotation(Override.class);
    }

    private void addMergeMethod(JavaRecordSource source, MessageDefinition message) {
        MethodBody body = body("return toBuilder().merge(toMerge).build();");

        MethodSource<JavaRecordSource> method = source.addMethod()
                .setPublic()
                .setReturnType(message.name().simpleName())
                .setName("merge")
                .setBody(body.toString());
        method.addAnnotation(Override.class);
        method.addParameter(message.name().simpleName(), "toMerge");
    }

    private void addBuilderMethods(JavaRecordSource source, MessageDefinition message) {
        MethodBody toBuilderBody = body("return builder().merge(this);");

        source.addMethod()
                .setPublic()
                .setReturnType(message.builderName().canonicalName())
                .setName("toBuilder")
                .setBody(toBuilderBody.toString());

        MethodBody builderBody = body(
                "return new $BuilderType();",
                param("BuilderType", message.builderName()));
        source.addMethod()
                .setPublic()
                .setStatic(true)
                .setReturnType(message.builderName().canonicalName())
                .setName("builder")
                .setBody(builderBody.toString());
    }

    private void addBuilderClass(JavaRecordSource source, MessageDefinition message) {
        source.addNestedType(builderFactory.buildBuilder(message));
    }
}
