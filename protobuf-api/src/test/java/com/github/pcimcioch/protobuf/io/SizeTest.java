package com.github.pcimcioch.protobuf.io;

import com.github.pcimcioch.protobuf.dto.ByteArray;
import com.github.pcimcioch.protobuf.dto.DoubleList;
import com.github.pcimcioch.protobuf.dto.IntList;
import com.github.pcimcioch.protobuf.dto.LongList;
import com.github.pcimcioch.protobuf.dto.ProtobufMessage;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.OutputStream;
import java.util.List;
import java.util.stream.Stream;

import static com.github.pcimcioch.protobuf.io.ByteUtils.ba;
import static org.assertj.core.api.Assertions.assertThat;

class SizeTest {
    private static final int NUMBER_1_SMALL = 1;
    private static final int NUMBER_1_BIG = 0b1111;
    private static final int NUMBER_2_SMALL = 0b10000;
    private static final int NUMBER_2_BIG = 0b11111111111;

    @Nested
    class Doubles {

        @ParameterizedTest
        @MethodSource("source")
        void size(int number, double value, int expectedSize) {
            // when then
            assertThat(Size.ofDouble(number, value)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> source() {
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
        @MethodSource("unpackedSource")
        void unpackedSize(int number, DoubleList values, int expectedSize) {
            // when then
            assertThat(Size.ofDoubleUnpacked(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> unpackedSource() {
            return Stream.of(
                    Arguments.of(NUMBER_1_SMALL, DoubleList.of(), 0),
                    Arguments.of(NUMBER_2_BIG, DoubleList.of(), 0),

                    Arguments.of(NUMBER_1_SMALL, DoubleList.of(1d), 9),
                    Arguments.of(NUMBER_1_BIG, DoubleList.of(1d), 9),
                    Arguments.of(NUMBER_2_SMALL, DoubleList.of(1d), 10),
                    Arguments.of(NUMBER_2_BIG, DoubleList.of(1d), 10),

                    Arguments.of(NUMBER_1_SMALL, DoubleList.of(Double.MIN_VALUE), 9),
                    Arguments.of(NUMBER_1_SMALL, DoubleList.of(Double.MAX_VALUE), 9),

                    Arguments.of(NUMBER_1_SMALL, DoubleList.of(1d, 2d, -1d, 0d), 36),
                    Arguments.of(NUMBER_1_BIG, DoubleList.of(1d, 2d, -1d, 0d), 36),
                    Arguments.of(NUMBER_2_SMALL, DoubleList.of(1d, 2d, -1d, 0d), 40),
                    Arguments.of(NUMBER_2_BIG, DoubleList.of(1d, 2d, -1d, 0d), 40)
            );
        }

        @ParameterizedTest
        @MethodSource("packedSource")
        void packedSize(int number, DoubleList values, int expectedSize) {
            // when then
            assertThat(Size.ofDoublePacked(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> packedSource() {
            return Stream.of(
                    Arguments.of(NUMBER_1_SMALL, DoubleList.of(), 0),
                    Arguments.of(NUMBER_2_BIG, DoubleList.of(), 0),

                    Arguments.of(NUMBER_1_SMALL, DoubleList.of(1d), 10),
                    Arguments.of(NUMBER_1_BIG, DoubleList.of(1d), 10),
                    Arguments.of(NUMBER_2_SMALL, DoubleList.of(1d), 11),
                    Arguments.of(NUMBER_2_BIG, DoubleList.of(1d), 11),

                    Arguments.of(NUMBER_1_SMALL, DoubleList.of(Double.MIN_VALUE), 10),
                    Arguments.of(NUMBER_1_SMALL, DoubleList.of(Double.MAX_VALUE), 10),

                    Arguments.of(NUMBER_1_SMALL, DoubleList.of(1d, 2d, -1d, 0d), 34),
                    Arguments.of(NUMBER_1_BIG, DoubleList.of(1d, 2d, -1d, 0d), 34),
                    Arguments.of(NUMBER_2_SMALL, DoubleList.of(1d, 2d, -1d, 0d), 35),
                    Arguments.of(NUMBER_2_BIG, DoubleList.of(1d, 2d, -1d, 0d), 35)
            );
        }

        @ParameterizedTest
        @MethodSource("packedNoTagSource")
        void packedNoTagSize(DoubleList values, int expectedSize) {
            // when then
            assertThat(Size.ofDoublePacked(values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> packedNoTagSource() {
            return Stream.of(
                    Arguments.of(DoubleList.of(), 0),

                    Arguments.of(DoubleList.of(1d), 8),
                    Arguments.of(DoubleList.of(Double.MIN_VALUE), 8),
                    Arguments.of(DoubleList.of(Double.MAX_VALUE), 8),

                    Arguments.of(DoubleList.of(1d, 2d, -1d, 0d), 32)
            );
        }
    }

    @Nested
    class Floats {

        @ParameterizedTest
        @MethodSource("source")
        void size(int number, float value, int expectedSize) {
            // when then
            assertThat(Size.ofFloat(number, value)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> source() {
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
        @MethodSource("unpackedSource")
        void unpackedSize(int number, List<Float> values, int expectedSize) {
            // when then
            assertThat(Size.ofFloatUnpacked(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> unpackedSource() {
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
        @MethodSource("packedSource")
        void packedSize(int number, List<Float> values, int expectedSize) {
            // when then
            assertThat(Size.ofFloatPacked(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> packedSource() {
            return Stream.of(
                    Arguments.of(NUMBER_1_SMALL, List.of(), 0),
                    Arguments.of(NUMBER_2_BIG, List.of(), 0),

                    Arguments.of(NUMBER_1_SMALL, List.of(1f), 6),
                    Arguments.of(NUMBER_1_BIG, List.of(1f), 6),
                    Arguments.of(NUMBER_2_SMALL, List.of(1f), 7),
                    Arguments.of(NUMBER_2_BIG, List.of(1f), 7),

                    Arguments.of(NUMBER_1_SMALL, List.of(Float.MIN_VALUE), 6),
                    Arguments.of(NUMBER_1_SMALL, List.of(Float.MAX_VALUE), 6),

                    Arguments.of(NUMBER_1_SMALL, List.of(1f, 2f, -1f, 0f), 18),
                    Arguments.of(NUMBER_1_BIG, List.of(1f, 2f, -1f, 0f), 18),
                    Arguments.of(NUMBER_2_SMALL, List.of(1f, 2f, -1f, 0f), 19),
                    Arguments.of(NUMBER_2_BIG, List.of(1f, 2f, -1f, 0f), 19)
            );
        }

        @ParameterizedTest
        @MethodSource("packedNoTagSource")
        void packedNoTagSize(List<Float> values, int expectedSize) {
            // when then
            assertThat(Size.ofFloatPacked(values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> packedNoTagSource() {
            return Stream.of(
                    Arguments.of(List.of(), 0),

                    Arguments.of(List.of(1f), 4),
                    Arguments.of(List.of(Float.MIN_VALUE), 4),
                    Arguments.of(List.of(Float.MAX_VALUE), 4),

                    Arguments.of(List.of(1f, 2f, -1f, 0f), 16)
            );
        }
    }

    @Nested
    // TODO remove
    class OldInt32 {

        @ParameterizedTest
        @MethodSource("source")
        void size(int number, int value, int expectedSize) {
            // when then
            assertThat(Size.ofInt32(number, value)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> source() {
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
        @MethodSource("unpackedSource")
        void unpackedSize(int number, List<Integer> values, int expectedSize) {
            // when then
            assertThat(Size.ofInt32Unpacked(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> unpackedSource() {
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
        @MethodSource("packedSource")
        void packedSize(int number, List<Integer> values, int expectedSize) {
            // when then
            assertThat(Size.ofInt32Packed(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> packedSource() {
            return Stream.of(
                    Arguments.of(NUMBER_1_SMALL, List.of(), 0),
                    Arguments.of(NUMBER_2_BIG, List.of(), 0),

                    Arguments.of(NUMBER_1_SMALL, List.of(1), 3),
                    Arguments.of(NUMBER_1_BIG, List.of(1), 3),
                    Arguments.of(NUMBER_2_SMALL, List.of(1), 4),
                    Arguments.of(NUMBER_2_BIG, List.of(1), 4),

                    Arguments.of(NUMBER_1_SMALL, List.of(Integer.MIN_VALUE), 12),
                    Arguments.of(NUMBER_1_SMALL, List.of(Integer.MAX_VALUE), 7),

                    Arguments.of(NUMBER_1_SMALL, List.of(1, 128, -1, 0), 16),
                    Arguments.of(NUMBER_1_BIG, List.of(1, 128, -1, 0), 16),
                    Arguments.of(NUMBER_2_SMALL, List.of(1, 128, -1, 0), 17),
                    Arguments.of(NUMBER_2_BIG, List.of(1, 128, -1, 0), 17)
            );
        }

        @ParameterizedTest
        @MethodSource("packedNoTagSource")
        void packedNoTagSize(List<Integer> values, int expectedSize) {
            // when then
            assertThat(Size.ofInt32Packed(values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> packedNoTagSource() {
            return Stream.of(
                    Arguments.of(List.of(), 0),

                    Arguments.of(List.of(1), 1),
                    Arguments.of(List.of(Integer.MIN_VALUE), 10),
                    Arguments.of(List.of(Integer.MAX_VALUE), 5),

                    Arguments.of(List.of(1, 128, -1, 0), 14)
            );
        }
    }

    @Nested
    class Int32 {

        @ParameterizedTest
        @MethodSource("source")
        void size(int number, int value, int expectedSize) {
            // when then
            assertThat(Size.ofInt32(number, value)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> source() {
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
        @MethodSource("unpackedSource")
        void unpackedSize(int number, IntList values, int expectedSize) {
            // when then
            assertThat(Size.ofInt32Unpacked(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> unpackedSource() {
            return Stream.of(
                    Arguments.of(NUMBER_1_SMALL, IntList.of(), 0),
                    Arguments.of(NUMBER_2_BIG, IntList.of(), 0),

                    Arguments.of(NUMBER_1_SMALL, IntList.of(1), 2),
                    Arguments.of(NUMBER_1_BIG, IntList.of(1), 2),
                    Arguments.of(NUMBER_2_SMALL, IntList.of(1), 3),
                    Arguments.of(NUMBER_2_BIG, IntList.of(1), 3),

                    Arguments.of(NUMBER_1_SMALL, IntList.of(Integer.MIN_VALUE), 11),
                    Arguments.of(NUMBER_1_SMALL, IntList.of(Integer.MAX_VALUE), 6),

                    Arguments.of(NUMBER_1_SMALL, IntList.of(1, 128, -1, 0), 18),
                    Arguments.of(NUMBER_1_BIG, IntList.of(1, 128, -1, 0), 18),
                    Arguments.of(NUMBER_2_SMALL, IntList.of(1, 128, -1, 0), 22),
                    Arguments.of(NUMBER_2_BIG, IntList.of(1, 128, -1, 0), 22)
            );
        }

        @ParameterizedTest
        @MethodSource("packedSource")
        void packedSize(int number, IntList values, int expectedSize) {
            // when then
            assertThat(Size.ofInt32Packed(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> packedSource() {
            return Stream.of(
                    Arguments.of(NUMBER_1_SMALL, IntList.of(), 0),
                    Arguments.of(NUMBER_2_BIG, IntList.of(), 0),

                    Arguments.of(NUMBER_1_SMALL, IntList.of(1), 3),
                    Arguments.of(NUMBER_1_BIG, IntList.of(1), 3),
                    Arguments.of(NUMBER_2_SMALL, IntList.of(1), 4),
                    Arguments.of(NUMBER_2_BIG, IntList.of(1), 4),

                    Arguments.of(NUMBER_1_SMALL, IntList.of(Integer.MIN_VALUE), 12),
                    Arguments.of(NUMBER_1_SMALL, IntList.of(Integer.MAX_VALUE), 7),

                    Arguments.of(NUMBER_1_SMALL, IntList.of(1, 128, -1, 0), 16),
                    Arguments.of(NUMBER_1_BIG, IntList.of(1, 128, -1, 0), 16),
                    Arguments.of(NUMBER_2_SMALL, IntList.of(1, 128, -1, 0), 17),
                    Arguments.of(NUMBER_2_BIG, IntList.of(1, 128, -1, 0), 17)
            );
        }

        @ParameterizedTest
        @MethodSource("packedNoTagSource")
        void packedNoTagSize(IntList values, int expectedSize) {
            // when then
            assertThat(Size.ofInt32Packed(values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> packedNoTagSource() {
            return Stream.of(
                    Arguments.of(IntList.of(), 0),

                    Arguments.of(IntList.of(1), 1),
                    Arguments.of(IntList.of(Integer.MIN_VALUE), 10),
                    Arguments.of(IntList.of(Integer.MAX_VALUE), 5),

                    Arguments.of(IntList.of(1, 128, -1, 0), 14)
            );
        }
    }

    @Nested
    // TODO remove
    class OldInt64 {

        @ParameterizedTest
        @MethodSource("source")
        void size(int number, long value, int expectedSize) {
            // when then
            assertThat(Size.ofInt64(number, value)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> source() {
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
        @MethodSource("unpackedSource")
        void unpackedSize(int number, List<Long> values, int expectedSize) {
            // when then
            assertThat(Size.ofInt64Unpacked(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> unpackedSource() {
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
        @MethodSource("packedSource")
        void packedSize(int number, List<Long> values, int expectedSize) {
            // when then
            assertThat(Size.ofInt64Packed(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> packedSource() {
            return Stream.of(
                    Arguments.of(NUMBER_1_SMALL, List.of(), 0),
                    Arguments.of(NUMBER_2_BIG, List.of(), 0),

                    Arguments.of(NUMBER_1_SMALL, List.of(1L), 3),
                    Arguments.of(NUMBER_1_BIG, List.of(1L), 3),
                    Arguments.of(NUMBER_2_SMALL, List.of(1L), 4),
                    Arguments.of(NUMBER_2_BIG, List.of(1L), 4),

                    Arguments.of(NUMBER_1_SMALL, List.of(Long.MIN_VALUE), 12),
                    Arguments.of(NUMBER_1_SMALL, List.of(Long.MAX_VALUE), 11),

                    Arguments.of(NUMBER_1_SMALL, List.of(1L, 128L, -1L, 0L), 16),
                    Arguments.of(NUMBER_1_BIG, List.of(1L, 128L, -1L, 0L), 16),
                    Arguments.of(NUMBER_2_SMALL, List.of(1L, 128L, -1L, 0L), 17),
                    Arguments.of(NUMBER_2_BIG, List.of(1L, 128L, -1L, 0L), 17)
            );
        }

        @ParameterizedTest
        @MethodSource("packedNoTagSource")
        void packedNoTagSize(List<Long> values, int expectedSize) {
            // when then
            assertThat(Size.ofInt64Packed(values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> packedNoTagSource() {
            return Stream.of(
                    Arguments.of(List.of(), 0),

                    Arguments.of(List.of(1L), 1),
                    Arguments.of(List.of(Long.MIN_VALUE), 10),
                    Arguments.of(List.of(Long.MAX_VALUE), 9),

                    Arguments.of(List.of(1L, 128L, -1L, 0L), 14)
            );
        }
    }

    @Nested
    class Int64 {

        @ParameterizedTest
        @MethodSource("source")
        void size(int number, long value, int expectedSize) {
            // when then
            assertThat(Size.ofInt64(number, value)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> source() {
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
        @MethodSource("unpackedSource")
        void unpackedSize(int number, LongList values, int expectedSize) {
            // when then
            assertThat(Size.ofInt64Unpacked(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> unpackedSource() {
            return Stream.of(
                    Arguments.of(NUMBER_1_SMALL, LongList.of(), 0),
                    Arguments.of(NUMBER_2_BIG, LongList.of(), 0),

                    Arguments.of(NUMBER_1_SMALL, LongList.of(1L), 2),
                    Arguments.of(NUMBER_1_BIG, LongList.of(1L), 2),
                    Arguments.of(NUMBER_2_SMALL, LongList.of(1L), 3),
                    Arguments.of(NUMBER_2_BIG, LongList.of(1L), 3),

                    Arguments.of(NUMBER_1_SMALL, LongList.of(Long.MIN_VALUE), 11),
                    Arguments.of(NUMBER_1_SMALL, LongList.of(Long.MAX_VALUE), 10),

                    Arguments.of(NUMBER_1_SMALL, LongList.of(1L, 128L, -1L, 0L), 18),
                    Arguments.of(NUMBER_1_BIG, LongList.of(1L, 128L, -1L, 0L), 18),
                    Arguments.of(NUMBER_2_SMALL, LongList.of(1L, 128L, -1L, 0L), 22),
                    Arguments.of(NUMBER_2_BIG, LongList.of(1L, 128L, -1L, 0L), 22)
            );
        }

        @ParameterizedTest
        @MethodSource("packedSource")
        void packedSize(int number, LongList values, int expectedSize) {
            // when then
            assertThat(Size.ofInt64Packed(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> packedSource() {
            return Stream.of(
                    Arguments.of(NUMBER_1_SMALL, LongList.of(), 0),
                    Arguments.of(NUMBER_2_BIG, LongList.of(), 0),

                    Arguments.of(NUMBER_1_SMALL, LongList.of(1L), 3),
                    Arguments.of(NUMBER_1_BIG, LongList.of(1L), 3),
                    Arguments.of(NUMBER_2_SMALL, LongList.of(1L), 4),
                    Arguments.of(NUMBER_2_BIG, LongList.of(1L), 4),

                    Arguments.of(NUMBER_1_SMALL, LongList.of(Long.MIN_VALUE), 12),
                    Arguments.of(NUMBER_1_SMALL, LongList.of(Long.MAX_VALUE), 11),

                    Arguments.of(NUMBER_1_SMALL, LongList.of(1L, 128L, -1L, 0L), 16),
                    Arguments.of(NUMBER_1_BIG, LongList.of(1L, 128L, -1L, 0L), 16),
                    Arguments.of(NUMBER_2_SMALL, LongList.of(1L, 128L, -1L, 0L), 17),
                    Arguments.of(NUMBER_2_BIG, LongList.of(1L, 128L, -1L, 0L), 17)
            );
        }

        @ParameterizedTest
        @MethodSource("packedNoTagSource")
        void packedNoTagSize(LongList values, int expectedSize) {
            // when then
            assertThat(Size.ofInt64Packed(values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> packedNoTagSource() {
            return Stream.of(
                    Arguments.of(LongList.of(), 0),

                    Arguments.of(LongList.of(1L), 1),
                    Arguments.of(LongList.of(Long.MIN_VALUE), 10),
                    Arguments.of(LongList.of(Long.MAX_VALUE), 9),

                    Arguments.of(LongList.of(1L, 128L, -1L, 0L), 14)
            );
        }
    }

    @Nested
    // TODO remove
    class OldUInt32 {

        @ParameterizedTest
        @MethodSource("source")
        void size(int number, int value, int expectedSize) {
            // when then
            assertThat(Size.ofUint32(number, value)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> source() {
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
        @MethodSource("unpackedSource")
        void unpackedSize(int number, List<Integer> values, int expectedSize) {
            // when then
            assertThat(Size.ofUint32Unpacked(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> unpackedSource() {
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
        @MethodSource("packedSource")
        void packedSize(int number, List<Integer> values, int expectedSize) {
            // when then
            assertThat(Size.ofUint32Packed(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> packedSource() {
            return Stream.of(
                    Arguments.of(NUMBER_1_SMALL, List.of(), 0),
                    Arguments.of(NUMBER_2_BIG, List.of(), 0),

                    Arguments.of(NUMBER_1_SMALL, List.of(1), 3),
                    Arguments.of(NUMBER_1_BIG, List.of(1), 3),
                    Arguments.of(NUMBER_2_SMALL, List.of(1), 4),
                    Arguments.of(NUMBER_2_BIG, List.of(1), 4),

                    Arguments.of(NUMBER_1_SMALL, List.of(Integer.MAX_VALUE), 7),

                    Arguments.of(NUMBER_1_SMALL, List.of(1, 128, 0), 6),
                    Arguments.of(NUMBER_1_BIG, List.of(1, 128, 0), 6),
                    Arguments.of(NUMBER_2_SMALL, List.of(1, 128, 0), 7),
                    Arguments.of(NUMBER_2_BIG, List.of(1, 128, 0), 7)
            );
        }

        @ParameterizedTest
        @MethodSource("packedNoTagSource")
        void packedNoTagSize(List<Integer> values, int expectedSize) {
            // when then
            assertThat(Size.ofUint32Packed(values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> packedNoTagSource() {
            return Stream.of(
                    Arguments.of(List.of(), 0),

                    Arguments.of(List.of(1), 1),
                    Arguments.of(List.of(Integer.MAX_VALUE), 5),

                    Arguments.of(List.of(1, 128, 0), 4)
            );
        }
    }

    @Nested
    class UInt32 {

        @ParameterizedTest
        @MethodSource("source")
        void size(int number, int value, int expectedSize) {
            // when then
            assertThat(Size.ofUint32(number, value)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> source() {
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
        @MethodSource("unpackedSource")
        void unpackedSize(int number, IntList values, int expectedSize) {
            // when then
            assertThat(Size.ofUint32Unpacked(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> unpackedSource() {
            return Stream.of(
                    Arguments.of(NUMBER_1_SMALL, IntList.of(), 0),
                    Arguments.of(NUMBER_2_BIG, IntList.of(), 0),

                    Arguments.of(NUMBER_1_SMALL, IntList.of(1), 2),
                    Arguments.of(NUMBER_1_BIG, IntList.of(1), 2),
                    Arguments.of(NUMBER_2_SMALL, IntList.of(1), 3),
                    Arguments.of(NUMBER_2_BIG, IntList.of(1), 3),

                    Arguments.of(NUMBER_1_SMALL, IntList.of(Integer.MAX_VALUE), 6),

                    Arguments.of(NUMBER_1_SMALL, IntList.of(1, 128, 0), 7),
                    Arguments.of(NUMBER_1_BIG, IntList.of(1, 128, 0), 7),
                    Arguments.of(NUMBER_2_SMALL, IntList.of(1, 128, 0), 10),
                    Arguments.of(NUMBER_2_BIG, IntList.of(1, 128, 0), 10)
            );
        }

        @ParameterizedTest
        @MethodSource("packedSource")
        void packedSize(int number, IntList values, int expectedSize) {
            // when then
            assertThat(Size.ofUint32Packed(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> packedSource() {
            return Stream.of(
                    Arguments.of(NUMBER_1_SMALL, IntList.of(), 0),
                    Arguments.of(NUMBER_2_BIG, IntList.of(), 0),

                    Arguments.of(NUMBER_1_SMALL, IntList.of(1), 3),
                    Arguments.of(NUMBER_1_BIG, IntList.of(1), 3),
                    Arguments.of(NUMBER_2_SMALL, IntList.of(1), 4),
                    Arguments.of(NUMBER_2_BIG, IntList.of(1), 4),

                    Arguments.of(NUMBER_1_SMALL, IntList.of(Integer.MAX_VALUE), 7),

                    Arguments.of(NUMBER_1_SMALL, IntList.of(1, 128, 0), 6),
                    Arguments.of(NUMBER_1_BIG, IntList.of(1, 128, 0), 6),
                    Arguments.of(NUMBER_2_SMALL, IntList.of(1, 128, 0), 7),
                    Arguments.of(NUMBER_2_BIG, IntList.of(1, 128, 0), 7)
            );
        }

        @ParameterizedTest
        @MethodSource("packedNoTagSource")
        void packedNoTagSize(IntList values, int expectedSize) {
            // when then
            assertThat(Size.ofUint32Packed(values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> packedNoTagSource() {
            return Stream.of(
                    Arguments.of(IntList.of(), 0),

                    Arguments.of(IntList.of(1), 1),
                    Arguments.of(IntList.of(Integer.MAX_VALUE), 5),

                    Arguments.of(IntList.of(1, 128, 0), 4)
            );
        }
    }

    @Nested
    // TODO remove
    class OldUInt64 {

        @ParameterizedTest
        @MethodSource("source")
        void size(int number, long value, int expectedSize) {
            // when then
            assertThat(Size.ofUint64(number, value)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> source() {
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
        @MethodSource("unpackedSource")
        void unpackedSize(int number, List<Long> values, int expectedSize) {
            // when then
            assertThat(Size.ofUint64Unpacked(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> unpackedSource() {
            return Stream.of(
                    Arguments.of(NUMBER_1_SMALL, List.of(), 0),
                    Arguments.of(NUMBER_2_BIG, List.of(), 0),

                    Arguments.of(NUMBER_1_SMALL, List.of(1L), 2),
                    Arguments.of(NUMBER_1_BIG, List.of(1L), 2),
                    Arguments.of(NUMBER_2_SMALL, List.of(1L), 3),
                    Arguments.of(NUMBER_2_BIG, List.of(1L), 3),

                    Arguments.of(NUMBER_1_SMALL, List.of(Long.MAX_VALUE), 10),

                    Arguments.of(NUMBER_1_SMALL, List.of(1L, 128L, 0L), 7),
                    Arguments.of(NUMBER_1_BIG, List.of(1L, 128L, 0L), 7),
                    Arguments.of(NUMBER_2_SMALL, List.of(1L, 128L, 0L), 10),
                    Arguments.of(NUMBER_2_BIG, List.of(1L, 128L, 0L), 10)
            );
        }

        @ParameterizedTest
        @MethodSource("packedSource")
        void packedSize(int number, List<Long> values, int expectedSize) {
            // when then
            assertThat(Size.ofUint64Packed(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> packedSource() {
            return Stream.of(
                    Arguments.of(NUMBER_1_SMALL, List.of(), 0),
                    Arguments.of(NUMBER_2_BIG, List.of(), 0),

                    Arguments.of(NUMBER_1_SMALL, List.of(1L), 3),
                    Arguments.of(NUMBER_1_BIG, List.of(1L), 3),
                    Arguments.of(NUMBER_2_SMALL, List.of(1L), 4),
                    Arguments.of(NUMBER_2_BIG, List.of(1L), 4),

                    Arguments.of(NUMBER_1_SMALL, List.of(Long.MAX_VALUE), 11),

                    Arguments.of(NUMBER_1_SMALL, List.of(1L, 128L, 0L), 6),
                    Arguments.of(NUMBER_1_BIG, List.of(1L, 128L, 0L), 6),
                    Arguments.of(NUMBER_2_SMALL, List.of(1L, 128L, 0L), 7),
                    Arguments.of(NUMBER_2_BIG, List.of(1L, 128L, 0L), 7)
            );
        }

        @ParameterizedTest
        @MethodSource("packedNoTagSource")
        void packedNoTagSize(List<Long> values, int expectedSize) {
            // when then
            assertThat(Size.ofUint64Packed(values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> packedNoTagSource() {
            return Stream.of(
                    Arguments.of(List.of(), 0),

                    Arguments.of(List.of(1L), 1),
                    Arguments.of(List.of(Long.MAX_VALUE), 9),

                    Arguments.of(List.of(1L, 128L, 0L), 4)
            );
        }
    }

    @Nested
    class UInt64 {

        @ParameterizedTest
        @MethodSource("source")
        void size(int number, long value, int expectedSize) {
            // when then
            assertThat(Size.ofUint64(number, value)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> source() {
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
        @MethodSource("unpackedSource")
        void unpackedSize(int number, LongList values, int expectedSize) {
            // when then
            assertThat(Size.ofUint64Unpacked(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> unpackedSource() {
            return Stream.of(
                    Arguments.of(NUMBER_1_SMALL, LongList.of(), 0),
                    Arguments.of(NUMBER_2_BIG, LongList.of(), 0),

                    Arguments.of(NUMBER_1_SMALL, LongList.of(1L), 2),
                    Arguments.of(NUMBER_1_BIG, LongList.of(1L), 2),
                    Arguments.of(NUMBER_2_SMALL, LongList.of(1L), 3),
                    Arguments.of(NUMBER_2_BIG, LongList.of(1L), 3),

                    Arguments.of(NUMBER_1_SMALL, LongList.of(Long.MAX_VALUE), 10),

                    Arguments.of(NUMBER_1_SMALL, LongList.of(1L, 128L, 0L), 7),
                    Arguments.of(NUMBER_1_BIG, LongList.of(1L, 128L, 0L), 7),
                    Arguments.of(NUMBER_2_SMALL, LongList.of(1L, 128L, 0L), 10),
                    Arguments.of(NUMBER_2_BIG, LongList.of(1L, 128L, 0L), 10)
            );
        }

        @ParameterizedTest
        @MethodSource("packedSource")
        void packedSize(int number, LongList values, int expectedSize) {
            // when then
            assertThat(Size.ofUint64Packed(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> packedSource() {
            return Stream.of(
                    Arguments.of(NUMBER_1_SMALL, LongList.of(), 0),
                    Arguments.of(NUMBER_2_BIG, LongList.of(), 0),

                    Arguments.of(NUMBER_1_SMALL, LongList.of(1L), 3),
                    Arguments.of(NUMBER_1_BIG, LongList.of(1L), 3),
                    Arguments.of(NUMBER_2_SMALL, LongList.of(1L), 4),
                    Arguments.of(NUMBER_2_BIG, LongList.of(1L), 4),

                    Arguments.of(NUMBER_1_SMALL, LongList.of(Long.MAX_VALUE), 11),

                    Arguments.of(NUMBER_1_SMALL, LongList.of(1L, 128L, 0L), 6),
                    Arguments.of(NUMBER_1_BIG, LongList.of(1L, 128L, 0L), 6),
                    Arguments.of(NUMBER_2_SMALL, LongList.of(1L, 128L, 0L), 7),
                    Arguments.of(NUMBER_2_BIG, LongList.of(1L, 128L, 0L), 7)
            );
        }

        @ParameterizedTest
        @MethodSource("packedNoTagSource")
        void packedNoTagSize(LongList values, int expectedSize) {
            // when then
            assertThat(Size.ofUint64Packed(values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> packedNoTagSource() {
            return Stream.of(
                    Arguments.of(LongList.of(), 0),

                    Arguments.of(LongList.of(1L), 1),
                    Arguments.of(LongList.of(Long.MAX_VALUE), 9),

                    Arguments.of(LongList.of(1L, 128L, 0L), 4)
            );
        }
    }

    @Nested
    // TODO remove
    class OldSInt32 {

        @ParameterizedTest
        @MethodSource("source")
        void size(int number, int value, int expectedSize) {
            // when then
            assertThat(Size.ofSint32(number, value)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> source() {
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
        @MethodSource("unpackedSource")
        void unpackedSize(int number, List<Integer> values, int expectedSize) {
            // when then
            assertThat(Size.ofSint32Unpacked(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> unpackedSource() {
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
        @MethodSource("packedSource")
        void packedSize(int number, List<Integer> values, int expectedSize) {
            // when then
            assertThat(Size.ofSint32Packed(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> packedSource() {
            return Stream.of(
                    Arguments.of(NUMBER_1_SMALL, List.of(), 0),
                    Arguments.of(NUMBER_2_BIG, List.of(), 0),

                    Arguments.of(NUMBER_1_SMALL, List.of(1), 3),
                    Arguments.of(NUMBER_1_BIG, List.of(1), 3),
                    Arguments.of(NUMBER_2_SMALL, List.of(1), 4),
                    Arguments.of(NUMBER_2_BIG, List.of(1), 4),

                    Arguments.of(NUMBER_1_SMALL, List.of(Integer.MIN_VALUE), 12),
                    Arguments.of(NUMBER_1_SMALL, List.of(Integer.MAX_VALUE), 12),

                    Arguments.of(NUMBER_1_SMALL, List.of(1, 64, -8193, 0), 9),
                    Arguments.of(NUMBER_1_BIG, List.of(1, 64, -8193, 0), 9),
                    Arguments.of(NUMBER_2_SMALL, List.of(1, 64, -8193, 0), 10),
                    Arguments.of(NUMBER_2_BIG, List.of(1, 64, -8193, 0), 10)
            );
        }

        @ParameterizedTest
        @MethodSource("packedNoTagSource")
        void packedNoTagSize(List<Integer> values, int expectedSize) {
            // when then
            assertThat(Size.ofSint32Packed(values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> packedNoTagSource() {
            return Stream.of(
                    Arguments.of(List.of(), 0),

                    Arguments.of(List.of(1), 1),
                    Arguments.of(List.of(Integer.MIN_VALUE), 10),
                    Arguments.of(List.of(Integer.MAX_VALUE), 10),

                    Arguments.of(List.of(1, 64, -8193, 0), 7)
            );
        }
    }

    @Nested
    class SInt32 {

        @ParameterizedTest
        @MethodSource("source")
        void size(int number, int value, int expectedSize) {
            // when then
            assertThat(Size.ofSint32(number, value)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> source() {
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
        @MethodSource("unpackedSource")
        void unpackedSize(int number, IntList values, int expectedSize) {
            // when then
            assertThat(Size.ofSint32Unpacked(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> unpackedSource() {
            return Stream.of(
                    Arguments.of(NUMBER_1_SMALL, IntList.of(), 0),
                    Arguments.of(NUMBER_2_BIG, IntList.of(), 0),

                    Arguments.of(NUMBER_1_SMALL, IntList.of(1), 2),
                    Arguments.of(NUMBER_1_BIG, IntList.of(1), 2),
                    Arguments.of(NUMBER_2_SMALL, IntList.of(1), 3),
                    Arguments.of(NUMBER_2_BIG, IntList.of(1), 3),

                    Arguments.of(NUMBER_1_SMALL, IntList.of(Integer.MIN_VALUE), 11),
                    Arguments.of(NUMBER_1_SMALL, IntList.of(Integer.MAX_VALUE), 11),

                    Arguments.of(NUMBER_1_SMALL, IntList.of(1, 64, -8193, 0), 11),
                    Arguments.of(NUMBER_1_BIG, IntList.of(1, 64, -8193, 0), 11),
                    Arguments.of(NUMBER_2_SMALL, IntList.of(1, 64, -8193, 0), 15),
                    Arguments.of(NUMBER_2_BIG, IntList.of(1, 64, -8193, 0), 15)
            );
        }

        @ParameterizedTest
        @MethodSource("packedSource")
        void packedSize(int number, IntList values, int expectedSize) {
            // when then
            assertThat(Size.ofSint32Packed(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> packedSource() {
            return Stream.of(
                    Arguments.of(NUMBER_1_SMALL, IntList.of(), 0),
                    Arguments.of(NUMBER_2_BIG, IntList.of(), 0),

                    Arguments.of(NUMBER_1_SMALL, IntList.of(1), 3),
                    Arguments.of(NUMBER_1_BIG, IntList.of(1), 3),
                    Arguments.of(NUMBER_2_SMALL, IntList.of(1), 4),
                    Arguments.of(NUMBER_2_BIG, IntList.of(1), 4),

                    Arguments.of(NUMBER_1_SMALL, IntList.of(Integer.MIN_VALUE), 12),
                    Arguments.of(NUMBER_1_SMALL, IntList.of(Integer.MAX_VALUE), 12),

                    Arguments.of(NUMBER_1_SMALL, IntList.of(1, 64, -8193, 0), 9),
                    Arguments.of(NUMBER_1_BIG, IntList.of(1, 64, -8193, 0), 9),
                    Arguments.of(NUMBER_2_SMALL, IntList.of(1, 64, -8193, 0), 10),
                    Arguments.of(NUMBER_2_BIG, IntList.of(1, 64, -8193, 0), 10)
            );
        }

        @ParameterizedTest
        @MethodSource("packedNoTagSource")
        void packedNoTagSize(IntList values, int expectedSize) {
            // when then
            assertThat(Size.ofSint32Packed(values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> packedNoTagSource() {
            return Stream.of(
                    Arguments.of(IntList.of(), 0),

                    Arguments.of(IntList.of(1), 1),
                    Arguments.of(IntList.of(Integer.MIN_VALUE), 10),
                    Arguments.of(IntList.of(Integer.MAX_VALUE), 10),

                    Arguments.of(IntList.of(1, 64, -8193, 0), 7)
            );
        }
    }

    @Nested
    // TODO remove
    class OldSInt64 {

        @ParameterizedTest
        @MethodSource("source")
        void size(int number, long value, int expectedSize) {
            // when then
            assertThat(Size.ofSint64(number, value)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> source() {
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
        @MethodSource("unpackedSource")
        void unpackedSize(int number, List<Long> values, int expectedSize) {
            // when then
            assertThat(Size.ofSint64Unpacked(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> unpackedSource() {
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
        @MethodSource("packedSource")
        void packedSize(int number, List<Long> values, int expectedSize) {
            // when then
            assertThat(Size.ofSint64Packed(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> packedSource() {
            return Stream.of(
                    Arguments.of(NUMBER_1_SMALL, List.of(), 0),
                    Arguments.of(NUMBER_2_BIG, List.of(), 0),

                    Arguments.of(NUMBER_1_SMALL, List.of(1L), 3),
                    Arguments.of(NUMBER_1_BIG, List.of(1L), 3),
                    Arguments.of(NUMBER_2_SMALL, List.of(1L), 4),
                    Arguments.of(NUMBER_2_BIG, List.of(1L), 4),

                    Arguments.of(NUMBER_1_SMALL, List.of(Long.MIN_VALUE), 12),
                    Arguments.of(NUMBER_1_SMALL, List.of(Long.MAX_VALUE), 12),

                    Arguments.of(NUMBER_1_SMALL, List.of(1L, 64L, -8193L, 0L), 9),
                    Arguments.of(NUMBER_1_BIG, List.of(1L, 64L, -8193L, 0L), 9),
                    Arguments.of(NUMBER_2_SMALL, List.of(1L, 64L, -8193L, 0L), 10),
                    Arguments.of(NUMBER_2_BIG, List.of(1L, 64L, -8193L, 0L), 10)
            );
        }

        @ParameterizedTest
        @MethodSource("packedNoTagSource")
        void packedNoTagSize(List<Long> values, int expectedSize) {
            // when then
            assertThat(Size.ofSint64Packed(values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> packedNoTagSource() {
            return Stream.of(
                    Arguments.of(List.of(), 0),

                    Arguments.of(List.of(1L), 1),
                    Arguments.of(List.of(Long.MIN_VALUE), 10),
                    Arguments.of(List.of(Long.MAX_VALUE), 10),

                    Arguments.of(List.of(1L, 64L, -8193L, 0L), 7)
            );
        }
    }

    @Nested
    class SInt64 {

        @ParameterizedTest
        @MethodSource("source")
        void size(int number, long value, int expectedSize) {
            // when then
            assertThat(Size.ofSint64(number, value)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> source() {
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
        @MethodSource("unpackedSource")
        void unpackedSize(int number, LongList values, int expectedSize) {
            // when then
            assertThat(Size.ofSint64Unpacked(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> unpackedSource() {
            return Stream.of(
                    Arguments.of(NUMBER_1_SMALL, LongList.of(), 0),
                    Arguments.of(NUMBER_2_BIG, LongList.of(), 0),

                    Arguments.of(NUMBER_1_SMALL, LongList.of(1L), 2),
                    Arguments.of(NUMBER_1_BIG, LongList.of(1L), 2),
                    Arguments.of(NUMBER_2_SMALL, LongList.of(1L), 3),
                    Arguments.of(NUMBER_2_BIG, LongList.of(1L), 3),

                    Arguments.of(NUMBER_1_SMALL, LongList.of(Long.MIN_VALUE), 11),
                    Arguments.of(NUMBER_1_SMALL, LongList.of(Long.MAX_VALUE), 11),

                    Arguments.of(NUMBER_1_SMALL, LongList.of(1L, 64L, -8193L, 0L), 11),
                    Arguments.of(NUMBER_1_BIG, LongList.of(1L, 64L, -8193L, 0L), 11),
                    Arguments.of(NUMBER_2_SMALL, LongList.of(1L, 64L, -8193L, 0L), 15),
                    Arguments.of(NUMBER_2_BIG, LongList.of(1L, 64L, -8193L, 0L), 15)
            );
        }

        @ParameterizedTest
        @MethodSource("packedSource")
        void packedSize(int number, LongList values, int expectedSize) {
            // when then
            assertThat(Size.ofSint64Packed(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> packedSource() {
            return Stream.of(
                    Arguments.of(NUMBER_1_SMALL, LongList.of(), 0),
                    Arguments.of(NUMBER_2_BIG, LongList.of(), 0),

                    Arguments.of(NUMBER_1_SMALL, LongList.of(1L), 3),
                    Arguments.of(NUMBER_1_BIG, LongList.of(1L), 3),
                    Arguments.of(NUMBER_2_SMALL, LongList.of(1L), 4),
                    Arguments.of(NUMBER_2_BIG, LongList.of(1L), 4),

                    Arguments.of(NUMBER_1_SMALL, LongList.of(Long.MIN_VALUE), 12),
                    Arguments.of(NUMBER_1_SMALL, LongList.of(Long.MAX_VALUE), 12),

                    Arguments.of(NUMBER_1_SMALL, LongList.of(1L, 64L, -8193L, 0L), 9),
                    Arguments.of(NUMBER_1_BIG, LongList.of(1L, 64L, -8193L, 0L), 9),
                    Arguments.of(NUMBER_2_SMALL, LongList.of(1L, 64L, -8193L, 0L), 10),
                    Arguments.of(NUMBER_2_BIG, LongList.of(1L, 64L, -8193L, 0L), 10)
            );
        }

        @ParameterizedTest
        @MethodSource("packedNoTagSource")
        void packedNoTagSize(LongList values, int expectedSize) {
            // when then
            assertThat(Size.ofSint64Packed(values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> packedNoTagSource() {
            return Stream.of(
                    Arguments.of(LongList.of(), 0),

                    Arguments.of(LongList.of(1L), 1),
                    Arguments.of(LongList.of(Long.MIN_VALUE), 10),
                    Arguments.of(LongList.of(Long.MAX_VALUE), 10),

                    Arguments.of(LongList.of(1L, 64L, -8193L, 0L), 7)
            );
        }
    }

    @Nested
    // TODO remove
    class OldFixed32 {

        @ParameterizedTest
        @MethodSource("source")
        void size(int number, int value, int expectedSize) {
            // when then
            assertThat(Size.ofFixed32(number, value)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> source() {
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
        @MethodSource("unpackedSource")
        void unpackedSize(int number, List<Integer> values, int expectedSize) {
            // when then
            assertThat(Size.ofFixed32Unpacked(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> unpackedSource() {
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
        @MethodSource("packedSource")
        void packedSize(int number, List<Integer> values, int expectedSize) {
            // when then
            assertThat(Size.ofFixed32Packed(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> packedSource() {
            return Stream.of(
                    Arguments.of(NUMBER_1_SMALL, List.of(), 0),
                    Arguments.of(NUMBER_2_BIG, List.of(), 0),

                    Arguments.of(NUMBER_1_SMALL, List.of(1), 6),
                    Arguments.of(NUMBER_1_BIG, List.of(1), 6),
                    Arguments.of(NUMBER_2_SMALL, List.of(1), 7),
                    Arguments.of(NUMBER_2_BIG, List.of(1), 7),

                    Arguments.of(NUMBER_1_SMALL, List.of(Integer.MIN_VALUE), 6),
                    Arguments.of(NUMBER_1_SMALL, List.of(Integer.MAX_VALUE), 6),

                    Arguments.of(NUMBER_1_SMALL, List.of(1, 2, -1, 0), 18),
                    Arguments.of(NUMBER_1_BIG, List.of(1, 2, -1, 0), 18),
                    Arguments.of(NUMBER_2_SMALL, List.of(1, 2, -1, 0), 19),
                    Arguments.of(NUMBER_2_BIG, List.of(1, 2, -1, 0), 19)
            );
        }

        @ParameterizedTest
        @MethodSource("packedNoTagSource")
        void packedNoTagSize(List<Integer> values, int expectedSize) {
            // when then
            assertThat(Size.ofFixed32Packed(values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> packedNoTagSource() {
            return Stream.of(
                    Arguments.of(List.of(), 0),

                    Arguments.of(List.of(1), 4),
                    Arguments.of(List.of(Integer.MIN_VALUE), 4),
                    Arguments.of(List.of(Integer.MAX_VALUE), 4),

                    Arguments.of(List.of(1, 2, -1, 0), 16)
            );
        }
    }

    @Nested
    class Fixed32 {

        @ParameterizedTest
        @MethodSource("source")
        void size(int number, int value, int expectedSize) {
            // when then
            assertThat(Size.ofFixed32(number, value)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> source() {
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
        @MethodSource("unpackedSource")
        void unpackedSize(int number, IntList values, int expectedSize) {
            // when then
            assertThat(Size.ofFixed32Unpacked(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> unpackedSource() {
            return Stream.of(
                    Arguments.of(NUMBER_1_SMALL, IntList.of(), 0),
                    Arguments.of(NUMBER_2_BIG, IntList.of(), 0),

                    Arguments.of(NUMBER_1_SMALL, IntList.of(1), 5),
                    Arguments.of(NUMBER_1_BIG, IntList.of(1), 5),
                    Arguments.of(NUMBER_2_SMALL, IntList.of(1), 6),
                    Arguments.of(NUMBER_2_BIG, IntList.of(1), 6),

                    Arguments.of(NUMBER_1_SMALL, IntList.of(Integer.MIN_VALUE), 5),
                    Arguments.of(NUMBER_1_SMALL, IntList.of(Integer.MAX_VALUE), 5),

                    Arguments.of(NUMBER_1_SMALL, IntList.of(1, 2, -1, 0), 20),
                    Arguments.of(NUMBER_1_BIG, IntList.of(1, 2, -1, 0), 20),
                    Arguments.of(NUMBER_2_SMALL, IntList.of(1, 2, -1, 0), 24),
                    Arguments.of(NUMBER_2_BIG, IntList.of(1, 2, -1, 0), 24)
            );
        }

        @ParameterizedTest
        @MethodSource("packedSource")
        void packedSize(int number, IntList values, int expectedSize) {
            // when then
            assertThat(Size.ofFixed32Packed(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> packedSource() {
            return Stream.of(
                    Arguments.of(NUMBER_1_SMALL, IntList.of(), 0),
                    Arguments.of(NUMBER_2_BIG, IntList.of(), 0),

                    Arguments.of(NUMBER_1_SMALL, IntList.of(1), 6),
                    Arguments.of(NUMBER_1_BIG, IntList.of(1), 6),
                    Arguments.of(NUMBER_2_SMALL, IntList.of(1), 7),
                    Arguments.of(NUMBER_2_BIG, IntList.of(1), 7),

                    Arguments.of(NUMBER_1_SMALL, IntList.of(Integer.MIN_VALUE), 6),
                    Arguments.of(NUMBER_1_SMALL, IntList.of(Integer.MAX_VALUE), 6),

                    Arguments.of(NUMBER_1_SMALL, IntList.of(1, 2, -1, 0), 18),
                    Arguments.of(NUMBER_1_BIG, IntList.of(1, 2, -1, 0), 18),
                    Arguments.of(NUMBER_2_SMALL, IntList.of(1, 2, -1, 0), 19),
                    Arguments.of(NUMBER_2_BIG, IntList.of(1, 2, -1, 0), 19)
            );
        }

        @ParameterizedTest
        @MethodSource("packedNoTagSource")
        void packedNoTagSize(IntList values, int expectedSize) {
            // when then
            assertThat(Size.ofFixed32Packed(values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> packedNoTagSource() {
            return Stream.of(
                    Arguments.of(IntList.of(), 0),

                    Arguments.of(IntList.of(1), 4),
                    Arguments.of(IntList.of(Integer.MIN_VALUE), 4),
                    Arguments.of(IntList.of(Integer.MAX_VALUE), 4),

                    Arguments.of(IntList.of(1, 2, -1, 0), 16)
            );
        }
    }

    @Nested
    // TODO remove
    class OldFixed64 {

        @ParameterizedTest
        @MethodSource("source")
        void size(int number, long value, int expectedSize) {
            // when then
            assertThat(Size.ofFixed64(number, value)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> source() {
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
        @MethodSource("unpackedSource")
        void unpackedSize(int number, List<Long> values, int expectedSize) {
            // when then
            assertThat(Size.ofFixed64Unpacked(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> unpackedSource() {
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
        @MethodSource("packedSource")
        void packedSize(int number, List<Long> values, int expectedSize) {
            // when then
            assertThat(Size.ofFixed64Packed(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> packedSource() {
            return Stream.of(
                    Arguments.of(NUMBER_1_SMALL, List.of(), 0),
                    Arguments.of(NUMBER_2_BIG, List.of(), 0),

                    Arguments.of(NUMBER_1_SMALL, List.of(1L), 10),
                    Arguments.of(NUMBER_1_BIG, List.of(1L), 10),
                    Arguments.of(NUMBER_2_SMALL, List.of(1L), 11),
                    Arguments.of(NUMBER_2_BIG, List.of(1L), 11),

                    Arguments.of(NUMBER_1_SMALL, List.of(Long.MIN_VALUE), 10),
                    Arguments.of(NUMBER_1_SMALL, List.of(Long.MAX_VALUE), 10),

                    Arguments.of(NUMBER_1_SMALL, List.of(1L, 2L, -1L, 0L), 34),
                    Arguments.of(NUMBER_1_BIG, List.of(1L, 2L, -1L, 0L), 34),
                    Arguments.of(NUMBER_2_SMALL, List.of(1L, 2L, -1L, 0L), 35),
                    Arguments.of(NUMBER_2_BIG, List.of(1L, 2L, -1L, 0L), 35)
            );
        }

        @ParameterizedTest
        @MethodSource("packedNoTagSource")
        void packedNoTagSize(List<Long> values, int expectedSize) {
            // when then
            assertThat(Size.ofFixed64Packed(values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> packedNoTagSource() {
            return Stream.of(
                    Arguments.of(List.of(), 0),

                    Arguments.of(List.of(1L), 8),
                    Arguments.of(List.of(Long.MIN_VALUE), 8),
                    Arguments.of(List.of(Long.MAX_VALUE), 8),

                    Arguments.of(List.of(1L, 2L, -1L, 0L), 32)
            );
        }
    }

    @Nested
    class Fixed64 {

        @ParameterizedTest
        @MethodSource("source")
        void size(int number, long value, int expectedSize) {
            // when then
            assertThat(Size.ofFixed64(number, value)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> source() {
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
        @MethodSource("unpackedSource")
        void unpackedSize(int number, LongList values, int expectedSize) {
            // when then
            assertThat(Size.ofFixed64Unpacked(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> unpackedSource() {
            return Stream.of(
                    Arguments.of(NUMBER_1_SMALL, LongList.of(), 0),
                    Arguments.of(NUMBER_2_BIG, LongList.of(), 0),

                    Arguments.of(NUMBER_1_SMALL, LongList.of(1L), 9),
                    Arguments.of(NUMBER_1_BIG, LongList.of(1L), 9),
                    Arguments.of(NUMBER_2_SMALL, LongList.of(1L), 10),
                    Arguments.of(NUMBER_2_BIG, LongList.of(1L), 10),

                    Arguments.of(NUMBER_1_SMALL, LongList.of(Long.MIN_VALUE), 9),
                    Arguments.of(NUMBER_1_SMALL, LongList.of(Long.MAX_VALUE), 9),

                    Arguments.of(NUMBER_1_SMALL, LongList.of(1L, 2L, -1L, 0L), 36),
                    Arguments.of(NUMBER_1_BIG, LongList.of(1L, 2L, -1L, 0L), 36),
                    Arguments.of(NUMBER_2_SMALL, LongList.of(1L, 2L, -1L, 0L), 40),
                    Arguments.of(NUMBER_2_BIG, LongList.of(1L, 2L, -1L, 0L), 40)
            );
        }

        @ParameterizedTest
        @MethodSource("packedSource")
        void packedSize(int number, LongList values, int expectedSize) {
            // when then
            assertThat(Size.ofFixed64Packed(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> packedSource() {
            return Stream.of(
                    Arguments.of(NUMBER_1_SMALL, LongList.of(), 0),
                    Arguments.of(NUMBER_2_BIG, LongList.of(), 0),

                    Arguments.of(NUMBER_1_SMALL, LongList.of(1L), 10),
                    Arguments.of(NUMBER_1_BIG, LongList.of(1L), 10),
                    Arguments.of(NUMBER_2_SMALL, LongList.of(1L), 11),
                    Arguments.of(NUMBER_2_BIG, LongList.of(1L), 11),

                    Arguments.of(NUMBER_1_SMALL, LongList.of(Long.MIN_VALUE), 10),
                    Arguments.of(NUMBER_1_SMALL, LongList.of(Long.MAX_VALUE), 10),

                    Arguments.of(NUMBER_1_SMALL, LongList.of(1L, 2L, -1L, 0L), 34),
                    Arguments.of(NUMBER_1_BIG, LongList.of(1L, 2L, -1L, 0L), 34),
                    Arguments.of(NUMBER_2_SMALL, LongList.of(1L, 2L, -1L, 0L), 35),
                    Arguments.of(NUMBER_2_BIG, LongList.of(1L, 2L, -1L, 0L), 35)
            );
        }

        @ParameterizedTest
        @MethodSource("packedNoTagSource")
        void packedNoTagSize(LongList values, int expectedSize) {
            // when then
            assertThat(Size.ofFixed64Packed(values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> packedNoTagSource() {
            return Stream.of(
                    Arguments.of(LongList.of(), 0),

                    Arguments.of(LongList.of(1L), 8),
                    Arguments.of(LongList.of(Long.MIN_VALUE), 8),
                    Arguments.of(LongList.of(Long.MAX_VALUE), 8),

                    Arguments.of(LongList.of(1L, 2L, -1L, 0L), 32)
            );
        }
    }

    @Nested
    // TODO remove
    class OldSFixed32 {

        @ParameterizedTest
        @MethodSource("source")
        void size(int number, int value, int expectedSize) {
            // when then
            assertThat(Size.ofSfixed32(number, value)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> source() {
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
        @MethodSource("unpackedSource")
        void unpackedSize(int number, List<Integer> values, int expectedSize) {
            // when then
            assertThat(Size.ofSfixed32Unpacked(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> unpackedSource() {
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
        @MethodSource("packedSource")
        void packedSize(int number, List<Integer> values, int expectedSize) {
            // when then
            assertThat(Size.ofSfixed32Packed(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> packedSource() {
            return Stream.of(
                    Arguments.of(NUMBER_1_SMALL, List.of(), 0),
                    Arguments.of(NUMBER_2_BIG, List.of(), 0),

                    Arguments.of(NUMBER_1_SMALL, List.of(1), 6),
                    Arguments.of(NUMBER_1_BIG, List.of(1), 6),
                    Arguments.of(NUMBER_2_SMALL, List.of(1), 7),
                    Arguments.of(NUMBER_2_BIG, List.of(1), 7),

                    Arguments.of(NUMBER_1_SMALL, List.of(Integer.MIN_VALUE), 6),
                    Arguments.of(NUMBER_1_SMALL, List.of(Integer.MAX_VALUE), 6),

                    Arguments.of(NUMBER_1_SMALL, List.of(1, 2, -1, 0), 18),
                    Arguments.of(NUMBER_1_BIG, List.of(1, 2, -1, 0), 18),
                    Arguments.of(NUMBER_2_SMALL, List.of(1, 2, -1, 0), 19),
                    Arguments.of(NUMBER_2_BIG, List.of(1, 2, -1, 0), 19)
            );
        }

        @ParameterizedTest
        @MethodSource("packedNoTagSource")
        void packedNoTagSize(List<Integer> values, int expectedSize) {
            // when then
            assertThat(Size.ofSfixed32Packed(values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> packedNoTagSource() {
            return Stream.of(
                    Arguments.of(List.of(), 0),

                    Arguments.of(List.of(1), 4),
                    Arguments.of(List.of(Integer.MIN_VALUE), 4),
                    Arguments.of(List.of(Integer.MAX_VALUE), 4),

                    Arguments.of(List.of(1, 2, -1, 0), 16)
            );
        }
    }

    @Nested
    class SFixed32 {

        @ParameterizedTest
        @MethodSource("source")
        void size(int number, int value, int expectedSize) {
            // when then
            assertThat(Size.ofSfixed32(number, value)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> source() {
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
        @MethodSource("unpackedSource")
        void unpackedSize(int number, IntList values, int expectedSize) {
            // when then
            assertThat(Size.ofSfixed32Unpacked(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> unpackedSource() {
            return Stream.of(
                    Arguments.of(NUMBER_1_SMALL, IntList.of(), 0),
                    Arguments.of(NUMBER_2_BIG, IntList.of(), 0),

                    Arguments.of(NUMBER_1_SMALL, IntList.of(1), 5),
                    Arguments.of(NUMBER_1_BIG, IntList.of(1), 5),
                    Arguments.of(NUMBER_2_SMALL, IntList.of(1), 6),
                    Arguments.of(NUMBER_2_BIG, IntList.of(1), 6),

                    Arguments.of(NUMBER_1_SMALL, IntList.of(Integer.MIN_VALUE), 5),
                    Arguments.of(NUMBER_1_SMALL, IntList.of(Integer.MAX_VALUE), 5),

                    Arguments.of(NUMBER_1_SMALL, IntList.of(1, 2, -1, 0), 20),
                    Arguments.of(NUMBER_1_BIG, IntList.of(1, 2, -1, 0), 20),
                    Arguments.of(NUMBER_2_SMALL, IntList.of(1, 2, -1, 0), 24),
                    Arguments.of(NUMBER_2_BIG, IntList.of(1, 2, -1, 0), 24)
            );
        }

        @ParameterizedTest
        @MethodSource("packedSource")
        void packedSize(int number, IntList values, int expectedSize) {
            // when then
            assertThat(Size.ofSfixed32Packed(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> packedSource() {
            return Stream.of(
                    Arguments.of(NUMBER_1_SMALL, IntList.of(), 0),
                    Arguments.of(NUMBER_2_BIG, IntList.of(), 0),

                    Arguments.of(NUMBER_1_SMALL, IntList.of(1), 6),
                    Arguments.of(NUMBER_1_BIG, IntList.of(1), 6),
                    Arguments.of(NUMBER_2_SMALL, IntList.of(1), 7),
                    Arguments.of(NUMBER_2_BIG, IntList.of(1), 7),

                    Arguments.of(NUMBER_1_SMALL, IntList.of(Integer.MIN_VALUE), 6),
                    Arguments.of(NUMBER_1_SMALL, IntList.of(Integer.MAX_VALUE), 6),

                    Arguments.of(NUMBER_1_SMALL, IntList.of(1, 2, -1, 0), 18),
                    Arguments.of(NUMBER_1_BIG, IntList.of(1, 2, -1, 0), 18),
                    Arguments.of(NUMBER_2_SMALL, IntList.of(1, 2, -1, 0), 19),
                    Arguments.of(NUMBER_2_BIG, IntList.of(1, 2, -1, 0), 19)
            );
        }

        @ParameterizedTest
        @MethodSource("packedNoTagSource")
        void packedNoTagSize(IntList values, int expectedSize) {
            // when then
            assertThat(Size.ofSfixed32Packed(values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> packedNoTagSource() {
            return Stream.of(
                    Arguments.of(IntList.of(), 0),

                    Arguments.of(IntList.of(1), 4),
                    Arguments.of(IntList.of(Integer.MIN_VALUE), 4),
                    Arguments.of(IntList.of(Integer.MAX_VALUE), 4),

                    Arguments.of(IntList.of(1, 2, -1, 0), 16)
            );
        }
    }

    @Nested
    // TODO remove
    class OldSFixed64 {

        @ParameterizedTest
        @MethodSource("source")
        void size(int number, long value, int expectedSize) {
            // when then
            assertThat(Size.ofSfixed64(number, value)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> source() {
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
        @MethodSource("unpackedSource")
        void unpackedSize(int number, List<Long> values, int expectedSize) {
            // when then
            assertThat(Size.ofSfixed64Unpacked(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> unpackedSource() {
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
        @MethodSource("packedSource")
        void packedSize(int number, List<Long> values, int expectedSize) {
            // when then
            assertThat(Size.ofSfixed64Packed(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> packedSource() {
            return Stream.of(
                    Arguments.of(NUMBER_1_SMALL, List.of(), 0),
                    Arguments.of(NUMBER_2_BIG, List.of(), 0),

                    Arguments.of(NUMBER_1_SMALL, List.of(1L), 10),
                    Arguments.of(NUMBER_1_BIG, List.of(1L), 10),
                    Arguments.of(NUMBER_2_SMALL, List.of(1L), 11),
                    Arguments.of(NUMBER_2_BIG, List.of(1L), 11),

                    Arguments.of(NUMBER_1_SMALL, List.of(Long.MIN_VALUE), 10),
                    Arguments.of(NUMBER_1_SMALL, List.of(Long.MAX_VALUE), 10),

                    Arguments.of(NUMBER_1_SMALL, List.of(1L, 2L, -1L, 0L), 34),
                    Arguments.of(NUMBER_1_BIG, List.of(1L, 2L, -1L, 0L), 34),
                    Arguments.of(NUMBER_2_SMALL, List.of(1L, 2L, -1L, 0L), 35),
                    Arguments.of(NUMBER_2_BIG, List.of(1L, 2L, -1L, 0L), 35)
            );
        }

        @ParameterizedTest
        @MethodSource("packedNoTagSource")
        void packedNoTagSize(List<Long> values, int expectedSize) {
            // when then
            assertThat(Size.ofSfixed64Packed(values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> packedNoTagSource() {
            return Stream.of(
                    Arguments.of(List.of(), 0),

                    Arguments.of(List.of(1L), 8),
                    Arguments.of(List.of(Long.MIN_VALUE), 8),
                    Arguments.of(List.of(Long.MAX_VALUE), 8),

                    Arguments.of(List.of(1L, 2L, -1L, 0L), 32)
            );
        }
    }

    @Nested
    class SFixed64 {

        @ParameterizedTest
        @MethodSource("source")
        void size(int number, long value, int expectedSize) {
            // when then
            assertThat(Size.ofSfixed64(number, value)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> source() {
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
        @MethodSource("unpackedSource")
        void unpackedSize(int number, LongList values, int expectedSize) {
            // when then
            assertThat(Size.ofSfixed64Unpacked(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> unpackedSource() {
            return Stream.of(
                    Arguments.of(NUMBER_1_SMALL, LongList.of(), 0),
                    Arguments.of(NUMBER_2_BIG, LongList.of(), 0),

                    Arguments.of(NUMBER_1_SMALL, LongList.of(1L), 9),
                    Arguments.of(NUMBER_1_BIG, LongList.of(1L), 9),
                    Arguments.of(NUMBER_2_SMALL, LongList.of(1L), 10),
                    Arguments.of(NUMBER_2_BIG, LongList.of(1L), 10),

                    Arguments.of(NUMBER_1_SMALL, LongList.of(Long.MIN_VALUE), 9),
                    Arguments.of(NUMBER_1_SMALL, LongList.of(Long.MAX_VALUE), 9),

                    Arguments.of(NUMBER_1_SMALL, LongList.of(1L, 2L, -1L, 0L), 36),
                    Arguments.of(NUMBER_1_BIG, LongList.of(1L, 2L, -1L, 0L), 36),
                    Arguments.of(NUMBER_2_SMALL, LongList.of(1L, 2L, -1L, 0L), 40),
                    Arguments.of(NUMBER_2_BIG, LongList.of(1L, 2L, -1L, 0L), 40)
            );
        }

        @ParameterizedTest
        @MethodSource("packedSource")
        void packedSize(int number, LongList values, int expectedSize) {
            // when then
            assertThat(Size.ofSfixed64Packed(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> packedSource() {
            return Stream.of(
                    Arguments.of(NUMBER_1_SMALL, LongList.of(), 0),
                    Arguments.of(NUMBER_2_BIG, LongList.of(), 0),

                    Arguments.of(NUMBER_1_SMALL, LongList.of(1L), 10),
                    Arguments.of(NUMBER_1_BIG, LongList.of(1L), 10),
                    Arguments.of(NUMBER_2_SMALL, LongList.of(1L), 11),
                    Arguments.of(NUMBER_2_BIG, LongList.of(1L), 11),

                    Arguments.of(NUMBER_1_SMALL, LongList.of(Long.MIN_VALUE), 10),
                    Arguments.of(NUMBER_1_SMALL, LongList.of(Long.MAX_VALUE), 10),

                    Arguments.of(NUMBER_1_SMALL, LongList.of(1L, 2L, -1L, 0L), 34),
                    Arguments.of(NUMBER_1_BIG, LongList.of(1L, 2L, -1L, 0L), 34),
                    Arguments.of(NUMBER_2_SMALL, LongList.of(1L, 2L, -1L, 0L), 35),
                    Arguments.of(NUMBER_2_BIG, LongList.of(1L, 2L, -1L, 0L), 35)
            );
        }

        @ParameterizedTest
        @MethodSource("packedNoTagSource")
        void packedNoTagSize(LongList values, int expectedSize) {
            // when then
            assertThat(Size.ofSfixed64Packed(values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> packedNoTagSource() {
            return Stream.of(
                    Arguments.of(LongList.of(), 0),

                    Arguments.of(LongList.of(1L), 8),
                    Arguments.of(LongList.of(Long.MIN_VALUE), 8),
                    Arguments.of(LongList.of(Long.MAX_VALUE), 8),

                    Arguments.of(LongList.of(1L, 2L, -1L, 0L), 32)
            );
        }
    }

    @Nested
    class Bool {

        @ParameterizedTest
        @MethodSource("source")
        void size(int number, boolean value, int expectedSize) {
            // when then
            assertThat(Size.ofBool(number, value)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> source() {
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
        @MethodSource("unpackedSource")
        void unpackedSize(int number, List<Boolean> values, int expectedSize) {
            // when then
            assertThat(Size.ofBoolUnpacked(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> unpackedSource() {
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
        @MethodSource("packedSource")
        void packedSize(int number, List<Boolean> values, int expectedSize) {
            // when then
            assertThat(Size.ofBoolPacked(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> packedSource() {
            return Stream.of(
                    Arguments.of(NUMBER_1_SMALL, List.of(), 0),
                    Arguments.of(NUMBER_2_BIG, List.of(), 0),

                    Arguments.of(NUMBER_1_SMALL, List.of(true), 3),
                    Arguments.of(NUMBER_1_BIG, List.of(true), 3),
                    Arguments.of(NUMBER_2_SMALL, List.of(true), 4),
                    Arguments.of(NUMBER_2_BIG, List.of(true), 4),

                    Arguments.of(NUMBER_1_SMALL, List.of(true, false, false, true), 6),
                    Arguments.of(NUMBER_1_BIG, List.of(true, false, false, true), 6),
                    Arguments.of(NUMBER_2_SMALL, List.of(true, false, false, true), 7),
                    Arguments.of(NUMBER_2_BIG, List.of(true, false, false, true), 7)
            );
        }

        @ParameterizedTest
        @MethodSource("packedNoTagSource")
        void packedNoTagSize(List<Boolean> values, int expectedSize) {
            // when then
            assertThat(Size.ofBoolPacked(values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> packedNoTagSource() {
            return Stream.of(
                    Arguments.of(List.of(), 0),

                    Arguments.of(List.of(true), 1),

                    Arguments.of(List.of(true, false, false, true), 4)
            );
        }
    }

    @Nested
    class Bytes {

        @ParameterizedTest
        @MethodSource("source")
        void size(int number, ByteArray value, int expectedSize) {
            // when then
            assertThat(Size.ofBytes(number, value)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> source() {
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
        @MethodSource("unpackedSource")
        void unpackedSize(int number, List<ByteArray> values, int expectedSize) {
            // when then
            assertThat(Size.ofBytesUnpacked(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> unpackedSource() {
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
    }

    @Nested
    class Strings {

        @ParameterizedTest
        @MethodSource("source")
        void size(int number, String value, int expectedSize) {
            // when then
            assertThat(Size.ofString(number, value)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> source() {
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
        @MethodSource("unpackedSource")
        void unpackedSize(int number, List<String> values, int expectedSize) {
            // when then
            assertThat(Size.ofStringUnpacked(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> unpackedSource() {
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
    }

    @Nested
    class Messages {

        @ParameterizedTest
        @MethodSource("source")
        void size(int number, TestMessage value, int expectedSize) {
            // when then
            assertThat(Size.ofMessage(number, value)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> source() {
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
        @MethodSource("unpackedSource")
        void unpackedSize(int number, List<TestMessage> values, int expectedSize) {
            // when then
            assertThat(Size.ofMessageUnpacked(number, values)).isEqualTo(expectedSize);
        }

        private static Stream<Arguments> unpackedSource() {
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

        public static ProtobufMessage<?> message(int size) {
            return new TestMessage(size);
        }

        private record TestMessage(int size) implements ProtobufMessage<TestMessage> {
            @Override
            public void writeTo(OutputStream output) {
            }

            @Override
            public void writeTo(ProtobufWriter writer) {
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
            public int protobufSize() {
                return size;
            }
        }
    }
}