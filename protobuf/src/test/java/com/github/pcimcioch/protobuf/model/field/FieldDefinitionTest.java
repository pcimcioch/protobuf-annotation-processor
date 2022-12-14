package com.github.pcimcioch.protobuf.model.field;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FieldDefinitionTest {

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
        assertThatCode(() -> FieldDefinition.scalar(name, 1, "bool", false))
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
        assertThatThrownBy(() -> FieldDefinition.scalar(name, 1, "bool", false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Incorrect field name");
    }

    @Test
    void negativeNumber() {
        // when then
        assertThatThrownBy(() -> FieldDefinition.scalar("name", -1, "bool", false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Number must be positive");
    }

    @Test
    void zeroNumber() {
        // when then
        assertThatThrownBy(() -> FieldDefinition.scalar("name", 0, "bool", false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Number must be positive");
    }

    @Test
    void nullEnumType() {
        // when then
        assertThatThrownBy(() -> FieldDefinition.enumeration("name", 1, null, false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Must provide field type");
    }

    @Test
    void nullMessageType() {
        // when then
        assertThatThrownBy(() -> FieldDefinition.message("name", 1, null, false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Must provide field type");
    }
}
