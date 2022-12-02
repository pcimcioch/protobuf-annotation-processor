package com.github.pcimcioch.protobuf.model;

import java.util.Optional;

import static com.github.pcimcioch.protobuf.model.TypeName.canonicalName;
import static com.github.pcimcioch.protobuf.model.TypeName.simpleName;
import static com.github.pcimcioch.protobuf.model.WireType.I32;
import static com.github.pcimcioch.protobuf.model.WireType.I64;
import static com.github.pcimcioch.protobuf.model.WireType.LEN;
import static com.github.pcimcioch.protobuf.model.WireType.VARINT;

/**
 * Types of scalar fields available in protobuf specification
 */
public enum ScalarFieldType implements FieldType {
    /**
     * double
     */
    DOUBLE("double",
            simpleName("double"),
            simpleName("Double"),
            "0d",
            I64,
            "%s._double(%d, %s)",
            "%s.readDouble()"),

    /**
     * float
     */
    FLOAT("float",
            simpleName("float"),
            simpleName("Float"),
            "0f",
            I32,
            "%s._float(%d, %s)",
            "%s.readFloat()"),

    /**
     * int32
     */
    INT32("int32",
            simpleName("int"),
            simpleName("Integer"),
            "0",
            VARINT,
            "%s.int32(%d, %s)",
            "(int) %s.readVarint()"),

    /**
     * int64
     */
    INT64("int64",
            simpleName("long"),
            simpleName("Long"),
            "0L",
            VARINT,
            "%s.int64(%d, %s)",
            "%s.readVarint()"),

    /**
     * uint32
     */
    UINT32("uint32",
            simpleName("int"),
            simpleName("Integer"),
            "0",
            VARINT,
            "%s.uint32(%d, %s)",
            "(int) %s.readVarint()"),

    /**
     * uint64
     */
    UINT64("uint64",
            simpleName("long"),
            simpleName("Long"),
            "0L",
            VARINT,
            "%s.uint64(%d, %s)",
            "%s.readVarint()"),

    /**
     * sint32
     */
    SINT32("sint32",
            simpleName("int"),
            simpleName("Integer"),
            "0",
            VARINT,
            "%s.sint32(%d, %s)",
            "(int) %s.readZigZag()"),

    /**
     * sint64
     */
    SINT64("sint64",
            simpleName("long"),
            simpleName("Long"),
            "0L",
            VARINT,
            "%s.sint64(%d, %s)",
            "%s.readZigZag()"),

    /**
     * fixed32
     */
    FIXED32("fixed32",
            simpleName("int"),
            simpleName("Integer"),
            "0",
            I32,
            "%s.fixed32(%d, %s)",
            "%s.readFixedInt()"),

    /**
     * fixed64
     */
    FIXED64("fixed64",
            simpleName("long"),
            simpleName("Long"),
            "0L",
            I64,
            "%s.fixed64(%d, %s)",
            "%s.readFixedLong()"),

    /**
     * sfixed32
     */
    SFIXED32("sfixed32",
            simpleName("int"),
            simpleName("Integer"),
            "0",
            I32,
            "%s.sfixed32(%d, %s)",
            "%s.readFixedInt()"),

    /**
     * sfixed64
     */
    SFIXED64("sfixed64",
            simpleName("long"),
            simpleName("Long"),
            "0L",
            I64,
            "%s.sfixed64(%d, %s)",
            "%s.readFixedLong()"),

    /**
     * bool
     */
    BOOL("bool",
            simpleName("boolean"),
            simpleName("Boolean"),
            "false",
            VARINT,
            "%s.bool(%d, %s)",
            "%s.readBoolean()"),

    /**
     * string
     */
    STRING("string",
            simpleName("String"),
            simpleName("String"),
            "\"\"",
            LEN,
            "%s.string(%d, %s)",
            "%s.readString()"),

    /**
     * bytes
     */
    BYTES("bytes",
            canonicalName("com.github.pcimcioch.protobuf.dto.ByteArray"),
            canonicalName("com.github.pcimcioch.protobuf.dto.ByteArray"),
            "com.github.pcimcioch.protobuf.dto.ByteArray.EMPTY",
            LEN,
            "%s.bytes(%d, %s)",
            "new com.github.pcimcioch.protobuf.dto.ByteArray(%s.readBytes())");

    private final String protoType;
    private final TypeName fieldJavaType;
    private final TypeName wrapperJavaType;
    private final String defaultValue;
    private final WireType wireType;
    private final String writeMethodTemplate;
    private final String readMethodTemplate;

    ScalarFieldType(String protoType, TypeName fieldJavaType, TypeName wrapperJavaType, String defaultValue, WireType wireType, String writeMethodTemplate, String readMethodTemplate) {
        this.protoType = protoType;
        this.fieldJavaType = fieldJavaType;
        this.wrapperJavaType = wrapperJavaType;
        this.defaultValue = defaultValue;
        this.wireType = wireType;
        this.writeMethodTemplate = writeMethodTemplate;
        this.readMethodTemplate = readMethodTemplate;
    }

    @Override
    public TypeName fieldJavaType() {
        return fieldJavaType;
    }

    @Override
    public TypeName wrapperJavaType() {
        return wrapperJavaType;
    }

    @Override
    public String defaultValue() {
        return defaultValue;
    }

    @Override
    public WireType wireType() {
        return wireType;
    }

    @Override
    public String writeMethod(String outputName, int number, String parameterName) {
        return String.format(writeMethodTemplate, outputName, number, parameterName);
    }

    @Override
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
