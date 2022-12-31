package com.github.pcimcioch.protobuf.model.message;

import com.github.pcimcioch.protobuf.model.message.ReservedDefinition.Range;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservedDefinitionTest {

    @Nested
    class RangeTest {

        @ParameterizedTest
        @MethodSource("incorrectRanges")
        void incorrectRangesTest(int from, int to, String message) {
            // when then
            assertThatThrownBy(() -> new Range(from, to))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(message);
        }

        static Stream<Arguments> incorrectRanges() {
            return Stream.of(
                    Arguments.of(10, 9, "Incorrect range [10, 9]"),
                    Arguments.of(-10, -11, "Incorrect range [-10, -11]")
            );
        }

        @ParameterizedTest
        @MethodSource("correctRanges")
        void incorrectRangesTest(int from, int to) {
            // when then
            assertThatCode(() -> new Range(from, to))
                    .doesNotThrowAnyException();
        }

        static Stream<Arguments> correctRanges() {
            return Stream.of(
                    Arguments.of(1, 10),
                    Arguments.of(10, 10),
                    Arguments.of(-10, -10),
                    Arguments.of(-10, -1),
                    Arguments.of(-10, 10),
                    Arguments.of(0, Integer.MAX_VALUE)
            );
        }
    }

    @Nested
    class CorrectReserved {

        @Test
        void empty() {
            // when then
            assertThatCode(() -> new ReservedDefinition(emptySet(), emptySet(), emptySet()))
                    .doesNotThrowAnyException();
        }

        @Test
        void full() {
            // when then
            assertThatCode(() -> new ReservedDefinition(
                    Set.of("FIRST", "SECOND"),
                    Set.of(10, 11),
                    Set.of(new Range(10, 20)))
            )
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    class IncorrectReserved {

        @Test
        void nullNames() {
            // when then
            assertThatThrownBy(() -> new ReservedDefinition(null, emptySet(), emptySet()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Reserved names cannot be null");
        }

        @Test
        void nullInNames() {
            // when then
            assertThatThrownBy(() -> new ReservedDefinition(asSet(null, "test"), emptySet(), emptySet()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Reserved names cannot contain null");
        }

        @Test
        void nullNumbers() {
            // when then
            assertThatThrownBy(() -> new ReservedDefinition(emptySet(), null, emptySet()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Reserved numbers cannot be null");
        }

        @Test
        void nullInNumbers() {
            // when then
            assertThatThrownBy(() -> new ReservedDefinition(emptySet(), asSet(1, null), emptySet()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Reserved numbers cannot contain null");
        }

        @Test
        void nullRanges() {
            // when then
            assertThatThrownBy(() -> new ReservedDefinition(emptySet(), emptySet(), null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Reserved ranges cannot be null");
        }

        @Test
        void nullInRanges() {
            // when then
            assertThatThrownBy(() -> new ReservedDefinition(emptySet(), emptySet(), asSet(new Range(1, 2), null)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Reserved ranges cannot contain null");
        }
    }

    @SafeVarargs
    private static <T> Set<T> asSet(T... elements) {
        return new HashSet<>(Arrays.asList(elements));
    }
}