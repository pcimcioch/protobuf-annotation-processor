package com.github.pcimcioch.protobuf.source;

import com.github.pcimcioch.protobuf.code.CodeBody;
import com.github.pcimcioch.protobuf.code.RecordSource;
import com.github.pcimcioch.protobuf.io.ProtobufReader;
import com.github.pcimcioch.protobuf.model.field.FieldDefinition;
import com.github.pcimcioch.protobuf.model.message.MessageDefinition;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static com.github.pcimcioch.protobuf.code.CodeBody.body;
import static com.github.pcimcioch.protobuf.code.CodeBody.param;
import static com.github.pcimcioch.protobuf.code.MethodSource.method;
import static com.github.pcimcioch.protobuf.code.ParameterSource.parameter;
import static com.github.pcimcioch.protobuf.code.ReturnSource.returns;
import static com.github.pcimcioch.protobuf.code.StaticSource.staticModifier;
import static com.github.pcimcioch.protobuf.code.ThrowsSource.throwsEx;
import static com.github.pcimcioch.protobuf.code.VisibilitySource.publicVisibility;
import static com.github.pcimcioch.protobuf.io.WireType.I32;
import static com.github.pcimcioch.protobuf.io.WireType.I64;
import static com.github.pcimcioch.protobuf.io.WireType.LEN;
import static com.github.pcimcioch.protobuf.io.WireType.VARINT;
import static com.github.pcimcioch.protobuf.model.field.FieldDefinition.ProtoKind.UNKNOWN;

class DecodingFactory {

    void addDecodingMethods(RecordSource messageRecord, MessageDefinition message) {
        addParseBytesMethod(messageRecord, message);
        addParseStreamMethod(messageRecord, message);
        addParseProtobufReaderMethod(messageRecord, message);
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

    private void addParseProtobufReaderMethod(RecordSource messageRecord, MessageDefinition message) {
        CodeBody body = body("""
                        $BuilderType builder = new $BuilderType();
                                        
                        int tag;
                        while ((tag = reader.readTag()) != -1) {
                            $readFields
                        }
                                        
                        return builder.build();""",
                param("BuilderType", message.builderName()),
                param("readFields", readFields(message))
        );

        messageRecord.add(method("parse")
                .set(publicVisibility())
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
            body.appendln(decodingCode(field));
        }

        body.appendln(defaultCode(message));

        return body.append("}");
    }

    private CodeBody decodingCode(FieldDefinition field) {
        return field.rules().repeated() ? decodingCodeRepeated(field) : decodingCodeSimple(field);
    }

    private CodeBody decodingCodeSimple(FieldDefinition field) {
        return switch (field.protoKind()) {
            case DOUBLE -> body("case $fieldTag -> builder.$field(reader.readDouble());",
                    param("fieldTag", I64.tagFrom(field.number())),
                    param("field", field.javaFieldName())
            );
            case FLOAT -> body("case $fieldTag -> builder.$field(reader.readFloat());",
                    param("fieldTag", I32.tagFrom(field.number())),
                    param("field", field.javaFieldName())
            );
            case INT32, ENUM -> body("case $fieldTag -> builder.$field(reader.readInt32());",
                    param("fieldTag", VARINT.tagFrom(field.number())),
                    param("field", field.javaFieldName())
            );
            case INT64 -> body("case $fieldTag -> builder.$field(reader.readInt64());",
                    param("fieldTag", VARINT.tagFrom(field.number())),
                    param("field", field.javaFieldName())
            );
            case UINT32 -> body("case $fieldTag -> builder.$field(reader.readUint32());",
                    param("fieldTag", VARINT.tagFrom(field.number())),
                    param("field", field.javaFieldName())
            );
            case UINT64 -> body("case $fieldTag -> builder.$field(reader.readUint64());",
                    param("fieldTag", VARINT.tagFrom(field.number())),
                    param("field", field.javaFieldName())
            );
            case SINT32 -> body("case $fieldTag -> builder.$field(reader.readSint32());",
                    param("fieldTag", VARINT.tagFrom(field.number())),
                    param("field", field.javaFieldName())
            );
            case SINT64 -> body("case $fieldTag -> builder.$field(reader.readSint64());",
                    param("fieldTag", VARINT.tagFrom(field.number())),
                    param("field", field.javaFieldName())
            );
            case FIXED32 -> body("case $fieldTag -> builder.$field(reader.readFixed32());",
                    param("fieldTag", I32.tagFrom(field.number())),
                    param("field", field.javaFieldName())
            );
            case FIXED64 -> body("case $fieldTag -> builder.$field(reader.readFixed64());",
                    param("fieldTag", I64.tagFrom(field.number())),
                    param("field", field.javaFieldName())
            );
            case SFIXED32 -> body("case $fieldTag -> builder.$field(reader.readSfixed32());",
                    param("fieldTag", I32.tagFrom(field.number())),
                    param("field", field.javaFieldName())
            );
            case SFIXED64 -> body("case $fieldTag -> builder.$field(reader.readSfixed64());",
                    param("fieldTag", I64.tagFrom(field.number())),
                    param("field", field.javaFieldName())
            );
            case BOOL -> body("case $fieldTag -> builder.$field(reader.readBool());",
                    param("fieldTag", VARINT.tagFrom(field.number())),
                    param("field", field.javaFieldName())
            );
            case STRING -> body("case $fieldTag -> builder.$field(reader.readString());",
                    param("fieldTag", LEN.tagFrom(field.number())),
                    param("field", field.javaFieldName())
            );
            case BYTES -> body("case $fieldTag -> builder.$field(reader.readBytes());",
                    param("fieldTag", LEN.tagFrom(field.number())),
                    param("field", field.javaFieldName())
            );
            case MESSAGE -> body("case $fieldTag -> builder.$merge(reader.readMessage($Type::parse));",
                    param("fieldTag", LEN.tagFrom(field.number())),
                    param("merge", field.javaFieldNamePrefixed("merge")),
                    param("Type", field.javaFieldType())
            );
            case UNKNOWN -> body();
        };
    }

    private CodeBody decodingCodeRepeated(FieldDefinition field) {
        return switch (field.protoKind()) {
            case DOUBLE -> body("""
                            case $fieldTag -> builder.$field(reader.readDouble());
                            case $packedFieldTag -> reader.readDoublePacked(builder::$field);""",
                    param("fieldTag", I64.tagFrom(field.number())),
                    param("packedFieldTag", LEN.tagFrom(field.number())),
                    param("field", field.javaFieldNamePrefixed("add"))
            );
            case FLOAT -> body("""
                            case $fieldTag -> builder.$field(reader.readFloat());
                            case $packedFieldTag -> reader.readFloatPacked(builder::$field);""",
                    param("fieldTag", I32.tagFrom(field.number())),
                    param("packedFieldTag", LEN.tagFrom(field.number())),
                    param("field", field.javaFieldNamePrefixed("add"))
            );
            case INT32 -> body("""
                            case $fieldTag -> builder.$field(reader.readInt32());
                            case $packedFieldTag -> reader.readInt32Packed(builder::$field);""",
                    param("fieldTag", VARINT.tagFrom(field.number())),
                    param("packedFieldTag", LEN.tagFrom(field.number())),
                    param("field", field.javaFieldNamePrefixed("add"))
            );
            case INT64 -> body("""
                            case $fieldTag -> builder.$field(reader.readInt64());
                            case $packedFieldTag -> reader.readInt64Packed(builder::$field);""",
                    param("fieldTag", VARINT.tagFrom(field.number())),
                    param("packedFieldTag", LEN.tagFrom(field.number())),
                    param("field", field.javaFieldNamePrefixed("add"))
            );
            case UINT32 -> body("""
                            case $fieldTag -> builder.$field(reader.readUint32());
                            case $packedFieldTag -> reader.readUint32Packed(builder::$field);""",
                    param("fieldTag", VARINT.tagFrom(field.number())),
                    param("packedFieldTag", LEN.tagFrom(field.number())),
                    param("field", field.javaFieldNamePrefixed("add"))
            );
            case UINT64 -> body("""
                            case $fieldTag -> builder.$field(reader.readUint64());
                            case $packedFieldTag -> reader.readUint64Packed(builder::$field);""",
                    param("fieldTag", VARINT.tagFrom(field.number())),
                    param("packedFieldTag", LEN.tagFrom(field.number())),
                    param("field", field.javaFieldNamePrefixed("add"))
            );
            case SINT32 -> body("""
                            case $fieldTag -> builder.$field(reader.readSint32());
                            case $packedFieldTag -> reader.readSint32Packed(builder::$field);""",
                    param("fieldTag", VARINT.tagFrom(field.number())),
                    param("packedFieldTag", LEN.tagFrom(field.number())),
                    param("field", field.javaFieldNamePrefixed("add"))
            );
            case SINT64 -> body("""
                            case $fieldTag -> builder.$field(reader.readSint64());
                            case $packedFieldTag -> reader.readSint64Packed(builder::$field);""",
                    param("fieldTag", VARINT.tagFrom(field.number())),
                    param("packedFieldTag", LEN.tagFrom(field.number())),
                    param("field", field.javaFieldNamePrefixed("add"))
            );
            case FIXED32 -> body("""
                            case $fieldTag -> builder.$field(reader.readFixed32());
                            case $packedFieldTag -> reader.readFixed32Packed(builder::$field);""",
                    param("fieldTag", I32.tagFrom(field.number())),
                    param("packedFieldTag", LEN.tagFrom(field.number())),
                    param("field", field.javaFieldNamePrefixed("add"))
            );
            case FIXED64 -> body("""
                            case $fieldTag -> builder.$field(reader.readFixed64());
                            case $packedFieldTag -> reader.readFixed64Packed(builder::$field);""",
                    param("fieldTag", I64.tagFrom(field.number())),
                    param("packedFieldTag", LEN.tagFrom(field.number())),
                    param("field", field.javaFieldNamePrefixed("add"))
            );
            case SFIXED32 -> body("""
                            case $fieldTag -> builder.$field(reader.readSfixed32());
                            case $packedFieldTag -> reader.readSfixed32Packed(builder::$field);""",
                    param("fieldTag", I32.tagFrom(field.number())),
                    param("packedFieldTag", LEN.tagFrom(field.number())),
                    param("field", field.javaFieldNamePrefixed("add"))
            );
            case SFIXED64 -> body("""
                            case $fieldTag -> builder.$field(reader.readSfixed64());
                            case $packedFieldTag -> reader.readSfixed64Packed(builder::$field);""",
                    param("fieldTag", I64.tagFrom(field.number())),
                    param("packedFieldTag", LEN.tagFrom(field.number())),
                    param("field", field.javaFieldNamePrefixed("add"))
            );
            case BOOL -> body("""
                            case $fieldTag -> builder.$field(reader.readBool());
                            case $packedFieldTag -> reader.readBoolPacked(builder::$field);""",
                    param("fieldTag", VARINT.tagFrom(field.number())),
                    param("packedFieldTag", LEN.tagFrom(field.number())),
                    param("field", field.javaFieldNamePrefixed("add"))
            );
            case STRING -> body("case $fieldTag -> builder.$field(reader.readString());",
                    param("fieldTag", LEN.tagFrom(field.number())),
                    param("field", field.javaFieldNamePrefixed("add"))
            );
            case BYTES -> body("case $fieldTag -> builder.$field(reader.readBytes());",
                    param("fieldTag", LEN.tagFrom(field.number())),
                    param("field", field.javaFieldNamePrefixed("add"))
            );
            case MESSAGE -> body("case $fieldTag -> builder.$field(reader.readMessage($Type::parse));",
                    param("fieldTag", LEN.tagFrom(field.number())),
                    param("field", field.javaFieldNamePrefixed("add")),
                    param("Type", field.javaFieldType().generic())
            );
            case ENUM -> body("""
                            case $fieldTag -> builder.$field(reader.readInt32());
                            case $packedFieldTag -> reader.readInt32Packed(builder::$field);""",
                    param("fieldTag", VARINT.tagFrom(field.number())),
                    param("packedFieldTag", LEN.tagFrom(field.number())),
                    param("field", field.javaFieldNamePrefixed("add") + "Value")
            );
            case UNKNOWN -> body();
        };
    }

    private CodeBody defaultCode(MessageDefinition message) {
        Optional<FieldDefinition> defaultField = message.fields().stream()
                .filter(f -> f.protoKind() == UNKNOWN)
                .findFirst();

        return defaultField
                .map(field -> body("default -> builder.$field(reader.readUnknownField(tag));",
                        param("field", field.javaFieldNamePrefixed("add"))))
                .orElse(body("default -> reader.skip(tag);"));
    }
}
