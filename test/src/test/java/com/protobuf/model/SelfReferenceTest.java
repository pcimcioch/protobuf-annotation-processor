package com.protobuf.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SelfReferenceTest {

    @Test
    void oneNode() {
        // when
        SelfReference first = new SelfReference(10, null);

        // then
        assertThat(first.value()).isEqualTo(10);
        assertThat(first.next()).isEqualTo(SelfReference.empty());
        assertThat(first.protobufSize()).isEqualTo(2);
    }

    @Test
    void multipleNodes() {
        // when
        SelfReference third = new SelfReference(30, null);
        SelfReference second = new SelfReference(20, third);
        SelfReference first = new SelfReference(10, second);

        // then
        assertThat(first.value()).isEqualTo(10);
        assertThat(first.next().value()).isEqualTo(20);
        assertThat(first.next().next().value()).isEqualTo(30);
        assertThat(first.next().next().next()).isEqualTo(SelfReference.empty());

        assertThat(first.protobufSize()).isEqualTo(10);
        assertThat(second.protobufSize()).isEqualTo(6);
        assertThat(third.protobufSize()).isEqualTo(2);
    }
}
