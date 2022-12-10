package com.protobuf.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class NullabilityTest {

    @Test
    void scalarTypeString() {
        // when
        Throwable thrown = catchThrowable(() -> FullRecord.builder().string(null).build());

        // then
        assertThat(thrown).isInstanceOf(NullPointerException.class);
    }

    @Test
    void scalarTypeByteArray() {
        // when
        Throwable thrown = catchThrowable(() -> FullRecord.builder().bytes(null).build());

        // then
        assertThat(thrown).isInstanceOf(NullPointerException.class);
    }
}
