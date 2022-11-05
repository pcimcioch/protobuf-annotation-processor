package com.github.pcimcioch.protobuf.model;

import java.util.Optional;

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
            new TypeName("double"),
            new TypeName("Double"),
            "0d",
            I64,
            "%s.writeDouble(%s)",
            "%s.readDouble()"),

    /**
     * float
     */
    FLOAT("float",
            new TypeName("float"),
            new TypeName("Float"),
            "0f",
            I32,
            "%s.writeFloat(%s)",
            "%s.readFloat()"),

    /**
     * int32
     */
    INT32("int32",
            new TypeName("int"),
            new TypeName("Integer"),
            "0",
            VARINT,
            "%s.writeVarint(%s)",
            "(int) %s.readVarint()"),

    /**
     * int64
     */
    INT64("int64",
            new TypeName("long"),
            new TypeName("Long"),
            "0L",
            VARINT,
            "%s.writeVarint(%s)",
            "%s.readVarint()"),

    /**
     * uint32
     */
    UINT32("uint32",
            new TypeName("int"),
            new TypeName("Integer"),
            "0",
            VARINT,
            "%s.writeVarint(%s)",
            "(int) %s.readVarint()"),

    /**
     * uint64
     */
    UINT64("uint64",
            new TypeName("long"),
            new TypeName("Long"),
            "0L",
            VARINT,
            "%s.writeVarint(%s)",
            "%s.readVarint()"),

    /**
     * sint32
     */
    SINT32("sint32",
            new TypeName("int"),
            new TypeName("Integer"),
            "0",
            VARINT,
            "%s.writeZigZag(%s)",
            "(int) %s.readZigZag()"),

    /**
     * sint64
     */
    SINT64("sint64",
            new TypeName("long"),
            new TypeName("Long"),
            "0L",
            VARINT,
            "%s.writeZigZag(%s)",
            "%s.readZigZag()"),

    /**
     * fixed32
     */
    FIXED32("fixed32",
            new TypeName("int"),
            new TypeName("Integer"),
            "0",
            I32,
            "%s.writeFixedInt(%s)",
            "%s.readFixedInt()"),

    /**
     * fixed64
     */
    FIXED64("fixed64",
            new TypeName("long"),
            new TypeName("Long"),
            "0L",
            I64,
            "%s.writeFixedLong(%s)",
            "%s.readFixedLong()"),

    /**
     * sfixed32
     */
    SFIXED32("sfixed32",
            new TypeName("int"),
            new TypeName("Integer"),
            "0",
            I32,
            "%s.writeFixedInt(%s)",
            "%s.readFixedInt()"),

    /**
     * sfixed64
     */
    SFIXED64("sfixed64",
            new TypeName("long"),
            new TypeName("Long"),
            "0L",
            I64,
            "%s.writeFixedLong(%s)",
            "%s.readFixedLong()"),

    /**
     * bool
     */
    BOOL("bool",
            new TypeName("boolean"),
            new TypeName("Boolean"),
            "false",
            VARINT,
            "%s.writeBoolean(%s)",
            "%s.readBoolean()"),

    /**
     * string
     */
    STRING("string",
            new TypeName("String"),
            new TypeName("String"),
            "\"\"",
            LEN,
            "%s.writeString(%s)",
            "%s.readString()"),

    /**
     * bytes
     */
    BYTES("bytes",
            new TypeName("com.github.pcimcioch.protobuf.dto.ByteArray"),
            new TypeName("com.github.pcimcioch.protobuf.dto.ByteArray"),
            "com.github.pcimcioch.protobuf.dto.ByteArray.EMPTY",
            LEN,
            "%s.writeBytes(%s.data())",
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
    public String writeMethod(String outputName, String parameterName) {
        return String.format(writeMethodTemplate, outputName, parameterName);
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
