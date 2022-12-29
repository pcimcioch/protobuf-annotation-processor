package com.github.pcimcioch.protobuf.model;

import com.github.pcimcioch.protobuf.model.field.FieldDefinition;
import com.github.pcimcioch.protobuf.model.message.EnumerationDefinition;
import com.github.pcimcioch.protobuf.model.message.EnumerationElementDefinition;
import com.github.pcimcioch.protobuf.model.message.MessageDefinition;
import com.github.pcimcioch.protobuf.model.message.ReservedDefinition;
import com.github.pcimcioch.protobuf.model.type.TypeName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.github.pcimcioch.protobuf.model.field.FieldDefinition.scalar;
import static com.github.pcimcioch.protobuf.model.type.TypeName.canonicalName;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProtoDefinitionsTest {

    @Test
    void correctDefinition() {
        // given
        List<MessageDefinition> messages = asList(
                message("com.example.MyType"),
                message("com.example.MyType2"),
                message("com.example2.MyType"),
                message("com.example.Parent.MyType")
        );
        List<EnumerationDefinition> enumerations = asList(
                enumeration("enum.com.example.MyType"),
                enumeration("enum.com.example.MyType2"),
                enumeration("enum.com.example2.MyType"),
                enumeration("enum.com.example.Parent.MyType")
        );

        // when then
        assertThatCode(() -> new ProtoDefinitions(messages, enumerations))
                .doesNotThrowAnyException();
    }

    @Test
    void empty() {
        // when then
        assertThatCode(() -> new ProtoDefinitions(emptyList(), emptyList()))
                .doesNotThrowAnyException();
    }

    @Test
    void nullMessages() {
        // when then
        assertThatThrownBy(() -> new ProtoDefinitions(null, emptyList()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Messages can be empty, but not null");
    }

    @Test
    void nullEnumerations() {
        // when then
        assertThatThrownBy(() -> new ProtoDefinitions(emptyList(), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Enumerations can be empty, but not null");
    }

    @Test
    void duplicatedMessageName() {
        // given
        List<MessageDefinition> messages = asList(
                message("com.example.MyType"),
                message("com.example.OtherType"),
                message("com.example.MyType")
        );

        // when then
        assertThatThrownBy(() -> new ProtoDefinitions(messages, emptyList()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Duplicated message name: com.example.MyType");
    }

    @Test
    void duplicatedEnumName() {
        // given
        List<EnumerationDefinition> enumerations = asList(
                enumeration("com.example.MyType"),
                enumeration("com.example.OtherType"),
                enumeration("com.example.MyType")
        );

        // when then
        assertThatThrownBy(() -> new ProtoDefinitions(emptyList(), enumerations))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Duplicated enumeration name: com.example.MyType");
    }

    @Test
    void duplicatedNameAmongMessageAndEnum() {
        // given
        List<MessageDefinition> messages = singletonList(
                message("com.example.MyType")
        );
        List<EnumerationDefinition> enumerations = singletonList(
                enumeration("com.example.MyType")
        );

        // when then
        assertThatThrownBy(() -> new ProtoDefinitions(messages, enumerations))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Duplicated enumeration name: com.example.MyType");
    }

    @Test
    void nullMessage() {
        // given
        List<MessageDefinition> messages = new ArrayList<>();
        messages.add(message("com.example.MyType"));
        messages.add(null);

        List<EnumerationDefinition> enumerations = asList(
                enumeration("enum.com.example.MyType"),
                enumeration("enum.com.example.MyType2")
        );

        // when then
        assertThatThrownBy(() -> new ProtoDefinitions(messages, enumerations))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Null message");
    }

    @Test
    void nullEnumeration() {
        // given
        List<MessageDefinition> messages = asList(
                message("com.example.MyType"),
                message("com.example.MyType2")
        );

        List<EnumerationDefinition> enumerations = asList(
                enumeration("enum.com.example.MyType"),
                null
        );

        // when then
        assertThatThrownBy(() -> new ProtoDefinitions(messages, enumerations))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Null enumeration");
    }

    private static MessageDefinition message(String typeCanonical) {
        TypeName type = canonicalName(typeCanonical);
        List<FieldDefinition> fields = singletonList(
                scalar("name1", 1, "bool", false)
        );
        ReservedDefinition nothingReserved = new ReservedDefinition(Set.of(), Set.of(), Set.of());

        return new MessageDefinition(type, fields, nothingReserved);
    }

    private static EnumerationDefinition enumeration(String typeCanonical) {
        TypeName type = canonicalName(typeCanonical);
        List<EnumerationElementDefinition> elements = singletonList(
                new EnumerationElementDefinition("TEST", 0)
        );
        ReservedDefinition nothingReserved = new ReservedDefinition(Set.of(), Set.of(), Set.of());

        return new EnumerationDefinition(type, elements, false, nothingReserved);
    }
}