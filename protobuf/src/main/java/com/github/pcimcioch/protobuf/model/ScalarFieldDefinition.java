package com.github.pcimcioch.protobuf.model;

import com.github.pcimcioch.protobuf.code.MethodBody;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaRecordSource;
import org.jboss.forge.roaster.model.source.MethodSource;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.github.pcimcioch.protobuf.code.MethodBody.body;
import static com.github.pcimcioch.protobuf.code.MethodBody.param;
import static com.github.pcimcioch.protobuf.model.TypeName.canonicalName;
import static com.github.pcimcioch.protobuf.model.TypeName.simpleName;

/**
 * Scalar field available in Protobuf documentation
 */
public class ScalarFieldDefinition implements FieldDefinition {

    private final String name;
    private final int number;
    private final TypeName type;
    private final String defaultValue;
    private final String ioMethod;

    private ScalarFieldDefinition(String name, int number, TypeName type, String defaultValue, String ioMethod) {
        this.name = name;
        this.number = number;
        this.type = type;
        this.defaultValue = defaultValue;
        this.ioMethod = ioMethod;
    }

    @Override
    public int number() {
        return number;
    }

    @Override
    public List<String> fieldNames() {
        return List.of(name);
    }

    @Override
    public void addBuilderCode(JavaClassSource builderClass) {
        // field
        builderClass.addField()
                .setPrivate()
                .setType(type.canonicalName())
                .setName(name)
                .setLiteralInitializer(defaultValue);

        // setter
        MethodBody body = body("""
                        this.$field = $field;
                        return this;
                        """,
                param("field", name)
        );

        MethodSource<JavaClassSource> method = builderClass.addMethod()
                .setPublic()
                .setReturnType(builderClass)
                .setName(name)
                .setBody(body.toString());
        method.addParameter(type.canonicalName(), name);
    }

    @Override
    public String builderField() {
        return name;
    }

    @Override
    public MethodBody decodingCode() {
        return body("builder.$fieldName(reader.$readMethod(tag, \"$fieldName\"));",
                param("fieldName", name),
                param("readMethod", ioMethod)
        );
    }

    @Override
    public MethodBody encodingCode() {
        return MethodBody.body("writer.$writerMethod($number, $name);",
                param("writerMethod", ioMethod),
                param("number", number),
                param("name", name)
        );
    }

    @Override
    public void addMessageCode(JavaRecordSource messageRecord) {
        messageRecord.addRecordComponent(type.canonicalName(), name);
    }

    @Override
    public void addMessageConstructorCode(MethodSource<JavaRecordSource> constructor) {
        MethodBody body = body();
        if (type.isPrimitive()) {
            body.append("this.$fieldName = $fieldName;",
                    param("fieldName", name)
            );
        } else {
            body.append("this.$fieldName = $Objects.requireNonNull($fieldName, \"Field $fieldName cannot be null\");",
                    param("fieldName", name),
                    param("Objects", Objects.class)
            );
        }

        constructor.addParameter(type.canonicalName(), name);
        constructor.setBody(constructor.getBody() + body);
    }

    /**
     * Creates scalar field definition
     *
     * @param name      name of the field
     * @param number    number of the field
     * @param protoType protobuf field name
     * @return new scalar field
     */
    public static Optional<ScalarFieldDefinition> create(String name, int number, String protoType) {
        Valid.name(name);
        Valid.number(number);

        return switch (protoType) {
            case "double" ->
                    Optional.of(new ScalarFieldDefinition(name, number, simpleName("double"), "0d", "_double"));
            case "float" -> Optional.of(new ScalarFieldDefinition(name, number, simpleName("float"), "0f", "_float"));
            case "int32" -> Optional.of(new ScalarFieldDefinition(name, number, simpleName("int"), "0", "int32"));
            case "int64" -> Optional.of(new ScalarFieldDefinition(name, number, simpleName("long"), "0L", "int64"));
            case "uint32" -> Optional.of(new ScalarFieldDefinition(name, number, simpleName("int"), "0", "uint32"));
            case "uint64" -> Optional.of(new ScalarFieldDefinition(name, number, simpleName("long"), "0L", "uint64"));
            case "sint32" -> Optional.of(new ScalarFieldDefinition(name, number, simpleName("int"), "0", "sint32"));
            case "sint64" -> Optional.of(new ScalarFieldDefinition(name, number, simpleName("long"), "0L", "sint64"));
            case "fixed32" -> Optional.of(new ScalarFieldDefinition(name, number, simpleName("int"), "0", "fixed32"));
            case "fixed64" -> Optional.of(new ScalarFieldDefinition(name, number, simpleName("long"), "0L", "fixed64"));
            case "sfixed32" -> Optional.of(new ScalarFieldDefinition(name, number, simpleName("int"), "0", "sfixed32"));
            case "sfixed64" ->
                    Optional.of(new ScalarFieldDefinition(name, number, simpleName("long"), "0L", "sfixed64"));
            case "bool" -> Optional.of(new ScalarFieldDefinition(name, number, simpleName("boolean"), "false", "bool"));
            case "string" ->
                    Optional.of(new ScalarFieldDefinition(name, number, simpleName("String"), "\"\"", "string"));
            case "bytes" ->
                    Optional.of(new ScalarFieldDefinition(name, number, canonicalName("com.github.pcimcioch.protobuf.dto.ByteArray"), "com.github.pcimcioch.protobuf.dto.ByteArray.EMPTY", "bytes"));
            default -> Optional.empty();
        };
    }
}
