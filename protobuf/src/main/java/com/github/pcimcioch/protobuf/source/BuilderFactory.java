package com.github.pcimcioch.protobuf.source;

import com.github.pcimcioch.protobuf.code.MethodBody;
import com.github.pcimcioch.protobuf.model.field.FieldDefinition;
import com.github.pcimcioch.protobuf.model.message.MessageDefinition;
import com.github.pcimcioch.protobuf.model.type.TypeName;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;

import java.util.List;
import java.util.Map;

import static com.github.pcimcioch.protobuf.code.MethodBody.body;
import static com.github.pcimcioch.protobuf.code.MethodBody.param;
import static com.github.pcimcioch.protobuf.model.field.FieldDefinition.ProtoKind.ENUM;
import static com.github.pcimcioch.protobuf.model.type.TypeName.canonicalName;
import static com.github.pcimcioch.protobuf.model.type.TypeName.simpleName;

class BuilderFactory {

    private static final Map<TypeName, String> DEFAULTS = Map.of(
            simpleName("double"), "0d",
            simpleName("float"), "0f",
            simpleName("int"), "0",
            simpleName("long"), "0L",
            simpleName("boolean"), "false",
            simpleName("String"), "\"\"",
            canonicalName("com.github.pcimcioch.protobuf.dto.ByteArray"), "com.github.pcimcioch.protobuf.dto.ByteArray.empty()"
    );

    JavaClassSource buildBuilder(MessageDefinition message) {
        JavaClassSource builderClass = buildSourceFile(message);

        for (FieldDefinition field : message.fields()) {
            addField(builderClass, field);
            addSetter(builderClass, field);
            if (field.protoKind() == ENUM) {
                addEnumSetter(builderClass, field);
            }
        }
        addBuildMethod(builderClass, message);

        return builderClass;
    }

    private JavaClassSource buildSourceFile(MessageDefinition message) {
        return Roaster.create(JavaClassSource.class)
                .setPublic()
                .setStatic(true)
                .setFinal(true)
                .setPackage(message.builderName().packageName())
                .setName(message.builderName().simpleName());
    }

    private void addField(JavaClassSource builderClass, FieldDefinition field) {
        FieldSource<JavaClassSource> fieldSource = builderClass.addField()
                .setPrivate()
                .setType(field.javaFieldType().canonicalName())
                .setName(field.javaFieldName())
                .setLiteralInitializer(DEFAULTS.getOrDefault(field.javaFieldType(), "null"));
        field.handleDeprecated(fieldSource);
    }

    private void addSetter(JavaClassSource builderClass, FieldDefinition field) {
        MethodBody body = body("""
                        this.$field = $field;
                        return this;
                        """,
                param("field", field.javaFieldName())
        );

        MethodSource<JavaClassSource> method = builderClass.addMethod()
                .setPublic()
                .setReturnType(builderClass)
                .setName(field.javaFieldName())
                .setBody(body.toString());
        method.addParameter(field.javaFieldType().canonicalName(), field.javaFieldName());
        field.handleDeprecated(method);
    }

    private void addEnumSetter(JavaClassSource builderClass, FieldDefinition field) {
        MethodBody enumBody = body("return this.$valueName($enumName.number());",
                param("valueName", field.javaFieldName()),
                param("enumName", field.name())
        );

        MethodSource<JavaClassSource> enumMethod = builderClass.addMethod()
                .setPublic()
                .setReturnType(builderClass)
                .setName(field.name())
                .setBody(enumBody.toString());
        enumMethod.addParameter(field.type().canonicalName(), field.name());
        field.handleDeprecated(enumMethod);
    }

    private void addBuildMethod(JavaClassSource builderClass, MessageDefinition message) {
        List<String> constructorParameters = message.fields().stream()
                .map(FieldDefinition::javaFieldName)
                .toList();

        MethodBody body = body("return new $MessageType($constructorParameters);",
                param("MessageType", message.name()),
                param("constructorParameters", constructorParameters));

        builderClass.addMethod()
                .setPublic()
                .setReturnType(message.name().canonicalName())
                .setName("build")
                .setBody(body.toString());
    }
}
