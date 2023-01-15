package com.protobuf.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DuplicatedNamesTest {

    @Test
    void duplicatedNamesTest() {
        // given
        com.protobuf.model.one.NameOne leftUp = com.protobuf.model.one.NameOne.empty();
        com.protobuf.model.two.NameOne rightUp = com.protobuf.model.two.NameOne.empty();
        com.protobuf.model.one.NameOne.NameTwo leftDown = com.protobuf.model.one.NameOne.NameTwo.empty();
        com.protobuf.model.two.NameOne.NameTwo rightDown = com.protobuf.model.two.NameOne.NameTwo.empty();

        // when then
        assertThat(leftUp.leftUp()).isEqualTo(0);
        assertThat(leftUp.leftDown()).isEqualTo(leftDown);
        assertThat(leftUp.rightUp()).isEqualTo(rightUp);
        assertThat(leftUp.rightDown()).isEqualTo(rightDown);

        assertThat(rightUp.leftUp()).isEqualTo(leftUp);
        assertThat(rightUp.leftDown()).isEqualTo(leftDown);
        assertThat(rightUp.rightUp()).isEqualTo(0);
        assertThat(rightUp.rightDown()).isEqualTo(rightDown);

        assertThat(leftDown.leftUp()).isEqualTo(leftUp);
        assertThat(leftDown.leftDown()).isEqualTo(0);
        assertThat(leftDown.rightUp()).isEqualTo(rightUp);
        assertThat(leftDown.rightDown()).isEqualTo(rightDown);

        assertThat(rightDown.leftUp()).isEqualTo(leftUp);
        assertThat(rightDown.leftDown()).isEqualTo(leftDown);
        assertThat(rightDown.rightUp()).isEqualTo(rightUp);
        assertThat(rightDown.rightDown()).isEqualTo(0);
    }
}
