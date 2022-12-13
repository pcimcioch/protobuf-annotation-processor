package com.github.pcimcioch.protobuf.source;

import com.github.pcimcioch.protobuf.code.MethodBody;
import com.github.pcimcioch.protobuf.io.ProtobufWriter;
import com.github.pcimcioch.protobuf.model.field.FieldDefinition;
import com.github.pcimcioch.protobuf.model.message.MessageDefinition;
import org.jboss.forge.roaster.model.source.JavaRecordSource;
import org.jboss.forge.roaster.model.source.MethodSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static com.github.pcimcioch.protobuf.code.MethodBody.body;
import static com.github.pcimcioch.protobuf.code.MethodBody.param;

class EncodingFactory {

    void addEncodingMethods(JavaRecordSource messageRecord, MessageDefinition message) {
        addMethodWriteToOutputStream(messageRecord);
        addMethodToByteArray(messageRecord);
        addMethodWriteToProtobufWriter(messageRecord, message);
    }

    private void addMethodWriteToOutputStream(JavaRecordSource record) {
        MethodBody body = body("writeTo(new $ProtobufWriter(output));",
                param("ProtobufWriter", ProtobufWriter.class));

        MethodSource<JavaRecordSource> method = record.addMethod()
                .setPublic()
                .setReturnTypeVoid()
                .setName("writeTo")
                .addThrows(IOException.class)
                .setBody(body.toString());
        method.addParameter(OutputStream.class, "output");
        method.addAnnotation(Override.class);
    }

    private void addMethodToByteArray(JavaRecordSource record) {
        MethodBody body = body("""
                        $ByteArrayOutputStream output = new $ByteArrayOutputStream();
                        writeTo(new $ProtobufWriter(output));
                        return output.toByteArray();""",
                param("ByteArrayOutputStream", ByteArrayOutputStream.class),
                param("ProtobufWriter", ProtobufWriter.class));

        MethodSource<JavaRecordSource> method = record.addMethod()
                .setPublic()
                .setReturnType(byte[].class)
                .setName("toByteArray")
                .addThrows(IOException.class)
                .setBody(body.toString());
        method.addAnnotation(Override.class);
    }

    private void addMethodWriteToProtobufWriter(JavaRecordSource record, MessageDefinition message) {
        MethodBody body = body();

        for (FieldDefinition field : message.fields()) {
            body.append(encodingCode(field),
                    param("number", field.number()),
                    param("name", field.javaFieldName())
            );
        }

        MethodSource<JavaRecordSource> method = record.addMethod()
                .setPrivate()
                .setReturnTypeVoid()
                .setName("writeTo")
                .addThrows(IOException.class)
                .setBody(body.toString());
        method.addParameter(ProtobufWriter.class, "writer");
    }

    private String encodingCode(FieldDefinition field) {
        return switch(field.protoType()) {
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
