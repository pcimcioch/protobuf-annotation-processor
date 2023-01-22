package com.github.pcimcioch.protobuf.model;

import com.github.pcimcioch.protobuf.code.TypeName;
import com.github.pcimcioch.protobuf.model.field.FieldDefinition;
import com.github.pcimcioch.protobuf.model.field.FieldRules;
import com.github.pcimcioch.protobuf.model.message.EnumerationDefinition;
import com.github.pcimcioch.protobuf.model.message.EnumerationElementDefinition;
import com.github.pcimcioch.protobuf.model.message.MessageDefinition;
import com.github.pcimcioch.protobuf.model.message.ReservedDefinition;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static com.github.pcimcioch.protobuf.code.TypeName.canonicalName;
import static com.github.pcimcioch.protobuf.model.field.FieldDefinition.scalar;
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
        List<ProtoDefinitionsWrapper> wrappers = asList(
                wrapper("wrapper.com.example.MyType"),
                wrapper("wrapper.com.example.MyType2"),
                wrapper("wrapper.com.example2.MyType"),
                wrapper("wrapper.com.example.Parent.MyType")
        );

        // when then
        assertThatCode(() -> new ProtoDefinitions(messages, enumerations, wrappers))
                .doesNotThrowAnyException();
    }

    @Test
    void empty() {
        // when then
        assertThatCode(() -> new ProtoDefinitions(emptyList(), emptyList(), emptyList()))
                .doesNotThrowAnyException();
    }

    @Test
    void nullMessages() {
        // when then
        assertThatThrownBy(() -> new ProtoDefinitions(null, emptyList(), emptyList()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Messages can be empty, but not null");
    }

    @Test
    void nullEnumerations() {
        // when then
        assertThatThrownBy(() -> new ProtoDefinitions(emptyList(), null, emptyList()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Enumerations can be empty, but not null");
    }

    @Test
    void nullWrappers() {
        // when then
        assertThatThrownBy(() -> new ProtoDefinitions(emptyList(), emptyList(), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Wrappers can be empty, but not null");
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
        assertThatThrownBy(() -> new ProtoDefinitions(messages, emptyList(), emptyList()))
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
        assertThatThrownBy(() -> new ProtoDefinitions(emptyList(), enumerations, emptyList()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Duplicated enumeration name: com.example.MyType");
    }

    @Test
    void duplicatedWrapperName() {
        // given
        List<ProtoDefinitionsWrapper> wrappers = asList(
                wrapper("com.example.MyType"),
                wrapper("com.example.OtherType"),
                wrapper("com.example.MyType")
        );

        // when then
        assertThatThrownBy(() -> new ProtoDefinitions(emptyList(), emptyList(), wrappers))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Duplicated wrapper name: com.example.MyType");
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
        assertThatThrownBy(() -> new ProtoDefinitions(messages, enumerations, emptyList()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Duplicated enumeration name: com.example.MyType");
    }

    @Test
    void duplicatedNameAmongMessageAndWrapper() {
        // given
        List<MessageDefinition> messages = singletonList(
                message("com.example.MyType")
        );
        List<ProtoDefinitionsWrapper> wrappers = singletonList(
                wrapper("com.example.MyType")
        );

        // when then
        assertThatThrownBy(() -> new ProtoDefinitions(messages, emptyList(), wrappers))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Duplicated wrapper name: com.example.MyType");
    }

    @Test
    void duplicatedNameAmongEnumerationAndWrapper() {
        // given
        List<EnumerationDefinition> enumerations = singletonList(
                enumeration("com.example.MyType")
        );
        List<ProtoDefinitionsWrapper> wrappers = singletonList(
                wrapper("com.example.MyType")
        );

        // when then
        assertThatThrownBy(() -> new ProtoDefinitions(emptyList(), enumerations, wrappers))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Duplicated wrapper name: com.example.MyType");
    }

    @Test
    void nullMessage() {
        // given
        List<MessageDefinition> messages = asList(
                message("com.example.MyType"),
                null
        );

        // when then
        assertThatThrownBy(() -> new ProtoDefinitions(messages, emptyList(), emptyList()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Null message");
    }

    @Test
    void nullEnumeration() {
        // given
        List<EnumerationDefinition> enumerations = asList(
                enumeration("enum.com.example.MyType"),
                null
        );

        // when then
        assertThatThrownBy(() -> new ProtoDefinitions(emptyList(), enumerations, emptyList()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Null enumeration");
    }

    @Test
    void nullWrapper() {
        // given
        List<ProtoDefinitionsWrapper> wrappers = asList(
                wrapper("enum.com.example.MyType"),
                null
        );

        // when then
        assertThatThrownBy(() -> new ProtoDefinitions(emptyList(), emptyList(), wrappers))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Null wrapper");
    }

    private static MessageDefinition message(String typeCanonical) {
        TypeName type = canonicalName(typeCanonical);
        List<FieldDefinition> fields = singletonList(
                scalar("name1", 1, "bool", new FieldRules(false, false))
        );
        ReservedDefinition nothingReserved = new ReservedDefinition(Set.of(), Set.of(), Set.of());

        return new MessageDefinition(type, fields, nothingReserved, emptyList(), emptyList());
    }

    private static EnumerationDefinition enumeration(String typeCanonical) {
        TypeName type = canonicalName(typeCanonical);
        List<EnumerationElementDefinition> elements = singletonList(
                new EnumerationElementDefinition("TEST", 0)
        );
        ReservedDefinition nothingReserved = new ReservedDefinition(Set.of(), Set.of(), Set.of());

        return new EnumerationDefinition(type, elements, false, nothingReserved);
    }

    private static ProtoDefinitionsWrapper wrapper(String typeCanonical) {
        TypeName type = canonicalName(typeCanonical);

        return new ProtoDefinitionsWrapper(type, new ProtoDefinitions(emptyList(), emptyList(), emptyList()));
    }
}