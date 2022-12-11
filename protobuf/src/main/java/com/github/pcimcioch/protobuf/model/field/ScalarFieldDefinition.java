package com.github.pcimcioch.protobuf.model.field;

import com.github.pcimcioch.protobuf.code.MethodBody;
import com.github.pcimcioch.protobuf.model.type.TypeName;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaRecordComponentSource;
import org.jboss.forge.roaster.model.source.JavaRecordSource;
import org.jboss.forge.roaster.model.source.MethodSource;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.github.pcimcioch.protobuf.code.MethodBody.body;
import static com.github.pcimcioch.protobuf.code.MethodBody.param;
import static com.github.pcimcioch.protobuf.model.type.TypeName.canonicalName;
import static com.github.pcimcioch.protobuf.model.type.TypeName.simpleName;

/**
 * Scalar field available in Protobuf documentation
 */
public class ScalarFieldDefinition extends FieldDefinition {

    private final String defaultValue;
    private final String ioMethod;

    private ScalarFieldDefinition(String name, int number, TypeName type, String defaultValue, String ioMethod, boolean deprecated) {
        super(name, number, type, deprecated);
        this.defaultValue = defaultValue;
        this.ioMethod = ioMethod;
    }

    @Override
    public List<String> fieldNames() {
        return List.of(name());
    }

    @Override
    public void addBuilderCode(JavaClassSource builderClass) {
        // field
        FieldSource<JavaClassSource> field = builderClass.addField()
                .setPrivate()
                .setType(type().canonicalName())
                .setName(name())
                .setLiteralInitializer(defaultValue);
        applyDeprecated(field);

        // setter
        MethodBody body = body("""
                        this.$field = $field;
                        return this;
                        """,
                param("field", name())
        );

        MethodSource<JavaClassSource> method = builderClass.addMethod()
                .setPublic()
                .setReturnType(builderClass)
                .setName(name())
                .setBody(body.toString());
        method.addParameter(type().canonicalName(), name());
        applyDeprecated(method);
    }

    @Override
    public String builderField() {
        return name();
    }

    @Override
    public MethodBody decodingCode() {
        return body("builder.$fieldName(reader.$readMethod(tag, \"$fieldName\"));",
                param("fieldName", name()),
                param("readMethod", ioMethod)
        );
    }

    @Override
    public MethodBody encodingCode() {
        return body("writer.$writerMethod($number, $name);",
                param("writerMethod", ioMethod),
                param("number", number()),
                param("name", name())
        );
    }

    @Override
    public void addMessageCode(JavaRecordSource messageRecord) {
        JavaRecordComponentSource component = messageRecord.addRecordComponent(type().canonicalName(), name());
        applyDeprecated(component);
    }

    @Override
    public void addMessageConstructorCode(MethodSource<JavaRecordSource> constructor) {
        MethodBody body = body();
        if (type().isPrimitive()) {
            body.append("this.$fieldName = $fieldName;",
                    param("fieldName", name())
            );
        } else {
            body.append("this.$fieldName = $Objects.requireNonNull($fieldName, \"Field $fieldName cannot be null\");",
                    param("fieldName", name()),
                    param("Objects", Objects.class)
            );
        }

        constructor.addParameter(type().canonicalName(), name());
        constructor.setBody(constructor.getBody() + body);
    }

    /**
     * Creates scalar field definition
     *
     * @param name       name of the field
     * @param number     number of the field
     * @param protoType  protobuf field name
     * @param deprecated whether field is deprecated
     * @return new scalar field
     */
    public static Optional<ScalarFieldDefinition> create(String name, int number, String protoType, boolean deprecated) {
        return switch (protoType) {
            case "double" ->
                    Optional.of(new ScalarFieldDefinition(name, number, simpleName("double"), "0d", "_double", deprecated));
            case "float" ->
                    Optional.of(new ScalarFieldDefinition(name, number, simpleName("float"), "0f", "_float", deprecated));
            case "int32" ->
                    Optional.of(new ScalarFieldDefinition(name, number, simpleName("int"), "0", "int32", deprecated));
            case "int64" ->
                    Optional.of(new ScalarFieldDefinition(name, number, simpleName("long"), "0L", "int64", deprecated));
            case "uint32" ->
                    Optional.of(new ScalarFieldDefinition(name, number, simpleName("int"), "0", "uint32", deprecated));
            case "uint64" ->
                    Optional.of(new ScalarFieldDefinition(name, number, simpleName("long"), "0L", "uint64", deprecated));
            case "sint32" ->
                    Optional.of(new ScalarFieldDefinition(name, number, simpleName("int"), "0", "sint32", deprecated));
            case "sint64" ->
                    Optional.of(new ScalarFieldDefinition(name, number, simpleName("long"), "0L", "sint64", deprecated));
            case "fixed32" ->
                    Optional.of(new ScalarFieldDefinition(name, number, simpleName("int"), "0", "fixed32", deprecated));
            case "fixed64" ->
                    Optional.of(new ScalarFieldDefinition(name, number, simpleName("long"), "0L", "fixed64", deprecated));
            case "sfixed32" ->
                    Optional.of(new ScalarFieldDefinition(name, number, simpleName("int"), "0", "sfixed32", deprecated));
            case "sfixed64" ->
                    Optional.of(new ScalarFieldDefinition(name, number, simpleName("long"), "0L", "sfixed64", deprecated));
            case "bool" ->
                    Optional.of(new ScalarFieldDefinition(name, number, simpleName("boolean"), "false", "bool", deprecated));
            case "string" ->
                    Optional.of(new ScalarFieldDefinition(name, number, simpleName("String"), "\"\"", "string", deprecated));
            case "bytes" ->
                    Optional.of(new ScalarFieldDefinition(name, number, canonicalName("com.github.pcimcioch.protobuf.dto.ByteArray"), "com.github.pcimcioch.protobuf.dto.ByteArray.EMPTY", "bytes", deprecated));
            default -> Optional.empty();
        };
    }
}
