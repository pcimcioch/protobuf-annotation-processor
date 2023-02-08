package com.github.pcimcioch.protobuf.model.field;

import com.github.pcimcioch.protobuf.code.TypeName;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.github.pcimcioch.protobuf.code.TypeName.canonicalName;
import static com.github.pcimcioch.protobuf.code.TypeName.simpleName;
import static com.github.pcimcioch.protobuf.model.field.FieldDefinition.ProtoKind.BOOL;
import static com.github.pcimcioch.protobuf.model.field.FieldDefinition.ProtoKind.BYTES;
import static com.github.pcimcioch.protobuf.model.field.FieldDefinition.ProtoKind.DOUBLE;
import static com.github.pcimcioch.protobuf.model.field.FieldDefinition.ProtoKind.ENUM;
import static com.github.pcimcioch.protobuf.model.field.FieldDefinition.ProtoKind.FIXED32;
import static com.github.pcimcioch.protobuf.model.field.FieldDefinition.ProtoKind.FIXED64;
import static com.github.pcimcioch.protobuf.model.field.FieldDefinition.ProtoKind.FLOAT;
import static com.github.pcimcioch.protobuf.model.field.FieldDefinition.ProtoKind.INT32;
import static com.github.pcimcioch.protobuf.model.field.FieldDefinition.ProtoKind.INT64;
import static com.github.pcimcioch.protobuf.model.field.FieldDefinition.ProtoKind.MESSAGE;
import static com.github.pcimcioch.protobuf.model.field.FieldDefinition.ProtoKind.SFIXED32;
import static com.github.pcimcioch.protobuf.model.field.FieldDefinition.ProtoKind.SFIXED64;
import static com.github.pcimcioch.protobuf.model.field.FieldDefinition.ProtoKind.SINT32;
import static com.github.pcimcioch.protobuf.model.field.FieldDefinition.ProtoKind.SINT64;
import static com.github.pcimcioch.protobuf.model.field.FieldDefinition.ProtoKind.STRING;
import static com.github.pcimcioch.protobuf.model.field.FieldDefinition.ProtoKind.UINT32;
import static com.github.pcimcioch.protobuf.model.field.FieldDefinition.ProtoKind.UINT64;
import static com.github.pcimcioch.protobuf.model.validation.Assertions.assertNonNull;
import static com.github.pcimcioch.protobuf.model.validation.Assertions.assertTrue;
import static java.util.Locale.ENGLISH;

/**
 * Message field
 */
public final class FieldDefinition {

    private final String name;
    private final String fieldName;
    private final int number;
    private final TypeName type;
    private final TypeName javaType;
    private final FieldRules rules;
    private final ProtoKind protoKind;

    private FieldDefinition(String name, String fieldName, int number, TypeName type, TypeName javaType, ProtoKind protoKind, FieldRules rules) {
        this.name = Valid.name(name);
        this.fieldName = Valid.fieldName(fieldName);
        this.number = Valid.number(number);
        this.type = Valid.type(type);
        this.javaType = Valid.javaType(javaType);
        this.protoKind = Valid.protoType(protoKind);
        this.rules = Valid.rules(rules);
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
    public TypeName type() {
        return type;
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
        return javaType;
    }

    /**
     * Returns field name in java code
     *
     * @return field name
     */
    public String javaFieldName() {
        return fieldName;
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
        return number == that.number && name.equals(that.name) && fieldName.equals(that.fieldName) && type.equals(that.type)
                && javaType.equals(that.javaType) && rules.equals(that.rules) && protoKind == that.protoKind;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, fieldName, number, type, javaType, rules, protoKind);
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
        Optional<ScalarDefinition> definition = rules.repeated()
                ? ScalarDefinition.fromRepeatable(protoType)
                : ScalarDefinition.from(protoType);
        return definition
                .map(def -> new FieldDefinition(name, name, number, def.type, def.javaType, def.protoKind, rules))
                .orElseThrow(() -> new IllegalArgumentException("Incorrect protobuf scalar type: " + protoType));
    }

    private record ScalarDefinition(TypeName type, TypeName javaType, ProtoKind protoKind) {
        private static Optional<ScalarDefinition> from(String protoType) {
            return Optional.ofNullable(switch (protoType) {
                case "double" -> new ScalarDefinition(simpleName("double"), simpleName("double"), DOUBLE);
                case "float" -> new ScalarDefinition(simpleName("float"), simpleName("float"), FLOAT);
                case "int32" -> new ScalarDefinition(simpleName("int"), simpleName("int"), INT32);
                case "int64" -> new ScalarDefinition(simpleName("long"), simpleName("long"), INT64);
                case "uint32" -> new ScalarDefinition(simpleName("int"), simpleName("int"), UINT32);
                case "uint64" -> new ScalarDefinition(simpleName("long"), simpleName("long"), UINT64);
                case "sint32" -> new ScalarDefinition(simpleName("int"), simpleName("int"), SINT32);
                case "sint64" -> new ScalarDefinition(simpleName("long"), simpleName("long"), SINT64);
                case "fixed32" -> new ScalarDefinition(simpleName("int"), simpleName("int"), FIXED32);
                case "fixed64" -> new ScalarDefinition(simpleName("long"), simpleName("long"), FIXED64);
                case "sfixed32" -> new ScalarDefinition(simpleName("int"), simpleName("int"), SFIXED32);
                case "sfixed64" -> new ScalarDefinition(simpleName("long"), simpleName("long"), SFIXED64);
                case "bool" -> new ScalarDefinition(simpleName("boolean"), simpleName("boolean"), BOOL);
                case "string" -> new ScalarDefinition(simpleName("String"), simpleName("String"), STRING);
                case "bytes" ->
                        new ScalarDefinition(canonicalName("com.github.pcimcioch.protobuf.dto.ByteArray"), canonicalName("com.github.pcimcioch.protobuf.dto.ByteArray"), BYTES);
                default -> null;
            });
        }

        private static Optional<ScalarDefinition> fromRepeatable(String protoType) {
            return Optional.ofNullable(switch (protoType) {
                case "double" ->
                        new ScalarDefinition(simpleName("Double").inList(), simpleName("Double").inList(), DOUBLE);
                case "float" -> new ScalarDefinition(simpleName("Float").inList(), simpleName("Float").inList(), FLOAT);
                case "int32" ->
                        new ScalarDefinition(simpleName("Integer").inList(), simpleName("Integer").inList(), INT32);
                case "int64" -> new ScalarDefinition(simpleName("Long").inList(), simpleName("Long").inList(), INT64);
                case "uint32" ->
                        new ScalarDefinition(simpleName("Integer").inList(), simpleName("Integer").inList(), UINT32);
                case "uint64" -> new ScalarDefinition(simpleName("Long").inList(), simpleName("Long").inList(), UINT64);
                case "sint32" ->
                        new ScalarDefinition(simpleName("Integer").inList(), simpleName("Integer").inList(), SINT32);
                case "sint64" -> new ScalarDefinition(simpleName("Long").inList(), simpleName("Long").inList(), SINT64);
                case "fixed32" ->
                        new ScalarDefinition(simpleName("Integer").inList(), simpleName("Integer").inList(), FIXED32);
                case "fixed64" ->
                        new ScalarDefinition(simpleName("Long").inList(), simpleName("Long").inList(), FIXED64);
                case "sfixed32" ->
                        new ScalarDefinition(simpleName("Integer").inList(), simpleName("Integer").inList(), SFIXED32);
                case "sfixed64" ->
                        new ScalarDefinition(simpleName("Long").inList(), simpleName("Long").inList(), SFIXED64);
                case "bool" ->
                        new ScalarDefinition(simpleName("Boolean").inList(), simpleName("Boolean").inList(), BOOL);
                case "string" ->
                        new ScalarDefinition(simpleName("String").inList(), simpleName("String").inList(), STRING);
                case "bytes" ->
                        new ScalarDefinition(canonicalName("com.github.pcimcioch.protobuf.dto.ByteArray").inList(), canonicalName("com.github.pcimcioch.protobuf.dto.ByteArray").inList(), BYTES);
                default -> null;
            });
        }
    }

    /**
     * Returns whether given protobuf type is scalar
     *
     * @param protoType protobuf type
     * @return whether type is scalar
     */
    public static boolean isScalar(String protoType) {
        return ScalarDefinition.from(protoType).isPresent();
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
        return rules.repeated()
                ? new FieldDefinition(name, name + "Value", number, type.inList(), simpleName("Integer").inList(), ENUM, rules)
                : new FieldDefinition(name, name + "Value", number, type, simpleName("int"), ENUM, rules);
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
        return rules.repeated()
                ? new FieldDefinition(name, name, number, type.inList(), type.inList(), MESSAGE, rules)
                : new FieldDefinition(name, name, number, type, type, MESSAGE, rules);
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
        MESSAGE
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

        private static String fieldName(String name) {
            assertNonNull(name, "Incorrect field name: <null>");
            assertTrue(namePattern.matcher(name).matches(), "Incorrect field name: " + name);
            return name;
        }

        private static TypeName type(TypeName type) {
            assertNonNull(type, "Must provide field type");
            return type;
        }

        private static TypeName javaType(TypeName type) {
            assertNonNull(type, "Must provide field java type");
            return type;
        }

        private static ProtoKind protoType(ProtoKind type) {
            assertNonNull(type, "Must provide proto type");
            return type;
        }

        // TODO add tests
        private static FieldRules rules(FieldRules rules) {
            assertNonNull(rules, "Must provide rules");
            return rules;
        }
    }
}
