package com.github.pcimcioch.protobuf.model.message;

import com.github.pcimcioch.protobuf.model.field.FieldDefinition;
import com.github.pcimcioch.protobuf.model.message.ReservedDefinition.Range;
import com.github.pcimcioch.protobuf.model.type.TypeName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Set;

import static com.github.pcimcioch.protobuf.model.field.FieldDefinition.scalar;
import static com.github.pcimcioch.protobuf.model.type.TypeName.canonicalName;
import static java.lang.Integer.MAX_VALUE;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MessageDefinitionTest {

    private static final TypeName NAME = canonicalName("com.example.MyType");
    private static final TypeName CHILD_MESSAGE_NAME = canonicalName("com.example.MyType.ChildMessage");
    private static final TypeName CHILD_ENUM_NAME = canonicalName("com.example.MyType.ChildEnum");
    private static final ReservedDefinition NO_RESERVED = new ReservedDefinition(Set.of(), Set.of(), Set.of());

    @Test
    void correctMessage() {
        // given
        List<FieldDefinition> fields = asList(
                scalar("test1", 1, "bool", false),
                scalar("test2", 2, "int32", false),
                scalar("test3", 3, "string", false)
        );

        // when
        assertThatCode(() -> new MessageDefinition(NAME, fields, NO_RESERVED, emptyList(), emptyList()))
                .doesNotThrowAnyException();
    }

    @Nested
    class NameValidation {

        @Test
        void nullName() {
            // given
            List<FieldDefinition> fields = singletonList(
                    scalar("test", 1, "bool", false)
            );

            // when then
            assertThatThrownBy(() -> new MessageDefinition(null, fields, NO_RESERVED, emptyList(), emptyList()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Message name cannot be null");
        }
    }

    @Nested
    class FieldsValidation {

        @Test
        void nullFields() {
            // when then
            assertThatThrownBy(() -> new MessageDefinition(NAME, null, NO_RESERVED, emptyList(), emptyList()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Message must have at least one field");
        }

        @Test
        void emptyFields() {
            // when then
            assertThatThrownBy(() -> new MessageDefinition(NAME, emptyList(), NO_RESERVED, emptyList(), emptyList()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Message must have at least one field");
        }

        @Test
        void duplicatedFieldName() {
            // given
            List<FieldDefinition> fields = asList(
                    scalar("test", 1, "bool", false),
                    scalar("test2", 2, "int32", false),
                    scalar("test", 3, "string", false)
            );

            // when then
            assertThatThrownBy(() -> new MessageDefinition(NAME, fields, NO_RESERVED, emptyList(), emptyList()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Duplicated field name: test");
        }

        @Test
        void duplicatedFieldNumbers() {
            // given
            List<FieldDefinition> fields = asList(
                    scalar("test1", 1, "bool", false),
                    scalar("test2", 2, "int32", false),
                    scalar("test3", 1, "string", false)
            );

            // when then
            assertThatThrownBy(() -> new MessageDefinition(NAME, fields, NO_RESERVED, emptyList(), emptyList()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Duplicated field number: 1");
        }

        @Test
        void nullField() {
            // given
            List<FieldDefinition> fields = asList(
                    scalar("test1", 1, "bool", false),
                    scalar("test2", 2, "int32", false),
                    null
            );

            // when
            assertThatThrownBy(() -> new MessageDefinition(NAME, fields, NO_RESERVED, emptyList(), emptyList()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Null field");
        }
    }

    @Nested
    class ReservedValidation {

        private static final ReservedDefinition RESERVED = new ReservedDefinition(
                Set.of("reserved1", "reserved2"),
                Set.of(100, 101, 102),
                Set.of(new Range(200, 300), new Range(1000, MAX_VALUE))
        );

        @ParameterizedTest
        @ValueSource(strings = {"name", "name1", "reserved", "reserved3"})
        void correctFieldNames(String fieldName) {
            // given
            List<FieldDefinition> fields = singletonList(
                    scalar(fieldName, 1, "bool", false)
            );

            // when
            assertThatCode(() -> new MessageDefinition(NAME, fields, RESERVED, emptyList(), emptyList()))
                    .doesNotThrowAnyException();
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 2, 99, 103, 199, 301, 999})
        void correctFieldNumbers(int fieldNumber) {
            // given
            List<FieldDefinition> fields = singletonList(
                    scalar("test", fieldNumber, "bool", false)
            );

            // when
            assertThatCode(() -> new MessageDefinition(NAME, fields, RESERVED, emptyList(), emptyList()))
                    .doesNotThrowAnyException();
        }

        @ParameterizedTest
        @ValueSource(strings = {"reserved1", "reserved2"})
        void incorrectFieldNames(String fieldName) {
            // given
            List<FieldDefinition> fields = singletonList(
                    scalar(fieldName, 1, "bool", false)
            );

            // when
            assertThatThrownBy(() -> new MessageDefinition(NAME, fields, RESERVED, emptyList(), emptyList()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("is reserved");
        }

        @ParameterizedTest
        @ValueSource(ints = {100, 101, 102, 200, 256, 300, 1000, 1234567, MAX_VALUE})
        void incorrectFieldNumbers(int fieldNumber) {
            // given
            List<FieldDefinition> fields = singletonList(
                    scalar("test", fieldNumber, "bool", false)
            );

            // when
            assertThatThrownBy(() -> new MessageDefinition(NAME, fields, RESERVED, emptyList(), emptyList()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("is reserved");
        }

        @Test
        void correctNested() {
            // given
            List<FieldDefinition> fields = asList(
                    scalar("test1", 1, "bool", false),
                    scalar("test2", 2, "int32", false),
                    scalar("test3", 3, "string", false)
            );
            MessageDefinition nestedMessage = new MessageDefinition(CHILD_MESSAGE_NAME, fields, NO_RESERVED, emptyList(), emptyList());
            List<EnumerationElementDefinition> elements = singletonList(
                    new EnumerationElementDefinition("TEST", 0)
            );
            EnumerationDefinition nestedEnumeration = new EnumerationDefinition(CHILD_ENUM_NAME, elements, false, NO_RESERVED);

            // when
            assertThatCode(() -> new MessageDefinition(NAME, fields, NO_RESERVED, singletonList(nestedMessage), singletonList(nestedEnumeration)))
                    .doesNotThrowAnyException();
        }

        @Test
        void nullMessages() {
            // given
            List<FieldDefinition> fields = singletonList(
                    scalar("test1", 1, "bool", false)
            );

            // when
            assertThatThrownBy(() -> new MessageDefinition(NAME, fields, NO_RESERVED, null, emptyList()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Nested messages cannot be null");
        }

        @Test
        void messagesWithNullValue() {
            // given
            List<FieldDefinition> fields = singletonList(
                    scalar("test1", 1, "bool", false)
            );

            // when
            assertThatThrownBy(() -> new MessageDefinition(NAME, fields, NO_RESERVED, singletonList(null), emptyList()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Null nested message");
        }

        @Test
        void incorrectNestedMessage() {
            // given
            List<FieldDefinition> fields = singletonList(
                    scalar("test1", 1, "bool", false)
            );
            MessageDefinition nestedMessage = new MessageDefinition(canonicalName("com.test.Incorrect"), fields, NO_RESERVED, emptyList(), emptyList());

            // when
            assertThatThrownBy(() -> new MessageDefinition(NAME, fields, NO_RESERVED, singletonList(nestedMessage), emptyList()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Nested message has non-nested type");
        }

        @Test
        void nullEnumerations() {
            // given
            List<FieldDefinition> fields = singletonList(
                    scalar("test1", 1, "bool", false)
            );

            // when
            assertThatThrownBy(() -> new MessageDefinition(NAME, fields, NO_RESERVED, emptyList(), null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Nested enumerations cannot be null");
        }

        @Test
        void enumerationsWithNullValue() {
            // given
            List<FieldDefinition> fields = singletonList(
                    scalar("test1", 1, "bool", false)
            );

            // when
            assertThatThrownBy(() -> new MessageDefinition(NAME, fields, NO_RESERVED, emptyList(), singletonList(null)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Null nested enumeration");
        }

        @Test
        void incorrectNestedEnumeration() {
            // given
            List<FieldDefinition> fields = singletonList(
                    scalar("test1", 1, "bool", false)
            );
            List<EnumerationElementDefinition> elements = singletonList(
                    new EnumerationElementDefinition("TEST", 0)
            );
            EnumerationDefinition nestedEnumeration = new EnumerationDefinition(canonicalName("com.test.Incorrect"), elements, false, NO_RESERVED);

            // when
            assertThatThrownBy(() -> new MessageDefinition(NAME, fields, NO_RESERVED, emptyList(), singletonList(nestedEnumeration)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Nested enumeration has non-nested type");
        }
    }

}