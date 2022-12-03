package com.github.pcimcioch.protobuf.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.pcimcioch.protobuf.model.TypeName.canonicalName;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProtoDefinitionsTest {

    @Test
    void correctDefinition() {
        // given
        MessageDefinition message1 = new MessageDefinition(canonicalName("com.example.MyType"), List.of(
                new FieldDefinition("name1", ScalarFieldType.BOOL, 1)
        ));
        MessageDefinition message2 = new MessageDefinition(canonicalName("com.example.MyType2"), List.of(
                new FieldDefinition("name1", ScalarFieldType.BOOL, 1)
        ));
        MessageDefinition message3 = new MessageDefinition(canonicalName("com.example2.MyType"), List.of(
                new FieldDefinition("name1", ScalarFieldType.BOOL, 1)
        ));
        MessageDefinition message4 = new MessageDefinition(canonicalName("com.example.Parent.MyType"), List.of(
                new FieldDefinition("name1", ScalarFieldType.BOOL, 1)
        ));
        EnumerationDefinition enum1 = new EnumerationDefinition(canonicalName("enum.com.example.MyType"), false, List.of(
                new EnumerationElementDefinition("TEST", 0)
        ));
        EnumerationDefinition enum2 = new EnumerationDefinition(canonicalName("enum.com.example.MyType2"), false, List.of(
                new EnumerationElementDefinition("TEST", 0)
        ));
        EnumerationDefinition enum3 = new EnumerationDefinition(canonicalName("enum.com.example2.MyType"), false, List.of(
                new EnumerationElementDefinition("TEST", 0)
        ));
        EnumerationDefinition enum4 = new EnumerationDefinition(canonicalName("enum.com.example.Parent.MyType"), false, List.of(
                new EnumerationElementDefinition("TEST", 0)
        ));

        // when then
        assertThatCode(() -> new ProtoDefinitions(
                List.of(message1, message2, message3, message4),
                List.of(enum1, enum2, enum3, enum4))
        ).doesNotThrowAnyException();
    }

    @Test
    void empty() {
        // when then
        assertThatCode(() -> new ProtoDefinitions(List.of(), List.of()))
                .doesNotThrowAnyException();
    }

    @Test
    void nullMessages() {
        // when then
        assertThatThrownBy(() -> new ProtoDefinitions(null, List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Messages can be empty, but not null");
    }

    @Test
    void nullEnumerations() {
        // when then
        assertThatThrownBy(() -> new ProtoDefinitions(List.of(), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Enumerations can be empty, but not null");
    }

    @Test
    void duplicatedMessageName() {
        // given
        MessageDefinition message1 = new MessageDefinition(canonicalName("com.example.MyType"), List.of(
                new FieldDefinition("name1", ScalarFieldType.BOOL, 1)
        ));
        MessageDefinition message2 = new MessageDefinition(canonicalName("com.example.OtherType"), List.of(
                new FieldDefinition("name2", ScalarFieldType.BOOL, 2)
        ));
        MessageDefinition message3 = new MessageDefinition(canonicalName("com.example.MyType"), List.of(
                new FieldDefinition("name3", ScalarFieldType.BOOL, 1)
        ));

        // when then
        assertThatThrownBy(() -> new ProtoDefinitions(List.of(message1, message2, message3), List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Duplicated message name: com.example.MyType");
    }

    @Test
    void duplicatedEnumName() {
        // given
        EnumerationDefinition enum1 = new EnumerationDefinition(canonicalName("com.example.MyType"), false, List.of(
                new EnumerationElementDefinition("TEST", 0)
        ));
        EnumerationDefinition enum2 = new EnumerationDefinition(canonicalName("com.example.OtherType"), false, List.of(
                new EnumerationElementDefinition("TEST", 0)
        ));
        EnumerationDefinition enum3 = new EnumerationDefinition(canonicalName("com.example.MyType"), false, List.of(
                new EnumerationElementDefinition("TEST", 0)
        ));

        // when then
        assertThatThrownBy(() -> new ProtoDefinitions(List.of(), List.of(enum1, enum2, enum3)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Duplicated enumeration name: com.example.MyType");
    }

    @Test
    void duplicatedNameAmongMessageAndEnum() {
        // given
        MessageDefinition message1 = new MessageDefinition(canonicalName("com.example.MyType"), List.of(
                new FieldDefinition("name1", ScalarFieldType.BOOL, 1)
        ));
        EnumerationDefinition enum1 = new EnumerationDefinition(canonicalName("com.example.MyType"), false, List.of(
                new EnumerationElementDefinition("TEST", 0)
        ));

        // when then
        assertThatThrownBy(() -> new ProtoDefinitions(List.of(message1), List.of(enum1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Duplicated enumeration name: com.example.MyType");
    }
}