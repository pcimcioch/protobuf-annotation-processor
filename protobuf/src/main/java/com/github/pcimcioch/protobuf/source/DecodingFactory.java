package com.github.pcimcioch.protobuf.source;

import com.github.pcimcioch.protobuf.io.ProtobufReader;
import com.github.pcimcioch.protobuf.io.Tag;
import com.github.pcimcioch.protobuf.model.FieldDefinition;
import com.github.pcimcioch.protobuf.model.MessageDefinition;
import org.jboss.forge.roaster.model.source.JavaRecordSource;

import java.io.ByteArrayInputStream;
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
        MethodBody body = body("return parse(new $ProtobufReader(new $ByteArrayInputStream(data)));",
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
        MethodBody body = body("return parse(new $ProtobufReader(stream));",
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
                        $BuilderType builder = new $BuilderType();
                                        
                        $Tag tag = null;
                        while ((tag = reader.tag()) != null) {
                            $readFields
                        }
                                        
                        return builder.build();
                        """,
                param("BuilderType", message.builderName()),
                param("Tag", Tag.class),
                param("readFields", readFields(message)));

        System.out.println(body.toString());

        messageRecord.addMethod()
                .setPrivate()
                .setStatic(true)
                .setReturnType(message.name().canonicalName())
                .setName("parse")
                .setBody(body.toString())
                .addThrows(IOException.class)
                .addParameter(ProtobufReader.class, "reader");
    }

    private MethodBody readFields(MessageDefinition message) {
        MethodBody body = body();
        boolean first = true;

        for (FieldDefinition field : message.fields()) {
            if (!first) {
                body.append("else ");
            }
            body.append("""
                            if (tag.number() == $fieldNumber) {
                                builder.$fieldName(reader.$readMethod(tag, "$fieldName"));
                            }
                            """,
                    param("fieldNumber", field.number()),
                    param("fieldName", field.name()),
                    param("readMethod", field.ioMethod()));
            first = false;
        }

        body.append("""
                else {
                    reader.skip(tag);
                }
                """);

        return body;
    }
}
