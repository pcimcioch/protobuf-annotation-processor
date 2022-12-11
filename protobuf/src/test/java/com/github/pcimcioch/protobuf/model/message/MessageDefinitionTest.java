package com.github.pcimcioch.protobuf.model.message;

import com.github.pcimcioch.protobuf.model.field.EnumerationFieldDefinition;
import com.github.pcimcioch.protobuf.model.field.FieldDefinition;
import com.github.pcimcioch.protobuf.model.field.ScalarFieldDefinition;
import com.github.pcimcioch.protobuf.model.message.ReservedDefinition.Range;
import com.github.pcimcioch.protobuf.model.type.TypeName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Set;

import static com.github.pcimcioch.protobuf.model.type.TypeName.canonicalName;
import static java.lang.Integer.MAX_VALUE;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MessageDefinitionTest {

    private static final TypeName NAME = canonicalName("com.example.MyType");
    private static final ReservedDefinition NO_RESERVED = new ReservedDefinition(Set.of(), Set.of(), Set.of());

    @Test
    void correctMessage() {
        // given
        List<FieldDefinition> fields = asList(
                scalarField("test1", 1, "bool"),
                scalarField("test2", 2, "int32"),
                scalarField("test3", 3, "string")
        );

        // when
        assertThatCode(() -> new MessageDefinition(NAME, fields, NO_RESERVED))
                .doesNotThrowAnyException();
    }

    @Nested
    class NameValidation {

        @Test
        void nullName() {
            // given
            List<FieldDefinition> fields = singletonList(
                    scalarField("test", 1, "bool")
            );

            // when then
            assertThatThrownBy(() -> new MessageDefinition(null, fields, NO_RESERVED))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Message name cannot be null");
        }
    }

    @Nested
    class FieldsValidation {

        @Test
        void nullFields() {
            // when then
            assertThatThrownBy(() -> new MessageDefinition(NAME, null, NO_RESERVED))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Message must have at least one field");
        }

        @Test
        void emptyFields() {
            // when then
            assertThatThrownBy(() -> new MessageDefinition(NAME, emptyList(), NO_RESERVED))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Message must have at least one field");
        }

        @Test
        void duplicatedFieldName() {
            // given
            List<FieldDefinition> fields = asList(
                    scalarField("test", 1, "bool"),
                    scalarField("test2", 2, "int32"),
                    scalarField("test", 3, "string")
            );

            // when then
            assertThatThrownBy(() -> new MessageDefinition(NAME, fields, NO_RESERVED))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Duplicated field name: test");
        }

        @Test
        void duplicatedEnumValueName() {
            // given
            List<FieldDefinition> fields = asList(
                    scalarField("testValue", 1, "bool"),
                    enumerationField("test", 2, canonicalName("com.example.TestEnum"))
            );

            // when then
            assertThatThrownBy(() -> new MessageDefinition(NAME, fields, NO_RESERVED))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Duplicated field name: testValue");
        }

        @Test
        void duplicatedFieldNumbers() {
            // given
            List<FieldDefinition> fields = asList(
                    scalarField("test1", 1, "bool"),
                    scalarField("test2", 2, "int32"),
                    scalarField("test3", 1, "string")
            );

            // when then
            assertThatThrownBy(() -> new MessageDefinition(NAME, fields, NO_RESERVED))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Duplicated field number: 1");
        }

        @Test
        void nullField() {
            // given
            List<FieldDefinition> fields = asList(
                    scalarField("test1", 1, "bool"),
                    scalarField("test2", 2, "int32"),
                    null
            );

            // when
            assertThatThrownBy(() -> new MessageDefinition(NAME, fields, NO_RESERVED))
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
                    scalarField(fieldName, 1, "bool")
            );

            // when
            assertThatCode(() -> new MessageDefinition(NAME, fields, RESERVED))
                    .doesNotThrowAnyException();
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 2, 99, 103, 199, 301, 999})
        void correctFieldNumbers(int fieldNumber) {
            // given
            List<FieldDefinition> fields = singletonList(
                    scalarField("test", fieldNumber, "bool")
            );

            // when
            assertThatCode(() -> new MessageDefinition(NAME, fields, RESERVED))
                    .doesNotThrowAnyException();
        }

        @ParameterizedTest
        @ValueSource(strings = {"reserved1", "reserved2"})
        void incorrectFieldNames(String fieldName) {
            // given
            List<FieldDefinition> fields = singletonList(
                    scalarField(fieldName, 1, "bool")
            );

            // when
            assertThatThrownBy(() -> new MessageDefinition(NAME, fields, RESERVED))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("is reserved");
        }

        @ParameterizedTest
        @ValueSource(ints = {100, 101, 102, 200, 256, 300, 1000, 1234567, MAX_VALUE})
        void incorrectFieldNumbers(int fieldNumber) {
            // given
            List<FieldDefinition> fields = singletonList(
                    scalarField("test", fieldNumber, "bool")
            );

            // when
            assertThatThrownBy(() -> new MessageDefinition(NAME, fields, RESERVED))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("is reserved");
        }
    }

    private static ScalarFieldDefinition scalarField(String name, int number, String protoType) {
        return ScalarFieldDefinition.create(name, number, protoType, false).orElseThrow();
    }

    private static EnumerationFieldDefinition enumerationField(String name, int number, TypeName type) {
        return EnumerationFieldDefinition.create(name, number, type, false);
    }
}