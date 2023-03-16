package com.protobuf.model;

import com.github.pcimcioch.protobuf.dto.BooleanList;
import com.github.pcimcioch.protobuf.dto.DoubleList;
import com.github.pcimcioch.protobuf.dto.FloatList;
import com.github.pcimcioch.protobuf.dto.IntList;
import com.github.pcimcioch.protobuf.dto.LongList;
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
        assertThat(model.protobufSize()).isEqualTo(0);
    }

    @Test
    void singleElements() {
        // when
        RepeatableScalar model = new RepeatableScalar(
                DoubleList.of(10d),
                FloatList.of(20f),
                IntList.of(30),
                LongList.of(40L),
                IntList.of(50),
                LongList.of(60L),
                IntList.of(70),
                LongList.of(80L),
                IntList.of(90),
                LongList.of(100L),
                IntList.of(110),
                LongList.of(120L),
                BooleanList.of(true),
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
        assertThat(model.protobufSize()).isEqualTo(67);
    }

    @Test
    void multipleElements() {
        // when
        RepeatableScalar model = new RepeatableScalar(
                DoubleList.of(10d, 11d),
                FloatList.of(20f, 21f),
                IntList.of(30, 31),
                LongList.of(40L, 41L),
                IntList.of(50, 51),
                LongList.of(60L, 61L),
                IntList.of(70, 71),
                LongList.of(80L, 81L),
                IntList.of(90, 91),
                LongList.of(100L, 101L),
                IntList.of(110, 111),
                LongList.of(120L, 121L),
                BooleanList.of(true, false),
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
        assertThat(model.protobufSize()).isEqualTo(134);
    }

    @Test
    void modifyOutputList() {
        // given
        RepeatableScalar model = new RepeatableScalar(DoubleList.of(), FloatList.of(), IntList.of(), LongList.of(),
                IntList.of(), LongList.of(), IntList.of(), LongList.of(), IntList.of(), LongList.of(), IntList.of(),
                LongList.of(), BooleanList.of(), List.of(), List.of());

        // when then
        assertThatThrownBy(() -> model.doubles().add(20d))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void modifyBuilderParameters() {
        // given
        List<Double> input = new ArrayList<>();
        input.add(10d);
        input.add(11d);
        RepeatableScalar.Builder modelBuilder = RepeatableScalar.builder()
                .addAllDoubles(input)
                .addDoubles(20d);

        // when
        input.add(12d);
        RepeatableScalar model = modelBuilder.build();

        // then
        assertThat(model.doubles()).containsExactly(10d, 11d, 20d);
        assertThat(model.protobufSize()).isEqualTo(27);
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
        assertThat(model.protobufSize()).isEqualTo(18);
    }
}
