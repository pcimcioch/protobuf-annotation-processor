package com.github.pcimcioch.protobuf.model.field;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.github.pcimcioch.protobuf.code.TypeName.canonicalName;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FieldDefinitionTest {

    private static final FieldRules NO_RULES = new FieldRules(false, false, false);

    @ParameterizedTest
    @ValueSource(strings = {
            "name",
            "_name",
            "nameLong123_",
            "t",
            "UPPER"
    })
    void correctNames(String name) {
        // when then
        assertThatCode(() -> FieldDefinition.scalar(name, 1, "bool", NO_RULES))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "1name",
            "special!",
            " "
    })
    @NullAndEmptySource
    void incorrectNames(String name) {
        // when then
        assertThatThrownBy(() -> FieldDefinition.scalar(name, 1, "bool", NO_RULES))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Incorrect name");
    }

    @Test
    void negativeNumber() {
        // when then
        assertThatThrownBy(() -> FieldDefinition.scalar("name", -1, "bool", NO_RULES))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Number must be positive");
    }

    @Test
    void zeroNumber() {
        // when then
        assertThatThrownBy(() -> FieldDefinition.scalar("name", 0, "bool", NO_RULES))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Number must be positive");
    }

    @Test
    void nullEnumType() {
        // when then
        assertThatThrownBy(() -> FieldDefinition.enumeration("name", 1, null, NO_RULES))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Must provide protobuf type");
    }

    @Test
    void nullMessageType() {
        // when then
        assertThatThrownBy(() -> FieldDefinition.message("name", 1, null, NO_RULES))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Must provide protobuf type");
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void nullRules() {
        // when then
        assertThatThrownBy(() -> FieldDefinition.scalar("name", 1, "bool", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Must provide rules");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "double",
            "float",
            "int32",
            "int64",
            "uint32",
            "uint64",
            "sint32",
            "sint64",
            "fixed32",
            "fixed64",
            "sfixed32",
            "sfixed64",
            "bool"
    })
    void primitiveScalarCanBePacked(String protoType) {
        // when then
        assertThatCode(() -> FieldDefinition.scalar("name", 1, protoType, new FieldRules(false, true, true)))
                .doesNotThrowAnyException();
    }

    @Test
    void enumCanBePacked() {
        // when then
        assertThatCode(() -> FieldDefinition.enumeration("name", 1, canonicalName("com.example.EnumTest"), new FieldRules(false, true, true)))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "string",
            "bytes"
    })
    void complexScalarCannotBePacked(String protoType) {
        // when then
        assertThatThrownBy(() -> FieldDefinition.scalar("name", 1, protoType, new FieldRules(false, true, true)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Only primitive types can be packed");
    }

    @Test
    void otherMessageCannotBePacked() {
        // when then
        assertThatThrownBy(() -> FieldDefinition.message("name", 1, canonicalName("com.example.MessageTest"), new FieldRules(false, true, true)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Only primitive types can be packed");
    }
}
