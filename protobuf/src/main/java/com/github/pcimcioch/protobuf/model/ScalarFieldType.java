package com.github.pcimcioch.protobuf.model;

import com.github.pcimcioch.protobuf.code.MethodBody;
import com.github.pcimcioch.protobuf.code.TypeName;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;

import java.util.Optional;

import static com.github.pcimcioch.protobuf.code.MethodBody.body;
import static com.github.pcimcioch.protobuf.code.MethodBody.param;
import static com.github.pcimcioch.protobuf.code.TypeName.canonicalName;
import static com.github.pcimcioch.protobuf.code.TypeName.simpleName;

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
            "_double",
            false),

    /**
     * float
     */
    FLOAT("float",
            simpleName("float"),
            "0f",
            "_float",
            false),

    /**
     * int32
     */
    INT32("int32",
            simpleName("int"),
            "0",
            "int32",
            false),

    /**
     * int64
     */
    INT64("int64",
            simpleName("long"),
            "0L",
            "int64",
            false),

    /**
     * uint32
     */
    UINT32("uint32",
            simpleName("int"),
            "0",
            "uint32",
            false),

    /**
     * uint64
     */
    UINT64("uint64",
            simpleName("long"),
            "0L",
            "uint64",
            false),

    /**
     * sint32
     */
    SINT32("sint32",
            simpleName("int"),
            "0",
            "sint32",
            false),

    /**
     * sint64
     */
    SINT64("sint64",
            simpleName("long"),
            "0L",
            "sint64",
            false),

    /**
     * fixed32
     */
    FIXED32("fixed32",
            simpleName("int"),
            "0",
            "fixed32",
            false),

    /**
     * fixed64
     */
    FIXED64("fixed64",
            simpleName("long"),
            "0L",
            "fixed64",
            false),

    /**
     * sfixed32
     */
    SFIXED32("sfixed32",
            simpleName("int"),
            "0",
            "sfixed32",
            false),

    /**
     * sfixed64
     */
    SFIXED64("sfixed64",
            simpleName("long"),
            "0L",
            "sfixed64",
            false),

    /**
     * bool
     */
    BOOL("bool",
            simpleName("boolean"),
            "false",
            "bool",
            false),

    /**
     * string
     */
    STRING("string",
            simpleName("String"),
            "\"\"",
            "string",
            true),

    /**
     * bytes
     */
    BYTES("bytes",
            canonicalName("com.github.pcimcioch.protobuf.dto.ByteArray"),
            "com.github.pcimcioch.protobuf.dto.ByteArray.EMPTY",
            "bytes",
            true);

    private final String protoType;
    private final TypeName fieldJavaType;
    private final String defaultValue;
    private final String ioMethod;
    private final boolean requireNonNull;

    ScalarFieldType(String protoType, TypeName fieldJavaType, String defaultValue, String ioMethod, boolean requireNonNull) {
        this.protoType = protoType;
        this.fieldJavaType = fieldJavaType;
        this.defaultValue = defaultValue;
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
    public String ioMethod() {
        return ioMethod;
    }

    @Override
    public boolean requireNonNull() {
        return requireNonNull;
    }

    @Override
    public void addBuilderMethods(JavaClassSource builderClass, String field) {
        MethodBody body = body("""
                            this.$field = $field;
                            return this;""",
                param("field", field)
        );

        MethodSource<JavaClassSource> method = builderClass.addMethod()
                .setPublic()
                .setReturnType(builderClass)
                .setName(field)
                .setBody(body.toString());
        method.addParameter(fieldJavaType().canonicalName(), field);
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