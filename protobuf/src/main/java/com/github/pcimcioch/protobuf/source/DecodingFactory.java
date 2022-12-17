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
                    .append(decodingCode(field))
                    .append("}");
        }

        return body.append("""
                else {
                    reader.skip(tag);
                }
                """
        );
    }

    private MethodBody decodingCode(FieldDefinition field) {
        return switch (field.protoKind()) {
            case DOUBLE -> body("builder.$field(reader._double(tag, \"$field\"));",
                    param("field", field.javaFieldName())
            );
            case FLOAT -> body("builder.$field(reader._float(tag, \"$field\"));",
                    param("field", field.javaFieldName())
            );
            case INT32, ENUM -> body("builder.$field(reader.int32(tag, \"$field\"));",
                    param("field", field.javaFieldName())
            );
            case INT64 -> body("builder.$field(reader.int64(tag, \"$field\"));",
                    param("field", field.javaFieldName())
            );
            case UINT32 -> body("builder.$field(reader.uint32(tag, \"$field\"));",
                    param("field", field.javaFieldName())
            );
            case UINT64 -> body("builder.$field(reader.uint64(tag, \"$field\"));",
                    param("field", field.javaFieldName())
            );
            case SINT32 -> body("builder.$field(reader.sint32(tag, \"$field\"));",
                    param("field", field.javaFieldName())
            );
            case SINT64 -> body("builder.$field(reader.sint64(tag, \"$field\"));",
                    param("field", field.javaFieldName())
            );
            case FIXED32 -> body("builder.$field(reader.fixed32(tag, \"$field\"));",
                    param("field", field.javaFieldName())
            );
            case FIXED64 -> body("builder.$field(reader.fixed64(tag, \"$field\"));",
                    param("field", field.javaFieldName())
            );
            case SFIXED32 -> body("builder.$field(reader.sfixed32(tag, \"$field\"));",
                    param("field", field.javaFieldName())
            );
            case SFIXED64 -> body("builder.$field(reader.sfixed64(tag, \"$field\"));",
                    param("field", field.javaFieldName())
            );
            case BOOL -> body("builder.$field(reader.bool(tag, \"$field\"));",
                    param("field", field.javaFieldName())
            );
            case STRING -> body("builder.$field(reader.string(tag, \"$field\"));",
                    param("field", field.javaFieldName())
            );
            case BYTES -> body("builder.$field(reader.bytes(tag, \"$field\"));",
                    param("field", field.javaFieldName())
            );
            case MESSAGE -> body("builder.$merge(reader.message(tag, \"$field\", $Type::parse));",
                    param("merge", field.javaFieldNamePrefixed("merge")),
                    param("field", field.javaFieldName()),
                    param("Type", field.javaFieldType())
            );
        };
    }
}
