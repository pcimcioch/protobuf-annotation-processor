package com.github.pcimcioch.protobuf.model;

import java.util.Optional;

import static com.github.pcimcioch.protobuf.model.WireType.I32;
import static com.github.pcimcioch.protobuf.model.WireType.I64;
import static com.github.pcimcioch.protobuf.model.WireType.LEN;
import static com.github.pcimcioch.protobuf.model.WireType.VARINT;

/**
 * Types of scalar fields available in protobuf specyfication
 */
public enum ScalarFieldType {
    /**
     * double
     */
    DOUBLE("double", "double", "0d", I64, "%s.writeDouble(%s)", "%s.readDouble()"),

    /**
     * float
     */
    FLOAT("float", "float", "0f", I32, "%s.writeFloat(%s)", "%s.readFloat()"),

    /**
     * int32
     */
    INT32("int32", "int", "0", VARINT, "%s.writeVarint(%s)", "(int) %s.readVarint()"),

    /**
     * int64
     */
    INT64("int64", "long", "0L", VARINT, "%s.writeVarint(%s)", "%s.readVarint()"),

    /**
     * uint32
     */
    UINT32("uint32", "int", "0", VARINT, "%s.writeVarint(%s)", "(int) %s.readVarint()"),

    /**
     * uint64
     */
    UINT64("uint64", "long", "0L", VARINT, "%s.writeVarint(%s)", "%s.readVarint()"),

    /**
     * sint32
     */
    SINT32("sint32", "int", "0", VARINT, "%s.writeZigZag(%s)", "(int) %s.readZigZag()"),

    /**
     * sint64
     */
    SINT64("sint64", "long", "0L", VARINT, "%s.writeZigZag(%s)", "%s.readZigZag()"),

    /**
     * fixed32
     */
    FIXED32("fixed32", "int", "0", I32, "%s.writeFixedInt(%s)", "%s.readFixedInt()"),

    /**
     * fixed64
     */
    FIXED64("fixed64", "long", "0L", I64, "%s.writeFixedLong(%s)", "%s.readFixedLong()"),

    /**
     * sfixed32
     */
    SFIXED32("sfixed32", "int", "0", I32, "%s.writeFixedInt(%s)", "%s.readFixedInt()"),

    /**
     * sfixed64
     */
    SFIXED64("sfixed64", "long", "0L", I64, "%s.writeFixedLong(%s)", "%s.readFixedLong()"),

    /**
     * bool
     */
    BOOL("bool", "boolean", "false", VARINT, "%s.writeBoolean(%s)", "%s.readBoolean()"),

    /**
     * string
     */
    STRING("string", "String", "\"\"", LEN, "%s.writeString(%s)", "%s.readString()"),

    /**
     * bytes
     */
    BYTES("bytes", "byte[]", "new byte[0]", LEN, "%s.writeBytes(%s)", "%s.readBytes()");

    private final String protoType;
    private final String fieldJavaType;
    private final String defaultValue;
    private final WireType wireType;
    private final String writeMethodTemplate;
    private final String readMethodTemplate;

    ScalarFieldType(String protoType, String fieldJavaType, String defaultValue, WireType wireType, String writeMethodTemplate, String readMethodTemplate) {
        this.protoType = protoType;
        this.fieldJavaType = fieldJavaType;
        this.defaultValue = defaultValue;
        this.wireType = wireType;
        this.writeMethodTemplate = writeMethodTemplate;
        this.readMethodTemplate = readMethodTemplate;
    }

    /**
     * Returns java type of the field
     *
     * @return java type
     */
    public String fieldJavaType() {
        return fieldJavaType;
    }

    /**
     * Returns default java value for this type
     *
     * @return default value
     */
    public String defaultValue() {
        return defaultValue;
    }

    /**
     * Returns wire type for this type
     *
     * @return wire type
     */
    public WireType wireType() {
        return wireType;
    }

    /**
     * Returns java code to write this field to given output
     *
     * @param outputName    output name
     * @param parameterName name of the parameter storing this scalar type
     * @return java code
     */
    public String writeMethod(String outputName, String parameterName) {
        return String.format(writeMethodTemplate, outputName, parameterName);
    }

    /**
     * Returns java code to read this field from given input
     *
     * @param inputName input name
     * @return java code
     */
    public String readMethod(String inputName) {
        return String.format(readMethodTemplate, inputName);
    }

    /**
     * Creates enum value from name of the scalar type
     *
     * @param protoType protobuf type name
     * @return enum type
     */
    public static Optional<ScalarFieldType> fromProtoType(String protoType) {
        for (ScalarFieldType value : values()) {
            if (value.protoType.equals(protoType)) {
                return Optional.of(value);
            }
        }

        return Optional.empty();
    }
}
