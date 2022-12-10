package com.github.pcimcioch.protobuf.model;

import com.github.pcimcioch.protobuf.code.MethodBody;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaRecordComponentSource;
import org.jboss.forge.roaster.model.source.JavaRecordSource;
import org.jboss.forge.roaster.model.source.MethodSource;

import java.util.List;

import static com.github.pcimcioch.protobuf.code.MethodBody.body;
import static com.github.pcimcioch.protobuf.code.MethodBody.param;

/**
 * Enumeration field definition
 */
public class EnumerationFieldDefinition extends FieldDefinition {

    private EnumerationFieldDefinition(String name, int number, TypeName type, boolean deprecated) {
        super(name, number, type, deprecated);
    }

    @Override
    public List<String> fieldNames() {
        return List.of(valueName(), enumName());
    }

    @Override
    public void addBuilderCode(JavaClassSource builderClass) {
        // field
        FieldSource<JavaClassSource> field = builderClass.addField()
                .setPrivate()
                .setType("int")
                .setName(valueName())
                .setLiteralInitializer("0");
        applyDeprecated(field);

        // value setter
        MethodBody valueBody = body("""
                        this.$field = $field;
                        return this;
                        """,
                param("field", valueName())
        );

        MethodSource<JavaClassSource> valueMethod = builderClass.addMethod()
                .setPublic()
                .setReturnType(builderClass)
                .setName(valueName())
                .setBody(valueBody.toString());
        valueMethod.addParameter("int", valueName());
        applyDeprecated(valueMethod);

        // enum setter
        MethodBody enumBody = body("return this.$valueName($enumName.number());",
                param("valueName", valueName()),
                param("enumName", enumName())
        );

        MethodSource<JavaClassSource> enumMethod = builderClass.addMethod()
                .setPublic()
                .setReturnType(builderClass)
                .setName(enumName())
                .setBody(enumBody.toString());
        enumMethod.addParameter(type().canonicalName(), enumName());
        applyDeprecated(enumMethod);
    }

    @Override
    public String builderField() {
        return valueName();
    }

    @Override
    public MethodBody decodingCode() {
        return MethodBody.body("builder.$fieldName(reader.int32(tag, \"$fieldName\"));",
                param("fieldName", valueName())
        );
    }

    @Override
    public MethodBody encodingCode() {
        return MethodBody.body("writer.int32($number, $name);",
                param("number", number()),
                param("name", valueName())
        );
    }

    @Override
    public void addMessageCode(JavaRecordSource messageRecord) {
        // field
        JavaRecordComponentSource component = messageRecord.addRecordComponent("int", valueName());
        applyDeprecated(component);

        // enum getter
        MethodBody body = body("return $enumType.forNumber($valueName);",
                param("enumType", type()),
                param("valueName", valueName())
        );

        MethodSource<JavaRecordSource> method = messageRecord.addMethod()
                .setPublic()
                .setReturnType(type().canonicalName())
                .setName(enumName())
                .setBody(body.toString());
        applyDeprecated(method);
    }

    @Override
    public void addMessageConstructorCode(MethodSource<JavaRecordSource> constructor) {
        MethodBody body = body("this.$fieldName = $fieldName;",
                param("fieldName", valueName())
        );

        constructor.addParameter("int", valueName());
        constructor.setBody(constructor.getBody() + body);
    }

    private String enumName() {
        return name();
    }

    private String valueName() {
        return name() + "Value";
    }

    /**
     * Creates enumeration field definition
     *
     * @param name       name of the field
     * @param number     number of the field
     * @param type       java enum type
     * @param deprecated whether field is deprecated
     * @return new enumeration field
     */
    public static EnumerationFieldDefinition create(String name, int number, TypeName type, boolean deprecated) {
        return new EnumerationFieldDefinition(name, number, type, deprecated);
    }
}
