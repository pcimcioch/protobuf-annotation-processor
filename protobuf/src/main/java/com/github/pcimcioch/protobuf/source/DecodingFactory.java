package com.github.pcimcioch.protobuf.source;

import com.github.pcimcioch.protobuf.code.MethodBody;
import com.github.pcimcioch.protobuf.io.ProtobufReader;
import com.github.pcimcioch.protobuf.io.Tag;
import com.github.pcimcioch.protobuf.model.field.FieldDefinition;
import com.github.pcimcioch.protobuf.model.message.MessageDefinition;
import org.jboss.forge.roaster.model.source.JavaRecordSource;
import org.jboss.forge.roaster.model.source.MethodSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.github.pcimcioch.protobuf.code.MethodBody.body;
import static com.github.pcimcioch.protobuf.code.MethodBody.param;

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

        MethodSource<JavaRecordSource> method = messageRecord.addMethod()
                .setPublic()
                .setStatic(true)
                .setReturnType(message.name().canonicalName())
                .setName("parse")
                .addThrows(IOException.class)
                .setBody(body.toString());
        method.addParameter(byte[].class, "data");
    }

    private void addParseStreamMethod(JavaRecordSource messageRecord, MessageDefinition message) {
        MethodBody body = body("return parse(new $ProtobufReader(stream));",
                param("ProtobufReader", ProtobufReader.class));

        MethodSource<JavaRecordSource> method = messageRecord.addMethod()
                .setPublic()
                .setStatic(true)
                .setReturnType(message.name().canonicalName())
                .setName("parse")
                .addThrows(IOException.class)
                .setBody(body.toString());
        method.addParameter(InputStream.class, "stream");
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

        MethodSource<JavaRecordSource> method = messageRecord.addMethod()
                .setPrivate()
                .setStatic(true)
                .setReturnType(message.name().canonicalName())
                .setName("parse")
                .addThrows(IOException.class)
                .setBody(body.toString());
        method.addParameter(ProtobufReader.class, "reader");
    }

    private MethodBody readFields(MessageDefinition message) {
        MethodBody body = body();

        for (FieldDefinition field : message.fields()) {
            body
                    .appendExceptFirst("else ")
                    .append("if (tag.number() == $fieldNumber) {", param("fieldNumber", field.number()))
                    .append(decodingCode(field),
                            param("fieldName", field.javaFieldName()),
                            param("Type", field.javaFieldType())
                    )
                    .append("}");
        }

        return body.append("""
                else {
                    reader.skip(tag);
                }
                """
        );
    }

    private String decodingCode(FieldDefinition field) {
        return switch (field.protoType()) {
            case DOUBLE -> "builder.$fieldName(reader._double(tag, \"$fieldName\"));";
            case FLOAT -> "builder.$fieldName(reader._float(tag, \"$fieldName\"));";
            case INT32, ENUM -> "builder.$fieldName(reader.int32(tag, \"$fieldName\"));";
            case INT64 -> "builder.$fieldName(reader.int64(tag, \"$fieldName\"));";
            case UINT32 -> "builder.$fieldName(reader.uint32(tag, \"$fieldName\"));";
            case UINT64 -> "builder.$fieldName(reader.uint64(tag, \"$fieldName\"));";
            case SINT32 -> "builder.$fieldName(reader.sint32(tag, \"$fieldName\"));";
            case SINT64 -> "builder.$fieldName(reader.sint64(tag, \"$fieldName\"));";
            case FIXED32 -> "builder.$fieldName(reader.fixed32(tag, \"$fieldName\"));";
            case FIXED64 -> "builder.$fieldName(reader.fixed64(tag, \"$fieldName\"));";
            case SFIXED32 -> "builder.$fieldName(reader.sfixed32(tag, \"$fieldName\"));";
            case SFIXED64 -> "builder.$fieldName(reader.sfixed64(tag, \"$fieldName\"));";
            case BOOL -> "builder.$fieldName(reader.bool(tag, \"$fieldName\"));";
            case STRING -> "builder.$fieldName(reader.string(tag, \"$fieldName\"));";
            case BYTES -> "builder.$fieldName(reader.bytes(tag, \"$fieldName\"));";
            case MESSAGE -> "builder.$fieldName(reader.message(tag, \"$fieldName\", $Type::parse));";
        };
    }
}
