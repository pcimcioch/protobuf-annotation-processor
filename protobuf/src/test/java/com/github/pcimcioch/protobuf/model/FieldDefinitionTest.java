package com.github.pcimcioch.protobuf.model;

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
        assertThatCode(() -> new FieldDefinition(name, ScalarFieldType.BOOL, 1))
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
        assertThatThrownBy(() -> new FieldDefinition(name, ScalarFieldType.BOOL, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Incorrect field name");
    }

    @Test
    void nullFieldType() {
        // when then
        assertThatThrownBy(() -> new FieldDefinition("name", null, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Must provide field type");
    }

    @Test
    void negativeNumber() {
        // when then
        assertThatThrownBy(() -> new FieldDefinition("name", ScalarFieldType.BOOL, -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Number must be positive");
    }

    @Test
    void zeroNumber() {
        // when then
        assertThatThrownBy(() -> new FieldDefinition("name", ScalarFieldType.BOOL, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Number must be positive");
    }
}