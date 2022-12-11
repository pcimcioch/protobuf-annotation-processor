package com.github.pcimcioch.protobuf.model.field;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.github.pcimcioch.protobuf.model.type.TypeName.canonicalName;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EnumerationFieldDefinitionTest {

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
        assertThatCode(() -> EnumerationFieldDefinition.create(name, 1, canonicalName("com.example.TestEnum"), false))
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
        assertThatThrownBy(() -> EnumerationFieldDefinition.create(name, 1, canonicalName("com.example.TestEnum"), false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Incorrect field name");
    }

    @Test
    void nullType() {
        // when then
        assertThatThrownBy(() -> EnumerationFieldDefinition.create("name", 1, null, false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Must provide enum type");
    }

    @Test
    void negativeNumber() {
        // when then
        assertThatThrownBy(() -> EnumerationFieldDefinition.create("name", -1, canonicalName("com.example.TestEnum"), false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Number must be positive");
    }

    @Test
    void zeroNumber() {
        // when then
        assertThatThrownBy(() -> EnumerationFieldDefinition.create("name", 0, canonicalName("com.example.TestEnum"), false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Number must be positive");
    }
}
