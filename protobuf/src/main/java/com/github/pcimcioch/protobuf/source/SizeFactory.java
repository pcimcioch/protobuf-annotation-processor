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
            body.appendln("totalSize += $method;",
                    param("method", sizeMethod(field))
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

    private CodeBody sizeMethod(FieldDefinition field) {
        String suffix = field.rules().repeated()
                ? field.rules().packed() ? "Packed" : "Unpacked"
                : "";
        String method = switch (field.protoKind()) {
            case DOUBLE -> "$Size.ofDouble$suffix($number, $name)";
            case FLOAT -> "$Size.ofFloat$suffix($number, $name)";
            case INT32 -> "$Size.ofInt32$suffix($number, $name)";
            case INT64 -> "$Size.ofInt64$suffix($number, $name)";
            case UINT32 -> "$Size.ofUint32$suffix($number, $name)";
            case UINT64 -> "$Size.ofUint64$suffix($number, $name)";
            case SINT32 -> "$Size.ofSint32$suffix($number, $name)";
            case SINT64 -> "$Size.ofSint64$suffix($number, $name)";
            case FIXED32 -> "$Size.ofFixed32$suffix($number, $name)";
            case FIXED64 -> "$Size.ofFixed64$suffix($number, $name)";
            case SFIXED32 -> "$Size.ofSfixed32$suffix($number, $name)";
            case SFIXED64 -> "$Size.ofSfixed64$suffix($number, $name)";
            case BOOL -> "$Size.ofBool$suffix($number, $name)";
            case STRING -> "$Size.ofString$suffix($number, $name)";
            case MESSAGE -> "$Size.ofMessage$suffix($number, $name)";
            case BYTES -> "$Size.ofBytes$suffix($number, $name)";
            case UNKNOWN -> "$Size.ofUnknownFields$suffix($name)";
            case ENUM -> field.rules().repeated()
                    ? "$Size.ofEnum$suffix($number, $name)"
                    : "$Size.ofInt32$suffix($number, $name)";
        };

        return body(method,
                param("Size", Size.class),
                param("number", field.number()),
                param("name", field.javaFieldName()),
                param("suffix", suffix)
        );
    }
}
