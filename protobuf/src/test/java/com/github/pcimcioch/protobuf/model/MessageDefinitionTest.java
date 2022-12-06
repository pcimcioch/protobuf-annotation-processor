package com.github.pcimcioch.protobuf.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.pcimcioch.protobuf.model.TypeName.canonicalName;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MessageDefinitionTest {

    @Test
    void correctMessage() {
        // given
        TypeName name = canonicalName("com.example.MyType");
        FieldDefinition field1 = new FieldDefinition("test1", ScalarFieldType.BOOL, 1);
        FieldDefinition field2 = new FieldDefinition("test2", ScalarFieldType.INT32, 2);
        FieldDefinition field3 = new FieldDefinition("test3", ScalarFieldType.STRING, 3);

        // when
        assertThatCode(() -> new MessageDefinition(name, List.of(field1, field2, field3)))
                .doesNotThrowAnyException();
    }

    @Test
    void nullName() {
        // given
        FieldDefinition field = new FieldDefinition("test", ScalarFieldType.BOOL, 1);

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
        FieldDefinition field1 = new FieldDefinition("test", ScalarFieldType.BOOL, 1);
        FieldDefinition field2 = new FieldDefinition("test2", ScalarFieldType.INT32, 2);
        FieldDefinition field3 = new FieldDefinition("test", ScalarFieldType.STRING, 3);

        // when then
        assertThatThrownBy(() -> new MessageDefinition(name, List.of(field1, field2, field3)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Duplicated field name: test");
    }

    @Test
    void duplicatedFieldNumbers() {
        // given
        TypeName name = canonicalName("com.example.MyType");
        FieldDefinition field1 = new FieldDefinition("test1", ScalarFieldType.BOOL, 1);
        FieldDefinition field2 = new FieldDefinition("test2", ScalarFieldType.INT32, 2);
        FieldDefinition field3 = new FieldDefinition("test3", ScalarFieldType.STRING, 1);

        // when then
        assertThatThrownBy(() -> new MessageDefinition(name, List.of(field1, field2, field3)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Duplicated field number: 1");
    }
}