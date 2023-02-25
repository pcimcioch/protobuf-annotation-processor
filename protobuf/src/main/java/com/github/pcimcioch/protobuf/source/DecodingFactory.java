package com.github.pcimcioch.protobuf.source;

import com.github.pcimcioch.protobuf.code.CodeBody;
import com.github.pcimcioch.protobuf.code.RecordSource;
import com.github.pcimcioch.protobuf.io.ProtobufReader;
import com.github.pcimcioch.protobuf.model.field.FieldDefinition;
import com.github.pcimcioch.protobuf.model.message.MessageDefinition;

import java.io.IOException;
import java.io.InputStream;

import static com.github.pcimcioch.protobuf.code.CodeBody.body;
import static com.github.pcimcioch.protobuf.code.CodeBody.param;
import static com.github.pcimcioch.protobuf.code.MethodSource.method;
import static com.github.pcimcioch.protobuf.code.ParameterSource.parameter;
import static com.github.pcimcioch.protobuf.code.ReturnSource.returns;
import static com.github.pcimcioch.protobuf.code.StaticSource.staticModifier;
import static com.github.pcimcioch.protobuf.code.ThrowsSource.throwsEx;
import static com.github.pcimcioch.protobuf.code.VisibilitySource.privateVisibility;
import static com.github.pcimcioch.protobuf.code.VisibilitySource.publicVisibility;

class DecodingFactory {

    void addDecodingMethods(RecordSource messageRecord, MessageDefinition message) {
        addParseBytesMethod(messageRecord, message);
        addParseStreamMethod(messageRecord, message);
        addParseProtobufInputMethod(messageRecord, message);
    }

    private void addParseBytesMethod(RecordSource messageRecord, MessageDefinition message) {
        CodeBody body = body("return parse(new $ProtobufReader(data));",
                param("ProtobufReader", ProtobufReader.class)
        );

        messageRecord.add(method("parse")
                .set(publicVisibility())
                .set(staticModifier())
                .set(returns(message.name()))
                .add(throwsEx(IOException.class))
                .set(body)
                .add(parameter(byte[].class, "data"))
        );
    }

    private void addParseStreamMethod(RecordSource messageRecord, MessageDefinition message) {
        CodeBody body = body("return parse(new $ProtobufReader(stream));",
                param("ProtobufReader", ProtobufReader.class)
        );

        messageRecord.add(method("parse")
                .set(publicVisibility())
                .set(staticModifier())
                .set(returns(message.name()))
                .add(throwsEx(IOException.class))
                .set(body)
                .add(parameter(InputStream.class, "stream"))
        );
    }

    private void addParseProtobufInputMethod(RecordSource messageRecord, MessageDefinition message) {
        CodeBody body = body("""
                        $BuilderType builder = new $BuilderType();
                                        
                        int tag;
                        while ((tag = reader.tag()) != -1) {
                            $readFields
                        }
                                        
                        return builder.build();""",
                param("BuilderType", message.builderName()),
                param("readFields", readFields(message))
        );

        messageRecord.add(method("parse")
                .set(privateVisibility())
                .set(staticModifier())
                .set(returns(message.name()))
                .add(throwsEx(IOException.class))
                .set(body)
                .add(parameter(ProtobufReader.class, "reader"))
        );
    }

    private CodeBody readFields(MessageDefinition message) {
        CodeBody body = body("switch(tag) {");

        for (FieldDefinition field : message.fields()) {
            body
                    .append("case $fieldTag -> ", param("fieldTag", field.tag()))
                    .appendln(decodingCode(field));
        }

        return body.append("""
                    default -> reader.skip(tag);
                }
                """
        );
    }

    private CodeBody decodingCode(FieldDefinition field) {
        return switch (field.protoKind()) {
            case DOUBLE -> body("builder.$field(reader.double_());",
                    param("field", field.rules().repeated() ? field.javaFieldNamePrefixed("add") : field.javaFieldName())
            );
            case FLOAT -> body("builder.$field(reader.float_());",
                    param("field", field.rules().repeated() ? field.javaFieldNamePrefixed("add") : field.javaFieldName())
            );
            case INT32, ENUM -> body("builder.$field(reader.int32());",
                    param("field", field.rules().repeated() ? field.javaFieldNamePrefixed("add") : field.javaFieldName())
            );
            case INT64 -> body("builder.$field(reader.int64());",
                    param("field", field.rules().repeated() ? field.javaFieldNamePrefixed("add") : field.javaFieldName())
            );
            case UINT32 -> body("builder.$field(reader.uint32());",
                    param("field", field.rules().repeated() ? field.javaFieldNamePrefixed("add") : field.javaFieldName())
            );
            case UINT64 -> body("builder.$field(reader.uint64());",
                    param("field", field.rules().repeated() ? field.javaFieldNamePrefixed("add") : field.javaFieldName())
            );
            case SINT32 -> body("builder.$field(reader.sint32());",
                    param("field", field.rules().repeated() ? field.javaFieldNamePrefixed("add") : field.javaFieldName())
            );
            case SINT64 -> body("builder.$field(reader.sint64());",
                    param("field", field.rules().repeated() ? field.javaFieldNamePrefixed("add") : field.javaFieldName())
            );
            case FIXED32 -> body("builder.$field(reader.fixed32());",
                    param("field", field.rules().repeated() ? field.javaFieldNamePrefixed("add") : field.javaFieldName())
            );
            case FIXED64 -> body("builder.$field(reader.fixed64());",
                    param("field", field.rules().repeated() ? field.javaFieldNamePrefixed("add") : field.javaFieldName())
            );
            case SFIXED32 -> body("builder.$field(reader.sfixed32());",
                    param("field", field.rules().repeated() ? field.javaFieldNamePrefixed("add") : field.javaFieldName())
            );
            case SFIXED64 -> body("builder.$field(reader.sfixed64());",
                    param("field", field.rules().repeated() ? field.javaFieldNamePrefixed("add") : field.javaFieldName())
            );
            case BOOL -> body("builder.$field(reader.bool());",
                    param("field", field.rules().repeated() ? field.javaFieldNamePrefixed("add") : field.javaFieldName())
            );
            case STRING -> body("builder.$field(reader.string());",
                    param("field", field.rules().repeated() ? field.javaFieldNamePrefixed("add") : field.javaFieldName())
            );
            case BYTES -> body("builder.$field(reader.bytes());",
                    param("field", field.rules().repeated() ? field.javaFieldNamePrefixed("add") : field.javaFieldName())
            );
            case MESSAGE -> body("builder.$merge(reader.message($Type::parse));",
                    param("merge", field.rules().repeated() ? field.javaFieldNamePrefixed("add") : field.javaFieldNamePrefixed("merge")),
                    param("field", field.javaFieldName()),
                    param("Type", field.rules().repeated() ? field.javaFieldType().generic() : field.javaFieldType())
            );
        };
    }
}
