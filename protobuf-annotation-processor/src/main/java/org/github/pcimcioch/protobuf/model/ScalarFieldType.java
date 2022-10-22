package org.github.pcimcioch.protobuf.model;

import java.util.Optional;

import static org.github.pcimcioch.protobuf.model.WireType.I32;
import static org.github.pcimcioch.protobuf.model.WireType.I64;
import static org.github.pcimcioch.protobuf.model.WireType.LEN;
import static org.github.pcimcioch.protobuf.model.WireType.VARINT;

public enum ScalarFieldType {
    DOUBLE("double", "double", "0d", I64, "%s.writeDouble(%s)", "%s.readDouble()"),
    FLOAT("float", "float", "0f", I32, "%s.writeFloat(%s)", "%s.readFloat()"),
    INT32("int32", "int", "0", VARINT, "%s.writeVarint(%s)", "(int) %s.readVarint()"),
    INT64("int64", "long", "0L", VARINT, "%s.writeVarint(%s)", "%s.readVarint()"),
    UINT32("uint32", "int", "0", VARINT, "%s.writeVarint(%s)", "(int) %s.readVarint()"),
    UINT64("uint64", "long", "0L", VARINT, "%s.writeVarint(%s)", "%s.readVarint()"),
    SINT32("sint32", "int", "0", VARINT, "%s.writeZigZag(%s)", "(int) %s.readZigZag()"),
    SINT64("sint64", "long", "0L", VARINT, "%s.writeZigZag(%s)", "%s.readZigZag()"),
    FIXED32("fixed32", "int", "0", I32, "%s.writeFixedInt(%s)", "%s.readFixedInt()"),
    FIXED64("fixed64", "long", "0L", I64, "%s.writeFixedLong(%s)", "%s.readFixedLong()"),
    SFIXED32("sfixed32", "int", "0", I32, "%s.writeFixedInt(%s)", "%s.readFixedInt()"),
    SFIXED64("sfixed64", "long", "0L", I64, "%s.writeFixedLong(%s)", "%s.readFixedLong()"),
    BOOL("bool", "boolean", "false", VARINT, "%s.writeBoolean(%s)", "%s.readBoolean()"),
    STRING("string", "String", "\"\"", LEN, "%s.writeString(%s)", "%s.readString()"),
    BYTES("bytes", "byte[]", "new byte[0]", LEN, "%s.writeBytes(%s)", "%s.readBytes()");

    private final String protoType;
    private final String fieldClass;
    private final String defaultValue;
    private final WireType wireType;
    private final String writeMethodTemplate;
    private final String readMethodTemplate;

    ScalarFieldType(String protoType, String fieldClass, String defaultValue, WireType wireType, String writeMethodTemplate, String readMethodTemplate) {
        this.protoType = protoType;
        this.fieldClass = fieldClass;
        this.defaultValue = defaultValue;
        this.wireType = wireType;
        this.writeMethodTemplate = writeMethodTemplate;
        this.readMethodTemplate = readMethodTemplate;
    }

    public String fieldClass() {
        return fieldClass;
    }

    public String defaultValue() {
        return defaultValue;
    }

    public WireType wireType() {
        return wireType;
    }

    public String writeMethod(String outputName, String parameterName) {
        return String.format(writeMethodTemplate, outputName, parameterName);
    }

    public String readMethod(String inputName) {
        return String.format(readMethodTemplate, inputName);
    }

    public static Optional<ScalarFieldType> fromProtoType(String protoType) {
        for (ScalarFieldType value : values()) {
            if (value.protoType.equals(protoType)) {
                return Optional.of(value);
            }
        }

        return Optional.empty();
    }
}
