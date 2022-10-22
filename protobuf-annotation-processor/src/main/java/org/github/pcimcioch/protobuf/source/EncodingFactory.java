package org.github.pcimcioch.protobuf.source;

import org.github.pcimcioch.protobuf.io.ProtobufOutput;
import org.github.pcimcioch.protobuf.model.FieldDefinition;
import org.github.pcimcioch.protobuf.model.MessageDefinition;
import org.jboss.forge.roaster.model.source.JavaRecordSource;

import java.io.IOException;
import java.io.OutputStream;

class EncodingFactory {

    void addEncodingMethods(JavaRecordSource messageRecord, MessageDefinition message) {
        addMethodWriteToOutputStream(messageRecord);
        addMethodToByteArray(messageRecord);
        addMethodWriteToProtobufOutput(messageRecord, message);
    }

    private void addMethodWriteToOutputStream(JavaRecordSource record) {
        String body = "writeTo(new " +
                ProtobufOutput.class.getCanonicalName() +
                "(output));";

        record.addMethod()
                .setPublic()
                .setName("writeTo")
                .setBody(body)
                .addThrows(IOException.class)
                .addParameter(OutputStream.class, "output");
    }

    private void addMethodToByteArray(JavaRecordSource record) {
        StringBuilder body = new StringBuilder();

        body.append("java.io.ByteArrayOutputStream output = new java.io.ByteArrayOutputStream();");
        body.append("writeTo(new ")
                .append(ProtobufOutput.class.getCanonicalName())
                .append("(output));");
        body.append("return output.toByteArray();");

        record.addMethod()
                .setPublic()
                .setName("toByteArray")
                .setBody(body.toString())
                .setReturnType(byte[].class)
                .addThrows(IOException.class);
    }

    private void addMethodWriteToProtobufOutput(JavaRecordSource record, MessageDefinition message) {
        StringBuilder body = new StringBuilder();

        for (FieldDefinition field : message.fields()) {
            body.append("output.writeVarint(")
                    .append(field.tag())
                    .append(");");
            body.append(field.protobufWriteMethod("output", field.name())).append(";");
        }

        record.addMethod()
                .setPrivate()
                .setName("writeTo")
                .setBody(body.toString())
                .addThrows(IOException.class)
                .addParameter(ProtobufOutput.class, "output");
    }
}
