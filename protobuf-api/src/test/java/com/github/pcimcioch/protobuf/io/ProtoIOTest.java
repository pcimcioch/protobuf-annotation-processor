package com.github.pcimcioch.protobuf.io;

import com.github.pcimcioch.protobuf.io.exception.InputEndedException;
import com.github.pcimcioch.protobuf.io.exception.MalformedVarintException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.stream.Stream;

import static com.github.pcimcioch.protobuf.io.ByteUtils.b;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProtoIOTest {
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final ProtobufOutput output = ProtobufOutput.from(outputStream, 4096);

    @Nested
    class FixedInts {

        @ParameterizedTest
        @MethodSource("arguments")
        void write(int value, byte[] bytes) throws IOException {
            // when
            output.writeFixedInt(value);

            // then
            assertBinary(bytes);
        }

        @ParameterizedTest
        @MethodSource("arguments")
        void read(int value, byte[] bytes) throws IOException {
            // when then
            assertThat(input(bytes).readFixedInt()).isEqualTo(value);
        }

        static Stream<Arguments> arguments() {
            return Stream.of(
                    Arguments.of(0, b(0b0, 0b0, 0b0, 0b0)),
                    Arguments.of(1, b(0b1, 0b0, 0b0, 0b0)),
                    Arguments.of(15, b(0b00001111, 0b0, 0b0, 0b0)),
                    Arguments.of(255, b(0b11111111, 0b0, 0b0, 0b0)),
                    Arguments.of(1000, b(0b11101000, 0b11, 0b0, 0b0)),
                    Arguments.of(Integer.MAX_VALUE, b(0b11111111, 0b11111111, 0b11111111, 0b01111111)),
                    Arguments.of(-1, b(0b11111111, 0b11111111, 0b11111111, 0b11111111)),
                    Arguments.of(-15, b(0b11110001, 0b11111111, 0b11111111, 0b11111111)),
                    Arguments.of(-255, b(0b1, 0b11111111, 0b11111111, 0b11111111)),
                    Arguments.of(-1000, b(0b00011000, 0b11111100, 0b11111111, 0b11111111)),
                    Arguments.of(Integer.MIN_VALUE, b(0b0, 0b0, 0b0, 0b10000000))
            );
        }

        @ParameterizedTest
        @MethodSource("unfinishedArguments")
        void readUnfinished(byte[] bytes) {
            // when then
            assertThatThrownBy(() -> input(bytes).readFixedInt())
                    .isInstanceOf(InputEndedException.class);
        }

        static Stream<byte[]> unfinishedArguments() {
            return Stream.of(
                    b(),
                    b(0b0),
                    b(0b1, 0b11),
                    b(0b1, 0b11, 0b111)
            );
        }
    }

    @Nested
    class FixedLongs {

        @ParameterizedTest
        @MethodSource("arguments")
        void write(long value, byte[] bytes) throws IOException {
            // when
            output.writeFixedLong(value);

            // then
            assertBinary(bytes);
        }

        @ParameterizedTest
        @MethodSource("arguments")
        void read(long value, byte[] bytes) throws IOException {
            // when then
            assertThat(input(bytes).readFixedLong()).isEqualTo(value);
        }

        static Stream<Arguments> arguments() {
            return Stream.of(
                    Arguments.of(0, b(0b0, 0b0, 0b0, 0b0, 0b0, 0b0, 0b0, 0b0)),
                    Arguments.of(1, b(0b1, 0b0, 0b0, 0b0, 0b0, 0b0, 0b0, 0b0)),
                    Arguments.of(15, b(0b00001111, 0b0, 0b0, 0b0, 0b0, 0b0, 0b0, 0b0)),
                    Arguments.of(255, b(0b11111111, 0b0, 0b0, 0b0, 0b0, 0b0, 0b0, 0b0)),
                    Arguments.of(1000, b(0b11101000, 0b11, 0b0, 0b0, 0b0, 0b0, 0b0, 0b0)),
                    Arguments.of(123456789123L, b(0b10000011, 0b00011010, 0b10011001, 0b10111110, 0b11100, 0b0, 0b0, 0b0)),
                    Arguments.of(Long.MAX_VALUE, b(0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b01111111)),
                    Arguments.of(-1, b(0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111)),
                    Arguments.of(-15, b(0b11110001, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111)),
                    Arguments.of(-255, b(0b1, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111)),
                    Arguments.of(-1000, b(0b00011000, 0b11111100, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111)),
                    Arguments.of(-123456789123L, b(0b01111101, 0b11100101, 0b01100110, 0b01000001, 0b11100011, 0b11111111, 0b11111111, 0b11111111)),
                    Arguments.of(Long.MIN_VALUE, b(0b0, 0b0, 0b0, 0b0, 0b0, 0b0, 0b0, 0b10000000))
            );
        }

        @ParameterizedTest
        @MethodSource("unfinishedArguments")
        void readUnfinished(byte[] bytes) {
            // when then
            assertThatThrownBy(() -> input(bytes).readFixedLong())
                    .isInstanceOf(InputEndedException.class);
        }

        static Stream<byte[]> unfinishedArguments() {
            return Stream.of(
                    b(),
                    b(0b0),
                    b(0b1, 0b11),
                    b(0b1, 0b11, 0b111),
                    b(0b1, 0b10, 0b11, 0b100),
                    b(0b1, 0b10, 0b11, 0b100, 0b101),
                    b(0b1, 0b10, 0b11, 0b100, 0b101, 0b110),
                    b(0b1, 0b10, 0b11, 0b100, 0b101, 0b110, 0b111)
            );
        }
    }

    @Nested
    class Floats {

        @ParameterizedTest
        @MethodSource("arguments")
        void write(float value, byte[] bytes) throws IOException {
            // when
            output.writeFloat(value);

            // then
            assertBinary(bytes);
        }

        @ParameterizedTest
        @MethodSource("arguments")
        void read(float value, byte[] bytes) throws IOException {
            // when then
            assertThat(input(bytes).readFloat()).isEqualTo(value);
        }

        static Stream<Arguments> arguments() {
            return Stream.of(
                    Arguments.of(0f, b(0b0, 0b0, 0b0, 0b0)),
                    Arguments.of(1f, b(0b00000000, 0b00000000, 0b10000000, 0b00111111)),
                    Arguments.of(123.456f, b(0b01111001, 0b11101001, 0b11110110, 0b01000010)),
                    Arguments.of(Float.MAX_VALUE, b(0b11111111, 0b11111111, 0b01111111, 0b01111111)),
                    Arguments.of(-0f, b(0b0, 0b0, 0b0, 0b10000000)),
                    Arguments.of(-1f, b(0b00000000, 0b00000000, 0b10000000, 0b10111111)),
                    Arguments.of(-123.456f, b(0b01111001, 0b11101001, 0b11110110, 0b11000010)),
                    Arguments.of(Float.MIN_VALUE, b(0b1, 0b0, 0b0, 0b0))
            );
        }

        @ParameterizedTest
        @MethodSource("unfinishedArguments")
        void readUnfinished(byte[] bytes) {
            // when then
            assertThatThrownBy(() -> input(bytes).readFloat())
                    .isInstanceOf(InputEndedException.class);
        }

        static Stream<byte[]> unfinishedArguments() {
            return Stream.of(
                    b(),
                    b(0b0),
                    b(0b1, 0b11),
                    b(0b1, 0b11, 0b111)
            );
        }
    }

    @Nested
    class Doubles {

        @ParameterizedTest
        @MethodSource("arguments")
        void write(double value, byte[] bytes) throws IOException {
            // when
            output.writeDouble(value);

            // then
            assertBinary(bytes);
        }

        @ParameterizedTest
        @MethodSource("arguments")
        void read(double value, byte[] bytes) throws IOException {
            // when then
            assertThat(input(bytes).readDouble()).isEqualTo(value);
        }

        static Stream<Arguments> arguments() {
            return Stream.of(
                    Arguments.of(0d, b(0b0, 0b0, 0b0, 0b0, 0b0, 0b0, 0b0, 0b0)),
                    Arguments.of(1d, b(0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b11110000, 0b00111111)),
                    Arguments.of(123456789.123456789d, b(0b01110101, 0b01101011, 0b01111110, 0b01010100, 0b00110100, 0b01101111, 0b10011101, 0b01000001)),
                    Arguments.of(Double.MAX_VALUE, b(0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11101111, 0b1111111)),
                    Arguments.of(-0d, b(0b0, 0b0, 0b0, 0b0, 0b0, 0b0, 0b0, 0b10000000)),
                    Arguments.of(-1d, b(0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b11110000, 0b10111111)),
                    Arguments.of(-123456789.123456789d, b(0b01110101, 0b01101011, 0b01111110, 0b01010100, 0b00110100, 0b01101111, 0b10011101, 0b11000001)),
                    Arguments.of(Double.MIN_VALUE, b(0b1, 0b0, 0b0, 0b0, 0b0, 0b0, 0b0, 0b0))
            );
        }

        @ParameterizedTest
        @MethodSource("unfinishedArguments")
        void readUnfinished(byte[] bytes) {
            // when then
            assertThatThrownBy(() -> input(bytes).readDouble())
                    .isInstanceOf(InputEndedException.class);
        }

        static Stream<byte[]> unfinishedArguments() {
            return Stream.of(
                    b(),
                    b(0b0),
                    b(0b1, 0b11),
                    b(0b1, 0b11, 0b111),
                    b(0b1, 0b10, 0b11, 0b100),
                    b(0b1, 0b10, 0b11, 0b100, 0b101),
                    b(0b1, 0b10, 0b11, 0b100, 0b101, 0b110),
                    b(0b1, 0b10, 0b11, 0b100, 0b101, 0b110, 0b111)
            );
        }
    }

    @Nested
    class Booleans {

        @ParameterizedTest
        @MethodSource("arguments")
        void write(boolean value, byte[] bytes) throws IOException {
            // when
            output.writeBoolean(value);

            // then
            assertBinary(bytes);
        }

        @ParameterizedTest
        @MethodSource("arguments")
        void read(boolean value, byte[] bytes) throws IOException {
            // when then
            assertThat(input(bytes).readBoolean()).isEqualTo(value);
        }

        static Stream<Arguments> arguments() {
            return Stream.of(
                    Arguments.of(true, b(0b1)),
                    Arguments.of(false, b(0b0))
            );
        }

        @ParameterizedTest
        @MethodSource("incorrectArguments")
        void readIncorrectValue(boolean value, byte[] bytes) throws IOException {
            // when then
            assertThat(input(bytes).readBoolean()).isEqualTo(value);
        }

        static Stream<Arguments> incorrectArguments() {
            return Stream.of(
                    Arguments.of(true, b(0b00000011)),
                    Arguments.of(true, b(0b01111111)),
                    Arguments.of(true, b(0b01111110)),
                    Arguments.of(true, b(0b00000010))

            );
        }

        @Test
        void readUnfinished() {
            // when then
            assertThatThrownBy(() -> input(new byte[0]).readBoolean())
                    .isInstanceOf(InputEndedException.class);
        }
    }

    @Nested
    class VarInts64 {

        @ParameterizedTest
        @MethodSource("arguments")
        void write(long value, byte[] bytes) throws IOException {
            // when
            output.writeVarint64(value);

            // then
            assertBinary(bytes);
        }

        @ParameterizedTest
        @MethodSource("arguments")
        void read(long value, byte[] bytes) throws IOException {
            // when then
            assertThat(input(bytes).readVarint64()).isEqualTo(value);
        }

        static Stream<Arguments> arguments() {
            return Stream.of(
                    Arguments.of(0, b(0b0)),
                    Arguments.of(1, b(0b1)),
                    Arguments.of(127, b(0b01111111)),
                    Arguments.of(150, b(0b10010110, 0b00000001)),
                    Arguments.of(123456, b(0b11000000, 0b11000100, 0b00000111)),
                    Arguments.of(Integer.MAX_VALUE, b(0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b00000111)),
                    Arguments.of(56270356588186L, b(0b10011010, 0b11001101, 0b11110110, 0b10110100, 0b11010111, 0b11100101, 0b00001100)),
                    Arguments.of(Long.MAX_VALUE, b(0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b01111111)),
                    Arguments.of(-1, b(0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b00000001)),
                    Arguments.of(-123456, b(0b11000000, 0b10111011, 0b11111000, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b00000001)),
                    Arguments.of(Integer.MIN_VALUE, b(0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b11111000, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b00000001)),
                    Arguments.of(-3212415784486396469L, b(0b11001011, 0b10111011, 0b11010010, 0b10110110, 0b11010011, 0b10011011, 0b11001101, 0b10110101, 0b11010011, 0b00000001)),
                    Arguments.of(Long.MIN_VALUE, b(0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b00000001))
            );
        }

        @ParameterizedTest
        @MethodSource("unfinishedArguments")
        void readUnfinished(byte[] bytes) {
            // when then
            assertThatThrownBy(() -> input(bytes).readVarint64())
                    .isInstanceOf(InputEndedException.class);
        }

        static Stream<byte[]> unfinishedArguments() {
            return Stream.of(
                    b(),
                    b(0b10000001)
            );
        }

        @Test
        void readOverTenBytes() {
            // given
            byte[] bytes = b(0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b00000001);

            // when
            assertThatThrownBy(() -> input(bytes).readVarint64())
                    .isInstanceOf(MalformedVarintException.class);
        }

        @Test
        void readOver64Bits() {
            // given
            byte[] bytes = b(0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b00000011);

            // when
            assertThatCode(() -> input(bytes).readVarint64())
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    class VarInts32 {

        @ParameterizedTest
        @MethodSource("arguments")
        void write(int value, byte[] bytes) throws IOException {
            // when
            output.writeVarint32(value);

            // then
            assertBinary(bytes);
        }

        @ParameterizedTest
        @MethodSource("arguments")
        void read(int value, byte[] bytes) throws IOException {
            // when then
            assertThat(input(bytes).readVarint32()).isEqualTo(value);
        }

        static Stream<Arguments> arguments() {
            return Stream.of(
                    Arguments.of(0, b(0b0)),
                    Arguments.of(1, b(0b1)),
                    Arguments.of(127, b(0b01111111)),
                    Arguments.of(150, b(0b10010110, 0b00000001)),
                    Arguments.of(123456, b(0b11000000, 0b11000100, 0b00000111)),
                    Arguments.of(Integer.MAX_VALUE, b(0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b00000111)),
                    Arguments.of(-1, b(0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b00000001)),
                    Arguments.of(-123456, b(0b11000000, 0b10111011, 0b11111000, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b00000001)),
                    Arguments.of(Integer.MIN_VALUE, b(0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b11111000, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b00000001))
            );
        }

        @ParameterizedTest
        @MethodSource("argumentsOver32")
        void readMoreThen32(int value, byte[] bytes) throws IOException {
            // when then
            assertThat(input(bytes).readVarint32()).isEqualTo(value);
        }

        static Stream<Arguments> argumentsOver32() {
            return Stream.of(
                    Arguments.of(-1, b(0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b01111111)),
                    Arguments.of(1990043290, b(0b10011010, 0b11001101, 0b11110110, 0b10110100, 0b11010111, 0b11100101, 0b00001100)),
                    Arguments.of(919903691, b(0b11001011, 0b10111011, 0b11010010, 0b10110110, 0b11010011, 0b10011011, 0b11001101, 0b10110101, 0b11010011, 0b00000001)),
                    Arguments.of(0, b(0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b00000001))
            );
        }

        @ParameterizedTest
        @MethodSource("unfinishedArguments")
        void readUnfinished(byte[] bytes) {
            // when then
            assertThatThrownBy(() -> input(bytes).readVarint32())
                    .isInstanceOf(InputEndedException.class);
        }

        static Stream<byte[]> unfinishedArguments() {
            return Stream.of(
                    b(),
                    b(0b10000001)
            );
        }

        @Test
        void readOverTenBytes() {
            // given
            byte[] bytes = b(0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b00000001);

            // when
            assertThatThrownBy(() -> input(bytes).readVarint32())
                    .isInstanceOf(MalformedVarintException.class);
        }

        @Test
        void readOver64Bits() {
            // given
            byte[] bytes = b(0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b00000011);

            // when
            assertThatCode(() -> input(bytes).readVarint32())
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    class Strings {

        @ParameterizedTest
        @MethodSource("arguments")
        void write(String value, byte[] bytes) throws IOException {
            // when
            output.writeString(value);

            // then
            assertBinary(bytes);
        }

        @ParameterizedTest
        @MethodSource("arguments")
        void read(String value, byte[] bytes) throws IOException {
            // when then
            assertThat(input(bytes).readString()).isEqualTo(value);
        }

        static Stream<Arguments> arguments() {
            return Stream.of(
                    Arguments.of("", b(0b0)),
                    Arguments.of("abc", b(0b11, 97, 98, 99)),
                    Arguments.of("\u0105\u0119\u0107", b(6, -60, -123, -60, -103, -60, -121))
            );
        }

        @ParameterizedTest
        @MethodSource("unfinishedArguments")
        void readUnfinished(byte[] bytes) {
            // when then
            assertThatThrownBy(() -> input(bytes).readString())
                    .isInstanceOf(InputEndedException.class);
        }

        static Stream<byte[]> unfinishedArguments() {
            return Stream.of(
                    b(),
                    b(0b1),
                    b(0b11, 97, 98)
            );
        }
    }

    @Nested
    class Bytes {

        @ParameterizedTest
        @MethodSource("arguments")
        void write(byte[] value, byte[] bytes) throws IOException {
            // when
            output.writeBytes(value);

            // then
            assertBinary(bytes);
        }

        @ParameterizedTest
        @MethodSource("arguments")
        void read(byte[] value, byte[] bytes) throws IOException {
            // when then
            assertThat(input(bytes).readBytes()).isEqualTo(value);
        }

        static Stream<Arguments> arguments() {
            return Stream.of(
                    Arguments.of(b(), b(0b0)),
                    Arguments.of(b(0b1, 0b11111111, 0b10101010), b(0b11, 0b1, 0b11111111, 0b10101010))
            );
        }

        @ParameterizedTest
        @MethodSource("unfinishedArguments")
        void readUnfinished(byte[] bytes) {
            // when then
            assertThatThrownBy(() -> input(bytes).readBytes())
                    .isInstanceOf(InputEndedException.class);
        }

        static Stream<byte[]> unfinishedArguments() {
            return Stream.of(
                    b(),
                    b(0b1),
                    b(0b11, 0b1, 0b01)
            );
        }
    }

    @Nested
    class ZigZag64 {

        @ParameterizedTest
        @MethodSource("arguments")
        void write(long value, byte[] bytes) throws IOException {
            // when
            output.writeZigZag64(value);

            // then
            assertBinary(bytes);
        }

        @ParameterizedTest
        @MethodSource("arguments")
        void read(long value, byte[] bytes) throws IOException {
            // when then
            assertThat(input(bytes).readZigZag64()).isEqualTo(value);
        }

        static Stream<Arguments> arguments() {
            return Stream.of(
                    Arguments.of(0, b(0b0)),
                    Arguments.of(-1, b(0b1)),
                    Arguments.of(1, b(0b10)),
                    Arguments.of(-2, b(0b11)),
                    Arguments.of(2, b(0b100)),
                    Arguments.of(2147483647, b(0b11111110, 0b11111111, 0b11111111, 0b11111111, 0b00001111)),
                    Arguments.of(-2147483648, b(0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b00001111)),
                    Arguments.of(28135178294093L, b(0b10011010, 0b11001101, 0b11110110, 0b10110100, 0b11010111, 0b11100101, 0b00001100)),
                    Arguments.of(-7617164144611577574L, b(0b11001011, 0b10111011, 0b11010010, 0b10110110, 0b11010011, 0b10011011, 0b11001101, 0b10110101, 0b11010011, 0b00000001)),
                    Arguments.of(9223372036854775807L, b(0b11111110, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b00000001)),
                    Arguments.of(-9223372036854775808L, b(0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b00000001))
            );
        }

        @ParameterizedTest
        @MethodSource("unfinishedArguments")
        void readUnfinished(byte[] bytes) {
            // when then
            assertThatThrownBy(() -> input(bytes).readZigZag64())
                    .isInstanceOf(InputEndedException.class);
        }

        static Stream<byte[]> unfinishedArguments() {
            return Stream.of(
                    b(),
                    b(0b10000001)
            );
        }
    }

    @Nested
    class ZigZag32 {

        @ParameterizedTest
        @MethodSource("arguments")
        void write(int value, byte[] bytes) throws IOException {
            // when
            output.writeZigZag32(value);

            // then
            assertBinary(bytes);
        }

        @ParameterizedTest
        @MethodSource("arguments")
        void read(int value, byte[] bytes) throws IOException {
            // when then
            assertThat(input(bytes).readZigZag32()).isEqualTo(value);
        }

        static Stream<Arguments> arguments() {
            return Stream.of(
                    Arguments.of(0, b(0b0)),
                    Arguments.of(-1, b(0b1)),
                    Arguments.of(1, b(0b10)),
                    Arguments.of(-2, b(0b11)),
                    Arguments.of(2, b(0b100)),
                    Arguments.of(-289374652, b(0b11110111, 0b10000110, 0b11111100, 0b10010011, 0b00000010)),
                    Arguments.of(289374652, b(0b11111000, 0b10000110, 0b11111100, 0b10010011, 0b00000010)),
                    Arguments.of(2147483647, b(0b11111110, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b00000001)),
                    Arguments.of(-2147483648, b(0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b00000001))
            );
        }

        @ParameterizedTest
        @MethodSource("argumentsOver32")
        void readOver32(int value, byte[] bytes) throws IOException {
            // when then
            assertThat(input(bytes).readZigZag32()).isEqualTo(value);
        }

        static Stream<Arguments> argumentsOver32() {
            return Stream.of(
                    Arguments.of(2147483647, b(0b11111110, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b00000001)),
                    Arguments.of(-2147483648, b(0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b00000001)),
                    Arguments.of(995021645, b(0b10011010, 0b11001101, 0b11110110, 0b10110100, 0b11010111, 0b11100101, 0b00001100)),
                    Arguments.of(-459951846, b(0b11001011, 0b10111011, 0b11010010, 0b10110110, 0b11010011, 0b10011011, 0b11001101, 0b10110101, 0b11010011, 0b00000001))
            );
        }

        @ParameterizedTest
        @MethodSource("unfinishedArguments")
        void readUnfinished(byte[] bytes) {
            // when then
            assertThatThrownBy(() -> input(bytes).readZigZag32())
                    .isInstanceOf(InputEndedException.class);
        }

        static Stream<byte[]> unfinishedArguments() {
            return Stream.of(
                    b(),
                    b(0b10000001)
            );
        }
    }

    @Nested
    class Mixed {

        @Test
        void write() throws IOException {
            // when
            output.writeFixedInt(1000);
            output.writeFixedLong(123456789123L);
            output.writeVarint32(123456);
            output.writeString("abc");
            output.writeZigZag32(2);

            // then
            assertBinary(b(
                    0b11101000, 0b11, 0b0, 0b0, // 1000 fixed int
                    0b10000011, 0b00011010, 0b10011001, 0b10111110, 0b11100, 0b0, 0b0, 0b0, // 123456789123L fixed long
                    0b11000000, 0b11000100, 0b00000111, // 123456 varint
                    0b11, 97, 98, 99, // "abc" string
                    0b100 // 2 zig zag
            ));
        }

        @Test
        void read() throws IOException {
            // given
            ProtobufInput input = input(b(
                    0b11101000, 0b11, 0b0, 0b0, // 1000 fixed int
                    0b10000011, 0b00011010, 0b10011001, 0b10111110, 0b11100, 0b0, 0b0, 0b0, // 123456789123L fixed long
                    0b11000000, 0b11000100, 0b00000111, // 123456 varint
                    0b11, 97, 98, 99, // "abc" string
                    0b100 // 2 zig zag
            ));

            // when then
            assertThat(input.readFixedInt()).isEqualTo(1000);
            assertThat(input.readFixedLong()).isEqualTo(123456789123L);
            assertThat(input.readVarint32()).isEqualTo(123456);
            assertThat(input.readString()).isEqualTo("abc");
            assertThat(input.readZigZag32()).isEqualTo(2);
        }
    }

    @Nested
    class Skip {
        @Test
        void skipZero() throws IOException {
            // given
            ProtobufInput input = input(b(10, 0, 0, 0));

            // when
            input.skip(0);

            // then
            assertThat(input.readFixedInt()).isEqualTo(10);
        }

        @Test
        void skip() throws IOException {
            // given
            ProtobufInput input = input(b(10, 20, 30, 10, 0, 0, 0));

            // when
            input.skip(3);

            // then
            assertThat(input.readFixedInt()).isEqualTo(10);
        }

        @Test
        void skipAll() throws IOException {
            // given
            ProtobufInput input = input(b(10, 20, 30));

            // when
            input.skip(3);

            // then
            assertThatThrownBy(() -> input.skip(1))
                    .isInstanceOf(InputEndedException.class);
        }

        @Test
        void skipTooMany() {
            // given
            ProtobufInput input = input(b(10, 20, 30));

            // when then
            assertThatThrownBy(() -> input.skip(4))
                    .isInstanceOf(InputEndedException.class);
        }
    }

    private ProtobufInput input(byte[] bytes) {
        return ProtobufInput.from(bytes);
    }

    private void assertBinary(byte[] bytes) throws IOException {
        output.close();
        assertThat(outputStream.toByteArray()).isEqualTo(bytes);
    }
}
