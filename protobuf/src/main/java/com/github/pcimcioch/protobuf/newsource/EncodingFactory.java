package com.github.pcimcioch.protobuf.newsource;

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
                        return output.toByteArray();
                        """,
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
            body.appendln(encodingCode(field),
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

    private String encodingCode(FieldDefinition field) {
        return switch (field.protoKind()) {
            case DOUBLE -> "writer._double($number, $name);";
            case FLOAT -> "writer._float($number, $name);";
            case INT32, ENUM -> "writer.int32($number, $name);";
            case INT64 -> "writer.int64($number, $name);";
            case UINT32 -> "writer.uint32($number, $name);";
            case UINT64 -> "writer.uint64($number, $name);";
            case SINT32 -> "writer.sint32($number, $name);";
            case SINT64 -> "writer.sint64($number, $name);";
            case FIXED32 -> "writer.fixed32($number, $name);";
            case FIXED64 -> "writer.fixed64($number, $name);";
            case SFIXED32 -> "writer.sfixed32($number, $name);";
            case SFIXED64 -> "writer.sfixed64($number, $name);";
            case BOOL -> "writer.bool($number, $name);";
            case STRING -> "writer.string($number, $name);";
            case MESSAGE -> "writer.message($number, $name);";
            case BYTES -> "writer.bytes($number, $name);";
        };
    }
}
