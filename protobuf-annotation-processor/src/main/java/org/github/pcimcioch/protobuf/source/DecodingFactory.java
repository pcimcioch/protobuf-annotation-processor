package org.github.pcimcioch.protobuf.source;

import org.github.pcimcioch.protobuf.exception.ProtobufParseException;
import org.github.pcimcioch.protobuf.io.ProtobufInput;
import org.github.pcimcioch.protobuf.model.FieldDefinition;
import org.github.pcimcioch.protobuf.model.MessageDefinition;
import org.jboss.forge.roaster.model.source.JavaRecordSource;

import java.io.IOException;
import java.io.InputStream;

class DecodingFactory {

    void addDecodingMethods(JavaRecordSource messageRecord, MessageDefinition message) {
        addParseBytesMethod(messageRecord, message);
        addParseStreamMethod(messageRecord, message);
        addParseProtobufInputMethod(messageRecord, message);
    }

    private void addParseBytesMethod(JavaRecordSource messageRecord, MessageDefinition message) {
        String body = "return parse(new org.github.pcimcioch.protobuf.io.ProtobufInput(new java.io.ByteArrayInputStream(data)));";

        messageRecord.addMethod()
                .setPublic()
                .setStatic(true)
                .setReturnType(message.messageTypeSimpleName())
                .setName("parse")
                .setBody(body)
                .addThrows(IOException.class)
                .addParameter(byte[].class, "data");
    }

    private void addParseStreamMethod(JavaRecordSource messageRecord, MessageDefinition message) {
        String body = "return parse(new " + ProtobufInput.class.getCanonicalName() + "(stream));";

        messageRecord.addMethod()
                .setPublic()
                .setStatic(true)
                .setReturnType(message.messageTypeSimpleName())
                .setName("parse")
                .setBody(body)
                .addThrows(IOException.class)
                .addParameter(InputStream.class, "stream");
    }

    private void addParseProtobufInputMethod(JavaRecordSource messageRecord, MessageDefinition message) {
        // TODO some kind of custom method builder?
        StringBuilder body = new StringBuilder();

        body.append(message.builderSimpleName()).append(" builder = new ").append(message.builderSimpleName()).append("();");

        body.append("while(true) {");
        appendTagReading(body);
        body.append("\n");
        appendFieldsReading(body, message);
        body.append("\n");
        appendUnknownFieldSupport(body);
        body.append("\n");
        body.append("}");

        body.append("return builder.build();");

        messageRecord.addMethod()
                .setPrivate()
                .setStatic(true)
                .setReturnType(message.messageTypeSimpleName())
                .setName("parse")
                .setBody(body.toString())
                .addThrows(IOException.class)
                .addParameter(ProtobufInput.class, "input");
    }

    private void appendTagReading(StringBuilder body) {
        body.append("""
                long tag = 0L;
                try {
                    tag = input.readVarint();
                } catch (java.io.EOFException ex) {
                    break;
                }
                long number = tag >>> 3;
                int wireType = (int) tag & 0b111;
                """);
    }

    private void appendFieldsReading(StringBuilder body, MessageDefinition message) {
        boolean first = true;

        for (FieldDefinition field : message.fields()) {
            if (!first) {
                body.append("else ");
            }
            body.append("if (number == ").append(field.number()).append(") {");

            body.append("if (wireType != ").append(field.wireType().id()).append(") {");
            body.append("throw new ").append(ProtobufParseException.class.getCanonicalName()).append("(\"Field [name=").append(field.name()).append(", number=").append(field.number()).append("] incorrect wire type. Expected ").append(field.wireType().id()).append(", got \" + wireType);");
            body.append("}");

            body.append("builder.").append(field.name()).append("(").append(field.protobufReadMethod("input")).append(");");

            body.append("}");
            first = false;
        }
    }

    private void appendUnknownFieldSupport(StringBuilder body) {
        body.append("else {");

        body.append("switch(wireType) {");
        body.append("case 0: input.readVarint(); break;");
        body.append("case 1: input.readFixedLong(); break;");// TODO input.skip()?
        body.append("case 2: input.readBytes(); break;");// TODO input.skip()?
        body.append("case 3: throw new ").append(ProtobufParseException.class.getCanonicalName()).append("(\"Wire Type SGROUP is not supported\");");
        body.append("case 4: throw new ").append(ProtobufParseException.class.getCanonicalName()).append("(\"Wire Type EGROUP is not supported\");");
        body.append("case 5: input.readFixedInt(); break;");// TODO input.skip()?
        body.append("default: throw new ").append(ProtobufParseException.class.getCanonicalName()).append("(\"Unknown wire type \" + wireType);");
        body.append("}");

        body.append("}");
    }
}
