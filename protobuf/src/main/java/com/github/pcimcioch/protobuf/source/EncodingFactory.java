package com.github.pcimcioch.protobuf.source;

import com.github.pcimcioch.protobuf.code.CodeBody;
import com.github.pcimcioch.protobuf.code.RecordSource;
import com.github.pcimcioch.protobuf.io.ProtobufWriter;
import com.github.pcimcioch.protobuf.model.field.FieldDefinition;
import com.github.pcimcioch.protobuf.model.message.MessageDefinition;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static com.github.pcimcioch.protobuf.code.AnnotationSource.annotation;
import static com.github.pcimcioch.protobuf.code.CodeBody.body;
import static com.github.pcimcioch.protobuf.code.CodeBody.param;
import static com.github.pcimcioch.protobuf.code.MethodSource.method;
import static com.github.pcimcioch.protobuf.code.ParameterSource.parameter;
import static com.github.pcimcioch.protobuf.code.ReturnSource.returns;
import static com.github.pcimcioch.protobuf.code.ThrowsSource.throwsEx;
import static com.github.pcimcioch.protobuf.code.VisibilitySource.privateVisibility;
import static com.github.pcimcioch.protobuf.code.VisibilitySource.publicVisibility;

class EncodingFactory {

    void addEncodingMethods(RecordSource messageRecord, MessageDefinition message) {
        addMethodWriteToOutputStream(messageRecord);
        addMethodToByteArray(messageRecord);
        addMethodWriteToProtobufWriter(messageRecord, message);
    }

    private void addMethodWriteToOutputStream(RecordSource record) {
        CodeBody body = body("writeTo(new $ProtobufWriter(output));",
                param("ProtobufWriter", ProtobufWriter.class)
        );

        record.add(method("writeTo")
                .set(publicVisibility())
                .add(throwsEx(IOException.class))
                .set(body)
                .add(parameter(OutputStream.class, "output"))
                .add(annotation(Override.class))
        );
    }

    private void addMethodToByteArray(RecordSource record) {
        CodeBody body = body("""
                        $ByteArrayOutputStream output = new $ByteArrayOutputStream();
                        writeTo(new $ProtobufWriter(output));
                        return output.toByteArray();""",
                param("ByteArrayOutputStream", ByteArrayOutputStream.class),
                param("ProtobufWriter", ProtobufWriter.class)
        );

        record.add(method("toByteArray")
                .set(publicVisibility())
                .set(returns(byte[].class))
                .add(throwsEx(IOException.class))
                .set(body)
                .add(annotation(Override.class))
        );
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
                .set(privateVisibility())
                .add(throwsEx(IOException.class))
                .set(body)
                .add(parameter(ProtobufWriter.class, "writer"))
        );
    }

    private String encodingMethod(FieldDefinition field) {
        return switch (field.protoKind()) {
            case DOUBLE -> "double_";
            case FLOAT -> "float_";
            case INT32, ENUM -> "int32";
            case INT64 -> "int64";
            case UINT32 -> "uint32";
            case UINT64 -> "uint64";
            case SINT32 -> "sint32";
            case SINT64 -> "sint64";
            case FIXED32 -> "fixed32";
            case FIXED64 -> "fixed64";
            case SFIXED32 -> "sfixed32";
            case SFIXED64 -> "sfixed64";
            case BOOL -> "bool";
            case STRING -> "string";
            case MESSAGE -> "message";
            case BYTES -> "bytes";
        };
    }
}
