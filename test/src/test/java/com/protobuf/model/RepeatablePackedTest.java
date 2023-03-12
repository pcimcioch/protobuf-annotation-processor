package com.protobuf.model;

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

class RepeatablePackedTest {

    @Test
    void defaultValues() {
        // when
        RepeatablePacked model = RepeatablePacked.empty();

        // then
        assertThat(model.doubles()).isEmpty();
        assertThat(model.floats()).isEmpty();
        assertThat(model.int32s()).isEmpty();
        assertThat(model.int64s()).isEmpty();
        assertThat(model.uint32s()).isEmpty();
        assertThat(model.uint64s()).isEmpty();
        assertThat(model.sint32s()).isEmpty();
        assertThat(model.sint64s()).isEmpty();
        assertThat(model.fixed32s()).isEmpty();
        assertThat(model.fixed64s()).isEmpty();
        assertThat(model.sfixed32s()).isEmpty();
        assertThat(model.sfixed64s()).isEmpty();
        assertThat(model.bools()).isEmpty();
        assertThat(model.orders()).isEmpty();
        assertThat(model.ordersValue()).isEmpty();
        assertThat(model.protobufSize()).isEqualTo(0);
    }

    @Test
    void singleElements() {
        // when
        RepeatablePacked model = new RepeatablePacked(
                List.of(10d),
                List.of(20f),
                List.of(30),
                List.of(40L),
                List.of(50),
                List.of(60L),
                List.of(70),
                List.of(80L),
                List.of(90),
                List.of(100L),
                List.of(110),
                List.of(120L),
                List.of(true),
                List.of(1)
        );

        // then
        assertThat(model.doubles()).containsExactly(10d);
        assertThat(model.floats()).containsExactly(20f);
        assertThat(model.int32s()).containsExactly(30);
        assertThat(model.int64s()).containsExactly(40L);
        assertThat(model.uint32s()).containsExactly(50);
        assertThat(model.uint64s()).containsExactly(60L);
        assertThat(model.sint32s()).containsExactly(70);
        assertThat(model.sint64s()).containsExactly(80L);
        assertThat(model.fixed32s()).containsExactly(90);
        assertThat(model.fixed64s()).containsExactly(100L);
        assertThat(model.sfixed32s()).containsExactly(110);
        assertThat(model.sfixed64s()).containsExactly(120L);
        assertThat(model.bools()).containsExactly(true);
        assertThat(model.orders()).containsExactly(SECOND);
        assertThat(model.ordersValue()).containsExactly(1);
        assertThat(model.protobufSize()).isEqualTo(74);
    }

    @Test
    void multipleElements() {
        // when
        RepeatablePacked model = new RepeatablePacked(
                List.of(10d, 11d),
                List.of(20f, 21f),
                List.of(30, 31),
                List.of(40L, 41L),
                List.of(50, 51),
                List.of(60L, 61L),
                List.of(70, 71),
                List.of(80L, 81L),
                List.of(90, 91),
                List.of(100L, 101L),
                List.of(110, 111),
                List.of(120L, 121L),
                List.of(true, false),
                List.of(1, 2)
        );

        // then
        assertThat(model.doubles()).containsExactly(10d, 11d);
        assertThat(model.floats()).containsExactly(20f, 21f);
        assertThat(model.int32s()).containsExactly(30, 31);
        assertThat(model.int64s()).containsExactly(40L, 41L);
        assertThat(model.uint32s()).containsExactly(50, 51);
        assertThat(model.uint64s()).containsExactly(60L, 61L);
        assertThat(model.sint32s()).containsExactly(70, 71);
        assertThat(model.sint64s()).containsExactly(80L, 81L);
        assertThat(model.fixed32s()).containsExactly(90, 91);
        assertThat(model.fixed64s()).containsExactly(100L, 101L);
        assertThat(model.sfixed32s()).containsExactly(110, 111);
        assertThat(model.sfixed64s()).containsExactly(120L, 121L);
        assertThat(model.bools()).containsExactly(true, false);
        assertThat(model.orders()).containsExactly(SECOND, THIRD);
        assertThat(model.ordersValue()).containsExactly(1, 2);
        assertThat(model.protobufSize()).isEqualTo(120);
    }

    @Test
    void modifyInputList() {
        // given
        List<Double> doubles = new ArrayList<>();
        List<Integer> enums = new ArrayList<>();
        doubles.add(10d);
        enums.add(1);
        RepeatablePacked model = new RepeatablePacked(doubles, List.of(), List.of(), List.of(), List.of(), List.of(),
                List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), enums);

        // when
        doubles.add(20d);
        enums.add(2);

        // then
        assertThat(model.doubles()).containsExactly(10d);
        assertThat(model.ordersValue()).containsExactly(1);
        assertThat(model.protobufSize()).isEqualTo(13);
    }

    @Test
    void modifyOutputList() {
        // given
        List<Double> doubles = new ArrayList<>();
        List<Integer> enums = new ArrayList<>();
        doubles.add(10d);
        enums.add(1);
        RepeatablePacked model = new RepeatablePacked(doubles, List.of(), List.of(), List.of(), List.of(), List.of(),
                List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), enums);

        // when then
        assertThatThrownBy(() -> model.doubles().add(20d))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> model.ordersValue().add(2))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> model.orders().add(THIRD))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void modifyBuilderParameters() {
        // given
        List<Double> doubles1 = new ArrayList<>();
        doubles1.add(10d);
        doubles1.add(11d);
        List<Double> doubles2 = new ArrayList<>();
        doubles2.add(20d);
        doubles2.add(21d);
        List<Integer> enums1 = new ArrayList<>();
        enums1.add(0);
        List<Integer> enums2 = new ArrayList<>();
        enums2.add(1);
        RepeatablePacked.Builder modelBuilder = RepeatablePacked.builder()
                .doubles(doubles1)
                .addAllDoubles(doubles2)
                .addDoubles(30d)
                .ordersValue(enums1)
                .addAllOrdersValue(enums2)
                .addOrdersValue(2);

        // when
        doubles1.add(12d);
        doubles2.add(22d);
        enums1.add(2);
        enums2.add(2);
        RepeatablePacked model = modelBuilder.build();

        // then
        assertThat(model.doubles()).containsExactly(10d, 11d, 20d, 21d, 30d);
        assertThat(model.ordersValue()).containsExactly(0, 1, 2);
        assertThat(model.protobufSize()).isEqualTo(47);
    }

    @Test
    void builderSetterOverrides() {
        // when
        RepeatablePacked model = RepeatablePacked.builder()
                .addAllDoubles(Set.of(10d, 20d))
                .doubles(List.of(100d, 200d))
                .addAllOrdersValue(Set.of(1, 2))
                .ordersValue(List.of(2, 1, 3, 3))
                .build();

        // then
        assertThat(model.doubles()).containsExactly(100d, 200d);
        assertThat(model.ordersValue()).containsExactly(2, 1, 3, 3);
        assertThat(model.protobufSize()).isEqualTo(24);
    }

    @Test
    void builderSetter() {
        // when
        RepeatablePacked model = RepeatablePacked.builder()
                .addOrders(FIRST)
                .addOrdersValue(2)
                .addAllOrders(List.of(THIRD, SECOND))
                .addAllOrdersValue(List.of(0, 3))
                .build();

        // then
        assertThat(model.ordersValue()).containsExactly(0, 2, 2, 1, 0, 3);
        assertThat(model.protobufSize()).isEqualTo(8);
    }

    @Test
    void unknownValue() {
        // when
        RepeatablePacked model = RepeatablePacked.builder()
                .ordersValue(List.of(0, 10, 2))
                .build();

        // then
        assertThat(model.ordersValue()).containsExactly(0, 10, 2);
        assertThat(model.orders()).containsExactly(FIRST, UNRECOGNIZED, THIRD);
        assertThat(model.protobufSize()).isEqualTo(5);
    }

    @Test
    void setUnknown() {
        // when
        Throwable thrown = catchThrowable(() -> RepeatablePacked.builder().orders(List.of(FIRST, UNRECOGNIZED)).build());

        // then
        assertThat(thrown)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Unrecognized enum does not have a number");
    }
}
