package com.github.pcimcioch.protobuf.source;

import com.github.pcimcioch.protobuf.code.ClassSource;
import com.github.pcimcioch.protobuf.code.CodeBody;
import com.github.pcimcioch.protobuf.code.InitializerSource;
import com.github.pcimcioch.protobuf.code.TypeName;
import com.github.pcimcioch.protobuf.dto.ProtoDto;
import com.github.pcimcioch.protobuf.model.field.FieldDefinition;
import com.github.pcimcioch.protobuf.model.message.MessageDefinition;

import java.util.List;
import java.util.Map;

import static com.github.pcimcioch.protobuf.code.AnnotationSource.annotation;
import static com.github.pcimcioch.protobuf.code.CodeBody.body;
import static com.github.pcimcioch.protobuf.code.CodeBody.param;
import static com.github.pcimcioch.protobuf.code.FieldSource.field;
import static com.github.pcimcioch.protobuf.code.FinalSource.finalModifier;
import static com.github.pcimcioch.protobuf.code.InitializerSource.initializer;
import static com.github.pcimcioch.protobuf.code.MethodSource.method;
import static com.github.pcimcioch.protobuf.code.ParameterSource.parameter;
import static com.github.pcimcioch.protobuf.code.ReturnSource.returns;
import static com.github.pcimcioch.protobuf.code.StaticSource.staticModifier;
import static com.github.pcimcioch.protobuf.code.TypeName.canonicalName;
import static com.github.pcimcioch.protobuf.code.TypeName.simpleName;
import static com.github.pcimcioch.protobuf.code.VisibilitySource.privateVisibility;
import static com.github.pcimcioch.protobuf.code.VisibilitySource.publicVisibility;
import static com.github.pcimcioch.protobuf.model.field.FieldDefinition.ProtoKind.ENUM;
import static com.github.pcimcioch.protobuf.model.field.FieldDefinition.ProtoKind.MESSAGE;

class BuilderFactory {
    private static final TypeName collection = canonicalName("java.util.Collection");
    private static final Map<TypeName, String> DEFAULTS = Map.of(
            simpleName("double"), "0d",
            simpleName("float"), "0f",
            simpleName("int"), "0",
            simpleName("long"), "0L",
            simpleName("boolean"), "false",
            simpleName("String"), "\"\"",
            canonicalName("com.github.pcimcioch.protobuf.dto.ByteArray"), "com.github.pcimcioch.protobuf.dto.ByteArray.empty()"
    );

    ClassSource buildBuilderClass(MessageDefinition message) {
        ClassSource builderClass = buildSourceFile(message);

        for (FieldDefinition field : message.fields()) {
            addField(builderClass, field);
            addFieldSetters(builderClass, field, message);
            addFieldModifiers(builderClass, field, message);
        }
        addBuildMethod(builderClass, message);
        addMergeMethod(builderClass, message);

        return builderClass;
    }

    private ClassSource buildSourceFile(MessageDefinition message) {
        return ClassSource.clazz(message.builderName())
                .set(publicVisibility())
                .set(staticModifier())
                .set(finalModifier());
    }

    private void addField(ClassSource builderClass, FieldDefinition field) {
        builderClass.add(field(field.javaFieldType(), field.javaFieldName())
                .set(privateVisibility())
                .set(initializerOf(field))
        );
    }

    private void addFieldSetters(ClassSource builderClass, FieldDefinition field, MessageDefinition message) {
        if (field.rules().repeated()) {
            addListSetter(builderClass, field, message);
            if (field.protoKind() == ENUM) {
                addEnumListSetter(builderClass, field, message);
            }
        } else {
            addSingleSetter(builderClass, field, message);
            if (field.protoKind() == ENUM) {
                addEnumSingleSetter(builderClass, field, message);
            }
        }
    }

    private void addSingleSetter(ClassSource builderClass, FieldDefinition field, MessageDefinition message) {
        CodeBody body = body("""
                        this.$field = $field;
                        return this;""",
                param("field", field.javaFieldName())
        );

        builderClass.add(method(field.javaFieldName())
                .set(publicVisibility())
                .set(returns(message.builderName()))
                .set(body)
                .add(parameter(field.javaFieldType(), field.javaFieldName()))
                .addIf(annotation(Deprecated.class), field.rules().deprecated())
        );
    }

    private void addListSetter(ClassSource builderClass, FieldDefinition field, MessageDefinition message) {
        CodeBody body = body("""
                        this.$field.clear();
                        this.$field.addAll($field);
                        return this;""",
                param("field", field.javaFieldName())
        );

        builderClass.add(method(field.javaFieldName())
                .set(publicVisibility())
                .set(returns(message.builderName()))
                .set(body)
                .add(parameter(field.javaFieldType(), field.javaFieldName()))
                .addIf(annotation(Deprecated.class), field.rules().deprecated())
        );
    }

    private void addEnumSingleSetter(ClassSource builderClass, FieldDefinition field, MessageDefinition message) {
        CodeBody body = body("return this.$valueName($enumName == null ? 0 : $enumName.number());",
                param("valueName", field.javaFieldName()),
                param("enumName", field.name())
        );

        builderClass.add(method(field.name())
                .set(publicVisibility())
                .set(returns(message.builderName()))
                .set(body)
                .add(parameter(field.type(), field.name()))
                .addIf(annotation(Deprecated.class), field.rules().deprecated())
        );
    }

    private void addEnumListSetter(ClassSource builderClass, FieldDefinition field, MessageDefinition message) {
        CodeBody body = body("""
                        this.$valueName.clear();
                        $enumName.forEach(e -> this.$valueName.add(e.number()));
                        return this;""",
                param("valueName", field.javaFieldName()),
                param("enumName", field.name())
        );

        builderClass.add(method(field.name())
                .set(publicVisibility())
                .set(returns(message.builderName()))
                .set(body)
                .add(parameter(field.type(), field.name()))
                .addIf(annotation(Deprecated.class), field.rules().deprecated())
        );
    }

    private void addFieldModifiers(ClassSource builderClass, FieldDefinition field, MessageDefinition message) {
        if (field.rules().repeated()) {
            addListAddSingle(builderClass, field, message);
            addListAddCollection(builderClass, field, message);
            if (field.protoKind() == ENUM) {
                addEnumListAddSingle(builderClass, field, message);
                addEnumListAddCollection(builderClass, field, message);
            }
        } else if (field.protoKind() == MESSAGE) {
            addFieldMerge(builderClass, field, message);
        }
    }

    private void addListAddSingle(ClassSource builderClass, FieldDefinition field, MessageDefinition message) {
        CodeBody body = body("""
                        this.$field.add($field);
                        return this;
                        """,
                param("field", field.javaFieldName())
        );

        builderClass.add(method(field.javaFieldNamePrefixed("add"))
                .set(publicVisibility())
                .set(returns(message.builderName()))
                .set(body)
                .add(parameter(field.javaFieldType().generic(), field.javaFieldName()))
                .addIf(annotation(Deprecated.class), field.rules().deprecated())
        );
    }

    private void addListAddCollection(ClassSource builderClass, FieldDefinition field, MessageDefinition message) {
        CodeBody body = body("""
                        this.$field.addAll($field);
                        return this;
                        """,
                param("field", field.javaFieldName())
        );

        builderClass.add(method(field.javaFieldNamePrefixed("add"))
                .set(publicVisibility())
                .set(returns(message.builderName()))
                .set(body)
                .add(parameter(collection.of(field.javaFieldType().generic()), field.javaFieldName()))
                .addIf(annotation(Deprecated.class), field.rules().deprecated())
        );
    }

    private void addEnumListAddSingle(ClassSource builderClass, FieldDefinition field, MessageDefinition message) {
        CodeBody body = body("""
                        this.$valueName.add($enumName.number());
                        return this;
                        """,
                param("$valueName", field.javaFieldName()),
                param("$enumName", field.name())
        );

        builderClass.add(method(field.javaFieldNamePrefixed("add"))
                .set(publicVisibility())
                .set(returns(message.builderName()))
                .set(body)
                .add(parameter(field.type().generic(), field.name()))
                .addIf(annotation(Deprecated.class), field.rules().deprecated())
        );
    }

    private void addEnumListAddCollection(ClassSource builderClass, FieldDefinition field, MessageDefinition message) {
        CodeBody body = body("""
                        $enumName.forEach(e -> this.$valueName.add(e.number()));
                        return this;
                        """,
                param("$valueName", field.javaFieldName()),
                param("$enumName", field.name())
        );

        builderClass.add(method(field.javaFieldNamePrefixed("add"))
                .set(publicVisibility())
                .set(returns(message.builderName()))
                .set(body)
                .add(parameter(collection.of(field.type().generic()), field.name()))
                .addIf(annotation(Deprecated.class), field.rules().deprecated())
        );
    }

    private void addFieldMerge(ClassSource builderClass, FieldDefinition field, MessageDefinition message) {
        CodeBody body = body("""
                        this.$field = $ProtoDto.merge(this.$field, $field);
                        return this;
                        """,
                param("field", field.javaFieldName()),
                param("ProtoDto", ProtoDto.class)
        );

        builderClass.add(method(field.javaFieldNamePrefixed("merge"))
                .set(publicVisibility())
                .set(returns(message.builderName()))
                .set(body)
                .add(parameter(field.javaFieldType(), field.javaFieldName()))
                .addIf(annotation(Deprecated.class), field.rules().deprecated())
        );
    }

    private void addBuildMethod(ClassSource builderClass, MessageDefinition message) {
        List<String> constructorParameters = message.fields().stream()
                .map(FieldDefinition::javaFieldName)
                .toList();

        CodeBody body = body("return new $MessageType($constructorParameters);",
                param("MessageType", message.name()),
                param("constructorParameters", constructorParameters));

        builderClass.add(method("build")
                .set(publicVisibility())
                .set(returns(message.name()))
                .set(body)
        );
    }

    private void addMergeMethod(ClassSource builderClass, MessageDefinition message) {
        CodeBody body = body("""
                if (toMerge == null) {
                    return this;
                }
                """);
        for (FieldDefinition field : message.fields()) {
            body.appendln("this.$field = $ProtoDto.merge(this.$field, toMerge.$field());",
                    param("field", field.javaFieldName()),
                    param("ProtoDto", ProtoDto.class)
            );
        }
        body.append("return this;");

        builderClass.add(method("merge")
                .set(publicVisibility())
                .set(returns(message.builderName()))
                .set(body)
                .add(parameter(message.name(), "toMerge"))
        );
    }

    private static InitializerSource initializerOf(FieldDefinition field) {
        if (field.rules().repeated()) {
            return initializer("new java.util.ArrayList<>()");
        }
        if (field.protoKind() == MESSAGE) {
            return initializer("null");
        }
        return initializer(DEFAULTS.get(field.javaFieldType()));
    }
}
