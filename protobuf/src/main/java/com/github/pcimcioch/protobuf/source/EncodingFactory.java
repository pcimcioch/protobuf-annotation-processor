package com.github.pcimcioch.protobuf.source;

import com.github.pcimcioch.protobuf.code.CodeBody;
import com.github.pcimcioch.protobuf.code.RecordSource;
import com.github.pcimcioch.protobuf.io.ProtobufWriter;
import com.github.pcimcioch.protobuf.model.field.FieldDefinition;
import com.github.pcimcioch.protobuf.model.message.MessageDefinition;

import java.io.IOException;

import static com.github.pcimcioch.protobuf.code.AnnotationSource.annotation;
import static com.github.pcimcioch.protobuf.code.CodeBody.body;
import static com.github.pcimcioch.protobuf.code.CodeBody.param;
import static com.github.pcimcioch.protobuf.code.MethodSource.method;
import static com.github.pcimcioch.protobuf.code.ParameterSource.parameter;
import static com.github.pcimcioch.protobuf.code.ThrowsSource.throwsEx;
import static com.github.pcimcioch.protobuf.code.VisibilitySource.publicVisibility;

class EncodingFactory {

    void addEncodingMethods(RecordSource messageRecord, MessageDefinition message) {
        addMethodWriteToProtobufWriter(messageRecord, message);
    }

    private void addMethodWriteToProtobufWriter(RecordSource record, MessageDefinition message) {
        CodeBody body = body();

        for (FieldDefinition field : message.fields()) {
            body.appendExceptFirst("\n");
            body.append("writer.$method($number, $name);",
                    param("method", encodingMethod(field)),
                    param("number", field.number()),
                    param("name", field.javaFieldName())
            );
        }

        record.add(method("writeTo")
                .set(publicVisibility())
                .add(throwsEx(IOException.class))
                .set(body)
                .add(annotation(Override.class))
                .add(parameter(ProtobufWriter.class, "writer"))
        );
    }

    private String encodingMethod(FieldDefinition field) {
        String suffix = field.rules().repeated()
                ? field.rules().packed() ? "Packed" : "Unpacked"
                : "";
        String method = switch (field.protoKind()) {
            case DOUBLE -> "writeDouble";
            case FLOAT -> "writeFloat";
            case INT32 -> "writeInt32";
            case INT64 -> "writeInt64";
            case UINT32 -> "writeUint32";
            case UINT64 -> "writeUint64";
            case SINT32 -> "writeSint32";
            case SINT64 -> "writeSint64";
            case FIXED32 -> "writeFixed32";
            case FIXED64 -> "writeFixed64";
            case SFIXED32 -> "writeSfixed32";
            case SFIXED64 -> "writeSfixed64";
            case BOOL -> "writeBool";
            case STRING -> "writeString";
            case MESSAGE -> "writeMessage";
            case BYTES -> "writeBytes";
            case ENUM -> field.rules().repeated() ? "writeEnum" : "writeInt32";
        };

        return method + suffix;
    }
}
