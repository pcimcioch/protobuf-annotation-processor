package com.github.pcimcioch.protobuf.source;

import com.github.pcimcioch.protobuf.code.CodeBody;
import com.github.pcimcioch.protobuf.code.RecordSource;
import com.github.pcimcioch.protobuf.io.Size;
import com.github.pcimcioch.protobuf.model.field.FieldDefinition;
import com.github.pcimcioch.protobuf.model.message.MessageDefinition;

import static com.github.pcimcioch.protobuf.code.AnnotationSource.annotation;
import static com.github.pcimcioch.protobuf.code.CodeBody.body;
import static com.github.pcimcioch.protobuf.code.CodeBody.param;
import static com.github.pcimcioch.protobuf.code.MethodSource.method;
import static com.github.pcimcioch.protobuf.code.ReturnSource.returns;
import static com.github.pcimcioch.protobuf.code.VisibilitySource.publicVisibility;

class SizeFactory {

    void addSizeMethods(RecordSource messageRecord, MessageDefinition message) {
        CodeBody body = body();

        body.appendln("int totalSize = 0;");
        for (FieldDefinition field : message.fields()) {
            body.appendln("totalSize += $Size.$method($number, $name);",
                    param("Size", Size.class),
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
        );
    }

    private String sizeMethod(FieldDefinition field) {
        String suffix = field.rules().repeated()
                ? field.rules().packed() ? "Packed" : "Unpacked"
                : "";
        String method = switch (field.protoKind()) {
            case DOUBLE -> "ofDouble";
            case FLOAT -> "ofFloat";
            case INT32 -> "ofInt32";
            case INT64 -> "ofInt64";
            case UINT32 -> "ofUint32";
            case UINT64 -> "ofUint64";
            case SINT32 -> "ofSint32";
            case SINT64 -> "ofSint64";
            case FIXED32 -> "ofFixed32";
            case FIXED64 -> "ofFixed64";
            case SFIXED32 -> "ofSfixed32";
            case SFIXED64 -> "ofSfixed64";
            case BOOL -> "ofBool";
            case STRING -> "ofString";
            case MESSAGE -> "ofMessage";
            case BYTES -> "ofBytes";
            case ENUM -> field.rules().repeated() ? "ofEnum" : "ofInt32";
        };

        return method + suffix;
    }
}
