package com.github.pcimcioch.protobuf.source;

import com.github.pcimcioch.protobuf.io.ProtobufWriter;
import com.github.pcimcioch.protobuf.model.FieldDefinition;
import com.github.pcimcioch.protobuf.model.MessageDefinition;
import org.jboss.forge.roaster.model.source.JavaRecordSource;

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
        MethodBody body = body("writeTo(new ${ProtobufWriter}(output));",
                param("ProtobufWriter", ProtobufWriter.class));

        record.addMethod()
                .setPublic()
                .setName("writeTo")
                .setBody(body.toString())
                .addThrows(IOException.class)
                .addParameter(OutputStream.class, "output");
    }

    private void addMethodToByteArray(JavaRecordSource record) {
        MethodBody body = body("""
                        java.io.ByteArrayOutputStream output = new java.io.ByteArrayOutputStream();
                        writeTo(new ${ProtobufWriter}(output));
                        return output.toByteArray();""",
                param("ProtobufWriter", ProtobufWriter.class));

        record.addMethod()
                .setPublic()
                .setName("toByteArray")
                .setBody(body.toString())
                .setReturnType(byte[].class)
                .addThrows(IOException.class);
    }

    private void addMethodWriteToProtobufWriter(JavaRecordSource record, MessageDefinition message) {
        MethodBody body = body();

        for (FieldDefinition field : message.fields()) {
            body.append("${writeToOutput};",
                            param("writeToOutput", field.protobufWriteMethod("output")));
        }

        record.addMethod()
                .setPrivate()
                .setName("writeTo")
                .setBody(body.toString())
                .addThrows(IOException.class)
                .addParameter(ProtobufWriter.class, "output");
    }
}
