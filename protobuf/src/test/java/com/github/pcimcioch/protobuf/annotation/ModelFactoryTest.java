package com.github.pcimcioch.protobuf.annotation;

import com.github.pcimcioch.protobuf.annotation.Enumeration.Element;
import com.github.pcimcioch.protobuf.annotation.ProtoFiles.ProtoFile;
import com.github.pcimcioch.protobuf.model.ProtoDefinitions;
import com.github.pcimcioch.protobuf.model.field.FieldDefinition;
import com.github.pcimcioch.protobuf.model.message.EnumerationDefinition;
import com.github.pcimcioch.protobuf.model.message.EnumerationElementDefinition;
import com.github.pcimcioch.protobuf.model.message.MessageDefinition;
import com.github.pcimcioch.protobuf.model.message.ReservedDefinition;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;
import java.util.function.IntFunction;

import static com.github.pcimcioch.protobuf.code.TypeName.canonicalName;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ModelFactoryTest {

    private static final Reserved NO_RESERVED = reserved();
    private static final ReservedDefinition NO_RESERVED_DEF = reservedDef();

    private final ModelFactory testee = new ModelFactory();

    @Nested
    class ScalarMessages {

        @Test
        void createMessage() {
            // given
            ProtoFiles files = files(
                    file(
                            "com.example",
                            message("MyMessage",
                                    field("int32", "field", 1)),
                            message("Other",
                                    field("string", "name", 2),
                                    field("float", "age", 10))));

            // when
            ProtoDefinitions definitions = testee.buildProtoDefinitions(files);

            // then
            ProtoDefinitions expected = definitions(
                    messageDef("com.example.MyMessage",
                            scalarField("int32", "field", 1)),
                    messageDef("com.example.Other",
                            scalarField("string", "name", 2),
                            scalarField("float", "age", 10)));

            assertThat(definitions).isEqualTo(expected);
        }

        @Test
        void multipleFiles() {
            // given
            ProtoFiles files = files(
                    file("com.example",
                            message("MyMessage",
                                    field("int32", "field", 1))),
                    file("com.test",
                            message("Other",
                                    field("string", "name", 2),
                                    field("float", "age", 10)))
            );

            // when
            ProtoDefinitions definitions = testee.buildProtoDefinitions(files);

            // then
            ProtoDefinitions expected = definitions(
                    messageDef("com.example.MyMessage",
                            scalarField("int32", "field", 1)),
                    messageDef("com.test.Other",
                            scalarField("string", "name", 2),
                            scalarField("float", "age", 10)));

            assertThat(definitions).isEqualTo(expected);
        }
    }

    @Nested
    class Enums {

        @Test
        void createEnum() {
            // given
            ProtoFiles files = files(
                    file(
                            "com.example",
                            enumeration("MyEnum",
                                    element("LEFT", 0),
                                    element("RIGHT", 1)),
                            message("MyMessage",
                                    field("MyEnum", "field", 1))));

            // when
            ProtoDefinitions definitions = testee.buildProtoDefinitions(files);

            // then
            ProtoDefinitions expected = definitions(
                    enumerationDef("com.example.MyEnum",
                            elementDef("LEFT", 0),
                            elementDef("RIGHT", 1)),
                    messageDef("com.example.MyMessage",
                            enumField("com.example.MyEnum", "field", 1)));

            assertThat(definitions).isEqualTo(expected);
        }

        @Test
        void allowAlias() {
            // given
            ProtoFiles files = files(
                    file(
                            "com.example",
                            enumeration("MyEnum", true, NO_RESERVED,
                                    element("LEFT", 0),
                                    element("RIGHT", 1),
                                    element("L", 0)),
                            message("MyMessage",
                                    field("MyEnum", "field", 1))));

            // when
            ProtoDefinitions definitions = testee.buildProtoDefinitions(files);

            // then
            ProtoDefinitions expected = definitions(
                    enumerationDef("com.example.MyEnum", true, NO_RESERVED_DEF,
                            elementDef("LEFT", 0),
                            elementDef("RIGHT", 1),
                            elementDef("L", 0)),
                    messageDef("com.example.MyMessage",
                            enumField("com.example.MyEnum", "field", 1)));

            assertThat(definitions).isEqualTo(expected);
        }
    }

    @Nested
    class OtherTypes {

        @Test
        void createOtherTypes() {
            // given
            ProtoFiles files = files(
                    file(
                            "com.example",
                            message("Address",
                                    field("string", "street", 1),
                                    field("int32", "number", 2)),
                            message("Work",
                                    field("Address", "address", 1),
                                    field("string", "name", 2),
                                    field("fixed32", "year", 3)),
                            message("MyMessage",
                                    field("string", "name", 1),
                                    field("int32", "age", 2),
                                    field("Address", "address", 3),
                                    field("Work", "work", 4))));

            // when
            ProtoDefinitions definitions = testee.buildProtoDefinitions(files);

            // then
            ProtoDefinitions expected = definitions(
                    messageDef("com.example.Address",
                            scalarField("string", "street", 1),
                            scalarField("int32", "number", 2)),
                    messageDef("com.example.Work",
                            messageField("com.example.Address", "address", 1),
                            scalarField("string", "name", 2),
                            scalarField("fixed32", "year", 3)),
                    messageDef("com.example.MyMessage",
                            scalarField("string", "name", 1),
                            scalarField("int32", "age", 2),
                            messageField("com.example.Address", "address", 3),
                            messageField("com.example.Work", "work", 4)));

            assertThat(definitions).isEqualTo(expected);
        }
    }

    @Nested
    class Deprecated {

        @Test
        void deprecatedFields() {
            // given
            ProtoFiles files = files(
                    file(
                            "com.example",
                            enumeration("MyEnum",
                                    element("LEFT", 0),
                                    element("RIGHT", 1)),
                            message("Address",
                                    field("string", "street", 1),
                                    field("int32", "number", 2, true)),
                            message("Other",
                                    field("double", "value", 1),
                                    field("float", "deprecatedValue", 2, true),
                                    field("MyEnum", "en", 3),
                                    field("MyEnum", "enDeprecated", 4, true),
                                    field("Address", "add", 5),
                                    field("Address", "addDeprecated", 6))));

            // when
            ProtoDefinitions definitions = testee.buildProtoDefinitions(files);

            // then
            ProtoDefinitions expected = definitions(
                    enumerationDef("com.example.MyEnum",
                            elementDef("LEFT", 0),
                            elementDef("RIGHT", 1)),
                    messageDef("com.example.Address",
                            scalarField("string", "street", 1),
                            scalarField("int32", "number", 2, true)),
                    messageDef("com.example.Other",
                            scalarField("double", "value", 1),
                            scalarField("float", "deprecatedValue", 2, true),
                            enumField("com.example.MyEnum", "en", 3),
                            enumField("com.example.MyEnum", "enDeprecated", 4, true),
                            messageField("com.example.Address", "add", 5),
                            messageField("com.example.Address", "addDeprecated", 6)));

            assertThat(definitions).isEqualTo(expected);
        }
    }

    @Nested
    class Reservations {

        @Test
        void messageReserved() {
            // given
            ProtoFiles files = files(
                    file(
                            "com.example",
                            message("MyMessage",
                                    reserved("TEST", 10, 20, range(100, 200)),
                                    field("int32", "field", 1))));

            // when
            ProtoDefinitions definitions = testee.buildProtoDefinitions(files);

            // then
            ProtoDefinitions expected = definitions(
                    messageDef("com.example.MyMessage",
                            reservedDef("TEST", 10, 20, range(100, 200)),
                            scalarField("int32", "field", 1)));

            assertThat(definitions).isEqualTo(expected);
        }

        @Test
        void enumReserved() {
            // given
            ProtoFiles files = files(
                    file(
                            "com.example",
                            enumeration("MyEnum",
                                    false,
                                    reserved("TEST", 10, 20, range(100, 200)),
                                    element("LEFT", 0),
                                    element("RIGHT", 1)),
                            message("MyMessage",
                                    field("MyEnum", "field", 1))));

            // when
            ProtoDefinitions definitions = testee.buildProtoDefinitions(files);

            // then
            ProtoDefinitions expected = definitions(
                    enumerationDef("com.example.MyEnum",
                            false,
                            reservedDef("TEST", 10, 20, rangeDef(100, 200)),
                            elementDef("LEFT", 0),
                            elementDef("RIGHT", 1)),
                    messageDef("com.example.MyMessage",
                            enumField("com.example.MyEnum", "field", 1)));

            assertThat(definitions).isEqualTo(expected);
        }
    }

    @Nested
    class Packages {

        @Test
        void messageTypes() {
            // given
            ProtoFiles files = files(
                    file(
                            "com.example",
                            message("MessageOne",
                                    field("int32", "field", 1)),
                            message(".test.MessageTwo",
                                    field("int32", "field", 1)),
                            message("com.test.MessageThree",
                                    field("int32", "field", 1))));

            // when
            ProtoDefinitions definitions = testee.buildProtoDefinitions(files);

            // then
            ProtoDefinitions expected = definitions(
                    messageDef("com.example.MessageOne",
                            scalarField("int32", "field", 1)),
                    messageDef("com.example.test.MessageTwo",
                            scalarField("int32", "field", 1)),
                    messageDef("com.test.MessageThree",
                            scalarField("int32", "field", 1)));

            assertThat(definitions).isEqualTo(expected);
        }

        @Test
        void enumTypes() {
            // given
            ProtoFiles files = files(
                    file(
                            "com.example",
                            enumeration("MessageOne",
                                    element("FIRST", 0)),
                            enumeration(".test.MessageTwo",
                                    element("FIRST", 0)),
                            enumeration("com.test.MessageThree",
                                    element("FIRST", 0))));

            // when
            ProtoDefinitions definitions = testee.buildProtoDefinitions(files);

            // then
            ProtoDefinitions expected = definitions(
                    enumerationDef("com.example.MessageOne",
                            elementDef("FIRST", 0)),
                    enumerationDef("com.example.test.MessageTwo",
                            elementDef("FIRST", 0)),
                    enumerationDef("com.test.MessageThree",
                            elementDef("FIRST", 0)));

            assertThat(definitions).isEqualTo(expected);
        }

        @Test
        void otherMessageReferences() {
            // given
            ProtoFiles files = files(
                    file(
                            "com.example",
                            message("com.example.MessageOne",
                                    field("int32", "field", 1)),
                            message("com.example.test.MessageTwo",
                                    field("int32", "field", 1)),
                            message("com.test.MessageThree",
                                    field("int32", "field", 1)),
                            message("com.example.Main",
                                    field("MessageOne", "one", 1),
                                    field(".test.MessageTwo", "two", 2),
                                    field("com.test.MessageThree", "three", 3))));

            // when
            ProtoDefinitions definitions = testee.buildProtoDefinitions(files);

            // then
            ProtoDefinitions expected = definitions(
                    messageDef("com.example.MessageOne",
                            scalarField("int32", "field", 1)),
                    messageDef("com.example.test.MessageTwo",
                            scalarField("int32", "field", 1)),
                    messageDef("com.test.MessageThree",
                            scalarField("int32", "field", 1)),
                    messageDef("com.example.Main",
                            messageField("com.example.MessageOne", "one", 1),
                            messageField("com.example.test.MessageTwo", "two", 2),
                            messageField("com.test.MessageThree", "three", 3)));

            assertThat(definitions).isEqualTo(expected);
        }

        @Test
        void otherEnumReferences() {
            // given
            ProtoFiles files = files(
                    file(
                            "com.example",
                            enumeration("com.example.MessageOne",
                                    element("FIRST", 0)),
                            enumeration("com.example.test.MessageTwo",
                                    element("FIRST", 0)),
                            enumeration("com.test.MessageThree",
                                    element("FIRST", 0)),
                            message("com.example.Main",
                                    field("MessageOne", "one", 1),
                                    field(".test.MessageTwo", "two", 2),
                                    field("com.test.MessageThree", "three", 3))));

            // when
            ProtoDefinitions definitions = testee.buildProtoDefinitions(files);

            // then
            ProtoDefinitions expected = definitions(
                    enumerationDef("com.example.MessageOne",
                            elementDef("FIRST", 0)),
                    enumerationDef("com.example.test.MessageTwo",
                            elementDef("FIRST", 0)),
                    enumerationDef("com.test.MessageThree",
                            elementDef("FIRST", 0)),
                    messageDef("com.example.Main",
                            enumField("com.example.MessageOne", "one", 1),
                            enumField("com.example.test.MessageTwo", "two", 2),
                            enumField("com.test.MessageThree", "three", 3)));

            assertThat(definitions).isEqualTo(expected);
        }
    }

    @Nested
    class Nesting {

        @Test
        void nestedClasses() {
            // given
            ProtoFiles files = files(
                    file("com.example",
                            message("NestedUser",
                                    field("string", "name", 1),
                                    field("int32", "age", 2),
                                    field("NestedUser.NestedAddress", "address", 3),
                                    field("NestedUser.NestedWork", "work", 4)),
                            message("NestedUser.NestedAddress",
                                    field("string", "street", 1),
                                    field("int32", "number", 2)),
                            message("NestedUser.NestedWork",
                                    field("string", "name", 1),
                                    field("int32", "year", 2),
                                    field("NestedUser.NestedAddress", "address", 3),
                                    field("NestedUser.NestedWork.NestedWorkType", "type", 4)),
                            enumeration("NestedUser.NestedWork.NestedWorkType",
                                    element("OFFICE", 0),
                                    element("PHYSICAL", 1))));

            // when
            ProtoDefinitions definitions = testee.buildProtoDefinitions(files);
            ProtoDefinitions expected = definitions(
                    messageDef("com.example.NestedUser",
                            scalarField("string", "name", 1),
                            scalarField("int32", "age", 2),
                            messageField("com.example.NestedUser.NestedAddress", "address", 3),
                            messageField("com.example.NestedUser.NestedWork", "work", 4),
                            messageDef("com.example.NestedUser.NestedAddress",
                                    scalarField("string", "street", 1),
                                    scalarField("int32", "number", 2)),
                            messageDef("com.example.NestedUser.NestedWork",
                                    scalarField("string", "name", 1),
                                    scalarField("int32", "year", 2),
                                    messageField("com.example.NestedUser.NestedAddress", "address", 3),
                                    enumField("com.example.NestedUser.NestedWork.NestedWorkType", "type", 4),
                                    enumerationDef("com.example.NestedUser.NestedWork.NestedWorkType",
                                            elementDef("OFFICE", 0),
                                            elementDef("PHYSICAL", 1)))));

            // then
            assertThat(definitions).isEqualTo(expected);
        }
    }

    @Nested
    class IncorrectModel {

        @Test
        void unknownFieldType() {
            // given
            ProtoFiles files = files(
                    file("com.example",
                            message("Test",
                                    field("Incorrect", "name", 1))));

            // when then
            assertThatThrownBy(() -> testee.buildProtoDefinitions(files))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Cannot find field type for com.example.Incorrect");
        }

        @Test
        void duplicatedName() {
            // given
            ProtoFiles files = files(
                    file("com.example",
                            message("Test",
                                    field("string", "name", 1)),
                            enumeration("Test",
                                    element("LEFT", 0))));

            // when then
            assertThatThrownBy(() -> testee.buildProtoDefinitions(files))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Duplicated type name com.example.Test");
        }

        @Test
        void enumerationWithNestedType() {
            // given
            ProtoFiles files = files(
                    file("com.example",
                            enumeration("Test",
                                    element("LEFT", 0)),
                            message("Test.Nested",
                                    field("string", "name", 1))));

            // when then
            assertThatThrownBy(() -> testee.buildProtoDefinitions(files))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Enumeration type can not contain any nested types");
        }

        @Test
        void emptyNestedType() {
            // given
            ProtoFiles files = files(
                    file("com.example",
                            message("Parent.Child",
                                    field("string", "name", 1))));

            // when then
            assertThatThrownBy(() -> testee.buildProtoDefinitions(files))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Type is not defined: com.example.Parent");
        }
    }

    private static ProtoFiles files(ProtoFile... files) {
        return new ProtoFiles(List.of(files));
    }

    private static ProtoFile file(String javaPackage, Object... annotations) {
        return new ProtoFile(
                javaPackage,
                extractList(Message.class, annotations),
                extractList(Enumeration.class, annotations)
        );
    }

    private static Message message(String name, Field... fields) {
        return message(name, NO_RESERVED, fields);
    }

    private static Message message(String name, Reserved reserved, Field... fields) {
        return new Message() {
            @Override
            public String name() {
                return name;
            }

            @Override
            public Field[] fields() {
                return fields;
            }

            @Override
            public Reserved reserved() {
                return reserved;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return Message.class;
            }
        };
    }

    private static Field field(String type, String name, int number) {
        return field(type, name, number, false);
    }

    private static Field field(String type, String name, int number, boolean deprecated) {
        return new Field() {
            @Override
            public String type() {
                return type;
            }

            @Override
            public String name() {
                return name;
            }

            @Override
            public int number() {
                return number;
            }

            @Override
            public boolean deprecated() {
                return deprecated;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return Field.class;
            }
        };
    }

    private static Enumeration enumeration(String name, Element... elements) {
        return enumeration(name, false, NO_RESERVED, elements);
    }

    private static Enumeration enumeration(String name, boolean allowAlias, Reserved reserved, Element... elements) {
        return new Enumeration() {
            @Override
            public String name() {
                return name;
            }

            @Override
            public boolean allowAlias() {
                return allowAlias;
            }

            @Override
            public Element[] elements() {
                return elements;
            }

            @Override
            public Reserved reserved() {
                return reserved;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return Enumeration.class;
            }
        };
    }

    private static Element element(String name, int number) {
        return new Element() {
            @Override
            public String name() {
                return name;
            }

            @Override
            public int number() {
                return number;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return Element.class;
            }
        };
    }

    private static Reserved reserved(Object... elements) {
        return new Reserved() {
            @Override
            public String[] names() {
                return extractArray(String.class, String[]::new, elements);
            }

            @Override
            public int[] numbers() {
                return extractIntArray(elements);
            }

            @Override
            public Range[] ranges() {
                return extractArray(Range.class, Range[]::new, elements);
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return Reserved.class;
            }
        };
    }

    private static Reserved.Range range(int from, int to) {
        return new Reserved.Range() {
            @Override
            public int from() {
                return from;
            }

            @Override
            public int to() {
                return to;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return Reserved.Range.class;
            }
        };
    }

    private static ProtoDefinitions definitions(Object... elements) {
        List<MessageDefinition> messages = stream(elements)
                .filter(MessageDefinition.class::isInstance)
                .map(MessageDefinition.class::cast)
                .toList();
        List<EnumerationDefinition> enumerations = stream(elements)
                .filter(EnumerationDefinition.class::isInstance)
                .map(EnumerationDefinition.class::cast)
                .toList();

        return new ProtoDefinitions(messages, enumerations);
    }

    private static MessageDefinition messageDef(String name, Object... elements) {
        return messageDef(name, NO_RESERVED_DEF, elements);
    }

    private static MessageDefinition messageDef(String name, ReservedDefinition reserved, Object... elements) {
        return new MessageDefinition(
                canonicalName(name),
                extractList(FieldDefinition.class, elements),
                reserved,
                extractList(MessageDefinition.class, elements),
                extractList(EnumerationDefinition.class, elements)
        );
    }

    private static FieldDefinition scalarField(String type, String name, int number) {
        return scalarField(type, name, number, false);
    }

    private static FieldDefinition scalarField(String type, String name, int number, boolean deprecated) {
        return FieldDefinition.scalar(name, number, type, deprecated);
    }

    private static FieldDefinition enumField(String type, String name, int number) {
        return enumField(type, name, number, false);
    }

    private static FieldDefinition enumField(String type, String name, int number, boolean deprecated) {
        return FieldDefinition.enumeration(name, number, canonicalName(type), deprecated);
    }

    private static FieldDefinition messageField(String type, String name, int number) {
        return messageField(type, name, number, false);
    }

    private static FieldDefinition messageField(String type, String name, int number, boolean deprecated) {
        return FieldDefinition.message(name, number, canonicalName(type), deprecated);
    }

    private static EnumerationDefinition enumerationDef(String name, EnumerationElementDefinition... elements) {
        return enumerationDef(name, false, NO_RESERVED_DEF, elements);
    }

    private static EnumerationDefinition enumerationDef(String name, boolean allowAlias, ReservedDefinition reserved, EnumerationElementDefinition... elements) {
        return new EnumerationDefinition(canonicalName(name), List.of(elements), allowAlias, reserved);
    }

    private static EnumerationElementDefinition elementDef(String name, int number) {
        return new EnumerationElementDefinition(name, number);
    }

    private static ReservedDefinition reservedDef(Object... elements) {
        return new ReservedDefinition(
                extractSet(String.class, elements),
                extractSet(Integer.class, elements),
                extractSet(ReservedDefinition.Range.class, elements)
        );
    }

    private static ReservedDefinition.Range rangeDef(int from, int to) {
        return new ReservedDefinition.Range(from, to);
    }

    private static <T> List<T> extractList(Class<T> clazz, Object... elements) {
        return stream(elements)
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .toList();
    }

    private static <T> Set<T> extractSet(Class<T> clazz, Object... elements) {
        return stream(elements)
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .collect(toSet());
    }

    private static <T> T[] extractArray(Class<T> clazz, IntFunction<T[]> generator, Object... elements) {
        return stream(elements)
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .toArray(generator);
    }

    private static int[] extractIntArray(Object... elements) {
        return stream(elements)
                .filter(Integer.class::isInstance)
                .mapToInt(Integer.class::cast)
                .toArray();
    }
}