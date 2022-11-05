package com.github.pcimcioch.protobuf.source;

import com.github.pcimcioch.protobuf.io.ProtobufInput;
import com.github.pcimcioch.protobuf.model.MessageDefinition;
import com.github.pcimcioch.protobuf.exception.ProtobufParseException;
import com.github.pcimcioch.protobuf.model.FieldDefinition;
import org.jboss.forge.roaster.model.source.JavaRecordSource;

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
        MethodBody body = body("return parse(new ${ProtobufInput}(new java.io.ByteArrayInputStream(data)));",
                param("ProtobufInput", ProtobufInput.class));

        messageRecord.addMethod()
                .setPublic()
                .setStatic(true)
                .setReturnType(message.messageTypeSimpleName())
                .setName("parse")
                .setBody(body.toString())
                .addThrows(IOException.class)
                .addParameter(byte[].class, "data");
    }

    private void addParseStreamMethod(JavaRecordSource messageRecord, MessageDefinition message) {
        MethodBody body = body("return parse(new ${ProtobufInput}(stream));",
                param("ProtobufInput", ProtobufInput.class));

        messageRecord.addMethod()
                .setPublic()
                .setStatic(true)
                .setReturnType(message.messageTypeSimpleName())
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
                param("BuilderType", message.builderSimpleName()),
                param("readTag", readTag()),
                param("readFields", readFields(message)),
                param("unknownFieldSupport", unknownFieldSupport()));

        messageRecord.addMethod()
                .setPrivate()
                .setStatic(true)
                .setReturnType(message.messageTypeSimpleName())
                .setName("parse")
                .setBody(body.toString())
                .addThrows(IOException.class)
                .addParameter(ProtobufInput.class, "input");
    }

    private MethodBody readTag() {
        return body("""
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

    private MethodBody readFields(MessageDefinition message) {
        MethodBody body = body();
        boolean first = true;

        for (FieldDefinition field : message.fields()) {
            if (!first) {
                body.append("else ");
            }
            body.append("""
                            if (number == ${fieldNumber}) {
                                if (wireType != ${fieldWireType}) {
                                    throw new ${ProtobufParseException}("Field [name=${fieldName}, number=${fieldNumber}] incorrect wire type. Expected ${fieldWireType}, got " + wireType);
                                }
                                builder.${fieldName}(${inputRead});
                            }
                            """,
                    param("fieldNumber", field.number()),
                    param("fieldWireType", field.wireType().id()),
                    param("ProtobufParseException", ProtobufParseException.class),
                    param("fieldName", field.name()),
                    param("inputRead", field.protobufReadMethod("input")));
        }

        return body;
    }

    private MethodBody unknownFieldSupport() {
        return body("""
                        else {
                            switch(wireType) {
                                case 0:
                                    input.readVarint();
                                    break;
                                case 1:
                                    input.skip(8);
                                    break;
                                case 2:
                                    input.skip(input.readVarint());
                                    break;
                                case 3: throw new ${ProtobufParseException}("Wire Type SGROUP is not supported");
                                case 4: throw new ${ProtobufParseException}("Wire Type EGROUP is not supported");
                                case 5:
                                    input.skip(4);
                                    break;
                                default: throw new ${ProtobufParseException}("Unknown wire type " + wireType);
                            }
                        }
                        """,
                param("ProtobufParseException", ProtobufParseException.class));
    }
}
