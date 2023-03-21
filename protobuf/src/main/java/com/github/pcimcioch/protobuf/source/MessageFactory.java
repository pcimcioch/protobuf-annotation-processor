package com.github.pcimcioch.protobuf.source;

import com.github.pcimcioch.protobuf.code.CodeBody;
import com.github.pcimcioch.protobuf.code.RecordSource;
import com.github.pcimcioch.protobuf.code.TypeName;
import com.github.pcimcioch.protobuf.dto.IntList;
import com.github.pcimcioch.protobuf.dto.ProtoDto;
import com.github.pcimcioch.protobuf.dto.ProtobufMessage;
import com.github.pcimcioch.protobuf.model.field.FieldDefinition;
import com.github.pcimcioch.protobuf.model.message.MessageDefinition;

import static com.github.pcimcioch.protobuf.code.AnnotationSource.annotation;
import static com.github.pcimcioch.protobuf.code.CodeBody.body;
import static com.github.pcimcioch.protobuf.code.CodeBody.param;
import static com.github.pcimcioch.protobuf.code.CompactConstructorSource.compactConstructor;
import static com.github.pcimcioch.protobuf.code.ImplementsSource.implementz;
import static com.github.pcimcioch.protobuf.code.MethodSource.method;
import static com.github.pcimcioch.protobuf.code.ParameterSource.parameter;
import static com.github.pcimcioch.protobuf.code.RecordSource.record;
import static com.github.pcimcioch.protobuf.code.ReturnSource.returns;
import static com.github.pcimcioch.protobuf.code.VisibilitySource.publicVisibility;
import static com.github.pcimcioch.protobuf.model.field.FieldDefinition.ProtoKind.ENUM;
import static com.github.pcimcioch.protobuf.model.field.FieldDefinition.ProtoKind.MESSAGE;

class MessageFactory {
    private final EncodingFactory encodingFactory = new EncodingFactory();
    private final DecodingFactory decodingFactory = new DecodingFactory();
    private final SizeFactory sizeFactory = new SizeFactory();
    private final BuilderMethodsFactory builderMethodsFactory = new BuilderMethodsFactory();
    private final BuilderClassFactory builderClassFactory = new BuilderClassFactory();

    RecordSource buildMessageRecord(MessageDefinition message) {
        RecordSource source = buildSourceFile(message);

        for (FieldDefinition field : message.fields()) {
            addField(source, field);
            addFieldGetter(source, field);
        }
        addConstructor(source, message);
        addEncodingMethods(source, message);
        addDecodingMethods(source, message);
        addSizeMethods(source, message);
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
                .addIf(annotation(Deprecated.class), field.rules().deprecated())
        );
    }

    private void addFieldGetter(RecordSource source, FieldDefinition field) {
        if (field.rules().repeated()) {
            if (field.protoKind() == ENUM) {
                addEnumListGetter(source, field);
            }
        } else {
            if (field.protoKind() == ENUM) {
                addEnumSingleGetter(source, field);
            }
            if (field.protoKind() == MESSAGE) {
                addMessageGetter(source, field);
            }
        }
    }

    private void addEnumSingleGetter(RecordSource source, FieldDefinition field) {
        CodeBody body = body("return $EnumType.forNumber($valueName);",
                param("EnumType", field.protobufType()),
                param("valueName", field.javaFieldName())
        );

        source.add(method(field.name())
                .set(publicVisibility())
                .set(returns(field.protobufType()))
                .set(body)
                .addIf(annotation(Deprecated.class), field.rules().deprecated())
        );
    }

    private void addEnumListGetter(RecordSource source, FieldDefinition field) {
        CodeBody body = body("return $valueName.valuesList();",
                param("valueName", field.javaFieldName())
        );

        source.add(method(field.name() + "Value")
                .set(publicVisibility())
                .set(returns(IntList.class))
                .set(body)
                .addIf(annotation(Deprecated.class), field.rules().deprecated())
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
                .addIf(annotation(Deprecated.class), field.rules().deprecated())
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

    private void addSizeMethods(RecordSource source, MessageDefinition message) {
        sizeFactory.addSizeMethods(source, message);
    }

    private void addBuilderMethods(RecordSource source, MessageDefinition message) {
        builderMethodsFactory.addBuilderMethods(source, message);
    }

    private void addBuilderClass(RecordSource source, MessageDefinition message) {
        source.add(builderClassFactory.buildBuilderClass(message));
    }
}
