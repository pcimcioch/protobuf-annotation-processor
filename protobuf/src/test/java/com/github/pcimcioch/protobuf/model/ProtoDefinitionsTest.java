package com.github.pcimcioch.protobuf.model;

import com.github.pcimcioch.protobuf.model.field.ScalarFieldDefinition;
import com.github.pcimcioch.protobuf.model.message.EnumerationDefinition;
import com.github.pcimcioch.protobuf.model.message.EnumerationElementDefinition;
import com.github.pcimcioch.protobuf.model.message.MessageDefinition;
import com.github.pcimcioch.protobuf.model.message.ReservedDefinition;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.github.pcimcioch.protobuf.model.type.TypeName.canonicalName;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProtoDefinitionsTest {

    private static final ReservedDefinition NO_RESERVED = new ReservedDefinition(Set.of(), Set.of(), Set.of());

    @Test
    void correctDefinition() {
        // given
        MessageDefinition message1 = new MessageDefinition(canonicalName("com.example.MyType"), List.of(
                scalarField("name1", 1)
        ), NO_RESERVED);
        MessageDefinition message2 = new MessageDefinition(canonicalName("com.example.MyType2"), List.of(
                scalarField("name1", 1)
        ), NO_RESERVED);
        MessageDefinition message3 = new MessageDefinition(canonicalName("com.example2.MyType"), List.of(
                scalarField("name1", 1)
        ), NO_RESERVED);
        MessageDefinition message4 = new MessageDefinition(canonicalName("com.example.Parent.MyType"), List.of(
                scalarField("name1", 1)
        ), NO_RESERVED);
        EnumerationDefinition enum1 = new EnumerationDefinition(canonicalName("enum.com.example.MyType"), List.of(
                new EnumerationElementDefinition("TEST", 0)
        ), false, NO_RESERVED);
        EnumerationDefinition enum2 = new EnumerationDefinition(canonicalName("enum.com.example.MyType2"), List.of(
                new EnumerationElementDefinition("TEST", 0)
        ), false, NO_RESERVED);
        EnumerationDefinition enum3 = new EnumerationDefinition(canonicalName("enum.com.example2.MyType"), List.of(
                new EnumerationElementDefinition("TEST", 0)
        ), false, NO_RESERVED);
        EnumerationDefinition enum4 = new EnumerationDefinition(canonicalName("enum.com.example.Parent.MyType"), List.of(
                new EnumerationElementDefinition("TEST", 0)
        ), false, NO_RESERVED);

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
                scalarField("name1", 1)
        ), NO_RESERVED);
        MessageDefinition message2 = new MessageDefinition(canonicalName("com.example.OtherType"), List.of(
                scalarField("name2", 2)
        ), NO_RESERVED);
        MessageDefinition message3 = new MessageDefinition(canonicalName("com.example.MyType"), List.of(
                scalarField("name3", 1)
        ), NO_RESERVED);

        // when then
        assertThatThrownBy(() -> new ProtoDefinitions(List.of(message1, message2, message3), List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Duplicated message name: com.example.MyType");
    }

    @Test
    void duplicatedEnumName() {
        // given
        EnumerationDefinition enum1 = new EnumerationDefinition(canonicalName("com.example.MyType"), List.of(
                new EnumerationElementDefinition("TEST", 0)
        ), false, NO_RESERVED);
        EnumerationDefinition enum2 = new EnumerationDefinition(canonicalName("com.example.OtherType"), List.of(
                new EnumerationElementDefinition("TEST", 0)
        ), false, NO_RESERVED);
        EnumerationDefinition enum3 = new EnumerationDefinition(canonicalName("com.example.MyType"), List.of(
                new EnumerationElementDefinition("TEST", 0)
        ), false, NO_RESERVED);

        // when then
        assertThatThrownBy(() -> new ProtoDefinitions(List.of(), List.of(enum1, enum2, enum3)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Duplicated enumeration name: com.example.MyType");
    }

    @Test
    void duplicatedNameAmongMessageAndEnum() {
        // given
        MessageDefinition message1 = new MessageDefinition(canonicalName("com.example.MyType"), List.of(
                scalarField("name1", 1)
        ), NO_RESERVED);
        EnumerationDefinition enum1 = new EnumerationDefinition(canonicalName("com.example.MyType"), List.of(
                new EnumerationElementDefinition("TEST", 0)
        ), false, NO_RESERVED);

        // when then
        assertThatThrownBy(() -> new ProtoDefinitions(List.of(message1), List.of(enum1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Duplicated enumeration name: com.example.MyType");
    }

    @Test
    void nullMessage() {
        // given
        List<MessageDefinition> messages = new ArrayList<>();
        List<EnumerationDefinition> enumerations = new ArrayList<>();

        messages.add(new MessageDefinition(canonicalName("com.example.MyType"), List.of(
                scalarField("name1", 1)
        ), NO_RESERVED));
        messages.add(null);
        enumerations.add(new EnumerationDefinition(canonicalName("enum.com.example.MyType"), List.of(
                new EnumerationElementDefinition("TEST", 0)
        ), false, NO_RESERVED));
        enumerations.add(new EnumerationDefinition(canonicalName("enum.com.example.MyType2"), List.of(
                new EnumerationElementDefinition("TEST", 0)
        ), false, NO_RESERVED));

        // when then
        assertThatThrownBy(() -> new ProtoDefinitions(messages, enumerations))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Null message");
    }

    @Test
    void nullEnumeration() {
        // given
        List<MessageDefinition> messages = new ArrayList<>();
        List<EnumerationDefinition> enumerations = new ArrayList<>();

        messages.add(new MessageDefinition(canonicalName("com.example.MyType"), List.of(
                scalarField("name1", 1)
        ), NO_RESERVED));
        messages.add(new MessageDefinition(canonicalName("com.example.MyType2"), List.of(
                scalarField("name1", 1)
        ), NO_RESERVED));
        enumerations.add(new EnumerationDefinition(canonicalName("enum.com.example.MyType"), List.of(
                new EnumerationElementDefinition("TEST", 0)
        ), false, NO_RESERVED));
        enumerations.add(null);

        // when then
        assertThatThrownBy(() -> new ProtoDefinitions(messages, enumerations))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Null enumeration");
    }

    private static ScalarFieldDefinition scalarField(String test1, int number) {
        return ScalarFieldDefinition.create(test1, number, "bool", false).orElseThrow();
    }
}