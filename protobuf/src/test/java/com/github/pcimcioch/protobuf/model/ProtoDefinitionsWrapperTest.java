package com.github.pcimcioch.protobuf.model;

import com.github.pcimcioch.protobuf.code.TypeName;
import com.github.pcimcioch.protobuf.model.field.FieldRules;
import com.github.pcimcioch.protobuf.model.message.EnumerationDefinition;
import com.github.pcimcioch.protobuf.model.message.EnumerationElementDefinition;
import com.github.pcimcioch.protobuf.model.message.MessageDefinition;
import com.github.pcimcioch.protobuf.model.message.ReservedDefinition;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.pcimcioch.protobuf.code.TypeName.canonicalName;
import static com.github.pcimcioch.protobuf.model.field.FieldDefinition.scalar;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProtoDefinitionsWrapperTest {

    @Test
    void correct() {
        // given
        TypeName name = canonicalName("com.example.Test");
        ProtoDefinitions definitions = new ProtoDefinitions(
                List.of(message("com.example.Test.Message")),
                List.of(enumeration("com.example.Test.Enumeration")),
                List.of(wrapper("com.example.Test.Wrapper"))
        );

        // when then
        assertThatCode(() -> new ProtoDefinitionsWrapper(name, definitions))
                .doesNotThrowAnyException();
    }

    @Test
    void nullName() {
        // given
        ProtoDefinitions definitions = new ProtoDefinitions(emptyList(), emptyList(), emptyList());

        // when then
        assertThatThrownBy(() -> new ProtoDefinitionsWrapper(null, definitions))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Wrapper name cannot be null");
    }

    @Test
    void nullDefinitions() {
        // given
        TypeName name = canonicalName("com.example.Test");

        // when then
        assertThatThrownBy(() -> new ProtoDefinitionsWrapper(name, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Definitions cannot be null");
    }

    @Test
    void incorrectNestedMessage() {
        // given
        TypeName name = canonicalName("com.example.Test");
        ProtoDefinitions definitions = new ProtoDefinitions(
                List.of(message("com.example.Test2.Message")),
                List.of(enumeration("com.example.Test.Enumeration")),
                List.of(wrapper("com.example.Test.Wrapper"))
        );

        // when then
        assertThatThrownBy(() -> new ProtoDefinitionsWrapper(name, definitions))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Message in wrapper has non-nested type");
    }

    @Test
    void incorrectNestedEnumeration() {
        // given
        TypeName name = canonicalName("com.example.Test");
        ProtoDefinitions definitions = new ProtoDefinitions(
                List.of(message("com.example.Test.Message")),
                List.of(enumeration("com.example.Test2.Enumeration")),
                List.of(wrapper("com.example.Test.Wrapper"))
        );

        // when then
        assertThatThrownBy(() -> new ProtoDefinitionsWrapper(name, definitions))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Enumeration in wrapper has non-nested type");
    }

    @Test
    void incorrectNestedWrapper() {
        // given
        TypeName name = canonicalName("com.example.Test");
        ProtoDefinitions definitions = new ProtoDefinitions(
                List.of(message("com.example.Test.Message")),
                List.of(enumeration("com.example.Test.Enumeration")),
                List.of(wrapper("com.example.Test2.Wrapper"))
        );

        // when then
        assertThatThrownBy(() -> new ProtoDefinitionsWrapper(name, definitions))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Wrapper in wrapper has non-nested type");
    }

    private MessageDefinition message(String name) {
        return new MessageDefinition(
                canonicalName(name),
                List.of(scalar("field", 1, "int32", new FieldRules(false, false))),
                new ReservedDefinition(emptySet(), emptySet(), emptySet()),
                emptyList(),
                emptyList()
        );
    }

    private EnumerationDefinition enumeration(String name) {
        return new EnumerationDefinition(
                canonicalName(name),
                List.of(new EnumerationElementDefinition("TEST", 0)),
                false,
                new ReservedDefinition(emptySet(), emptySet(), emptySet())
        );
    }

    private ProtoDefinitionsWrapper wrapper(String name) {
        return new ProtoDefinitionsWrapper(
                canonicalName(name),
                new ProtoDefinitions(emptyList(), emptyList(), emptyList())
        );
    }
}