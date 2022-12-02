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
            "_double"),

    /**
     * float
     */
    FLOAT("float",
            simpleName("float"),
            simpleName("Float"),
            "0f",
            I32,
            "_float"),

    /**
     * int32
     */
    INT32("int32",
            simpleName("int"),
            simpleName("Integer"),
            "0",
            VARINT,
            "int32"),

    /**
     * int64
     */
    INT64("int64",
            simpleName("long"),
            simpleName("Long"),
            "0L",
            VARINT,
            "int64"),

    /**
     * uint32
     */
    UINT32("uint32",
            simpleName("int"),
            simpleName("Integer"),
            "0",
            VARINT,
            "uint32"),

    /**
     * uint64
     */
    UINT64("uint64",
            simpleName("long"),
            simpleName("Long"),
            "0L",
            VARINT,
            "uint64"),

    /**
     * sint32
     */
    SINT32("sint32",
            simpleName("int"),
            simpleName("Integer"),
            "0",
            VARINT,
            "sint32"),

    /**
     * sint64
     */
    SINT64("sint64",
            simpleName("long"),
            simpleName("Long"),
            "0L",
            VARINT,
            "sint64"),

    /**
     * fixed32
     */
    FIXED32("fixed32",
            simpleName("int"),
            simpleName("Integer"),
            "0",
            I32,
            "fixed32"),

    /**
     * fixed64
     */
    FIXED64("fixed64",
            simpleName("long"),
            simpleName("Long"),
            "0L",
            I64,
            "fixed64"),

    /**
     * sfixed32
     */
    SFIXED32("sfixed32",
            simpleName("int"),
            simpleName("Integer"),
            "0",
            I32,
            "sfixed32"),

    /**
     * sfixed64
     */
    SFIXED64("sfixed64",
            simpleName("long"),
            simpleName("Long"),
            "0L",
            I64,
            "sfixed64"),

    /**
     * bool
     */
    BOOL("bool",
            simpleName("boolean"),
            simpleName("Boolean"),
            "false",
            VARINT,
            "bool"),

    /**
     * string
     */
    STRING("string",
            simpleName("String"),
            simpleName("String"),
            "\"\"",
            LEN,
            "string"),

    /**
     * bytes
     */
    BYTES("bytes",
            canonicalName("com.github.pcimcioch.protobuf.dto.ByteArray"),
            canonicalName("com.github.pcimcioch.protobuf.dto.ByteArray"),
            "com.github.pcimcioch.protobuf.dto.ByteArray.EMPTY",
            LEN,
            "bytes");

    private final String protoType;
    private final TypeName fieldJavaType;
    private final TypeName wrapperJavaType;
    private final String defaultValue;
    private final WireType wireType;
    private final String ioMethod;

    ScalarFieldType(String protoType, TypeName fieldJavaType, TypeName wrapperJavaType, String defaultValue, WireType wireType, String ioMethod) {
        this.protoType = protoType;
        this.fieldJavaType = fieldJavaType;
        this.wrapperJavaType = wrapperJavaType;
        this.defaultValue = defaultValue;
        this.wireType = wireType;
        this.ioMethod = ioMethod;
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
    public String ioMethod() {
        return ioMethod;
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
