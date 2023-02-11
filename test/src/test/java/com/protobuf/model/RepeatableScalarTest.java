package com.protobuf.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.protobuf.ByteUtils.ba;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RepeatableScalarTest {

    @Test
    void defaultValues() {
        // when
        RepeatableScalar model = RepeatableScalar.empty();

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
        assertThat(model.strings()).isEmpty();
        assertThat(model.bytes()).isEmpty();
    }

    @Test
    void singleElements() {
        // when
        RepeatableScalar model = new RepeatableScalar(
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
                List.of("test"),
                List.of(ba(1))
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
        assertThat(model.strings()).containsExactly("test");
        assertThat(model.bytes()).containsExactly(ba(1));
    }

    @Test
    void multipleElements() {
        // when
        RepeatableScalar model = new RepeatableScalar(
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
                List.of("test", "foobar"),
                List.of(ba(1, 2))
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
        assertThat(model.strings()).containsExactly("test", "foobar");
        assertThat(model.bytes()).containsExactly(ba(1, 2));
    }

    @Test
    void modifyInputList() {
        // given
        List<Double> input = new ArrayList<>();
        input.add(10d);
        RepeatableScalar model = new RepeatableScalar(input, List.of(), List.of(), List.of(), List.of(), List.of(),
                List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of());

        // when
        input.add(20d);

        // then
        assertThat(model.doubles()).containsExactly(10d);
    }

    @Test
    void modifyOutputList() {
        // given
        List<Double> input = new ArrayList<>();
        input.add(10d);
        RepeatableScalar model = new RepeatableScalar(input, List.of(), List.of(), List.of(), List.of(), List.of(),
                List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of());

        // when then
        assertThatThrownBy(() -> model.doubles().add(20d))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void modifyBuilderParameters() {
        // given
        List<Double> input1 = new ArrayList<>();
        input1.add(10d);
        input1.add(11d);
        List<Double> input2 = new ArrayList<>();
        input2.add(20d);
        input2.add(21d);
        RepeatableScalar.Builder modelBuilder = RepeatableScalar.builder()
                .doubles(input1)
                .addAllDoubles(input2)
                .addDoubles(30d);

        // when
        input1.add(12d);
        input2.add(22d);
        RepeatableScalar model = modelBuilder.build();

        // then
        assertThat(model.doubles()).containsExactly(10d, 11d, 20d, 21d, 30d);
    }

    @Test
    void builderSetterOverrides() {
        // when
        RepeatableScalar model = RepeatableScalar.builder()
                .addAllDoubles(Set.of(10d, 20d))
                .doubles(List.of(100d, 200d))
                .build();

        // then
        assertThat(model.doubles()).containsExactly(100d, 200d);
    }
}
