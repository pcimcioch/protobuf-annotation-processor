package com.github.pcimcioch.protobuf.io;

import com.github.pcimcioch.protobuf.dto.ByteArray;
import com.github.pcimcioch.protobuf.dto.ProtobufMessage;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.OutputStream;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class SizeTest {
    private static final int NUMBER_1_SMALL = 1;
    private static final int NUMBER_1_BIG = 0b1111;
    private static final int NUMBER_2_SMALL = 0b10000;
    private static final int NUMBER_2_BIG = 0b11111111111;

    private final Size testee = Size.inPlace();

    @ParameterizedTest
    @MethodSource("doubleSource")
    void doubleSize(int number, double value, int expectedSize) {
        // when then
        assertThat(testee.double_(number, value)).isEqualTo(expectedSize);
    }

    private static Stream<Arguments> doubleSource() {
        return Stream.of(
                Arguments.of(NUMBER_1_SMALL, 0d, 0),
                Arguments.of(NUMBER_2_BIG, 0d, 0),

                Arguments.of(NUMBER_1_SMALL, 1d, 9),
                Arguments.of(NUMBER_1_BIG, 1d, 9),
                Arguments.of(NUMBER_2_SMALL, 1d, 10),
                Arguments.of(NUMBER_2_BIG, 1d, 10),

                Arguments.of(NUMBER_1_SMALL, Double.MIN_VALUE, 9),
                Arguments.of(NUMBER_1_SMALL, Double.MAX_VALUE, 9)
        );
    }

    @ParameterizedTest
    @MethodSource("doubleListSource")
    void doubleListSize(int number, List<Double> values, int expectedSize) {
        // when then
        assertThat(testee.double_(number, values)).isEqualTo(expectedSize);
    }

    private static Stream<Arguments> doubleListSource() {
        return Stream.of(
                Arguments.of(NUMBER_1_SMALL, List.of(), 0),
                Arguments.of(NUMBER_2_BIG, List.of(), 0),

                Arguments.of(NUMBER_1_SMALL, List.of(1d), 9),
                Arguments.of(NUMBER_1_BIG, List.of(1d), 9),
                Arguments.of(NUMBER_2_SMALL, List.of(1d), 10),
                Arguments.of(NUMBER_2_BIG, List.of(1d), 10),

                Arguments.of(NUMBER_1_SMALL, List.of(Double.MIN_VALUE), 9),
                Arguments.of(NUMBER_1_SMALL, List.of(Double.MAX_VALUE), 9),

                Arguments.of(NUMBER_1_SMALL, List.of(1d, 2d, -1d, 0d), 36),
                Arguments.of(NUMBER_1_BIG, List.of(1d, 2d, -1d, 0d), 36),
                Arguments.of(NUMBER_2_SMALL, List.of(1d, 2d, -1d, 0d), 40),
                Arguments.of(NUMBER_2_BIG, List.of(1d, 2d, -1d, 0d), 40)
        );
    }

    @ParameterizedTest
    @MethodSource("floatSource")
    void floatSize(int number, float value, int expectedSize) {
        // when then
        assertThat(testee.float_(number, value)).isEqualTo(expectedSize);
    }

    private static Stream<Arguments> floatSource() {
        return Stream.of(
                Arguments.of(NUMBER_1_SMALL, 0f, 0),
                Arguments.of(NUMBER_2_BIG, 0f, 0),

                Arguments.of(NUMBER_1_SMALL, 1f, 5),
                Arguments.of(NUMBER_1_BIG, 1f, 5),
                Arguments.of(NUMBER_2_SMALL, 1f, 6),
                Arguments.of(NUMBER_2_BIG, 1f, 6),

                Arguments.of(NUMBER_1_SMALL, Float.MIN_VALUE, 5),
                Arguments.of(NUMBER_1_SMALL, Float.MAX_VALUE, 5)
        );
    }

    @ParameterizedTest
    @MethodSource("floatListSource")
    void floatListSize(int number, List<Float> values, int expectedSize) {
        // when then
        assertThat(testee.float_(number, values)).isEqualTo(expectedSize);
    }

    private static Stream<Arguments> floatListSource() {
        return Stream.of(
                Arguments.of(NUMBER_1_SMALL, List.of(), 0),
                Arguments.of(NUMBER_2_BIG, List.of(), 0),

                Arguments.of(NUMBER_1_SMALL, List.of(1f), 5),
                Arguments.of(NUMBER_1_BIG, List.of(1f), 5),
                Arguments.of(NUMBER_2_SMALL, List.of(1f), 6),
                Arguments.of(NUMBER_2_BIG, List.of(1f), 6),

                Arguments.of(NUMBER_1_SMALL, List.of(Float.MIN_VALUE), 5),
                Arguments.of(NUMBER_1_SMALL, List.of(Float.MAX_VALUE), 5),

                Arguments.of(NUMBER_1_SMALL, List.of(1f, 2f, -1f, 0f), 20),
                Arguments.of(NUMBER_1_BIG, List.of(1f, 2f, -1f, 0f), 20),
                Arguments.of(NUMBER_2_SMALL, List.of(1f, 2f, -1f, 0f), 24),
                Arguments.of(NUMBER_2_BIG, List.of(1f, 2f, -1f, 0f), 24)
        );
    }

    @ParameterizedTest
    @MethodSource("int32Source")
    void int32Size(int number, int value, int expectedSize) {
        // when then
        assertThat(testee.int32(number, value)).isEqualTo(expectedSize);
    }

    private static Stream<Arguments> int32Source() {
        return Stream.of(
                Arguments.of(NUMBER_1_SMALL, 0, 0),
                Arguments.of(NUMBER_2_BIG, 0, 0),

                Arguments.of(NUMBER_1_SMALL, 1, 2),
                Arguments.of(NUMBER_1_BIG, 1, 2),
                Arguments.of(NUMBER_2_SMALL, 1, 3),
                Arguments.of(NUMBER_2_BIG, 1, 3),

                Arguments.of(NUMBER_1_SMALL, 127, 2),
                Arguments.of(NUMBER_1_SMALL, 128, 3),
                Arguments.of(NUMBER_1_SMALL, 16383, 3),
                Arguments.of(NUMBER_1_SMALL, 16384, 4),
                Arguments.of(NUMBER_1_SMALL, 2097151, 4),
                Arguments.of(NUMBER_1_SMALL, 2097152, 5),
                Arguments.of(NUMBER_1_SMALL, Integer.MAX_VALUE, 6),
                Arguments.of(NUMBER_1_SMALL, Integer.MIN_VALUE, 11),
                Arguments.of(NUMBER_1_SMALL, -1, 11)
        );
    }

    @ParameterizedTest
    @MethodSource("int32ListSource")
    void int32ListSize(int number, List<Integer> values, int expectedSize) {
        // when then
        assertThat(testee.int32(number, values)).isEqualTo(expectedSize);
    }

    private static Stream<Arguments> int32ListSource() {
        return Stream.of(
                Arguments.of(NUMBER_1_SMALL, List.of(), 0),
                Arguments.of(NUMBER_2_BIG, List.of(), 0),

                Arguments.of(NUMBER_1_SMALL, List.of(1), 2),
                Arguments.of(NUMBER_1_BIG, List.of(1), 2),
                Arguments.of(NUMBER_2_SMALL, List.of(1), 3),
                Arguments.of(NUMBER_2_BIG, List.of(1), 3),

                Arguments.of(NUMBER_1_SMALL, List.of(Integer.MIN_VALUE), 11),
                Arguments.of(NUMBER_1_SMALL, List.of(Integer.MAX_VALUE), 6),

                Arguments.of(NUMBER_1_SMALL, List.of(1, 128, -1, 0), 18),
                Arguments.of(NUMBER_1_BIG, List.of(1, 128, -1, 0), 18),
                Arguments.of(NUMBER_2_SMALL, List.of(1, 128, -1, 0), 22),
                Arguments.of(NUMBER_2_BIG, List.of(1, 128, -1, 0), 22)
        );
    }

    @ParameterizedTest
    @MethodSource("int64Source")
    void int64Size(int number, long value, int expectedSize) {
        // when then
        assertThat(testee.int64(number, value)).isEqualTo(expectedSize);
    }

    private static Stream<Arguments> int64Source() {
        return Stream.of(
                Arguments.of(NUMBER_1_SMALL, 0L, 0),
                Arguments.of(NUMBER_2_BIG, 0L, 0),

                Arguments.of(NUMBER_1_SMALL, 1L, 2),
                Arguments.of(NUMBER_1_BIG, 1L, 2),
                Arguments.of(NUMBER_2_SMALL, 1L, 3),
                Arguments.of(NUMBER_2_BIG, 1L, 3),

                Arguments.of(NUMBER_1_SMALL, 127L, 2),
                Arguments.of(NUMBER_1_SMALL, 128L, 3),
                Arguments.of(NUMBER_1_SMALL, 16383L, 3),
                Arguments.of(NUMBER_1_SMALL, 16384L, 4),
                Arguments.of(NUMBER_1_SMALL, 2097151L, 4),
                Arguments.of(NUMBER_1_SMALL, 2097152L, 5),
                Arguments.of(NUMBER_1_SMALL, Long.MAX_VALUE, 10),
                Arguments.of(NUMBER_1_SMALL, Long.MIN_VALUE, 11),
                Arguments.of(NUMBER_1_SMALL, -1L, 11)
        );
    }

    @ParameterizedTest
    @MethodSource("int64ListSource")
    void int64ListSize(int number, List<Long> values, int expectedSize) {
        // when then
        assertThat(testee.int64(number, values)).isEqualTo(expectedSize);
    }

    private static Stream<Arguments> int64ListSource() {
        return Stream.of(
                Arguments.of(NUMBER_1_SMALL, List.of(), 0),
                Arguments.of(NUMBER_2_BIG, List.of(), 0),

                Arguments.of(NUMBER_1_SMALL, List.of(1L), 2),
                Arguments.of(NUMBER_1_BIG, List.of(1L), 2),
                Arguments.of(NUMBER_2_SMALL, List.of(1L), 3),
                Arguments.of(NUMBER_2_BIG, List.of(1L), 3),

                Arguments.of(NUMBER_1_SMALL, List.of(Long.MIN_VALUE), 11),
                Arguments.of(NUMBER_1_SMALL, List.of(Long.MAX_VALUE), 10),

                Arguments.of(NUMBER_1_SMALL, List.of(1L, 128L, -1L, 0L), 18),
                Arguments.of(NUMBER_1_BIG, List.of(1L, 128L, -1L, 0L), 18),
                Arguments.of(NUMBER_2_SMALL, List.of(1L, 128L, -1L, 0L), 22),
                Arguments.of(NUMBER_2_BIG, List.of(1L, 128L, -1L, 0L), 22)
        );
    }

    @ParameterizedTest
    @MethodSource("uint32Source")
    void uint32Size(int number, int value, int expectedSize) {
        // when then
        assertThat(testee.uint32(number, value)).isEqualTo(expectedSize);
    }

    private static Stream<Arguments> uint32Source() {
        return Stream.of(
                Arguments.of(NUMBER_1_SMALL, 0, 0),
                Arguments.of(NUMBER_2_BIG, 0, 0),

                Arguments.of(NUMBER_1_SMALL, 1, 2),
                Arguments.of(NUMBER_1_BIG, 1, 2),
                Arguments.of(NUMBER_2_SMALL, 1, 3),
                Arguments.of(NUMBER_2_BIG, 1, 3),

                Arguments.of(NUMBER_1_SMALL, 127, 2),
                Arguments.of(NUMBER_1_SMALL, 128, 3),
                Arguments.of(NUMBER_1_SMALL, 16383, 3),
                Arguments.of(NUMBER_1_SMALL, 16384, 4),
                Arguments.of(NUMBER_1_SMALL, 2097151, 4),
                Arguments.of(NUMBER_1_SMALL, 2097152, 5),
                Arguments.of(NUMBER_1_SMALL, Integer.MAX_VALUE, 6)
        );
    }

    @ParameterizedTest
    @MethodSource("uint32ListSource")
    void uint32ListSize(int number, List<Integer> values, int expectedSize) {
        // when then
        assertThat(testee.uint32(number, values)).isEqualTo(expectedSize);
    }

    private static Stream<Arguments> uint32ListSource() {
        return Stream.of(
                Arguments.of(NUMBER_1_SMALL, List.of(), 0),
                Arguments.of(NUMBER_2_BIG, List.of(), 0),

                Arguments.of(NUMBER_1_SMALL, List.of(1), 2),
                Arguments.of(NUMBER_1_BIG, List.of(1), 2),
                Arguments.of(NUMBER_2_SMALL, List.of(1), 3),
                Arguments.of(NUMBER_2_BIG, List.of(1), 3),

                Arguments.of(NUMBER_1_SMALL, List.of(Integer.MAX_VALUE), 6),

                Arguments.of(NUMBER_1_SMALL, List.of(1, 128, 0), 7),
                Arguments.of(NUMBER_1_BIG, List.of(1, 128, 0), 7),
                Arguments.of(NUMBER_2_SMALL, List.of(1, 128, 0), 10),
                Arguments.of(NUMBER_2_BIG, List.of(1, 128, 0), 10)
        );
    }

    @ParameterizedTest
    @MethodSource("uint64Source")
    void uint64Size(int number, long value, int expectedSize) {
        // when then
        assertThat(testee.uint64(number, value)).isEqualTo(expectedSize);
    }

    private static Stream<Arguments> uint64Source() {
        return Stream.of(
                Arguments.of(NUMBER_1_SMALL, 0L, 0),
                Arguments.of(NUMBER_2_BIG, 0L, 0),

                Arguments.of(NUMBER_1_SMALL, 1L, 2),
                Arguments.of(NUMBER_1_BIG, 1L, 2),
                Arguments.of(NUMBER_2_SMALL, 1L, 3),
                Arguments.of(NUMBER_2_BIG, 1L, 3),

                Arguments.of(NUMBER_1_SMALL, 127L, 2),
                Arguments.of(NUMBER_1_SMALL, 128L, 3),
                Arguments.of(NUMBER_1_SMALL, 16383L, 3),
                Arguments.of(NUMBER_1_SMALL, 16384L, 4),
                Arguments.of(NUMBER_1_SMALL, 2097151L, 4),
                Arguments.of(NUMBER_1_SMALL, 2097152L, 5),
                Arguments.of(NUMBER_1_SMALL, Long.MAX_VALUE, 10)
        );
    }

    @ParameterizedTest
    @MethodSource("uint64ListSource")
    void uint64ListSize(int number, List<Long> values, int expectedSize) {
        // when then
        assertThat(testee.uint64(number, values)).isEqualTo(expectedSize);
    }

    private static Stream<Arguments> uint64ListSource() {
        return Stream.of(
                Arguments.of(NUMBER_1_SMALL, List.of(), 0),
                Arguments.of(NUMBER_2_BIG, List.of(), 0),

                Arguments.of(NUMBER_1_SMALL, List.of(1L), 2),
                Arguments.of(NUMBER_1_BIG, List.of(1L), 2),
                Arguments.of(NUMBER_2_SMALL, List.of(1L), 3),
                Arguments.of(NUMBER_2_BIG, List.of(1L), 3),

                Arguments.of(NUMBER_1_SMALL, List.of(Long.MIN_VALUE), 11),
                Arguments.of(NUMBER_1_SMALL, List.of(Long.MAX_VALUE), 10),

                Arguments.of(NUMBER_1_SMALL, List.of(1L, 128L, 0L), 7),
                Arguments.of(NUMBER_1_BIG, List.of(1L, 128L, 0L), 7),
                Arguments.of(NUMBER_2_SMALL, List.of(1L, 128L, 0L), 10),
                Arguments.of(NUMBER_2_BIG, List.of(1L, 128L, 0L), 10)
        );
    }

    @ParameterizedTest
    @MethodSource("sint32Source")
    void sint32Size(int number, int value, int expectedSize) {
        // when then
        assertThat(testee.sint32(number, value)).isEqualTo(expectedSize);
    }

    private static Stream<Arguments> sint32Source() {
        return Stream.of(
                Arguments.of(NUMBER_1_SMALL, 0, 0),
                Arguments.of(NUMBER_2_BIG, 0, 0),

                Arguments.of(NUMBER_1_SMALL, 1, 2),
                Arguments.of(NUMBER_1_BIG, 1, 2),
                Arguments.of(NUMBER_2_SMALL, 1, 3),
                Arguments.of(NUMBER_2_BIG, 1, 3),

                Arguments.of(NUMBER_1_SMALL, 63, 2),
                Arguments.of(NUMBER_1_SMALL, 64, 3),
                Arguments.of(NUMBER_1_SMALL, 8191, 3),
                Arguments.of(NUMBER_1_SMALL, 8192, 4),
                Arguments.of(NUMBER_1_SMALL, 1048575, 4),
                Arguments.of(NUMBER_1_SMALL, 1048576, 5),
                Arguments.of(NUMBER_1_SMALL, Integer.MAX_VALUE, 11),
                Arguments.of(NUMBER_1_SMALL, -64, 2),
                Arguments.of(NUMBER_1_SMALL, -65, 3),
                Arguments.of(NUMBER_1_SMALL, -8192, 3),
                Arguments.of(NUMBER_1_SMALL, -8193, 4),
                Arguments.of(NUMBER_1_SMALL, -1048576, 4),
                Arguments.of(NUMBER_1_SMALL, -1048577, 5),
                Arguments.of(NUMBER_1_SMALL, Integer.MIN_VALUE, 11)
        );
    }

    @ParameterizedTest
    @MethodSource("sint32ListSource")
    void sint32ListSize(int number, List<Integer> values, int expectedSize) {
        // when then
        assertThat(testee.sint32(number, values)).isEqualTo(expectedSize);
    }

    private static Stream<Arguments> sint32ListSource() {
        return Stream.of(
                Arguments.of(NUMBER_1_SMALL, List.of(), 0),
                Arguments.of(NUMBER_2_BIG, List.of(), 0),

                Arguments.of(NUMBER_1_SMALL, List.of(1), 2),
                Arguments.of(NUMBER_1_BIG, List.of(1), 2),
                Arguments.of(NUMBER_2_SMALL, List.of(1), 3),
                Arguments.of(NUMBER_2_BIG, List.of(1), 3),

                Arguments.of(NUMBER_1_SMALL, List.of(Integer.MIN_VALUE), 11),
                Arguments.of(NUMBER_1_SMALL, List.of(Integer.MAX_VALUE), 11),

                Arguments.of(NUMBER_1_SMALL, List.of(1, 64, -8193, 0), 11),
                Arguments.of(NUMBER_1_BIG, List.of(1, 64, -8193, 0), 11),
                Arguments.of(NUMBER_2_SMALL, List.of(1, 64, -8193, 0), 15),
                Arguments.of(NUMBER_2_BIG, List.of(1, 64, -8193, 0), 15)
        );
    }

    @ParameterizedTest
    @MethodSource("sint64Source")
    void sint64Size(int number, long value, int expectedSize) {
        // when then
        assertThat(testee.sint64(number, value)).isEqualTo(expectedSize);
    }

    private static Stream<Arguments> sint64Source() {
        return Stream.of(
                Arguments.of(NUMBER_1_SMALL, 0L, 0),
                Arguments.of(NUMBER_2_BIG, 0L, 0),

                Arguments.of(NUMBER_1_SMALL, 1L, 2),
                Arguments.of(NUMBER_1_BIG, 1L, 2),
                Arguments.of(NUMBER_2_SMALL, 1L, 3),
                Arguments.of(NUMBER_2_BIG, 1L, 3),

                Arguments.of(NUMBER_1_SMALL, 63L, 2),
                Arguments.of(NUMBER_1_SMALL, 64L, 3),
                Arguments.of(NUMBER_1_SMALL, 8191L, 3),
                Arguments.of(NUMBER_1_SMALL, 8192L, 4),
                Arguments.of(NUMBER_1_SMALL, 1048575L, 4),
                Arguments.of(NUMBER_1_SMALL, 1048576L, 5),
                Arguments.of(NUMBER_1_SMALL, Long.MAX_VALUE, 11),
                Arguments.of(NUMBER_1_SMALL, -64L, 2),
                Arguments.of(NUMBER_1_SMALL, -65L, 3),
                Arguments.of(NUMBER_1_SMALL, -8192L, 3),
                Arguments.of(NUMBER_1_SMALL, -8193L, 4),
                Arguments.of(NUMBER_1_SMALL, -1048576L, 4),
                Arguments.of(NUMBER_1_SMALL, -1048577L, 5),
                Arguments.of(NUMBER_1_SMALL, Long.MIN_VALUE, 11)
        );
    }

    @ParameterizedTest
    @MethodSource("sint64ListSource")
    void sint64ListSize(int number, List<Long> values, int expectedSize) {
        // when then
        assertThat(testee.sint64(number, values)).isEqualTo(expectedSize);
    }

    private static Stream<Arguments> sint64ListSource() {
        return Stream.of(
                Arguments.of(NUMBER_1_SMALL, List.of(), 0),
                Arguments.of(NUMBER_2_BIG, List.of(), 0),

                Arguments.of(NUMBER_1_SMALL, List.of(1L), 2),
                Arguments.of(NUMBER_1_BIG, List.of(1L), 2),
                Arguments.of(NUMBER_2_SMALL, List.of(1L), 3),
                Arguments.of(NUMBER_2_BIG, List.of(1L), 3),

                Arguments.of(NUMBER_1_SMALL, List.of(Long.MIN_VALUE), 11),
                Arguments.of(NUMBER_1_SMALL, List.of(Long.MAX_VALUE), 11),

                Arguments.of(NUMBER_1_SMALL, List.of(1L, 64L, -8193L, 0L), 11),
                Arguments.of(NUMBER_1_BIG, List.of(1L, 64L, -8193L, 0L), 11),
                Arguments.of(NUMBER_2_SMALL, List.of(1L, 64L, -8193L, 0L), 15),
                Arguments.of(NUMBER_2_BIG, List.of(1L, 64L, -8193L, 0L), 15)
        );
    }

    @ParameterizedTest
    @MethodSource("fixed32Source")
    void fixed32Size(int number, int value, int expectedSize) {
        // when then
        assertThat(testee.fixed32(number, value)).isEqualTo(expectedSize);
    }

    private static Stream<Arguments> fixed32Source() {
        return Stream.of(
                Arguments.of(NUMBER_1_SMALL, 0, 0),
                Arguments.of(NUMBER_2_BIG, 0, 0),

                Arguments.of(NUMBER_1_SMALL, 1, 5),
                Arguments.of(NUMBER_1_BIG, 1, 5),
                Arguments.of(NUMBER_2_SMALL, 1, 6),
                Arguments.of(NUMBER_2_BIG, 1, 6),

                Arguments.of(NUMBER_1_SMALL, Integer.MIN_VALUE, 5),
                Arguments.of(NUMBER_1_SMALL, Integer.MAX_VALUE, 5)
        );
    }

    @ParameterizedTest
    @MethodSource("fixed32ListSource")
    void fixed32ListSize(int number, List<Integer> values, int expectedSize) {
        // when then
        assertThat(testee.fixed32(number, values)).isEqualTo(expectedSize);
    }

    private static Stream<Arguments> fixed32ListSource() {
        return Stream.of(
                Arguments.of(NUMBER_1_SMALL, List.of(), 0),
                Arguments.of(NUMBER_2_BIG, List.of(), 0),

                Arguments.of(NUMBER_1_SMALL, List.of(1), 5),
                Arguments.of(NUMBER_1_BIG, List.of(1), 5),
                Arguments.of(NUMBER_2_SMALL, List.of(1), 6),
                Arguments.of(NUMBER_2_BIG, List.of(1), 6),

                Arguments.of(NUMBER_1_SMALL, List.of(Integer.MIN_VALUE), 5),
                Arguments.of(NUMBER_1_SMALL, List.of(Integer.MAX_VALUE), 5),

                Arguments.of(NUMBER_1_SMALL, List.of(1, 2, -1, 0), 20),
                Arguments.of(NUMBER_1_BIG, List.of(1, 2, -1, 0), 20),
                Arguments.of(NUMBER_2_SMALL, List.of(1, 2, -1, 0), 24),
                Arguments.of(NUMBER_2_BIG, List.of(1, 2, -1, 0), 24)
        );
    }

    @ParameterizedTest
    @MethodSource("fixed64Source")
    void fixed64Size(int number, long value, int expectedSize) {
        // when then
        assertThat(testee.fixed64(number, value)).isEqualTo(expectedSize);
    }

    private static Stream<Arguments> fixed64Source() {
        return Stream.of(
                Arguments.of(NUMBER_1_SMALL, 0L, 0),
                Arguments.of(NUMBER_2_BIG, 0L, 0),

                Arguments.of(NUMBER_1_SMALL, 1L, 9),
                Arguments.of(NUMBER_1_BIG, 1L, 9),
                Arguments.of(NUMBER_2_SMALL, 1L, 10),
                Arguments.of(NUMBER_2_BIG, 1L, 10),

                Arguments.of(NUMBER_1_SMALL, Long.MIN_VALUE, 9),
                Arguments.of(NUMBER_1_SMALL, Long.MAX_VALUE, 9)
        );
    }

    @ParameterizedTest
    @MethodSource("fixed64ListSource")
    void fixed64ListSize(int number, List<Long> values, int expectedSize) {
        // when then
        assertThat(testee.fixed64(number, values)).isEqualTo(expectedSize);
    }

    private static Stream<Arguments> fixed64ListSource() {
        return Stream.of(
                Arguments.of(NUMBER_1_SMALL, List.of(), 0),
                Arguments.of(NUMBER_2_BIG, List.of(), 0),

                Arguments.of(NUMBER_1_SMALL, List.of(1L), 9),
                Arguments.of(NUMBER_1_BIG, List.of(1L), 9),
                Arguments.of(NUMBER_2_SMALL, List.of(1L), 10),
                Arguments.of(NUMBER_2_BIG, List.of(1L), 10),

                Arguments.of(NUMBER_1_SMALL, List.of(Long.MIN_VALUE), 9),
                Arguments.of(NUMBER_1_SMALL, List.of(Long.MAX_VALUE), 9),

                Arguments.of(NUMBER_1_SMALL, List.of(1L, 2L, -1L, 0L), 36),
                Arguments.of(NUMBER_1_BIG, List.of(1L, 2L, -1L, 0L), 36),
                Arguments.of(NUMBER_2_SMALL, List.of(1L, 2L, -1L, 0L), 40),
                Arguments.of(NUMBER_2_BIG, List.of(1L, 2L, -1L, 0L), 40)
        );
    }

    @ParameterizedTest
    @MethodSource("sfixed32Source")
    void sfixed32Size(int number, int value, int expectedSize) {
        // when then
        assertThat(testee.sfixed32(number, value)).isEqualTo(expectedSize);
    }

    private static Stream<Arguments> sfixed32Source() {
        return Stream.of(
                Arguments.of(NUMBER_1_SMALL, 0, 0),
                Arguments.of(NUMBER_2_BIG, 0, 0),

                Arguments.of(NUMBER_1_SMALL, 1, 5),
                Arguments.of(NUMBER_1_BIG, 1, 5),
                Arguments.of(NUMBER_2_SMALL, 1, 6),
                Arguments.of(NUMBER_2_BIG, 1, 6),

                Arguments.of(NUMBER_1_SMALL, Integer.MIN_VALUE, 5),
                Arguments.of(NUMBER_1_SMALL, Integer.MAX_VALUE, 5)
        );
    }

    @ParameterizedTest
    @MethodSource("sfixed32ListSource")
    void sfixed32ListSize(int number, List<Integer> values, int expectedSize) {
        // when then
        assertThat(testee.sfixed32(number, values)).isEqualTo(expectedSize);
    }

    private static Stream<Arguments> sfixed32ListSource() {
        return Stream.of(
                Arguments.of(NUMBER_1_SMALL, List.of(), 0),
                Arguments.of(NUMBER_2_BIG, List.of(), 0),

                Arguments.of(NUMBER_1_SMALL, List.of(1), 5),
                Arguments.of(NUMBER_1_BIG, List.of(1), 5),
                Arguments.of(NUMBER_2_SMALL, List.of(1), 6),
                Arguments.of(NUMBER_2_BIG, List.of(1), 6),

                Arguments.of(NUMBER_1_SMALL, List.of(Integer.MIN_VALUE), 5),
                Arguments.of(NUMBER_1_SMALL, List.of(Integer.MAX_VALUE), 5),

                Arguments.of(NUMBER_1_SMALL, List.of(1, 2, -1, 0), 20),
                Arguments.of(NUMBER_1_BIG, List.of(1, 2, -1, 0), 20),
                Arguments.of(NUMBER_2_SMALL, List.of(1, 2, -1, 0), 24),
                Arguments.of(NUMBER_2_BIG, List.of(1, 2, -1, 0), 24)
        );
    }

    @ParameterizedTest
    @MethodSource("sfixed64Source")
    void sfixed64Size(int number, long value, int expectedSize) {
        // when then
        assertThat(testee.sfixed64(number, value)).isEqualTo(expectedSize);
    }

    private static Stream<Arguments> sfixed64Source() {
        return Stream.of(
                Arguments.of(NUMBER_1_SMALL, 0L, 0),
                Arguments.of(NUMBER_2_BIG, 0L, 0),

                Arguments.of(NUMBER_1_SMALL, 1L, 9),
                Arguments.of(NUMBER_1_BIG, 1L, 9),
                Arguments.of(NUMBER_2_SMALL, 1L, 10),
                Arguments.of(NUMBER_2_BIG, 1L, 10),

                Arguments.of(NUMBER_1_SMALL, Long.MIN_VALUE, 9),
                Arguments.of(NUMBER_1_SMALL, Long.MAX_VALUE, 9)
        );
    }

    @ParameterizedTest
    @MethodSource("sfixed64ListSource")
    void sfixed64ListSize(int number, List<Long> values, int expectedSize) {
        // when then
        assertThat(testee.sfixed64(number, values)).isEqualTo(expectedSize);
    }

    private static Stream<Arguments> sfixed64ListSource() {
        return Stream.of(
                Arguments.of(NUMBER_1_SMALL, List.of(), 0),
                Arguments.of(NUMBER_2_BIG, List.of(), 0),

                Arguments.of(NUMBER_1_SMALL, List.of(1L), 9),
                Arguments.of(NUMBER_1_BIG, List.of(1L), 9),
                Arguments.of(NUMBER_2_SMALL, List.of(1L), 10),
                Arguments.of(NUMBER_2_BIG, List.of(1L), 10),

                Arguments.of(NUMBER_1_SMALL, List.of(Long.MIN_VALUE), 9),
                Arguments.of(NUMBER_1_SMALL, List.of(Long.MAX_VALUE), 9),

                Arguments.of(NUMBER_1_SMALL, List.of(1L, 2L, -1L, 0L), 36),
                Arguments.of(NUMBER_1_BIG, List.of(1L, 2L, -1L, 0L), 36),
                Arguments.of(NUMBER_2_SMALL, List.of(1L, 2L, -1L, 0L), 40),
                Arguments.of(NUMBER_2_BIG, List.of(1L, 2L, -1L, 0L), 40)
        );
    }

    @ParameterizedTest
    @MethodSource("boolSource")
    void boolSize(int number, boolean value, int expectedSize) {
        // when then
        assertThat(testee.bool(number, value)).isEqualTo(expectedSize);
    }

    private static Stream<Arguments> boolSource() {
        return Stream.of(
                Arguments.of(NUMBER_1_SMALL, false, 0),
                Arguments.of(NUMBER_2_BIG, false, 0),

                Arguments.of(NUMBER_1_SMALL, true, 2),
                Arguments.of(NUMBER_1_BIG, true, 2),
                Arguments.of(NUMBER_2_SMALL, true, 3),
                Arguments.of(NUMBER_2_BIG, true, 3)
        );
    }

    @ParameterizedTest
    @MethodSource("boolListSource")
    void boolListSize(int number, List<Boolean> values, int expectedSize) {
        // when then
        assertThat(testee.bool(number, values)).isEqualTo(expectedSize);
    }

    private static Stream<Arguments> boolListSource() {
        return Stream.of(
                Arguments.of(NUMBER_1_SMALL, List.of(), 0),
                Arguments.of(NUMBER_2_BIG, List.of(), 0),

                Arguments.of(NUMBER_1_SMALL, List.of(true), 2),
                Arguments.of(NUMBER_1_BIG, List.of(true), 2),
                Arguments.of(NUMBER_2_SMALL, List.of(true), 3),
                Arguments.of(NUMBER_2_BIG, List.of(true), 3),

                Arguments.of(NUMBER_1_SMALL, List.of(true, false, false, true), 8),
                Arguments.of(NUMBER_1_BIG, List.of(true, false, false, true), 8),
                Arguments.of(NUMBER_2_SMALL, List.of(true, false, false, true), 12),
                Arguments.of(NUMBER_2_BIG, List.of(true, false, false, true), 12)
        );
    }

    @ParameterizedTest
    @MethodSource("bytesSource")
    void bytesSize(int number, ByteArray value, int expectedSize) {
        // when then
        assertThat(testee.bytes(number, value)).isEqualTo(expectedSize);
    }

    private static Stream<Arguments> bytesSource() {
        return Stream.of(
                Arguments.of(NUMBER_1_SMALL, ByteArray.empty(), 0),
                Arguments.of(NUMBER_2_BIG, ByteArray.empty(), 0),
                Arguments.of(NUMBER_1_SMALL, ByteArray.empty(), 0),
                Arguments.of(NUMBER_2_BIG, ByteArray.empty(), 0),

                Arguments.of(NUMBER_1_SMALL, ba(1), 3),
                Arguments.of(NUMBER_1_BIG, ba(1), 3),
                Arguments.of(NUMBER_2_SMALL, ba(1), 4),
                Arguments.of(NUMBER_2_BIG, ba(1), 4),

                Arguments.of(NUMBER_1_SMALL, ba(1, 2, 3, 4, 5, 6), 8)
        );
    }

    @ParameterizedTest
    @MethodSource("bytesListSource")
    void bytesListSize(int number, List<ByteArray> values, int expectedSize) {
        // when then
        assertThat(testee.bytes(number, values)).isEqualTo(expectedSize);
    }

    private static Stream<Arguments> bytesListSource() {
        return Stream.of(
                Arguments.of(NUMBER_1_SMALL, List.of(), 0),
                Arguments.of(NUMBER_2_BIG, List.of(), 0),

                Arguments.of(NUMBER_1_SMALL, List.of(ba(1)), 3),
                Arguments.of(NUMBER_1_BIG, List.of(ba(1)), 3),
                Arguments.of(NUMBER_2_SMALL, List.of(ba(1)), 4),
                Arguments.of(NUMBER_2_BIG, List.of(ba(1)), 4),

                Arguments.of(NUMBER_1_SMALL, List.of(ba(1, 2, 3, 4)), 6),

                Arguments.of(NUMBER_1_SMALL, List.of(ba(1), ba(2), ba(), ba(10, 20, 30)), 13),
                Arguments.of(NUMBER_1_BIG, List.of(ba(1), ba(2), ba(), ba(10, 20, 30)), 13),
                Arguments.of(NUMBER_2_SMALL, List.of(ba(1), ba(2), ba(), ba(10, 20, 30)), 17),
                Arguments.of(NUMBER_2_BIG, List.of(ba(1), ba(2), ba(), ba(10, 20, 30)), 17)
        );
    }

    @ParameterizedTest
    @MethodSource("stringSource")
    void stringSize(int number, String value, int expectedSize) {
        // when then
        assertThat(testee.string(number, value)).isEqualTo(expectedSize);
    }

    private static Stream<Arguments> stringSource() {
        return Stream.of(
                Arguments.of(NUMBER_1_SMALL, "", 0),
                Arguments.of(NUMBER_2_BIG, "", 0),
                Arguments.of(NUMBER_1_SMALL, "", 0),
                Arguments.of(NUMBER_2_BIG, "", 0),

                Arguments.of(NUMBER_1_SMALL, "a", 3),
                Arguments.of(NUMBER_1_BIG, "a", 3),
                Arguments.of(NUMBER_2_SMALL, "a", 4),
                Arguments.of(NUMBER_2_BIG, "a", 4),

                Arguments.of(NUMBER_1_SMALL, "test12", 8),
                Arguments.of(NUMBER_1_SMALL, "testąę", 16)
        );
    }

    @ParameterizedTest
    @MethodSource("stringListSource")
    void stringListSize(int number, List<String> values, int expectedSize) {
        // when then
        assertThat(testee.string(number, values)).isEqualTo(expectedSize);
    }

    private static Stream<Arguments> stringListSource() {
        return Stream.of(
                Arguments.of(NUMBER_1_SMALL, List.of(), 0),
                Arguments.of(NUMBER_2_BIG, List.of(), 0),

                Arguments.of(NUMBER_1_SMALL, List.of("a"), 3),
                Arguments.of(NUMBER_1_BIG, List.of("a"), 3),
                Arguments.of(NUMBER_2_SMALL, List.of("a"), 4),
                Arguments.of(NUMBER_2_BIG, List.of("a"), 4),

                Arguments.of(NUMBER_1_SMALL, List.of("test"), 6),
                Arguments.of(NUMBER_1_SMALL, List.of("testść"), 16),

                Arguments.of(NUMBER_1_SMALL, List.of("a", "b", "", "foo"), 13),
                Arguments.of(NUMBER_1_BIG, List.of("a", "b", "", "foo"), 13),
                Arguments.of(NUMBER_2_SMALL, List.of("a", "b", "", "foo"), 17),
                Arguments.of(NUMBER_2_BIG, List.of("a", "b", "", "foo"), 17)
        );
    }

    @ParameterizedTest
    @MethodSource("messageSource")
    void messageSize(int number, TestMessage value, int expectedSize) {
        // when then
        assertThat(testee.message(number, value)).isEqualTo(expectedSize);
    }

    private static Stream<Arguments> messageSource() {
        return Stream.of(
                Arguments.of(NUMBER_1_SMALL, null, 0),
                Arguments.of(NUMBER_2_BIG, null, 0),
                Arguments.of(NUMBER_1_SMALL, null, 0),
                Arguments.of(NUMBER_2_BIG, null, 0),

                Arguments.of(NUMBER_1_SMALL, message(1), 3),
                Arguments.of(NUMBER_1_BIG, message(1), 3),
                Arguments.of(NUMBER_2_SMALL, message(1), 4),
                Arguments.of(NUMBER_2_BIG, message(1), 4),

                Arguments.of(NUMBER_1_SMALL, message(6), 8),
                Arguments.of(NUMBER_1_SMALL, message(127), 129),
                Arguments.of(NUMBER_1_SMALL, message(128), 131)
        );
    }

    @ParameterizedTest
    @MethodSource("messageListSource")
    void messageListSize(int number, List<TestMessage> values, int expectedSize) {
        // when then
        assertThat(testee.message(number, values)).isEqualTo(expectedSize);
    }

    private static Stream<Arguments> messageListSource() {
        return Stream.of(
                Arguments.of(NUMBER_1_SMALL, List.of(), 0),
                Arguments.of(NUMBER_2_BIG, List.of(), 0),

                Arguments.of(NUMBER_1_SMALL, List.of(message(1)), 3),
                Arguments.of(NUMBER_1_BIG, List.of(message(1)), 3),
                Arguments.of(NUMBER_2_SMALL, List.of(message(1)), 4),
                Arguments.of(NUMBER_2_BIG, List.of(message(1)), 4),

                Arguments.of(NUMBER_1_SMALL, List.of(message(4)), 6),

                Arguments.of(NUMBER_1_SMALL, List.of(message(1), message(1), message(0), message(3)), 13),
                Arguments.of(NUMBER_1_BIG, List.of(message(1), message(1), message(0), message(3)), 13),
                Arguments.of(NUMBER_2_SMALL, List.of(message(1), message(1), message(0), message(3)), 17),
                Arguments.of(NUMBER_2_BIG, List.of(message(1), message(1), message(0), message(3)), 17)
        );
    }

    public static ByteArray ba(int... bytes) {
        byte[] data = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            data[i] = (byte) bytes[i];
        }

        return ByteArray.fromByteArray(data);
    }

    public static ProtobufMessage<?> message(int size) {
        return new TestMessage(size);
    }

    private record TestMessage(int size) implements ProtobufMessage<TestMessage> {
        @Override
        public void writeTo(OutputStream output) {
        }

        @Override
        public byte[] toByteArray() {
            return new byte[0];
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public TestMessage merge(TestMessage toMerge) {
            return null;
        }

        @Override
        public int protobufSize(Size s) {
            return size;
        }
    }
}