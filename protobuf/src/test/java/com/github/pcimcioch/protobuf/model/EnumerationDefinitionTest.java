package com.github.pcimcioch.protobuf.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.github.pcimcioch.protobuf.model.TypeName.canonicalName;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EnumerationDefinitionTest {

    @Test
    void correctEnumeration() {
        // given
        TypeName name = canonicalName("com.example.MyType");
        EnumerationElementDefinition element1 = new EnumerationElementDefinition("TEST1", 0);
        EnumerationElementDefinition element2 = new EnumerationElementDefinition("TEST2", 1);
        EnumerationElementDefinition element3 = new EnumerationElementDefinition("TEST3", 2);

        // when
        assertThatCode(() -> new EnumerationDefinition(name, false, List.of(element1, element2, element3)))
                .doesNotThrowAnyException();
    }

    @Test
    void nullName() {
        // given
        EnumerationElementDefinition element = new EnumerationElementDefinition("TEST", 0);

        // when then
        assertThatThrownBy(() -> new EnumerationDefinition(null, false, List.of(element)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Enum name cannot be null");
    }

    @Test
    void nullFields() {
        // given
        TypeName name = canonicalName("com.example.MyType");

        // when then
        assertThatThrownBy(() -> new EnumerationDefinition(name, false, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Enum must have at least one element");
    }

    @Test
    void emptyFields() {
        // given
        TypeName name = canonicalName("com.example.MyType");

        // when then
        assertThatThrownBy(() -> new EnumerationDefinition(name, false, List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Enum must have at least one element");
    }

    @Test
    void duplicatedElementName() {
        // given
        TypeName name = canonicalName("com.example.MyType");
        EnumerationElementDefinition element1 = new EnumerationElementDefinition("TEST", 0);
        EnumerationElementDefinition element2 = new EnumerationElementDefinition("TEST2", 1);
        EnumerationElementDefinition element3 = new EnumerationElementDefinition("TEST", 2);

        // when then
        assertThatThrownBy(() -> new EnumerationDefinition(name, false, List.of(element1, element2, element3)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Duplicated element name: TEST");
    }

    @Test
    void duplicatedFieldNumbersWithoutAllowAlias() {
        // given
        TypeName name = canonicalName("com.example.MyType");
        EnumerationElementDefinition element1 = new EnumerationElementDefinition("TEST1", 0);
        EnumerationElementDefinition element2 = new EnumerationElementDefinition("TEST2", 1);
        EnumerationElementDefinition element3 = new EnumerationElementDefinition("TEST3", 1);

        // when then
        assertThatThrownBy(() -> new EnumerationDefinition(name, false, List.of(element1, element2, element3)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Duplicated element number: 1");
    }

    @Test
    void duplicatedFieldNumbersWithAllowAlias() {
        // given
        TypeName name = canonicalName("com.example.MyType");
        EnumerationElementDefinition element1 = new EnumerationElementDefinition("TEST1", 0);
        EnumerationElementDefinition element2 = new EnumerationElementDefinition("TEST2", 1);
        EnumerationElementDefinition element3 = new EnumerationElementDefinition("TEST3", 1);

        // when then
        assertThatCode(() -> new EnumerationDefinition(name, true, List.of(element1, element2, element3)))
                .doesNotThrowAnyException();
    }

    @Test
    void noZeroNumber() {
        // given
        TypeName name = canonicalName("com.example.MyType");
        EnumerationElementDefinition element1 = new EnumerationElementDefinition("TEST1", 1);
        EnumerationElementDefinition element2 = new EnumerationElementDefinition("TEST2", 2);
        EnumerationElementDefinition element3 = new EnumerationElementDefinition("TEST3", 3);

        // when then
        assertThatThrownBy(() -> new EnumerationDefinition(name, false, List.of(element1, element2, element3)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Enum must contain element with number 0");
    }

    @Test
    void nullElement() {
        // given
        TypeName name = canonicalName("com.example.MyType");
        List<EnumerationElementDefinition> elements = new ArrayList<>();
        elements.add(new EnumerationElementDefinition("TEST1", 0));
        elements.add(new EnumerationElementDefinition("TEST2", 1));
        elements.add(null);

        // when
        assertThatThrownBy(() -> new EnumerationDefinition(name, false, elements))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Null element in enumeration");
    }
}