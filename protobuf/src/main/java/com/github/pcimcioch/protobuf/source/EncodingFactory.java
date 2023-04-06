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
            body.append(encodingMethod(field));
        }

        record.add(method("writeTo")
                .set(publicVisibility())
                .add(throwsEx(IOException.class))
                .set(body)
                .add(annotation(Override.class))
                .add(parameter(ProtobufWriter.class, "writer"))
        );
    }

    private CodeBody encodingMethod(FieldDefinition field) {
        String suffix = field.rules().repeated()
                ? field.rules().packed() ? "Packed" : "Unpacked"
                : "";
        String method = switch (field.protoKind()) {
            case DOUBLE -> "writer.writeDouble$suffix($number, $name);";
            case FLOAT -> "writer.writeFloat$suffix($number, $name);";
            case INT32 -> "writer.writeInt32$suffix($number, $name);";
            case INT64 -> "writer.writeInt64$suffix($number, $name);";
            case UINT32 -> "writer.writeUint32$suffix($number, $name);";
            case UINT64 -> "writer.writeUint64$suffix($number, $name);";
            case SINT32 -> "writer.writeSint32$suffix($number, $name);";
            case SINT64 -> "writer.writeSint64$suffix($number, $name);";
            case FIXED32 -> "writer.writeFixed32$suffix($number, $name);";
            case FIXED64 -> "writer.writeFixed64$suffix($number, $name);";
            case SFIXED32 -> "writer.writeSfixed32$suffix($number, $name);";
            case SFIXED64 -> "writer.writeSfixed64$suffix($number, $name);";
            case BOOL -> "writer.writeBool$suffix($number, $name);";
            case STRING -> "writer.writeString$suffix($number, $name);";
            case MESSAGE -> "writer.writeMessage$suffix($number, $name);";
            case UNKNOWN -> "writer.writeUnknownFields$suffix($name);";
            case BYTES -> "writer.writeBytes$suffix($number, $name);";
            case ENUM -> field.rules().repeated() ? "writer.writeEnum$suffix($number, $name);" : "writer.writeInt32$suffix($number, $name);";
        };

        return body(method,
                param("suffix", suffix),
                param("number", field.number()),
                param("name", field.javaFieldName())
        );
    }
}
