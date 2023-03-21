package com.protobuf.model;

import com.github.pcimcioch.protobuf.dto.EnumList;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.protobuf.model.RepeatableEnum.FIRST;
import static com.protobuf.model.RepeatableEnum.SECOND;
import static com.protobuf.model.RepeatableEnum.THIRD;
import static com.protobuf.model.RepeatableEnum.UNRECOGNIZED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;

class RepeatableEnumTest {

    @Test
    void defaultValues() {
        // when
        RepeatableEnumMessage model = RepeatableEnumMessage.empty();

        // then
        assertThat(model.orders()).isEmpty();
        assertThat(model.ordersValue()).isEmpty();
        assertThat(model.protobufSize()).isEqualTo(0);
    }

    @Test
    void singleElements() {
        // when
        RepeatableEnumMessage model = new RepeatableEnumMessage(EnumList.of(RepeatableEnum::forNumber, 1));

        // then
        assertThat(model.orders()).containsExactly(SECOND);
        assertThat(model.ordersValue()).containsExactly(1);
        assertThat(model.protobufSize()).isEqualTo(2);
    }

    @Test
    void multipleElements() {
        // when
        RepeatableEnumMessage model = new RepeatableEnumMessage(EnumList.of(RepeatableEnum::forNumber, 1, 2));

        // then
        assertThat(model.orders()).containsExactly(SECOND, THIRD);
        assertThat(model.ordersValue()).containsExactly(1, 2);
        assertThat(model.protobufSize()).isEqualTo(4);
    }

    @Test
    void modifyOutputList() {
        // given
        RepeatableEnumMessage model = new RepeatableEnumMessage(EnumList.of(RepeatableEnum::forNumber, 1));

        // when then
        assertThatThrownBy(() -> model.ordersValue().add(2))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> model.orders().add(THIRD))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void modifyBuilderParameters() {
        // given
        List<Integer> input1 = new ArrayList<>();
        input1.add(0);
        List<Integer> input2 = new ArrayList<>();
        input2.add(1);
        RepeatableEnumMessage.Builder modelBuilder = RepeatableEnumMessage.builder()
                .ordersValue(input1)
                .addAllOrdersValue(input2)
                .addOrdersValue(2);

        // when
        input1.add(2);
        input2.add(2);
        RepeatableEnumMessage model = modelBuilder.build();

        // then
        assertThat(model.ordersValue()).containsExactly(0, 1, 2);
        assertThat(model.protobufSize()).isEqualTo(6);
    }

    @Test
    void builderSetterOverride() {
        // when
        RepeatableEnumMessage model = RepeatableEnumMessage.builder()
                .addAllOrdersValue(Set.of(1, 2))
                .ordersValue(List.of(2, 1, 3, 3))
                .build();

        // then
        assertThat(model.ordersValue()).containsExactly(2, 1, 3, 3);
        assertThat(model.protobufSize()).isEqualTo(8);
    }

    @Test
    void builderSetter() {
        // when
        RepeatableEnumMessage model = RepeatableEnumMessage.builder()
                .addOrders(FIRST)
                .addOrdersValue(2)
                .addAllOrders(List.of(THIRD, SECOND))
                .addAllOrdersValue(List.of(0, 3))
                .build();

        // then
        assertThat(model.ordersValue()).containsExactly(0, 2, 2, 1, 0, 3);
        assertThat(model.protobufSize()).isEqualTo(12);
    }

    @Test
    void unknownValue() {
        // when
        RepeatableEnumMessage model = RepeatableEnumMessage.builder()
                .ordersValue(List.of(0, 10, 2))
                .build();

        // then
        assertThat(model.ordersValue()).containsExactly(0, 10, 2);
        assertThat(model.orders()).containsExactly(FIRST, UNRECOGNIZED, THIRD);
        assertThat(model.protobufSize()).isEqualTo(6);
    }

    @Test
    void setUnknown() {
        // when
        Throwable thrown = catchThrowable(() -> RepeatableEnumMessage.builder().orders(List.of(FIRST, UNRECOGNIZED)).build());

        // then
        assertThat(thrown)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Unrecognized enum does not have a number");
    }
}
