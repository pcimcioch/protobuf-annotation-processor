package com.protobuf.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MultipleFilesTest {

    @Test
    void nestedFiles() {
        // given
        Multiple.MultipleEnum enumeration = Multiple.MultipleEnum.TEST;
        Multiple.MultipleMessage message = new Multiple.MultipleMessage(100, 0);

        // when then
        assertThat(message.intField()).isEqualTo(100);
        assertThat(message.enumField()).isEqualTo(enumeration);
    }
}
