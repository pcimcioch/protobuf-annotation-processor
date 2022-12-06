package com.github.pcimcioch.protobuf.model;

import com.github.pcimcioch.protobuf.code.MethodBody;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaRecordSource;
import org.jboss.forge.roaster.model.source.MethodSource;

import static com.github.pcimcioch.protobuf.code.MethodBody.body;
import static com.github.pcimcioch.protobuf.code.MethodBody.param;

/**
 * Represents enumeration field type
 */
public class EnumerationFieldType implements FieldType {

    private static final String SUFFIX = "Value";
    private static final TypeName TYPE = TypeName.simpleName("int");

    private final TypeName enumerationType;

    /**
     * Constructor
     *
     * @param enumerationType enumeration type
     */
    public EnumerationFieldType(TypeName enumerationType) {
        this.enumerationType = enumerationType;
    }

    @Override
    public TypeName fieldJavaType() {
        return TYPE;
    }

    @Override
    public String defaultValue() {
        return "0";
    }

    @Override
    public String ioMethod() {
        return "int32";
    }

    @Override
    public boolean requireNonNull() {
        return false;
    }

    @Override
    public void addBuilderMethods(JavaClassSource builderClass, String field) {
        addBuilderValueMethod(builderClass, field);
        addBuilderEnumMethod(builderClass, field);
    }

    @Override
    public void addMessageMethods(JavaRecordSource messageRecord, String field) {
        String enumName = enumName(field);

        MethodBody body = body("return $enumType.forNumber($field);",
                param("enumType", enumerationType),
                param("field", field)
        );

        messageRecord.addMethod()
                .setPublic()
                .setReturnType(enumerationType.canonicalName())
                .setName(enumName)
                .setBody(body.toString());
    }

    private void addBuilderValueMethod(JavaClassSource builderClass, String field) {
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

    private void addBuilderEnumMethod(JavaClassSource builderClass, String field) {
        String enumName = enumName(field);
        MethodBody body = body("return this.$field($enumName.number());",
                param("field", field),
                param("enumName", enumName)
        );

        MethodSource<JavaClassSource> method = builderClass.addMethod()
                .setPublic()
                .setReturnType(builderClass)
                .setName(enumName)
                .setBody(body.toString());
        method.addParameter(enumerationType.canonicalName(), enumName);
    }

    private static String enumName(String fieldName) {
        return fieldName.substring(0, fieldName.length() - SUFFIX.length());
    }

    /**
     * Computes internal value field name based on enumeration name
     *
     * @param enumName enumeration field name
     * @return internal value field name
     */
    public static String fieldName(String enumName) {
        return enumName + SUFFIX;
    }
}
