package com.github.pcimcioch.protobuf.io;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProtobufIOTest {

    private final ByteArrayOutputStream output = new ByteArrayOutputStream();

    @Nested
    class FixedInts {

        @ParameterizedTest
        @MethodSource("arguments")
        void write(int value, byte[] bytes) throws IOException {
            // when
            output().writeFixedInt(value);

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
                    Arguments.of(1, b(0b0, 0b0, 0b0, 0b1)),
                    Arguments.of(15, b(0b0, 0b0, 0b0, 0b00001111)),
                    Arguments.of(255, b(0b0, 0b0, 0b0, 0b11111111)),
                    Arguments.of(1000, b(0b0, 0b0, 0b11, 0b11101000)),
                    Arguments.of(Integer.MAX_VALUE, b(0b01111111, 0b11111111, 0b11111111, 0b11111111)),
                    Arguments.of(-1, b(0b11111111, 0b11111111, 0b11111111, 0b11111111)),
                    Arguments.of(-15, b(0b11111111, 0b11111111, 0b11111111, 0b11110001)),
                    Arguments.of(-255, b(0b11111111, 0b11111111, 0b11111111, 0b1)),
                    Arguments.of(-1000, b(0b11111111, 0b11111111, 0b11111100, 0b00011000)),
                    Arguments.of(Integer.MIN_VALUE, b(0b10000000, 0b0, 0b0, 0b0))
            );
        }

        @ParameterizedTest
        @MethodSource("unfinishedArguments")
        void readUnfinished(byte[] bytes) {
            // when then
            assertThatThrownBy(() -> input(bytes).readFixedInt()).isInstanceOf(EOFException.class);
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
            output().writeFixedLong(value);

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
                    Arguments.of(1, b(0b0, 0b0, 0b0, 0b0, 0b0, 0b0, 0b0, 0b1)),
                    Arguments.of(15, b(0b0, 0b0, 0b0, 0b0, 0b0, 0b0, 0b0, 0b00001111)),
                    Arguments.of(255, b(0b0, 0b0, 0b0, 0b0, 0b0, 0b0, 0b0, 0b11111111)),
                    Arguments.of(1000, b(0b0, 0b0, 0b0, 0b0, 0b0, 0b0, 0b11, 0b11101000)),
                    Arguments.of(123456789123L, b(0b0, 0b0, 0b0, 0b11100, 0b10111110, 0b10011001, 0b00011010, 0b10000011)),
                    Arguments.of(Long.MAX_VALUE, b(0b01111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111)),
                    Arguments.of(-1, b(0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111)),
                    Arguments.of(-15, b(0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11110001)),
                    Arguments.of(-255, b(0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b1)),
                    Arguments.of(-1000, b(0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111100, 0b00011000)),
                    Arguments.of(-123456789123L, b(0b11111111, 0b11111111, 0b11111111, 0b11100011, 0b01000001, 0b01100110, 0b11100101, 0b01111101)),
                    Arguments.of(Long.MIN_VALUE, b(0b10000000, 0b0, 0b0, 0b0, 0b0, 0b0, 0b0, 0b0))
            );
        }

        @ParameterizedTest
        @MethodSource("unfinishedArguments")
        void readUnfinished(byte[] bytes) {
            // when then
            assertThatThrownBy(() -> input(bytes).readFixedLong()).isInstanceOf(EOFException.class);
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
            output().writeFloat(value);

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
                    Arguments.of(1f, b(0b00111111, 0b10000000, 0b00000000, 0b00000000)),
                    Arguments.of(123.456f, b(0b01000010, 0b11110110, 0b11101001, 0b01111001)),
                    Arguments.of(Float.MAX_VALUE, b(0b01111111, 0b01111111, 0b11111111, 0b11111111)),
                    Arguments.of(-0f, b(0b10000000, 0b0, 0b0, 0b0)),
                    Arguments.of(-1f, b(0b10111111, 0b10000000, 0b00000000, 0b00000000)),
                    Arguments.of(-123.456f, b(0b11000010, 0b11110110, 0b11101001, 0b01111001)),
                    Arguments.of(Float.MIN_VALUE, b(0b0, 0b0, 0b0, 0b1))
            );
        }

        @ParameterizedTest
        @MethodSource("unfinishedArguments")
        void readUnfinished(byte[] bytes) {
            // when then
            assertThatThrownBy(() -> input(bytes).readFloat()).isInstanceOf(EOFException.class);
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
            output().writeDouble(value);

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
                    Arguments.of(1d, b(0b00111111, 0b11110000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000)),
                    Arguments.of(123456789.123456789d, b(0b01000001, 0b10011101, 0b01101111, 0b00110100, 0b01010100, 0b01111110, 0b01101011, 0b01110101)),
                    Arguments.of(Double.MAX_VALUE, b(0b1111111, 0b11101111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111)),
                    Arguments.of(-0d, b(0b10000000, 0b0, 0b0, 0b0, 0b0, 0b0, 0b0, 0b0)),
                    Arguments.of(-1d, b(0b10111111, 0b11110000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000)),
                    Arguments.of(-123456789.123456789d, b(0b11000001, 0b10011101, 0b01101111, 0b00110100, 0b01010100, 0b01111110, 0b01101011, 0b01110101)),
                    Arguments.of(Double.MIN_VALUE, b(0b0, 0b0, 0b0, 0b0, 0b0, 0b0, 0b0, 0b1))
            );
        }

        @ParameterizedTest
        @MethodSource("unfinishedArguments")
        void readUnfinished(byte[] bytes) {
            // when then
            assertThatThrownBy(() -> input(bytes).readDouble()).isInstanceOf(EOFException.class);
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
            output().writeBoolean(value);

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
                    Arguments.of(true, b(0b11)),
                    Arguments.of(true, b(0b11111111)),
                    Arguments.of(true, b(0b11111110)),
                    Arguments.of(true, b(0b10))

            );
        }

        @Test
        void readUnfinished() {
            // when then
            assertThatThrownBy(() -> input(new byte[0]).readBoolean()).isInstanceOf(EOFException.class);
        }
    }

    @Nested
    class VarInts {

        @ParameterizedTest
        @MethodSource("arguments")
        void write(long value, byte[] bytes) throws IOException {
            // when
            output().writeVarint(value);

            // then
            assertBinary(bytes);
        }

        @ParameterizedTest
        @MethodSource("arguments")
        void read(long value, byte[] bytes) throws IOException {
            // when then
            assertThat(input(bytes).readVarint()).isEqualTo(value);
        }

        static Stream<Arguments> arguments() {
            return Stream.of(
                    Arguments.of(0, b(0b0)),
                    Arguments.of(1, b(0b1)),
                    Arguments.of(127, b(0b01111111)),
                    Arguments.of(150, b(0b10010110, 0b00000001)),
                    Arguments.of(123456, b(0b11000000, 0b11000100, 0b00000111)),
                    Arguments.of(Integer.MAX_VALUE, b(0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b00000111)),
                    Arguments.of(Long.MAX_VALUE, b(0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b01111111)),
                    Arguments.of(-1, b(0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b00000001)),
                    Arguments.of(-123456, b(0b11000000, 0b10111011, 0b11111000, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b00000001)),
                    Arguments.of(Integer.MIN_VALUE, b(0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b11111000, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b00000001)),
                    Arguments.of(Long.MIN_VALUE, b(0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b00000001))
            );
        }

        @ParameterizedTest
        @MethodSource("unfinishedArguments")
        void readUnfinished(byte[] bytes) {
            // when then
            assertThatThrownBy(() -> input(bytes).readVarint()).isInstanceOf(EOFException.class);
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
            assertThatCode(() -> input(bytes).readVarint()).doesNotThrowAnyException();
        }

        @Test
        void readOver64Bits() {
            // given
            byte[] bytes = b(0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b10000000, 0b00000011);

            // when
            assertThatCode(() -> input(bytes).readVarint()).doesNotThrowAnyException();
        }
    }

    @Nested
    class Strings {

        @ParameterizedTest
        @MethodSource("arguments")
        void write(String value, byte[] bytes) throws IOException {
            // when
            output().writeString(value);

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
                    Arguments.of("ąęć", b(0b1111, -61, -124, -30, -128, -90, -61, -124, -30, -124, -94, -61, -124, -30, -128, -95))
            );
        }

        @ParameterizedTest
        @MethodSource("unfinishedArguments")
        void readUnfinished(byte[] bytes) {
            // when then
            assertThatThrownBy(() -> input(bytes).readString()).isInstanceOf(EOFException.class);
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
            output().writeBytes(value);

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
            assertThatThrownBy(() -> input(bytes).readBytes()).isInstanceOf(EOFException.class);
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
    class ZigZag {

        @ParameterizedTest
        @MethodSource("arguments")
        void write(long value, byte[] bytes) throws IOException {
            // when
            output().writeZigZag(value);

            // then
            assertBinary(bytes);
        }

        @ParameterizedTest
        @MethodSource("arguments")
        void read(long value, byte[] bytes) throws IOException {
            // when then
            assertThat(input(bytes).readZigZag()).isEqualTo(value);
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
                    Arguments.of(9223372036854775807L, b(0b11111110, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b00000001)),
                    Arguments.of(-9223372036854775808L, b(0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b11111111, 0b00000001))
            );
        }

        @ParameterizedTest
        @MethodSource("unfinishedArguments")
        void readUnfinished(byte[] bytes) {
            // when then
            assertThatThrownBy(() -> input(bytes).readZigZag()).isInstanceOf(EOFException.class);
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
            output().writeFixedInt(1000);
            output().writeFixedLong(123456789123L);
            output().writeVarint(123456);
            output().writeString("abc");
            output().writeZigZag(2);

            // then
            assertBinary(b(
                    0b0, 0b0, 0b11, 0b11101000, // 1000 fixed int
                    0b0, 0b0, 0b0, 0b11100, 0b10111110, 0b10011001, 0b00011010, 0b10000011, // 123456789123L fixed long
                    0b11000000, 0b11000100, 0b00000111, // 123456 varint
                    0b11, 97, 98, 99, // "abc" string
                    0b100 // 2 zig zag
            ));
        }

        @Test
        void read() throws IOException {
            // given
            ProtobufInput input = input(b(
                    0b0, 0b0, 0b11, 0b11101000, // 1000 fixed int
                    0b0, 0b0, 0b0, 0b11100, 0b10111110, 0b10011001, 0b00011010, 0b10000011, // 123456789123L fixed long
                    0b11000000, 0b11000100, 0b00000111, // 123456 varint
                    0b11, 97, 98, 99, // "abc" string
                    0b100 // 2 zig zag
            ));

            // when then
            assertThat(input.readFixedInt()).isEqualTo(1000);
            assertThat(input.readFixedLong()).isEqualTo(123456789123L);
            assertThat(input.readVarint()).isEqualTo(123456);
            assertThat(input.readString()).isEqualTo("abc");
            assertThat(input.readZigZag()).isEqualTo(2);
        }
    }

    private ProtobufOutput output() {
        return new ProtobufOutput(output);
    }

    private ProtobufInput input(byte[] bytes) {
        return new ProtobufInput(new ByteArrayInputStream(bytes));
    }

    private void assertBinary(byte[] bytes) {
        assertThat(output.toByteArray()).isEqualTo(bytes);
    }

    private static byte[] b(int... values) {
        byte[] bytes = new byte[values.length];
        for (int i = 0; i < values.length; i++) {
            bytes[i] = (byte) values[i];
        }
        return bytes;
    }
}
