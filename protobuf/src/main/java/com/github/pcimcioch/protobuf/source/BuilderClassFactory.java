package com.github.pcimcioch.protobuf.source;

import com.github.pcimcioch.protobuf.code.ClassSource;
import com.github.pcimcioch.protobuf.code.CodeBody;
import com.github.pcimcioch.protobuf.code.InitializerSource;
import com.github.pcimcioch.protobuf.code.TypeName;
import com.github.pcimcioch.protobuf.dto.BooleanList;
import com.github.pcimcioch.protobuf.dto.ByteArray;
import com.github.pcimcioch.protobuf.dto.DoubleList;
import com.github.pcimcioch.protobuf.dto.EnumList;
import com.github.pcimcioch.protobuf.dto.FloatList;
import com.github.pcimcioch.protobuf.dto.IntList;
import com.github.pcimcioch.protobuf.dto.LongList;
import com.github.pcimcioch.protobuf.dto.ObjectList;
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
                        this.$field = value;
                        return this;""",
                param("field", field.javaFieldName())
        );

        builderClass.add(method(field.javaFieldName())
                .set(publicVisibility())
                .set(returns(message.builderName()))
                .set(body)
                .add(parameter(field.javaFieldType(), "value"))
                .addIf(annotation(Deprecated.class), field.rules().deprecated())
        );
    }

    private void addListSetter(ClassSource builderClass, FieldDefinition field, MessageDefinition message) {
        CodeBody body = body("""
                        this.$field.clear();
                        if (values != null) {
                          this.$field.addAll(values);
                        }
                        return this;""",
                param("field", field.javaFieldName())
        );

        builderClass.add(method(field.javaFieldName())
                .set(publicVisibility())
                .set(returns(message.builderName()))
                .set(body)
                .add(parameter(collectionAddType(field), "values"))
                .addIf(annotation(Deprecated.class), field.rules().deprecated())
        );
    }

    private void addEnumSingleSetter(ClassSource builderClass, FieldDefinition field, MessageDefinition message) {
        CodeBody body = body("return this.$field(value == null ? 0 : value.number());",
                param("field", field.javaFieldName())
        );

        builderClass.add(method(field.name())
                .set(publicVisibility())
                .set(returns(message.builderName()))
                .set(body)
                .add(parameter(field.protobufType(), "value"))
                .addIf(annotation(Deprecated.class), field.rules().deprecated())
        );
    }

    private void addEnumListSetter(ClassSource builderClass, FieldDefinition field, MessageDefinition message) {
        CodeBody body = body("""
                        this.$field.clear();
                        if (values != null) {
                          this.$field.addAllValues(values);
                        }
                        return this;""",
                param("field", field.javaFieldName())
        );

        builderClass.add(method(field.javaFieldName() + "Value")
                .set(publicVisibility())
                .set(returns(message.builderName()))
                .set(body)
                .add(parameter(simpleName("Integer").inCollection(), "values"))
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
                        this.$field.add(value);
                        return this;
                        """,
                param("field", field.javaFieldName()))
                : body("""
                        if (value != null) {
                          this.$field.add(value);
                        }
                        return this;
                        """,
                param("field", field.javaFieldName()));

        builderClass.add(method(field.javaFieldNamePrefixed("add"))
                .set(publicVisibility())
                .set(returns(message.builderName()))
                .set(body)
                .add(parameter(addType, "value"))
                .addIf(annotation(Deprecated.class), field.rules().deprecated())
        );
    }

    private void addListAddCollection(ClassSource builderClass, FieldDefinition field, MessageDefinition message) {
        CodeBody body = body("""
                        if (values != null) {
                          this.$field.addAll(values);
                        }
                        return this;
                        """,
                param("field", field.javaFieldName())
        );

        builderClass.add(method(field.javaFieldNamePrefixed("addAll"))
                .set(publicVisibility())
                .set(returns(message.builderName()))
                .set(body)
                .add(parameter(collectionAddType(field), "values"))
                .addIf(annotation(Deprecated.class), field.rules().deprecated())
        );
    }

    private void addEnumListAddSingle(ClassSource builderClass, FieldDefinition field, MessageDefinition message) {
        CodeBody body = body("""
                        this.$field.addValue(value);
                        return this;
                        """,
                param("field", field.javaFieldName())
        );

        builderClass.add(method(field.javaFieldNamePrefixed("add") + "Value")
                .set(publicVisibility())
                .set(returns(message.builderName()))
                .set(body)
                .add(parameter(int.class, "value"))
                .addIf(annotation(Deprecated.class), field.rules().deprecated())
        );
    }

    private void addEnumListAddCollection(ClassSource builderClass, FieldDefinition field, MessageDefinition message) {
        CodeBody body = body("""
                        if (values != null) {
                          this.$field.addAllValues(values);
                        }
                        return this;
                        """,
                param("field", field.javaFieldName())
        );

        builderClass.add(method(field.javaFieldNamePrefixed("addAll") + "Value")
                .set(publicVisibility())
                .set(returns(message.builderName()))
                .set(body)
                .add(parameter(simpleName("Integer").inCollection(), "values"))
                .addIf(annotation(Deprecated.class), field.rules().deprecated())
        );
    }

    private void addFieldMerge(ClassSource builderClass, FieldDefinition field, MessageDefinition message) {
        CodeBody body = body("""
                        this.$field = $ProtoDto.merge(this.$field, value);
                        return this;
                        """,
                param("field", field.javaFieldName()),
                param("ProtoDto", ProtoDto.class)
        );

        builderClass.add(method(field.javaFieldNamePrefixed("merge"))
                .set(publicVisibility())
                .set(returns(message.builderName()))
                .set(body)
                .add(parameter(field.javaFieldType(), "value"))
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
                case DOUBLE -> body("com.github.pcimcioch.protobuf.dto.DoubleList.builder()");
                case FLOAT -> body("com.github.pcimcioch.protobuf.dto.FloatList.builder()");
                case INT32, UINT32, SINT32, FIXED32, SFIXED32 ->
                        body("com.github.pcimcioch.protobuf.dto.IntList.builder()");
                case INT64, UINT64, SINT64, FIXED64, SFIXED64 ->
                        body("com.github.pcimcioch.protobuf.dto.LongList.builder()");
                case BOOL -> body("com.github.pcimcioch.protobuf.dto.BooleanList.builder()");
                case STRING, BYTES, MESSAGE, UNKNOWN -> body("com.github.pcimcioch.protobuf.dto.ObjectList.builder()");
                case ENUM -> body("com.github.pcimcioch.protobuf.dto.EnumList.builder($enumType::forNumber)",
                        param("enumType", field.protobufType()));
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
            case MESSAGE, UNKNOWN -> "null";
        });
    }

    private static TypeName builderFieldType(FieldDefinition field) {
        if (!field.rules().repeated()) {
            return field.javaFieldType();
        }

        return switch (field.protoKind()) {
            case DOUBLE -> canonicalName(DoubleList.Builder.class);
            case FLOAT -> canonicalName(FloatList.Builder.class);
            case INT32, UINT32, SINT32, FIXED32, SFIXED32 -> canonicalName(IntList.Builder.class);
            case INT64, UINT64, SINT64, FIXED64, SFIXED64 -> canonicalName(LongList.Builder.class);
            case BOOL -> canonicalName(BooleanList.Builder.class);
            case STRING -> canonicalName(ObjectList.Builder.class).of(simpleName("String"));
            case BYTES -> canonicalName(ObjectList.Builder.class).of(canonicalName(ByteArray.class));
            case MESSAGE, UNKNOWN -> canonicalName(ObjectList.Builder.class).of(field.protobufType());
            case ENUM -> canonicalName(EnumList.Builder.class).of(field.protobufType());
        };
    }

    private static String fieldToRecordTransform(FieldDefinition field) {
        return field.rules().repeated()
                ? field.javaFieldName() + ".build()"
                : field.javaFieldName();

    }

    private static TypeName singleAddType(FieldDefinition field) {
        return switch (field.protoKind()) {
            case DOUBLE -> simpleName("double");
            case FLOAT -> simpleName("float");
            case INT32, UINT32, SINT32, FIXED32, SFIXED32 -> simpleName("int");
            case INT64, UINT64, SINT64, FIXED64, SFIXED64 -> simpleName("long");
            case BOOL -> simpleName("boolean");
            case STRING -> simpleName("String");
            case BYTES -> canonicalName(ByteArray.class);
            case MESSAGE, ENUM, UNKNOWN -> field.protobufType();
        };
    }

    private static TypeName collectionAddType(FieldDefinition field) {
        return switch (field.protoKind()) {
            case DOUBLE -> simpleName("Double").inCollection();
            case FLOAT -> simpleName("Float").inCollection();
            case INT32, UINT32, SINT32, FIXED32, SFIXED32 -> simpleName("Integer").inCollection();
            case INT64, UINT64, SINT64, FIXED64, SFIXED64 -> simpleName("Long").inCollection();
            case BOOL -> simpleName("Boolean").inCollection();
            case STRING -> simpleName("String").inCollection();
            case BYTES -> canonicalName(ByteArray.class).inCollection();
            case MESSAGE, ENUM, UNKNOWN -> field.protobufType().inCollection();
        };
    }
}
