package com.github.pcimcioch.protobuf.source;

import com.github.pcimcioch.protobuf.code.CodeBody;
import com.github.pcimcioch.protobuf.code.RecordSource;
import com.github.pcimcioch.protobuf.dto.ProtoDto;
import com.github.pcimcioch.protobuf.dto.ProtobufMessage;
import com.github.pcimcioch.protobuf.model.field.FieldDefinition;
import com.github.pcimcioch.protobuf.model.message.MessageDefinition;

import static com.github.pcimcioch.protobuf.code.AnnotationSource.annotation;
import static com.github.pcimcioch.protobuf.code.CodeBody.body;
import static com.github.pcimcioch.protobuf.code.CodeBody.param;
import static com.github.pcimcioch.protobuf.code.CompactConstructorSource.compactConstructor;
import static com.github.pcimcioch.protobuf.code.FieldSource.field;
import static com.github.pcimcioch.protobuf.code.FinalSource.finalModifier;
import static com.github.pcimcioch.protobuf.code.ImplementsSource.implementz;
import static com.github.pcimcioch.protobuf.code.InitializerSource.initializer;
import static com.github.pcimcioch.protobuf.code.MethodSource.method;
import static com.github.pcimcioch.protobuf.code.ParameterSource.parameter;
import static com.github.pcimcioch.protobuf.code.RecordSource.record;
import static com.github.pcimcioch.protobuf.code.ReturnSource.returns;
import static com.github.pcimcioch.protobuf.code.StaticSource.staticModifier;
import static com.github.pcimcioch.protobuf.code.VisibilitySource.privateVisibility;
import static com.github.pcimcioch.protobuf.code.VisibilitySource.publicVisibility;
import static com.github.pcimcioch.protobuf.model.field.FieldDefinition.ProtoKind.ENUM;
import static com.github.pcimcioch.protobuf.model.field.FieldDefinition.ProtoKind.MESSAGE;

class MessageFactory {
    private final EncodingFactory encodingFactory = new EncodingFactory();
    private final DecodingFactory decodingFactory = new DecodingFactory();
    private final BuilderFactory builderFactory = new BuilderFactory();

    RecordSource buildMessageRecord(MessageDefinition message) {
        RecordSource source = buildSourceFile(message);

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

    private RecordSource buildSourceFile(MessageDefinition message) {
        return record(message.name())
                .set(publicVisibility())
                .add(implementz(ProtobufMessage.class.getCanonicalName() + "<" + message.name().canonicalName() + ">"));
    }

    private void addField(RecordSource source, FieldDefinition field) {
        source.add(parameter(field.javaFieldType(), field.javaFieldName())
                .addIf(annotation(Deprecated.class), field.deprecated())
        );
    }

    private void addEnumGetter(RecordSource source, FieldDefinition field) {
        CodeBody body = body("return $EnumType.forNumber($valueName);",
                param("EnumType", field.type()),
                param("valueName", field.javaFieldName())
        );

        source.add(method(field.name())
                .set(publicVisibility())
                .set(returns(field.type()))
                .set(body)
                .addIf(annotation(Deprecated.class), field.deprecated())
        );
    }

    private void addMessageGetter(RecordSource source, FieldDefinition field) {
        CodeBody body = body("return $field == null ? $FieldType.empty() : $field;",
                param("field", field.javaFieldName()),
                param("FieldType", field.javaFieldType())
        );

        source.add(method(field.javaFieldName())
                .set(publicVisibility())
                .set(returns(field.javaFieldType()))
                .set(body)
                .add(annotation(Override.class))
                .addIf(annotation(Deprecated.class), field.deprecated())
        );
    }

    private void addConstructor(RecordSource source, MessageDefinition message) {
        CodeBody body = body();
        for (FieldDefinition field : message.fields()) {
            body.append("$fieldName = $ProtoDto.copy($fieldName);",
                    param("fieldName", field.javaFieldName()),
                    param("ProtoDto", ProtoDto.class)
            );
        }

        source.add(compactConstructor()
                .set(publicVisibility())
                .set(body)
        );
    }

    private void addEncodingMethods(RecordSource source, MessageDefinition message) {
        encodingFactory.addEncodingMethods(source, message);
    }

    private void addDecodingMethods(RecordSource source, MessageDefinition message) {
        decodingFactory.addDecodingMethods(source, message);
    }

    private void addEmptyMethods(RecordSource source, MessageDefinition message) {
        source.add(field(message.name(), "EMPTY")
                .set(privateVisibility())
                .set(staticModifier())
                .set(finalModifier())
                .set(initializer("new Builder().build()"))
        );

        CodeBody emptyBody = body("return EMPTY;");
        source.add(method("empty")
                .set(publicVisibility())
                .set(staticModifier())
                .set(returns(message.name()))
                .set(emptyBody)
        );

        CodeBody isEmptyBody = body("return EMPTY.equals(this);");
        source.add(method("isEmpty")
                .set(publicVisibility())
                .set(returns(boolean.class))
                .set(isEmptyBody)
                .add(annotation(Override.class))
        );
    }

    private void addMergeMethod(RecordSource source, MessageDefinition message) {
        CodeBody body = body("return toBuilder().merge(toMerge).build();");

        source.add(method("merge")
                .set(publicVisibility())
                .set(returns(message.name()))
                .set(body)
                .add(annotation(Override.class))
                .add(parameter(message.name(), "toMerge"))
        );
    }

    private void addBuilderMethods(RecordSource source, MessageDefinition message) {
        CodeBody toBuilderBody = body("return builder().merge(this);");
        source.add(method("toBuilder")
                .set(publicVisibility())
                .set(returns(message.builderName()))
                .set(toBuilderBody)
        );

        CodeBody builderBody = body("return new $BuilderType();",
                param("BuilderType", message.builderName())
        );
        source.add(method("builder")
                .set(publicVisibility())
                .set(staticModifier())
                .set(returns(message.builderName()))
                .set(builderBody)
        );
    }

    private void addBuilderClass(RecordSource source, MessageDefinition message) {
        source.add(builderFactory.buildBuilderClass(message));
    }
}
