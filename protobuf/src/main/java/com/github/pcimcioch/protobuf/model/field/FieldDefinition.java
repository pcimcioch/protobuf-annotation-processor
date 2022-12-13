package com.github.pcimcioch.protobuf.model.field;

import com.github.pcimcioch.protobuf.model.type.TypeName;
import org.jboss.forge.roaster.model.source.AnnotationTargetSource;

import java.util.Optional;
import java.util.regex.Pattern;

import static com.github.pcimcioch.protobuf.model.field.ProtoType.BOOL;
import static com.github.pcimcioch.protobuf.model.field.ProtoType.BYTES;
import static com.github.pcimcioch.protobuf.model.field.ProtoType.DOUBLE;
import static com.github.pcimcioch.protobuf.model.field.ProtoType.ENUM;
import static com.github.pcimcioch.protobuf.model.field.ProtoType.FIXED32;
import static com.github.pcimcioch.protobuf.model.field.ProtoType.FIXED64;
import static com.github.pcimcioch.protobuf.model.field.ProtoType.FLOAT;
import static com.github.pcimcioch.protobuf.model.field.ProtoType.INT32;
import static com.github.pcimcioch.protobuf.model.field.ProtoType.INT64;
import static com.github.pcimcioch.protobuf.model.field.ProtoType.MESSAGE;
import static com.github.pcimcioch.protobuf.model.field.ProtoType.SFIXED32;
import static com.github.pcimcioch.protobuf.model.field.ProtoType.SFIXED64;
import static com.github.pcimcioch.protobuf.model.field.ProtoType.SINT32;
import static com.github.pcimcioch.protobuf.model.field.ProtoType.SINT64;
import static com.github.pcimcioch.protobuf.model.field.ProtoType.STRING;
import static com.github.pcimcioch.protobuf.model.field.ProtoType.UINT32;
import static com.github.pcimcioch.protobuf.model.field.ProtoType.UINT64;
import static com.github.pcimcioch.protobuf.model.type.TypeName.canonicalName;
import static com.github.pcimcioch.protobuf.model.type.TypeName.simpleName;

/**
 * Message field
 */
// TODO make tests
public class FieldDefinition {

    private static final TypeName ENUM_VALUE_TYPE = simpleName("int");

    private final String name;
    private final int number;
    private final TypeName type;
    private final boolean deprecated;
    private final ProtoType protoType;

    private FieldDefinition(String name, int number, TypeName type, ProtoType protoType, boolean deprecated) {
        this.name = Valid.name(name);
        this.number = Valid.number(number);
        this.type = Valid.type(type);
        this.protoType = Valid.protoType(protoType);
        this.deprecated = deprecated;
    }

    public String name() {
        return name;
    }

    public int number() {
        return number;
    }

    public TypeName type() {
        return type;
    }

    public ProtoType protoType() {
        return protoType;
    }

    public TypeName javaFieldType() {
        if (protoType == ENUM) {
            return ENUM_VALUE_TYPE;
        } else {
            return type;
        }
    }

    public String javaFieldName() {
        if (protoType == ENUM) {
            return name + "Value";
        } else {
            return name;
        }
    }

    public void applyDeprecated(AnnotationTargetSource<?, ?> source) {
        if (deprecated) {
            source.addAnnotation(Deprecated.class);
        }
    }

    public static Optional<FieldDefinition> scalar(String name, int number, String protoType, boolean deprecated) {
        return switch (protoType) {
            case "double" -> Optional.of(new FieldDefinition(name, number, simpleName("double"), DOUBLE, deprecated));
            case "float" -> Optional.of(new FieldDefinition(name, number, simpleName("float"), FLOAT, deprecated));
            case "int32" -> Optional.of(new FieldDefinition(name, number, simpleName("int"), INT32, deprecated));
            case "int64" -> Optional.of(new FieldDefinition(name, number, simpleName("long"), INT64, deprecated));
            case "uint32" -> Optional.of(new FieldDefinition(name, number, simpleName("int"), UINT32, deprecated));
            case "uint64" -> Optional.of(new FieldDefinition(name, number, simpleName("long"), UINT64, deprecated));
            case "sint32" -> Optional.of(new FieldDefinition(name, number, simpleName("int"), SINT32, deprecated));
            case "sint64" -> Optional.of(new FieldDefinition(name, number, simpleName("long"), SINT64, deprecated));
            case "fixed32" -> Optional.of(new FieldDefinition(name, number, simpleName("int"), FIXED32, deprecated));
            case "fixed64" -> Optional.of(new FieldDefinition(name, number, simpleName("long"), FIXED64, deprecated));
            case "sfixed32" -> Optional.of(new FieldDefinition(name, number, simpleName("int"), SFIXED32, deprecated));
            case "sfixed64" -> Optional.of(new FieldDefinition(name, number, simpleName("long"), SFIXED64, deprecated));
            case "bool" -> Optional.of(new FieldDefinition(name, number, simpleName("boolean"), BOOL, deprecated));
            case "string" -> Optional.of(new FieldDefinition(name, number, simpleName("String"), STRING, deprecated));
            case "bytes" ->
                    Optional.of(new FieldDefinition(name, number, canonicalName("com.github.pcimcioch.protobuf.dto.ByteArray"), BYTES, deprecated));
            default -> Optional.empty();
        };
    }

    public static FieldDefinition enumeration(String name, int number, TypeName type, boolean deprecated) {
        return new FieldDefinition(name, number, type, ENUM, deprecated);
    }

    // TODO Add integration tests
    public static FieldDefinition message(String name, int number, TypeName type, boolean deprecated) {
        return new FieldDefinition(name, number, type, MESSAGE, deprecated);
    }

    private static final class Valid {
        private static final Pattern namePattern = Pattern.compile("^[a-zA-z_][a-zA-Z0-9_]*$");

        private static int number(int number) {
            if (number <= 0) {
                throw new IllegalArgumentException("Number must be positive, but was: " + number);
            }

            return number;
        }

        private static String name(String name) {
            if (name == null) {
                throw new IllegalArgumentException("Incorrect field name: <null>");
            }
            if (!namePattern.matcher(name).matches()) {
                throw new IllegalArgumentException("Incorrect field name: " + name);
            }

            return name;
        }

        private static TypeName type(TypeName type) {
            if (type == null) {
                throw new IllegalArgumentException("Must provide field type");
            }

            return type;
        }

        private static ProtoType protoType(ProtoType type) {
            if (type == null) {
                throw new IllegalArgumentException("Must provide proto type");
            }

            return type;
        }
    }
}
