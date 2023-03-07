package com.github.pcimcioch.protobuf.source;

import com.github.pcimcioch.protobuf.code.CodeBody;
import com.github.pcimcioch.protobuf.code.ParameterSource;
import com.github.pcimcioch.protobuf.code.RecordSource;
import com.github.pcimcioch.protobuf.io.Size;
import com.github.pcimcioch.protobuf.model.field.FieldDefinition;
import com.github.pcimcioch.protobuf.model.message.MessageDefinition;

import static com.github.pcimcioch.protobuf.code.AnnotationSource.annotation;
import static com.github.pcimcioch.protobuf.code.CodeBody.body;
import static com.github.pcimcioch.protobuf.code.CodeBody.param;
import static com.github.pcimcioch.protobuf.code.MethodSource.method;
import static com.github.pcimcioch.protobuf.code.ParameterSource.parameter;
import static com.github.pcimcioch.protobuf.code.ReturnSource.returns;
import static com.github.pcimcioch.protobuf.code.VisibilitySource.publicVisibility;

class SizeFactory {

    void addSizeMethods(RecordSource messageRecord, MessageDefinition message) {
        CodeBody body = body();

        body.appendln("int totalSize = 0;");
        for (FieldDefinition field : message.fields()) {
            body.append("totalSize += size.$method($number, $name);",
                    param("method", sizeMethod(field)),
                    param("number", field.number()),
                    param("name", field.javaFieldName())
            );
        }
        body.append("return totalSize;");

        messageRecord.add(method("protobufSize")
                .set(publicVisibility())
                .set(returns(int.class))
                .set(body)
                .add(annotation(Override.class))
                .add(parameter(Size.class, "size"))
        );
    }

    private String sizeMethod(FieldDefinition field) {
        return switch (field.protoKind()) {
            case DOUBLE -> "double_";
            case FLOAT -> "float_";
            case INT32, ENUM -> "int32";
            case INT64 -> "int64";
            case UINT32 -> "uint32";
            case UINT64 -> "uint64";
            case SINT32 -> "sint32";
            case SINT64 -> "sint64";
            case FIXED32 -> "fixed32";
            case FIXED64 -> "fixed64";
            case SFIXED32 -> "sfixed32";
            case SFIXED64 -> "sfixed64";
            case BOOL -> "bool";
            case STRING -> "string";
            case MESSAGE -> "message";
            case BYTES -> "bytes";
        };
    }
}
