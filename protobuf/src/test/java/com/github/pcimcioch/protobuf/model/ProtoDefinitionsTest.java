package com.github.pcimcioch.protobuf.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.pcimcioch.protobuf.model.TypeName.canonicalName;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class ProtoDefinitionsTest {

    @Test
    void correctDefinition() {
        // given
        MessageDefinition message1 = new MessageDefinition(canonicalName("com.example.MyType"), List.of(
                new FieldDefinition("name1", ScalarFieldType.BOOL, 0)
        ));
        MessageDefinition message2 = new MessageDefinition(canonicalName("com.example.MyType2"), List.of(
                new FieldDefinition("name1", ScalarFieldType.BOOL, 0)
        ));
        MessageDefinition message3 = new MessageDefinition(canonicalName("com.example2.MyType"), List.of(
                new FieldDefinition("name1", ScalarFieldType.BOOL, 0)
        ));
        MessageDefinition message4 = new MessageDefinition(canonicalName("com.example.Parent.MyType"), List.of(
                new FieldDefinition("name1", ScalarFieldType.BOOL, 0)
        ));

        // when then
        assertThatCode(() -> new ProtoDefinitions(List.of(message1, message2, message3, message4)))
                .doesNotThrowAnyException();
    }

    @Test
    void emptyMessages() {
        // when then
        assertThatCode(() -> new ProtoDefinitions(List.of()))
                .doesNotThrowAnyException();
    }

    @Test
    void nullMessages() {
        // when then
        assertThatThrownBy(() -> new ProtoDefinitions(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Message can be empty, but not null");
    }

    @Test
    void duplicatedMessageName() {
        // given
        MessageDefinition message1 = new MessageDefinition(canonicalName("com.example.MyType"), List.of(
                new FieldDefinition("name1", ScalarFieldType.BOOL, 0)
        ));
        MessageDefinition message2 = new MessageDefinition(canonicalName("com.example.OtherType"), List.of(
                new FieldDefinition("name2", ScalarFieldType.BOOL, 1)
        ));
        MessageDefinition message3 = new MessageDefinition(canonicalName("com.example.MyType"), List.of(
                new FieldDefinition("name3", ScalarFieldType.BOOL, 0)
        ));

        // when then
        assertThatThrownBy(() -> new ProtoDefinitions(List.of(message1, message2, message3)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Duplicated message name: com.example.MyType");
    }
}