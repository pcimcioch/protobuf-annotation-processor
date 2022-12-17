package com.protobuf.model;

import org.junit.jupiter.api.Test;

import static com.protobuf.model.SimpleEnum.FIRST;
import static com.protobuf.model.SimpleEnum.SECOND;
import static com.protobuf.model.SimpleEnum.THIRD;
import static com.protobuf.model.SimpleEnum.UNRECOGNIZED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class EnumTest {

    @Test
    void simpleEnum() {
        // when
        SimpleEnumMessage enumeration = new SimpleEnumMessage(0);

        // then
        assertThat(enumeration.orderValue()).isEqualTo(0);
        assertThat(enumeration.order()).isEqualTo(FIRST);
    }

    @Test
    void simpleEnumDefault() {
        // when
        SimpleEnumMessage enumeration = SimpleEnumMessage.empty();

        // then
        assertThat(enumeration.orderValue()).isEqualTo(0);
        assertThat(enumeration.order()).isEqualTo(FIRST);
    }

    @Test
    void simpleEnumBuilderValue() {
        // when
        SimpleEnumMessage enumeration = SimpleEnumMessage.builder()
                .orderValue(1)
                .build();

        // then
        assertThat(enumeration.orderValue()).isEqualTo(1);
        assertThat(enumeration.order()).isEqualTo(SECOND);
    }

    @Test
    void simpleEnumBuilderEnum() {
        // when
        SimpleEnumMessage enumeration = SimpleEnumMessage.builder()
                .order(THIRD)
                .build();

        // then
        assertThat(enumeration.orderValue()).isEqualTo(2);
        assertThat(enumeration.order()).isEqualTo(THIRD);
    }

    @Test
    void simpleEnumBuilderUnknownValue() {
        // when
        SimpleEnumMessage enumeration = SimpleEnumMessage.builder()
                .orderValue(10)
                .build();

        // then
        assertThat(enumeration.orderValue()).isEqualTo(10);
        assertThat(enumeration.order()).isEqualTo(UNRECOGNIZED);
    }

    @Test
    void simpleEnumBuilderUnknownEnum() {
        // when
        Throwable thrown = catchThrowable(() -> SimpleEnumMessage.builder().order(UNRECOGNIZED));

        // then
        assertThat(thrown)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Unrecognized enum does not have a number");
    }
}
