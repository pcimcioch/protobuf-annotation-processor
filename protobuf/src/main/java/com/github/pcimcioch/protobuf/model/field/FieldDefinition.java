package com.github.pcimcioch.protobuf.model.field;

import com.github.pcimcioch.protobuf.model.type.TypeName;
import org.jboss.forge.roaster.model.source.AnnotationTargetSource;

import java.util.Set;
import java.util.regex.Pattern;

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
import static com.github.pcimcioch.protobuf.model.type.TypeName.canonicalName;
import static com.github.pcimcioch.protobuf.model.type.TypeName.simpleName;
import static com.github.pcimcioch.protobuf.model.validation.Assertions.assertNonNull;
import static com.github.pcimcioch.protobuf.model.validation.Assertions.assertTrue;
import static java.util.Locale.ENGLISH;

/**
 * Message field
 */
public class FieldDefinition {

    private static final TypeName ENUM_VALUE_TYPE = simpleName("int");
    private static final Set<String> SCALAR_TYPES = Set.of("double", "float", "int32", "int64", "uint32", "uint64", "sint32", "sint64", "fixed32", "fixed64", "sfixed32", "sfixed64", "bool", "string", "bytes");

    private final String name;
    private final int number;
    private final TypeName type;
    private final boolean deprecated;
    private final ProtoKind protoKind;

    private FieldDefinition(String name, int number, TypeName type, ProtoKind protoKind, boolean deprecated) {
        this.name = Valid.name(name);
        this.number = Valid.number(number);
        this.type = Valid.type(type);
        this.protoKind = Valid.protoType(protoKind);
        this.deprecated = deprecated;
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
        if (protoKind == ENUM) {
            return ENUM_VALUE_TYPE;
        } else {
            return type;
        }
    }

    /**
     * Returns field name in java code
     *
     * @return field name
     */
    public String javaFieldName() {
        if (protoKind == ENUM) {
            return name + "Value";
        } else {
            return name;
        }
    }

    /**
     * Apply any deprecated annotations if this field is deprecated
     *
     * @param source source code
     */
    public void handleDeprecated(AnnotationTargetSource<?, ?> source) {
        if (deprecated) {
            source.addAnnotation(Deprecated.class);
        }
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
     * Creates new scalar field
     *
     * @param name       name
     * @param number     number
     * @param protoType  protobuf type name
     * @param deprecated deprecated
     * @return scalar field
     */
    public static FieldDefinition scalar(String name, int number, String protoType, boolean deprecated) {
        return switch (protoType) {
            case "double" -> new FieldDefinition(name, number, simpleName("double"), DOUBLE, deprecated);
            case "float" -> new FieldDefinition(name, number, simpleName("float"), FLOAT, deprecated);
            case "int32" -> new FieldDefinition(name, number, simpleName("int"), INT32, deprecated);
            case "int64" -> new FieldDefinition(name, number, simpleName("long"), INT64, deprecated);
            case "uint32" -> new FieldDefinition(name, number, simpleName("int"), UINT32, deprecated);
            case "uint64" -> new FieldDefinition(name, number, simpleName("long"), UINT64, deprecated);
            case "sint32" -> new FieldDefinition(name, number, simpleName("int"), SINT32, deprecated);
            case "sint64" -> new FieldDefinition(name, number, simpleName("long"), SINT64, deprecated);
            case "fixed32" -> new FieldDefinition(name, number, simpleName("int"), FIXED32, deprecated);
            case "fixed64" -> new FieldDefinition(name, number, simpleName("long"), FIXED64, deprecated);
            case "sfixed32" -> new FieldDefinition(name, number, simpleName("int"), SFIXED32, deprecated);
            case "sfixed64" -> new FieldDefinition(name, number, simpleName("long"), SFIXED64, deprecated);
            case "bool" -> new FieldDefinition(name, number, simpleName("boolean"), BOOL, deprecated);
            case "string" -> new FieldDefinition(name, number, simpleName("String"), STRING, deprecated);
            case "bytes" ->
                    new FieldDefinition(name, number, canonicalName("com.github.pcimcioch.protobuf.dto.ByteArray"), BYTES, deprecated);
            default -> throw new IllegalArgumentException("Incorrect protobuf scalar type: " + protoType);
        };
    }

    /**
     * Returns whether given protobuf type is scalar
     *
     * @param protoType protobuf type
     * @return whether type is scalar
     */
    public static boolean isScalar(String protoType) {
        return SCALAR_TYPES.contains(protoType);
    }

    /**
     * Creates new enumeration field
     *
     * @param name       name
     * @param number     number
     * @param type       type
     * @param deprecated deprecated
     * @return new field
     */
    public static FieldDefinition enumeration(String name, int number, TypeName type, boolean deprecated) {
        return new FieldDefinition(name, number, type, ENUM, deprecated);
    }

    /**
     * Creates new message field
     *
     * @param name       name
     * @param number     number
     * @param type       type
     * @param deprecated deprecated
     * @return new field
     */
    public static FieldDefinition message(String name, int number, TypeName type, boolean deprecated) {
        return new FieldDefinition(name, number, type, MESSAGE, deprecated);
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
    }

    private static final class Valid {
        private static final Pattern namePattern = Pattern.compile("^[a-zA-z_][a-zA-Z0-9_]*$");

        private static int number(int number) {
            assertTrue(number > 0, "Number must be positive, but was: " + number);
            return number;
        }

        private static String name(String name) {
            assertNonNull(name, "Incorrect field name: <null>");
            assertTrue(namePattern.matcher(name).matches(), "Incorrect field name: " + name);
            return name;
        }

        private static TypeName type(TypeName type) {
            assertNonNull(type, "Must provide field type");
            return type;
        }

        private static ProtoKind protoType(ProtoKind type) {
            assertNonNull(type, "Must provide proto type");
            return type;
        }
    }
}
