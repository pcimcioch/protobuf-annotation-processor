package com.github.pcimcioch.protobuf.source;

import com.github.pcimcioch.protobuf.io.ProtobufWriter;
import com.github.pcimcioch.protobuf.model.FieldDefinition;
import com.github.pcimcioch.protobuf.model.MessageDefinition;
import org.jboss.forge.roaster.model.source.JavaRecordSource;
import org.jboss.forge.roaster.model.source.MethodSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static com.github.pcimcioch.protobuf.source.MethodBody.body;
import static com.github.pcimcioch.protobuf.source.MethodBody.param;

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
            body.append("writer.$writerMethod($number, $name);",
                    param("writerMethod", field.ioMethod()),
                    param("number", field.number()),
                    param("name", field.name()));
        }

        MethodSource<JavaRecordSource> method = record.addMethod()
                .setPrivate()
                .setReturnTypeVoid()
                .setName("writeTo")
                .addThrows(IOException.class)
                .setBody(body.toString());
        method.addParameter(ProtobufWriter.class, "writer");
    }
}
