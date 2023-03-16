package com.github.pcimcioch.protobuf.source;

import com.github.pcimcioch.protobuf.code.ClassSource;
import com.github.pcimcioch.protobuf.code.CodeBody;
import com.github.pcimcioch.protobuf.code.InitializerSource;
import com.github.pcimcioch.protobuf.code.TypeName;
import com.github.pcimcioch.protobuf.dto.ByteArray;
import com.github.pcimcioch.protobuf.dto.DoubleList;
import com.github.pcimcioch.protobuf.dto.FloatList;
import com.github.pcimcioch.protobuf.dto.ProtoDto;
import com.github.pcimcioch.protobuf.model.field.FieldDefinition;
import com.github.pcimcioch.protobuf.model.message.MessageDefinition;

import java.util.List;

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

class BuilderClassFactory {

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
        builderClass.add(field(builderFieldType(field), field.javaFieldName())
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
                        if ($field != null) {
                          this.$field.addAll($field);
                        }
                        return this;""",
                param("field", field.javaFieldName())
        );

        builderClass.add(method(field.javaFieldName())
                .set(publicVisibility())
                .set(returns(message.builderName()))
                .set(body)
                .add(parameter(collectionAddType(field), field.javaFieldName()))
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
                .add(parameter(field.protobufType(), field.name()))
                .addIf(annotation(Deprecated.class), field.rules().deprecated())
        );
    }

    private void addEnumListSetter(ClassSource builderClass, FieldDefinition field, MessageDefinition message) {
        CodeBody body = body("""
                        this.$valueName.clear();
                        if ($enumName != null) {
                          $enumName.forEach(e -> this.$valueName.add(e.number()));
                        }
                        return this;""",
                param("valueName", field.javaFieldName()),
                param("enumName", field.name())
        );

        builderClass.add(method(field.name())
                .set(publicVisibility())
                .set(returns(message.builderName()))
                .set(body)
                .add(parameter(field.protobufType().inCollection(), field.name()))
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
        TypeName addType = singleAddType(field);
        CodeBody body = addType.isPrimitive()
                ? body("""
                        this.$field.add($field);
                        return this;
                        """,
                param("field", field.javaFieldName()))
                : body("""
                        if ($field != null) {
                          this.$field.add($field);
                        }
                        return this;
                        """,
                param("field", field.javaFieldName()));

        builderClass.add(method(field.javaFieldNamePrefixed("add"))
                .set(publicVisibility())
                .set(returns(message.builderName()))
                .set(body)
                .add(parameter(addType, field.javaFieldName()))
                .addIf(annotation(Deprecated.class), field.rules().deprecated())
        );
    }

    private void addListAddCollection(ClassSource builderClass, FieldDefinition field, MessageDefinition message) {
        CodeBody body = body("""
                        if ($field != null) {
                          this.$field.addAll($field);
                        }
                        return this;
                        """,
                param("field", field.javaFieldName())
        );

        builderClass.add(method(field.javaFieldNamePrefixed("addAll"))
                .set(publicVisibility())
                .set(returns(message.builderName()))
                .set(body)
                .add(parameter(collectionAddType(field), field.javaFieldName()))
                .addIf(annotation(Deprecated.class), field.rules().deprecated())
        );
    }

    private void addEnumListAddSingle(ClassSource builderClass, FieldDefinition field, MessageDefinition message) {
        CodeBody body = body("""
                        if ($enumName != null) {
                          this.$valueName.add($enumName.number());
                        }
                        return this;
                        """,
                param("valueName", field.javaFieldName()),
                param("enumName", field.name())
        );

        builderClass.add(method(field.namePrefixed("add"))
                .set(publicVisibility())
                .set(returns(message.builderName()))
                .set(body)
                .add(parameter(field.protobufType(), field.name()))
                .addIf(annotation(Deprecated.class), field.rules().deprecated())
        );
    }

    private void addEnumListAddCollection(ClassSource builderClass, FieldDefinition field, MessageDefinition message) {
        CodeBody body = body("""
                        if ($enumName != null) {
                          $enumName.forEach(e -> this.$valueName.add(e.number()));
                        }
                        return this;
                        """,
                param("valueName", field.javaFieldName()),
                param("enumName", field.name())
        );

        builderClass.add(method(field.namePrefixed("addAll"))
                .set(publicVisibility())
                .set(returns(message.builderName()))
                .set(body)
                .add(parameter(field.protobufType().inCollection(), field.name()))
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
                .map(BuilderClassFactory::fieldToRecordTransform)
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
            return initializer(switch (field.protoKind()) {
                case DOUBLE -> "com.github.pcimcioch.protobuf.dto.DoubleList.builder()";
                case FLOAT -> "com.github.pcimcioch.protobuf.dto.FloatList.builder()";
                default -> "new java.util.ArrayList<>()";
            });
        }

        return initializer(switch (field.protoKind()) {
            case DOUBLE -> "0d";
            case FLOAT -> "0f";
            case INT32, UINT32, SINT32, FIXED32, SFIXED32, ENUM -> "0";
            case INT64, UINT64, SINT64, FIXED64, SFIXED64 -> "0L";
            case BOOL -> "false";
            case STRING -> "\"\"";
            case BYTES -> "com.github.pcimcioch.protobuf.dto.ByteArray.empty()";
            case MESSAGE -> "null";
        });
    }

    private static TypeName builderFieldType(FieldDefinition field) {
        if (!field.rules().repeated()) {
            return field.javaFieldType();
        }

        return switch (field.protoKind()) {
            case DOUBLE -> canonicalName(DoubleList.Builder.class);
            case FLOAT -> canonicalName(FloatList.Builder.class);
            default -> field.javaFieldType();
        };
    }

    private static String fieldToRecordTransform(FieldDefinition field) {
        if (!field.rules().repeated()) {
            return field.javaFieldName();
        }

        return switch (field.protoKind()) {
            case DOUBLE, FLOAT -> field.javaFieldName() + ".build()";
            default -> field.javaFieldName();
        };
    }

    private static TypeName singleAddType(FieldDefinition field) {
        return switch (field.protoKind()) {
            case DOUBLE -> simpleName("double");
            case FLOAT -> simpleName("float");
            case INT32, UINT32, SINT32, FIXED32, SFIXED32, ENUM -> simpleName("int");
            case INT64, UINT64, SINT64, FIXED64, SFIXED64 -> simpleName("long");
            case BOOL -> simpleName("boolean");
            case STRING -> simpleName("String");
            case BYTES -> canonicalName(ByteArray.class);
            case MESSAGE -> field.protobufType();
        };
    }

    private static TypeName collectionAddType(FieldDefinition field) {
        return switch (field.protoKind()) {
            case DOUBLE -> simpleName("Double").inCollection();
            case FLOAT -> simpleName("Float").inCollection();
            case INT32, UINT32, SINT32, FIXED32, SFIXED32, ENUM -> simpleName("Integer").inCollection();
            case INT64, UINT64, SINT64, FIXED64, SFIXED64 -> simpleName("Long").inCollection();
            case BOOL -> simpleName("Boolean").inCollection();
            case STRING -> simpleName("String").inCollection();
            case BYTES -> canonicalName(ByteArray.class).inCollection();
            case MESSAGE -> field.protobufType().inCollection();
        };
    }
}
