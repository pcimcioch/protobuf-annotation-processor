package com.github.pcimcioch.protobuf.model.field;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FieldRulesTest {

    @Test
    void packedButNotRepeated() {
        // when then
        assertThatThrownBy(() -> new FieldRules(false, false, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Only repeated fields can be packed");
    }
}