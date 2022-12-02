package com.github.pcimcioch.protobuf.source;

import com.github.pcimcioch.protobuf.io.ProtobufReader;
import com.github.pcimcioch.protobuf.io.Tag;
import com.github.pcimcioch.protobuf.model.MessageDefinition;
import com.github.pcimcioch.protobuf.model.FieldDefinition;
import org.jboss.forge.roaster.model.source.JavaRecordSource;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import static com.github.pcimcioch.protobuf.source.MethodBody.body;
import static com.github.pcimcioch.protobuf.source.MethodBody.param;

class DecodingFactory {

    void addDecodingMethods(JavaRecordSource messageRecord, MessageDefinition message) {
        addParseBytesMethod(messageRecord, message);
        addParseStreamMethod(messageRecord, message);
        addParseProtobufInputMethod(messageRecord, message);
    }

    private void addParseBytesMethod(JavaRecordSource messageRecord, MessageDefinition message) {
        MethodBody body = body("return parse(new ${ProtobufReader}(new ${ByteArrayInputStream}(data)));",
                param("ProtobufReader", ProtobufReader.class),
                param("ByteArrayInputStream", ByteArrayInputStream.class));

        messageRecord.addMethod()
                .setPublic()
                .setStatic(true)
                .setReturnType(message.name().canonicalName())
                .setName("parse")
                .setBody(body.toString())
                .addThrows(IOException.class)
                .addParameter(byte[].class, "data");
    }

    private void addParseStreamMethod(JavaRecordSource messageRecord, MessageDefinition message) {
        MethodBody body = body("return parse(new ${ProtobufReader}(stream));",
                param("ProtobufReader", ProtobufReader.class));

        messageRecord.addMethod()
                .setPublic()
                .setStatic(true)
                .setReturnType(message.name().canonicalName())
                .setName("parse")
                .setBody(body.toString())
                .addThrows(IOException.class)
                .addParameter(InputStream.class, "stream");
    }

    private void addParseProtobufInputMethod(JavaRecordSource messageRecord, MessageDefinition message) {
        MethodBody body = body("""
                ${BuilderType} builder = new ${BuilderType}();
                
                while (true) {
                    ${readTag}
                    ${readFields}
                    ${unknownFieldSupport}
                }
                
                return builder.build();
                """,
                param("BuilderType", message.builderName().canonicalName()),
                param("readTag", readTag()),
                param("readFields", readFields(message)),
                param("unknownFieldSupport", unknownFieldSupport()));

        messageRecord.addMethod()
                .setPrivate()
                .setStatic(true)
                .setReturnType(message.name().canonicalName())
                .setName("parse")
                .setBody(body.toString())
                .addThrows(IOException.class)
                .addParameter(ProtobufReader.class, "reader");
    }

    private MethodBody readTag() {
        return body("""
                ${Tag} tag = null;
                try {
                    tag = reader.tag();
                } catch (${EOFException} ex) {
                    break;
                }
                """,
                param("Tag", Tag.class),
                param("EOFException", EOFException.class));
    }

    private MethodBody readFields(MessageDefinition message) {
        MethodBody body = body();
        boolean first = true;

        for (FieldDefinition field : message.fields()) {
            if (!first) {
                body.append("else ");
            }
            body.append("""
                            if (tag.number() == ${fieldNumber}) {
                                builder.${fieldName}(reader.${readMethod}(tag, "${fieldName}"));
                            }
                            """,
                    param("fieldNumber", field.number()),
                    param("fieldName", field.name()),
                    param("readMethod", field.ioMethod()));
            first = false;
        }

        return body;
    }

    private MethodBody unknownFieldSupport() {
        return body("""
                        else {
                            reader.skip(tag);
                        }
                        """);
    }
}
