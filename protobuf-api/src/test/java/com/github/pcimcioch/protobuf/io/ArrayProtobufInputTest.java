package com.github.pcimcioch.protobuf.io;

import com.github.pcimcioch.protobuf.io.exception.InputEndedException;
import com.github.pcimcioch.protobuf.io.exception.LimitExceededException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ArrayProtobufInputTest {

    @Nested
    class ReadByte {
        private final ProtobufInput testee = testee(0, 1, 2, 3, 4);

        @Test
        void readOneByte() throws IOException {
            // when
            byte read = testee.readByte();

            // then
            assertThat(read).isEqualTo((byte) 0);
        }

        @Test
        void readMultipleByte() throws IOException {
            // when
            byte read1 = testee.readByte();
            byte read2 = testee.readByte();
            byte read3 = testee.readByte();

            // then
            assertThat(read1).isEqualTo((byte) 0);
            assertThat(read2).isEqualTo((byte) 1);
            assertThat(read3).isEqualTo((byte) 2);
        }

        @Test
        void readAllData() throws IOException {
            // when
            byte read1 = testee.readByte();
            byte read2 = testee.readByte();
            byte read3 = testee.readByte();
            byte read4 = testee.readByte();
            byte read5 = testee.readByte();

            // then
            assertThat(read1).isEqualTo((byte) 0);
            assertThat(read2).isEqualTo((byte) 1);
            assertThat(read3).isEqualTo((byte) 2);
            assertThat(read4).isEqualTo((byte) 3);
            assertThat(read5).isEqualTo((byte) 4);
        }

        @Test
        void readJustOverCapacity() throws IOException {
            // when
            byte read1 = testee.readByte();
            byte read2 = testee.readByte();
            byte read3 = testee.readByte();
            byte read4 = testee.readByte();
            byte read5 = testee.readByte();

            assertThatThrownBy(testee::readByte)
                    .isInstanceOf(InputEndedException.class);

            // then
            assertThat(read1).isEqualTo((byte) 0);
            assertThat(read2).isEqualTo((byte) 1);
            assertThat(read3).isEqualTo((byte) 2);
            assertThat(read4).isEqualTo((byte) 3);
            assertThat(read5).isEqualTo((byte) 4);
        }

        @Test
        void readOverCapacity() throws IOException {
            // when
            byte read1 = testee.readByte();
            byte read2 = testee.readByte();
            byte read3 = testee.readByte();

            assertThatThrownBy(() -> testee.readBytes(3))
                    .isInstanceOf(InputEndedException.class);

            // then
            assertThat(read1).isEqualTo((byte) 0);
            assertThat(read2).isEqualTo((byte) 1);
            assertThat(read3).isEqualTo((byte) 2);
        }

        @Test
        void readToLimit() throws IOException {
            // given
            testee.setLimit(3);

            // when
            byte read1 = testee.readByte();
            byte read2 = testee.readByte();
            byte read3 = testee.readByte();

            // then
            assertThat(read1).isEqualTo((byte) 0);
            assertThat(read2).isEqualTo((byte) 1);
            assertThat(read3).isEqualTo((byte) 2);
        }

        @Test
        void readJustOverLimit() throws IOException {
            // given
            testee.setLimit(3);

            // when
            byte read1 = testee.readByte();
            byte read2 = testee.readByte();
            byte read3 = testee.readByte();

            assertThatThrownBy(testee::readByte)
                    .isInstanceOf(LimitExceededException.class);

            // then
            assertThat(read1).isEqualTo((byte) 0);
            assertThat(read2).isEqualTo((byte) 1);
            assertThat(read3).isEqualTo((byte) 2);
        }

        @Test
        void readOverLimit() throws IOException {
            // given
            testee.setLimit(3);

            // when
            assertThatThrownBy(() -> testee.readBytes(4))
                    .isInstanceOf(LimitExceededException.class);
        }
    }

    @Nested
    class ReadByteArray {

        @Test
        void readByteArray() throws IOException {
            // given
            ProtobufInput testee = testee(0, 1, 2, 3);

            // when
            byte read1 = testee.readByte();
            byte[] readArray = testee.readBytes(3);
            assertThatThrownBy(() -> testee.readBytes(1))
                    .isInstanceOf(InputEndedException.class);

            // then
            assertThat(read1).isEqualTo((byte) 0);
            assertThat(readArray).containsExactly(1, 2, 3);
        }

        @Test
        void readOverCapacity() {
            // given
            ProtobufInput testee = testee(0, 1, 2);

            // when
            assertThatThrownBy(() -> testee.readBytes(4))
                    .isInstanceOf(InputEndedException.class);
        }

        @Test
        void readOverLimit() throws IOException {
            // given
            ProtobufInput testee = testee(0, 1, 2, 3);
            testee.setLimit(3);

            // when
            assertThatThrownBy(() -> testee.readBytes(4))
                    .isInstanceOf(LimitExceededException.class);
        }

        @Test
        void readArrayDecreasesLimit() throws IOException {
            // given
            ProtobufInput testee = testee(0, 1, 2, 3, 4, 5, 6, 7);
            testee.setLimit(6);

            // when
            byte[] readArray = testee.readBytes(6);
            assertThatThrownBy(() -> testee.readBytes(1))
                    .isInstanceOf(LimitExceededException.class);

            // then
            assertThat(readArray).containsExactly(0, 1, 2, 3, 4, 5);
        }
    }

    @Nested
    class ReadString {

        @Test
        void readString() throws IOException {
            // given
            ProtobufInput testee = testee(0, 'b', 'a', 'r');

            // when
            byte read1 = testee.readByte();
            String readString = testee.readString(3);
            assertThatThrownBy(() -> testee.readString(1))
                    .isInstanceOf(InputEndedException.class);

            // then
            assertThat(read1).isEqualTo((byte) 0);
            assertThat(readString).isEqualTo("bar");
        }

        @Test
        void readOverCapacity() {
            // given
            ProtobufInput testee = testee('b', 'a', 'r');

            // when
            assertThatThrownBy(() -> testee.readString(4))
                    .isInstanceOf(InputEndedException.class);
        }

        @Test
        void readOverLimit() throws IOException {
            // given
            ProtobufInput testee = testee('f', 'o', 'o', '!');
            testee.setLimit(3);

            // when
            assertThatThrownBy(() -> testee.readString(4))
                    .isInstanceOf(LimitExceededException.class);
        }

        @Test
        void readStringDecreasesLimit() throws IOException {
            // given
            ProtobufInput testee = testee('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
            testee.setLimit(6);

            // when
            String readString = testee.readString(6);
            assertThatThrownBy(() -> testee.readString(1))
                    .isInstanceOf(LimitExceededException.class);

            // then
            assertThat(readString).isEqualTo("abcdef");
        }
    }

    @Nested
    class Skip {

        @Test
        void skip() throws IOException {
            // given
            ProtobufInput testee = testee(0, 1, 2, 3);

            // when
            byte read1 = testee.readByte();
            testee.skip(3);
            assertThatThrownBy(() -> testee.skip(1))
                    .isInstanceOf(InputEndedException.class);

            // then
            assertThat(read1).isEqualTo((byte) 0);
        }

        @Test
        void skipOverCapacity() {
            // given
            ProtobufInput testee = testee(0, 1, 2);

            // when
            assertThatThrownBy(() -> testee.skip(4))
                    .isInstanceOf(InputEndedException.class);
        }

        @Test
        void skipOverLimit() throws IOException {
            // given
            ProtobufInput testee = testee(0, 1, 2, 3);
            testee.setLimit(3);

            // when
            assertThatThrownBy(() -> testee.skip(4))
                    .isInstanceOf(LimitExceededException.class);
        }

        @Test
        void skipDecreasesLimit() throws IOException {
            // given
            ProtobufInput testee = testee(0, 1, 2, 3, 4, 5, 6, 7);
            testee.setLimit(6);

            // when
            testee.skip(6);
            assertThatThrownBy(() -> testee.skip(1))
                    .isInstanceOf(LimitExceededException.class);
        }
    }

    @Nested
    class Mixed {

        @Test
        void mixedCalls() throws IOException {
            // given
            ProtobufInput testee = testee(
                    0, 1, 2,
                    3,
                    4,
                    'f', 'o', 'o',
                    't', 'e', 's', 't', 's',
                    13, 14, 15, 16, 17, 18, 19, 20, 21,
                    22, 23, 24, 25,
                    26,
                    27, 28
            );
            testee.setLimit(27);

            // when
            byte[] readArray1 = testee.readBytes(3);
            byte read1 = testee.readByte();
            byte read2 = testee.readByte();

            String readString1 = testee.readString(3);
            String readString2 = testee.readString(5);

            testee.skip(9);

            byte[] readArray2 = testee.readBytes(4);
            byte read3 = testee.readByte();

            assertThatThrownBy(() -> testee.readBytes(1))
                    .isInstanceOf(LimitExceededException.class);
            testee.setLimit(2);

            byte read4 = testee.readByte();
            byte read5 = testee.readByte();

            // then
            assertThat(readArray1).containsExactly(0, 1, 2);
            assertThat(read1).isEqualTo((byte) 3);
            assertThat(read2).isEqualTo((byte) 4);
            assertThat(readString1).isEqualTo("foo");
            assertThat(readString2).isEqualTo("tests");
            assertThat(readArray2).containsExactly(22, 23, 24, 25);
            assertThat(read3).isEqualTo((byte) 26);
            assertThat(read4).isEqualTo((byte) 27);
            assertThat(read5).isEqualTo((byte) 28);
        }
    }

    private static ProtobufInput testee(int... values) {
        byte[] bytes = new byte[values.length];
        for (int i = 0; i < values.length; i++) {
            bytes[i] = (byte) values[i];
        }

        return ProtobufInput.from(bytes);
    }
}
