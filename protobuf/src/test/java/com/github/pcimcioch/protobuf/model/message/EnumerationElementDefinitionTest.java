package com.github.pcimcioch.protobuf.model.message;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EnumerationElementDefinitionTest {

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
        assertThatCode(() -> new EnumerationElementDefinition(name, 0))
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
        assertThatThrownBy(() -> new EnumerationElementDefinition(name, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Incorrect enum name");
    }

    @Test
    void reservedName() {
        // when then
        assertThatThrownBy(() -> new EnumerationElementDefinition("UNRECOGNIZED", 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Used restricted enum name");
    }
}