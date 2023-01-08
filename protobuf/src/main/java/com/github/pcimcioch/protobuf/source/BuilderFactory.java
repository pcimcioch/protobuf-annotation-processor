package com.github.pcimcioch.protobuf.source;

import com.github.pcimcioch.protobuf.code.CodeBody;
import com.github.pcimcioch.protobuf.dto.ProtoDto;
import com.github.pcimcioch.protobuf.model.field.FieldDefinition;
import com.github.pcimcioch.protobuf.model.message.MessageDefinition;
import com.github.pcimcioch.protobuf.code.TypeName;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;

import java.util.List;
import java.util.Map;

import static com.github.pcimcioch.protobuf.code.CodeBody.body;
import static com.github.pcimcioch.protobuf.code.CodeBody.param;
import static com.github.pcimcioch.protobuf.model.field.FieldDefinition.ProtoKind.ENUM;
import static com.github.pcimcioch.protobuf.model.field.FieldDefinition.ProtoKind.MESSAGE;
import static com.github.pcimcioch.protobuf.code.TypeName.canonicalName;
import static com.github.pcimcioch.protobuf.code.TypeName.simpleName;

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
            if (field.protoKind() == MESSAGE) {
                addFieldMerge(builderClass, field);
            }
        }
        addBuildMethod(builderClass, message);
        addMergeMethod(builderClass, message);
        addEmptyRecord(builderClass, message);

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
                .setLiteralInitializer(defaultOf(field));
        field.handleDeprecated(fieldSource);
    }

    private static String defaultOf(FieldDefinition field) {
        if (field.protoKind() == MESSAGE) {
            return "null";
        }
        return DEFAULTS.get(field.javaFieldType());
    }

    private void addSetter(JavaClassSource builderClass, FieldDefinition field) {
        CodeBody body = body("""
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
        CodeBody enumBody = body("return this.$valueName($enumName == null ? 0 : $enumName.number());",
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

    private void addFieldMerge(JavaClassSource builderClass, FieldDefinition field) {
        CodeBody body = body("""
                        this.$field = $ProtoDto.merge(this.$field, $field);
                        return this;
                        """,
                param("field", field.javaFieldName()),
                param("ProtoDto", ProtoDto.class)
        );

        MethodSource<JavaClassSource> method = builderClass.addMethod()
                .setPublic()
                .setReturnType(builderClass)
                .setName(field.javaFieldNamePrefixed("merge"))
                .setBody(body.toString());
        method.addParameter(field.javaFieldType().canonicalName(), field.javaFieldName());
        field.handleDeprecated(method);
    }

    private void addBuildMethod(JavaClassSource builderClass, MessageDefinition message) {
        List<String> constructorParameters = message.fields().stream()
                .map(FieldDefinition::javaFieldName)
                .toList();

        CodeBody body = body("return new $MessageType($constructorParameters);",
                param("MessageType", message.name()),
                param("constructorParameters", constructorParameters));

        builderClass.addMethod()
                .setPublic()
                .setReturnType(message.name().canonicalName())
                .setName("build")
                .setBody(body.toString());
    }

    private void addMergeMethod(JavaClassSource builderClass, MessageDefinition message) {
        CodeBody body = body("""
                if (toMerge == null) {
                    return this;
                }
                """);
        for (FieldDefinition field : message.fields()) {
            body.append("this.$field = $ProtoDto.merge(this.$field, toMerge.$field());",
                    param("field", field.javaFieldName()),
                    param("ProtoDto", ProtoDto.class)
            );
        }
        body.append("return this;");

        MethodSource<JavaClassSource> method = builderClass.addMethod()
                .setPublic()
                .setReturnType(builderClass)
                .setName("merge")
                .setBody(body.toString());
        method.addParameter(message.name().canonicalName(), "toMerge");
    }

    private void addEmptyRecord(JavaClassSource builderClass, MessageDefinition message) {
        builderClass.addField()
                .setPrivate()
                .setStatic(true)
                .setFinal(true)
                .setType(message.name().canonicalName())
                .setName("EMPTY")
                .setLiteralInitializer("new Builder().build()");
    }
}
