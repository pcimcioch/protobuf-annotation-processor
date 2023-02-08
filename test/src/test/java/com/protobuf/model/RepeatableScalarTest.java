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
        assertThat(model.double_()).isEmpty();
        assertThat(model.float_()).isEmpty();
        assertThat(model.int32()).isEmpty();
        assertThat(model.int64()).isEmpty();
        assertThat(model.uint32()).isEmpty();
        assertThat(model.uint64()).isEmpty();
        assertThat(model.sint32()).isEmpty();
        assertThat(model.sint64()).isEmpty();
        assertThat(model.fixed32()).isEmpty();
        assertThat(model.fixed64()).isEmpty();
        assertThat(model.sfixed32()).isEmpty();
        assertThat(model.sfixed64()).isEmpty();
        assertThat(model.bool()).isEmpty();
        assertThat(model.string()).isEmpty();
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
        assertThat(model.double_()).containsExactly(10d);
        assertThat(model.float_()).containsExactly(20f);
        assertThat(model.int32()).containsExactly(30);
        assertThat(model.int64()).containsExactly(40L);
        assertThat(model.uint32()).containsExactly(50);
        assertThat(model.uint64()).containsExactly(60L);
        assertThat(model.sint32()).containsExactly(70);
        assertThat(model.sint64()).containsExactly(80L);
        assertThat(model.fixed32()).containsExactly(90);
        assertThat(model.fixed64()).containsExactly(100L);
        assertThat(model.sfixed32()).containsExactly(110);
        assertThat(model.sfixed64()).containsExactly(120L);
        assertThat(model.bool()).containsExactly(true);
        assertThat(model.string()).containsExactly("test");
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
        assertThat(model.double_()).containsExactly(10d, 11d);
        assertThat(model.float_()).containsExactly(20f, 21f);
        assertThat(model.int32()).containsExactly(30, 31);
        assertThat(model.int64()).containsExactly(40L, 41L);
        assertThat(model.uint32()).containsExactly(50, 51);
        assertThat(model.uint64()).containsExactly(60L, 61L);
        assertThat(model.sint32()).containsExactly(70, 71);
        assertThat(model.sint64()).containsExactly(80L, 81L);
        assertThat(model.fixed32()).containsExactly(90, 91);
        assertThat(model.fixed64()).containsExactly(100L, 101L);
        assertThat(model.sfixed32()).containsExactly(110, 111);
        assertThat(model.sfixed64()).containsExactly(120L, 121L);
        assertThat(model.bool()).containsExactly(true, false);
        assertThat(model.string()).containsExactly("test", "foobar");
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
        assertThat(model.double_()).containsExactly(10d);
    }

    @Test
    void modifyOutputList() {
        // given
        List<Double> input = new ArrayList<>();
        input.add(10d);
        RepeatableScalar model = new RepeatableScalar(input, List.of(), List.of(), List.of(), List.of(), List.of(),
                List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of());

        // when then
        assertThatThrownBy(() -> model.double_().add(20d))
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
                .double_(input1)
                .addAllDouble_(input2)
                .addDouble_(30d);

        // when
        input1.add(12d);
        input2.add(22d);
        RepeatableScalar model = modelBuilder.build();

        // then
        assertThat(model.double_()).containsExactly(10d, 11d, 20d, 21d, 30d);
    }

    @Test
    void builderSetter() {
        // when
        RepeatableScalar model = RepeatableScalar.builder()
                .addAllDouble_(Set.of(10d, 20d))
                .double_(List.of(100d, 200d))
                .build();

        // then
        assertThat(model.double_()).containsExactly(100d, 200d);
    }
}
