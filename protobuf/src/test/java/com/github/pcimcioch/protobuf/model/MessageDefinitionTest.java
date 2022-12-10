package com.github.pcimcioch.protobuf.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.github.pcimcioch.protobuf.model.TypeName.canonicalName;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MessageDefinitionTest {

    @Test
    void correctMessage() {
        // given
        TypeName name = canonicalName("com.example.MyType");
        FieldDefinition field1 = scalarField("test1", 1, "bool");
        FieldDefinition field2 = scalarField("test2", 2, "int32");
        FieldDefinition field3 = scalarField("test3", 3, "string");

        // when
        assertThatCode(() -> new MessageDefinition(name, List.of(field1, field2, field3)))
                .doesNotThrowAnyException();
    }

    @Test
    void nullName() {
        // given
        FieldDefinition field = scalarField("test", 1, "bool");

        // when then
        assertThatThrownBy(() -> new MessageDefinition(null, List.of(field)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Message name cannot be null");
    }

    @Test
    void nullFields() {
        // given
        TypeName name = canonicalName("com.example.MyType");

        // when then
        assertThatThrownBy(() -> new MessageDefinition(name, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Message must have at least one field");
    }

    @Test
    void emptyFields() {
        // given
        TypeName name = canonicalName("com.example.MyType");

        // when then
        assertThatThrownBy(() -> new MessageDefinition(name, List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Message must have at least one field");
    }

    @Test
    void duplicatedFieldName() {
        // given
        TypeName name = canonicalName("com.example.MyType");
        FieldDefinition field1 = scalarField("test", 1, "bool");
        FieldDefinition field2 = scalarField("test2", 2, "int32");
        FieldDefinition field3 = scalarField("test", 3, "string");

        // when then
        assertThatThrownBy(() -> new MessageDefinition(name, List.of(field1, field2, field3)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Duplicated field name: test");
    }

    @Test
    void duplicatedEnumValueName() {
        // given
        TypeName name = canonicalName("com.example.MyType");
        FieldDefinition field1 = scalarField("testValue", 1, "bool");
        FieldDefinition field2 = enumerationField("test", 2, canonicalName("com.example.TestEnum"));

        // when then
        assertThatThrownBy(() -> new MessageDefinition(name, List.of(field1, field2)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Duplicated field name: testValue");
    }

    @Test
    void duplicatedFieldNumbers() {
        // given
        TypeName name = canonicalName("com.example.MyType");
        FieldDefinition field1 = scalarField("test1", 1, "bool");
        FieldDefinition field2 = scalarField("test2", 2, "int32");
        FieldDefinition field3 = scalarField("test3", 1, "string");

        // when then
        assertThatThrownBy(() -> new MessageDefinition(name, List.of(field1, field2, field3)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Duplicated field number: 1");
    }

    @Test
    void nullField() {
        // given
        TypeName name = canonicalName("com.example.MyType");
        List<FieldDefinition> fields = new ArrayList<>();
        fields.add(scalarField("test1", 1, "bool"));
        fields.add(scalarField("test2", 2, "int32"));
        fields.add(null);

        // when
        assertThatThrownBy(() -> new MessageDefinition(name, fields))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Null field");
    }

    private static ScalarFieldDefinition scalarField(String name, int number, String protoType) {
        return ScalarFieldDefinition.create(name, number, protoType).orElseThrow();
    }

    private static EnumerationFieldDefinition enumerationField(String name, int number, TypeName type) {
        return EnumerationFieldDefinition.create(name, number, type);
    }
}