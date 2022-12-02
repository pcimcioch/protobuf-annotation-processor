package com.github.pcimcioch.protobuf.model;

import java.util.Optional;

import static com.github.pcimcioch.protobuf.model.TypeName.canonicalName;
import static com.github.pcimcioch.protobuf.model.TypeName.simpleName;

/**
 * Types of scalar fields available in protobuf specification
 */
public enum ScalarFieldType implements FieldType {
    /**
     * double
     */
    DOUBLE("double",
            simpleName("double"),
            "0d",
            "== 0d",
            "_double",
            false),

    /**
     * float
     */
    FLOAT("float",
            simpleName("float"),
            "0f",
            "== 0f",
            "_float",
            false),

    /**
     * int32
     */
    INT32("int32",
            simpleName("int"),
            "0",
            "== 0",
            "int32",
            false),

    /**
     * int64
     */
    INT64("int64",
            simpleName("long"),
            "0L",
            "== 0L",
            "int64",
            false),

    /**
     * uint32
     */
    UINT32("uint32",
            simpleName("int"),
            "0",
            "== 0",
            "uint32",
            false),

    /**
     * uint64
     */
    UINT64("uint64",
            simpleName("long"),
            "0L",
            "== 0L",
            "uint64",
            false),

    /**
     * sint32
     */
    SINT32("sint32",
            simpleName("int"),
            "0",
            "== 0",
            "sint32",
            false),

    /**
     * sint64
     */
    SINT64("sint64",
            simpleName("long"),
            "0L",
            "== 0L",
            "sint64",
            false),

    /**
     * fixed32
     */
    FIXED32("fixed32",
            simpleName("int"),
            "0",
            "== 0",
            "fixed32",
            false),

    /**
     * fixed64
     */
    FIXED64("fixed64",
            simpleName("long"),
            "0L",
            "== 0L",
            "fixed64",
            false),

    /**
     * sfixed32
     */
    SFIXED32("sfixed32",
            simpleName("int"),
            "0",
            "== 0",
            "sfixed32",
            false),

    /**
     * sfixed64
     */
    SFIXED64("sfixed64",
            simpleName("long"),
            "0L",
            "== 0L",
            "sfixed64",
            false),

    /**
     * bool
     */
    BOOL("bool",
            simpleName("boolean"),
            "false",
            "== false",
            "bool",
            false),

    /**
     * string
     */
    STRING("string",
            simpleName("String"),
            "\"\"",
            ".equals(\"\")",
            "string",
            true),

    /**
     * bytes
     */
    BYTES("bytes",
            canonicalName("com.github.pcimcioch.protobuf.dto.ByteArray"),
            "com.github.pcimcioch.protobuf.dto.ByteArray.EMPTY",
            ".isEmpty()",
            "bytes",
            true);

    private final String protoType;
    private final TypeName fieldJavaType;
    private final String defaultValue;
    private final String defaultCheck;
    private final String ioMethod;
    private final boolean requireNonNull;

    ScalarFieldType(String protoType, TypeName fieldJavaType, String defaultValue, String defaultCheck, String ioMethod,
                    boolean requireNonNull) {
        this.protoType = protoType;
        this.fieldJavaType = fieldJavaType;
        this.defaultValue = defaultValue;
        this.defaultCheck = defaultCheck;
        this.ioMethod = ioMethod;
        this.requireNonNull = requireNonNull;
    }

    @Override
    public TypeName fieldJavaType() {
        return fieldJavaType;
    }

    @Override
    public String defaultValue() {
        return defaultValue;
    }

    @Override
    public String defaultCheck() {
        return defaultCheck;
    }

    @Override
    public String ioMethod() {
        return ioMethod;
    }

    @Override
    public boolean requireNonNull() {
        return requireNonNull;
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