package com.github.pcimcioch.protobuf.model.message;

import com.github.pcimcioch.protobuf.code.TypeName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Set;

import static com.github.pcimcioch.protobuf.code.TypeName.canonicalName;
import static java.lang.Integer.MAX_VALUE;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EnumerationDefinitionTest {

    private static final TypeName NAME = canonicalName("com.example.MyType");
    private static final ReservedDefinition NO_RESERVED = new ReservedDefinition(Set.of(), Set.of(), Set.of());

    @Test
    void correctEnumeration() {
        // given
        List<EnumerationElementDefinition> elements = asList(
                new EnumerationElementDefinition("TEST1", 0),
                new EnumerationElementDefinition("TEST2", 1),
                new EnumerationElementDefinition("TEST3", 2)
        );

        // when
        assertThatCode(() -> new EnumerationDefinition(NAME, elements, false, NO_RESERVED))
                .doesNotThrowAnyException();
    }

    @Nested
    class NameValidation {

        @Test
        void nullName() {
            // given
            List<EnumerationElementDefinition> elements = singletonList(
                    new EnumerationElementDefinition("TEST", 0)
            );

            // when then
            assertThatThrownBy(() -> new EnumerationDefinition(null, elements, false, NO_RESERVED))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Enum name cannot be null");
        }
    }

    @Nested
    class ElementsValidation {

        @Test
        void nullFields() {
            // when then
            assertThatThrownBy(() -> new EnumerationDefinition(NAME, null, false, NO_RESERVED))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Enum must have at least one element");
        }

        @Test
        void emptyFields() {
            // when then
            assertThatThrownBy(() -> new EnumerationDefinition(NAME, emptyList(), false, NO_RESERVED))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Enum must have at least one element");
        }

        @Test
        void duplicatedElementName() {
            // given
            List<EnumerationElementDefinition> elements = asList(
                    new EnumerationElementDefinition("TEST", 0),
                    new EnumerationElementDefinition("TEST2", 1),
                    new EnumerationElementDefinition("TEST", 2)
            );

            // when then
            assertThatThrownBy(() -> new EnumerationDefinition(NAME, elements, false, NO_RESERVED))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Duplicated element name: TEST");
        }

        @Test
        void duplicatedFieldNumbersWithoutAllowAlias() {
            // given
            List<EnumerationElementDefinition> elements = asList(
                    new EnumerationElementDefinition("TEST1", 0),
                    new EnumerationElementDefinition("TEST2", 1),
                    new EnumerationElementDefinition("TEST3", 1)
            );

            // when then
            assertThatThrownBy(() -> new EnumerationDefinition(NAME, elements, false, NO_RESERVED))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Duplicated element number: 1");
        }

        @Test
        void duplicatedFieldNumbersWithAllowAlias() {
            // given
            List<EnumerationElementDefinition> elements = asList(
                    new EnumerationElementDefinition("TEST1", 0),
                    new EnumerationElementDefinition("TEST2", 1),
                    new EnumerationElementDefinition("TEST3", 1)
            );

            // when then
            assertThatCode(() -> new EnumerationDefinition(NAME, elements, true, NO_RESERVED))
                    .doesNotThrowAnyException();
        }

        @Test
        void noZeroNumber() {
            // given
            List<EnumerationElementDefinition> elements = asList(
                    new EnumerationElementDefinition("TEST1", 1),
                    new EnumerationElementDefinition("TEST2", 2),
                    new EnumerationElementDefinition("TEST3", 3)
            );

            // when then
            assertThatThrownBy(() -> new EnumerationDefinition(NAME, elements, false, NO_RESERVED))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Enum must contain element with number 0");
        }

        @Test
        void nullElement() {
            // given
            List<EnumerationElementDefinition> elements = asList(
                    new EnumerationElementDefinition("TEST1", 0),
                    new EnumerationElementDefinition("TEST2", 1),
                    null
            );

            // when
            assertThatThrownBy(() -> new EnumerationDefinition(NAME, elements, false, NO_RESERVED))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Null element in enumeration");
        }
    }

    @Nested
    class ReservedValidation {

        private static final ReservedDefinition RESERVED = new ReservedDefinition(
                Set.of("reserved1", "reserved2"),
                Set.of(100, 101, 102),
                Set.of(new ReservedDefinition.Range(200, 300), new ReservedDefinition.Range(1000, MAX_VALUE))
        );

        @ParameterizedTest
        @ValueSource(strings = {"name", "name1", "reserved", "reserved3"})
        void correctElementNames(String elementName) {
            // given
            List<EnumerationElementDefinition> elements = singletonList(
                    new EnumerationElementDefinition(elementName, 0)
            );

            // when
            assertThatCode(() -> new EnumerationDefinition(NAME, elements, false, RESERVED))
                    .doesNotThrowAnyException();
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 2, 99, 103, 199, 301, 999})
        void correctElementNumbers(int elementNumber) {
            // given
            List<EnumerationElementDefinition> elements = asList(
                    new EnumerationElementDefinition("first", 0),
                    new EnumerationElementDefinition("second", elementNumber)
            );

            // when
            assertThatCode(() -> new EnumerationDefinition(NAME, elements, false, RESERVED))
                    .doesNotThrowAnyException();
        }

        @ParameterizedTest
        @ValueSource(strings = {"reserved1", "reserved2"})
        void incorrectElementNames(String elementName) {
            // given
            List<EnumerationElementDefinition> elements = singletonList(
                    new EnumerationElementDefinition(elementName, 0)
            );

            // when
            assertThatThrownBy(() -> new EnumerationDefinition(NAME, elements, false, RESERVED))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("is reserved");
        }

        @ParameterizedTest
        @ValueSource(ints = {100, 101, 102, 200, 256, 300, 1000, 1234567, MAX_VALUE})
        void incorrectElementNumbers(int elementNumber) {
            // given
            List<EnumerationElementDefinition> elements = asList(
                    new EnumerationElementDefinition("first", 0),
                    new EnumerationElementDefinition("second", elementNumber)
            );

            // when
            assertThatThrownBy(() -> new EnumerationDefinition(NAME, elements, false, RESERVED))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("is reserved");
        }
    }
}