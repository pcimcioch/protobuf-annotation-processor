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

import static com.github.pcimcioch.protobuf.code.MethodBody.body;
import static com.github.pcimcioch.protobuf.code.MethodBody.param;

/**
 * Message field definition
 */
// TODO add integration tests
public class MessageFieldDefinition extends FieldDefinition {

    private MessageFieldDefinition(String name, int number, TypeName type, boolean deprecated) {
        super(name, number, type, deprecated);
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
                .setLiteralInitializer("null");
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
        return body("builder.$fieldName(reader.message(tag, \"$fieldName\", $Type::parse));",
                param("fieldName", name()),
                param("Type", type().canonicalName())
        );
    }

    @Override
    public MethodBody encodingCode() {
        return body("writer.message($number, $name);",
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
        MethodBody body = body("this.$fieldName = $Objects.requireNonNull($fieldName, \"Field $fieldName cannot be null\");",
                param("fieldName", name()),
                param("Objects", Objects.class)
        );

        constructor.addParameter(type().canonicalName(), name());
        constructor.setBody(constructor.getBody() + body);
    }

    /**
     * Creates message field definition
     *
     * @param name       name of the field
     * @param number     number of the field
     * @param type       java enum type
     * @param deprecated whether field is deprecated
     * @return new message field
     */
    public static MessageFieldDefinition create(String name, int number, TypeName type, boolean deprecated) {
        return new MessageFieldDefinition(name, number, type, deprecated);
    }
}
