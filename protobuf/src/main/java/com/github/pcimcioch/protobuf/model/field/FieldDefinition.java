package com.github.pcimcioch.protobuf.model.field;

import com.github.pcimcioch.protobuf.code.TypeName;
import com.github.pcimcioch.protobuf.dto.BooleanList;
import com.github.pcimcioch.protobuf.dto.ByteArray;
import com.github.pcimcioch.protobuf.dto.DoubleList;
import com.github.pcimcioch.protobuf.dto.EnumList;
import com.github.pcimcioch.protobuf.dto.FloatList;
import com.github.pcimcioch.protobuf.dto.IntList;
import com.github.pcimcioch.protobuf.dto.LongList;
import com.github.pcimcioch.protobuf.dto.ObjectList;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.github.pcimcioch.protobuf.code.TypeName.canonicalName;
import static com.github.pcimcioch.protobuf.code.TypeName.simpleName;
import static com.github.pcimcioch.protobuf.model.field.FieldDefinition.ProtoKind.BYTES;
import static com.github.pcimcioch.protobuf.model.field.FieldDefinition.ProtoKind.ENUM;
import static com.github.pcimcioch.protobuf.model.field.FieldDefinition.ProtoKind.MESSAGE;
import static com.github.pcimcioch.protobuf.model.field.FieldDefinition.ProtoKind.STRING;
import static com.github.pcimcioch.protobuf.model.validation.Assertions.assertFalse;
import static com.github.pcimcioch.protobuf.model.validation.Assertions.assertNonNull;
import static com.github.pcimcioch.protobuf.model.validation.Assertions.assertTrue;
import static java.util.Locale.ENGLISH;

/**
 * Message field
 */
public final class FieldDefinition {

    private final String name;
    private final int number;
    private final TypeName protobufType;
    private final FieldRules rules;
    private final ProtoKind protoKind;

    private FieldDefinition(String name, int number, ProtoKind protoKind, FieldRules rules, TypeName protobufType) {
        this.name = Valid.name(name);
        this.number = Valid.number(number);
        this.protoKind = Valid.protoType(protoKind);
        this.rules = Valid.rules(protoKind, rules);
        this.protobufType = Valid.protobufType(protoKind, protobufType);
    }

    /**
     * Returns protobuf field name
     *
     * @return protobuf field name
     */
    public String name() {
        return name;
    }

    /**
     * Returns number of the field
     *
     * @return number of the field
     */
    public int number() {
        return number;
    }

    /**
     * Returns protobuf type of the field
     *
     * @return protobuf type of the field
     */
    public TypeName protobufType() {
        return protobufType;
    }

    /**
     * Returns protobuf kind of the field
     *
     * @return protobuf kind of the field
     */
    public ProtoKind protoKind() {
        return protoKind;
    }

    /**
     * return field java type
     *
     * @return field java type
     */
    public TypeName javaFieldType() {
        return switch (protoKind) {
            case DOUBLE -> rules.repeated() ? canonicalName(DoubleList.class) : simpleName("double");
            case FLOAT -> rules.repeated() ? canonicalName(FloatList.class) : simpleName("float");
            case INT32, UINT32, SINT32, FIXED32, SFIXED32 ->
                    rules.repeated() ? canonicalName(IntList.class) : simpleName("int");
            case INT64, UINT64, SINT64, FIXED64, SFIXED64 ->
                    rules.repeated() ? canonicalName(LongList.class) : simpleName("long");
            case BOOL -> rules.repeated() ? canonicalName(BooleanList.class) : simpleName("boolean");
            case STRING ->
                    rules.repeated() ? canonicalName(ObjectList.class).of(simpleName("String")) : simpleName("String");
            case BYTES ->
                    rules.repeated() ? canonicalName(ObjectList.class).of(canonicalName(ByteArray.class)) : canonicalName(ByteArray.class);
            case MESSAGE -> rules.repeated() ? canonicalName(ObjectList.class).of(protobufType) : protobufType;
            case ENUM -> rules.repeated() ? canonicalName(EnumList.class).of(protobufType) : simpleName("int");
        };
    }

    /**
     * Returns field name in java code
     *
     * @return field name
     */
    public String javaFieldName() {
        return (protoKind == ENUM && !rules().repeated())
                ? name + "Value"
                : name;
    }

    /**
     * Returns field rules
     *
     * @return field rules
     */
    public FieldRules rules() {
        return rules;
    }

    /**
     * Returns field name in java code prefixed in camelCase
     *
     * @param prefix prefix to use
     * @return prefixed java field name
     */
    public String javaFieldNamePrefixed(String prefix) {
        return prefix + javaFieldName().substring(0, 1).toUpperCase(ENGLISH) + javaFieldName().substring(1);
    }

    /**
     * Returns field name in java code prefixed in camelCase
     *
     * @param prefix prefix to use
     * @return prefixed field name
     */
    public String namePrefixed(String prefix) {
        return prefix + name().substring(0, 1).toUpperCase(ENGLISH) + name().substring(1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldDefinition that = (FieldDefinition) o;
        return number == that.number && name.equals(that.name) && rules.equals(that.rules) && protoKind == that.protoKind;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, number, rules, protoKind);
    }

    /**
     * Creates new scalar field
     *
     * @param name      name
     * @param number    number
     * @param protoType protobuf type name
     * @param rules     field rules
     * @return scalar field
     */
    public static FieldDefinition scalar(String name, int number, String protoType, FieldRules rules) {
        ProtoKind protoKind = ProtoKind.fromScalar(protoType).orElseThrow(() -> new IllegalArgumentException("Incorrect protobuf scalar type: " + protoType));
        return new FieldDefinition(name, number, protoKind, rules, null);
    }

    /**
     * Returns whether given protobuf type is scalar
     *
     * @param protoType protobuf type
     * @return whether type is scalar
     */
    public static boolean isScalar(String protoType) {
        return ProtoKind.fromScalar(protoType).isPresent();
    }

    /**
     * Creates new enumeration field
     *
     * @param name   name
     * @param number number
     * @param type   type
     * @param rules  field rules
     * @return new field
     */
    public static FieldDefinition enumeration(String name, int number, TypeName type, FieldRules rules) {
        return new FieldDefinition(name, number, ENUM, rules, type);
    }

    /**
     * Creates new message field
     *
     * @param name   name
     * @param number number
     * @param type   type
     * @param rules  field rules
     * @return new field
     */
    public static FieldDefinition message(String name, int number, TypeName type, FieldRules rules) {
        return new FieldDefinition(name, number, MESSAGE, rules, type);
    }

    /**
     * Protobuf field type
     */
    public enum ProtoKind {
        /**
         * double
         */
        DOUBLE,

        /**
         * float
         */
        FLOAT,

        /**
         * int32
         */
        INT32,

        /**
         * int64
         */
        INT64,

        /**
         * uint32
         */
        UINT32,

        /**
         * uint64
         */
        UINT64,

        /**
         * sint32
         */
        SINT32,

        /**
         * sint64
         */
        SINT64,

        /**
         * fixed32
         */
        FIXED32,

        /**
         * fixed64
         */
        FIXED64,

        /**
         * sfixed32
         */
        SFIXED32,

        /**
         * sfixed64
         */
        SFIXED64,

        /**
         * bool
         */
        BOOL,

        /**
         * string
         */
        STRING,

        /**
         * bytes
         */
        BYTES,

        /**
         * Enumeration
         */
        ENUM,

        /**
         * Other message
         */
        MESSAGE;

        /**
         * Returns enum representing this protobuf scalar type
         *
         * @param protoType name of protobuf scalar type
         * @return optional enum
         */
        public static Optional<ProtoKind> fromScalar(String protoType) {
            return Optional.ofNullable(switch (protoType) {
                case "double" -> DOUBLE;
                case "float" -> FLOAT;
                case "int32" -> INT32;
                case "int64" -> INT64;
                case "uint32" -> UINT32;
                case "uint64" -> UINT64;
                case "sint32" -> SINT32;
                case "sint64" -> SINT64;
                case "fixed32" -> FIXED32;
                case "fixed64" -> FIXED64;
                case "sfixed32" -> SFIXED32;
                case "sfixed64" -> SFIXED64;
                case "bool" -> BOOL;
                case "string" -> STRING;
                case "bytes" -> BYTES;
                default -> null;
            });
        }
    }

    private static final class Valid {
        private static final Pattern namePattern = Pattern.compile("^[a-zA-z_][a-zA-Z0-9_]*$");

        private static int number(int number) {
            assertTrue(number > 0, "Number must be positive, but was: " + number);
            return number;
        }

        private static String name(String name) {
            assertNonNull(name, "Incorrect name: <null>");
            assertTrue(namePattern.matcher(name).matches(), "Incorrect name: " + name);
            return name;
        }

        private static ProtoKind protoType(ProtoKind type) {
            assertNonNull(type, "Must provide proto type");
            return type;
        }

        private static FieldRules rules(ProtoKind kind, FieldRules rules) {
            assertNonNull(rules, "Must provide rules");
            assertFalse(rules.repeated() && rules.packed() && (kind == STRING || kind == MESSAGE || kind == BYTES), "Only primitive types can be packed");

            return rules;
        }

        private static TypeName protobufType(ProtoKind kind, TypeName protobufType) {
            if (kind == ENUM || kind == MESSAGE) {
                assertNonNull(protobufType, "Must provide protobuf type");
            }

            return protobufType;
        }
    }
}
