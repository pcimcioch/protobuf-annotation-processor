package com.github.pcimcioch.protobuf.io;

import com.github.pcimcioch.protobuf.io.exception.InputEndedException;
import com.github.pcimcioch.protobuf.io.exception.LimitExceededException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static com.github.pcimcioch.protobuf.io.ByteUtils.b;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StreamProtobufInputTest {

    @Nested
    class ReadByte {
        private final ProtobufInput testee = testee(b(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), 4);

        @Test
        void readOneByte() throws IOException {
            // when
            byte read = testee.readRawByte();

            // then
            assertThat(read).isEqualTo((byte) 0);
        }

        @Test
        void readMultipleByte() throws IOException {
            // when
            byte read1 = testee.readRawByte();
            byte read2 = testee.readRawByte();
            byte read3 = testee.readRawByte();

            // then
            assertThat(read1).isEqualTo((byte) 0);
            assertThat(read2).isEqualTo((byte) 1);
            assertThat(read3).isEqualTo((byte) 2);
        }

        @Test
        void readBufferSize() throws IOException {
            // when
            byte read1 = testee.readRawByte();
            byte read2 = testee.readRawByte();
            byte read3 = testee.readRawByte();
            byte read4 = testee.readRawByte();

            // then
            assertThat(read1).isEqualTo((byte) 0);
            assertThat(read2).isEqualTo((byte) 1);
            assertThat(read3).isEqualTo((byte) 2);
            assertThat(read4).isEqualTo((byte) 3);
        }

        @Test
        void readAllData() throws IOException {
            // when
            byte read1 = testee.readRawByte();
            byte read2 = testee.readRawByte();
            byte read3 = testee.readRawByte();
            byte read4 = testee.readRawByte();
            byte read5 = testee.readRawByte();
            byte read6 = testee.readRawByte();
            byte read7 = testee.readRawByte();
            byte read8 = testee.readRawByte();
            byte read9 = testee.readRawByte();
            byte read10 = testee.readRawByte();

            // then
            assertThat(read1).isEqualTo((byte) 0);
            assertThat(read2).isEqualTo((byte) 1);
            assertThat(read3).isEqualTo((byte) 2);
            assertThat(read4).isEqualTo((byte) 3);
            assertThat(read5).isEqualTo((byte) 4);
            assertThat(read6).isEqualTo((byte) 5);
            assertThat(read7).isEqualTo((byte) 6);
            assertThat(read8).isEqualTo((byte) 7);
            assertThat(read9).isEqualTo((byte) 8);
            assertThat(read10).isEqualTo((byte) 9);
        }

        @Test
        void readJustOverCapacity() throws IOException {
            // when
            byte read1 = testee.readRawByte();
            byte read2 = testee.readRawByte();
            byte read3 = testee.readRawByte();
            byte read4 = testee.readRawByte();
            byte read5 = testee.readRawByte();
            byte read6 = testee.readRawByte();
            byte read7 = testee.readRawByte();
            byte read8 = testee.readRawByte();
            byte read9 = testee.readRawByte();
            byte read10 = testee.readRawByte();

            assertThatThrownBy(testee::readRawByte)
                    .isInstanceOf(InputEndedException.class);

            // then
            assertThat(read1).isEqualTo((byte) 0);
            assertThat(read2).isEqualTo((byte) 1);
            assertThat(read3).isEqualTo((byte) 2);
            assertThat(read4).isEqualTo((byte) 3);
            assertThat(read5).isEqualTo((byte) 4);
            assertThat(read6).isEqualTo((byte) 5);
            assertThat(read7).isEqualTo((byte) 6);
            assertThat(read8).isEqualTo((byte) 7);
            assertThat(read9).isEqualTo((byte) 8);
            assertThat(read10).isEqualTo((byte) 9);
        }

        @Test
        void readOverCapacity() throws IOException {
            // when
            byte read1 = testee.readRawByte();
            byte read2 = testee.readRawByte();
            byte read3 = testee.readRawByte();
            byte read4 = testee.readRawByte();
            byte read5 = testee.readRawByte();
            byte read6 = testee.readRawByte();
            byte read7 = testee.readRawByte();
            byte read8 = testee.readRawByte();

            assertThatThrownBy(() -> testee.readRawBytes(3))
                    .isInstanceOf(InputEndedException.class);

            // then
            assertThat(read1).isEqualTo((byte) 0);
            assertThat(read2).isEqualTo((byte) 1);
            assertThat(read3).isEqualTo((byte) 2);
            assertThat(read4).isEqualTo((byte) 3);
            assertThat(read5).isEqualTo((byte) 4);
            assertThat(read6).isEqualTo((byte) 5);
            assertThat(read7).isEqualTo((byte) 6);
            assertThat(read8).isEqualTo((byte) 7);
        }

        @Test
        void readToLimit() throws IOException {
            // given
            testee.setLimit(3);

            // when
            byte read1 = testee.readRawByte();
            byte read2 = testee.readRawByte();
            byte read3 = testee.readRawByte();

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
            byte read1 = testee.readRawByte();
            byte read2 = testee.readRawByte();
            byte read3 = testee.readRawByte();

            assertThatThrownBy(testee::readRawByte)
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
            assertThatThrownBy(() -> testee.readRawBytes(4))
                    .isInstanceOf(LimitExceededException.class);
        }
    }

    @Nested
    class ReadByteArray {

        @Test
        void readByteArrayFromBuffer() throws IOException {
            // given
            ProtobufInput testee = testee(b(0, 1, 2, 3), 4);

            // when
            byte read1 = testee.readRawByte();
            byte[] readArray = testee.readRawBytes(3);
            assertThatThrownBy(() -> testee.readRawBytes(1))
                    .isInstanceOf(InputEndedException.class);

            // then
            assertThat(read1).isEqualTo((byte) 0);
            assertThat(readArray).containsExactly(1, 2, 3);
        }

        @Test
        void readByteArrayWhenBufferEmpty() throws IOException {
            // given
            ProtobufInput testee = testee(b(0, 1, 2, 3), 4);

            // when
            byte[] readArray = testee.readRawBytes(3);
            byte read1 = testee.readRawByte();
            assertThatThrownBy(() -> testee.readRawBytes(1))
                    .isInstanceOf(InputEndedException.class);

            // then
            assertThat(readArray).containsExactly(0, 1, 2);
            assertThat(read1).isEqualTo((byte) 3);
        }

        @Test
        void readByteArrayOverMultipleBuffers() throws IOException {
            // given
            ProtobufInput testee = testee(b(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10), 4);

            // when
            byte read1 = testee.readRawByte();
            byte[] readArray = testee.readRawBytes(9);
            byte read2 = testee.readRawByte();

            assertThatThrownBy(() -> testee.readRawBytes(1))
                    .isInstanceOf(InputEndedException.class);

            // then
            assertThat(read1).isEqualTo((byte) 0);
            assertThat(readArray).containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9);
            assertThat(read2).isEqualTo((byte) 10);
        }

        @Test
        void readOverCapacity() {
            // given
            ProtobufInput testee = testee(b(0, 1, 2), 4);

            // when
            assertThatThrownBy(() -> testee.readRawBytes(4))
                    .isInstanceOf(InputEndedException.class);
        }

        @Test
        void readOverLimit() throws IOException {
            // given
            ProtobufInput testee = testee(b(0, 1, 2, 3), 4);
            testee.setLimit(3);

            // when
            assertThatThrownBy(() -> testee.readRawBytes(4))
                    .isInstanceOf(LimitExceededException.class);
        }

        @Test
        void readArrayDecreasesLimit() throws IOException {
            // given
            ProtobufInput testee = testee(b(0, 1, 2, 3, 4, 5, 6, 7), 4);
            testee.setLimit(6);

            // when
            byte[] readArray = testee.readRawBytes(6);
            assertThatThrownBy(() -> testee.readRawBytes(1))
                    .isInstanceOf(LimitExceededException.class);

            // then
            assertThat(readArray).containsExactly(0, 1, 2, 3, 4, 5);
        }
    }

    @Nested
    class ReadString {

        @Test
        void readStringFromBuffer() throws IOException {
            // given
            ProtobufInput testee = testee(b(0, 'b', 'a', 'r'), 4);

            // when
            byte read1 = testee.readRawByte();
            String readString = testee.readRawString(3);
            assertThatThrownBy(() -> testee.readRawString(1))
                    .isInstanceOf(InputEndedException.class);

            // then
            assertThat(read1).isEqualTo((byte) 0);
            assertThat(readString).isEqualTo("bar");
        }

        @Test
        void readStringWhenBufferEmpty() throws IOException {
            // given
            ProtobufInput testee = testee(b('b', 'a', 'r', 3), 4);

            // when
            String readString = testee.readRawString(3);
            byte read1 = testee.readRawByte();
            assertThatThrownBy(() -> testee.readRawString(1))
                    .isInstanceOf(InputEndedException.class);

            // then
            assertThat(readString).isEqualTo("bar");
            assertThat(read1).isEqualTo((byte) 3);
        }

        @Test
        void readStringOverMultipleBuffers() throws IOException {
            // given
            ProtobufInput testee = testee(b(0, 't', 'e', 's', 't', ' ', 'f', 'o', 'o', '!', 10), 4);

            // when
            byte read1 = testee.readRawByte();
            String readString = testee.readRawString(9);
            byte read2 = testee.readRawByte();

            assertThatThrownBy(() -> testee.readRawString(1))
                    .isInstanceOf(InputEndedException.class);

            // then
            assertThat(read1).isEqualTo((byte) 0);
            assertThat(readString).isEqualTo("test foo!");
            assertThat(read2).isEqualTo((byte) 10);
        }

        @Test
        void readOverCapacity() {
            // given
            ProtobufInput testee = testee(b('b', 'a', 'r'), 4);

            // when
            assertThatThrownBy(() -> testee.readRawString(4))
                    .isInstanceOf(InputEndedException.class);
        }

        @Test
        void readOverLimit() throws IOException {
            // given
            ProtobufInput testee = testee(b('f', 'o', 'o', '!'), 4);
            testee.setLimit(3);

            // when
            assertThatThrownBy(() -> testee.readRawString(4))
                    .isInstanceOf(LimitExceededException.class);
        }

        @Test
        void readStringDecreasesLimit() throws IOException {
            // given
            ProtobufInput testee = testee(b('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'), 4);
            testee.setLimit(6);

            // when
            String readString = testee.readRawString(6);
            assertThatThrownBy(() -> testee.readRawString(1))
                    .isInstanceOf(LimitExceededException.class);

            // then
            assertThat(readString).isEqualTo("abcdef");
        }
    }

    @Nested
    class Skip {

        @Test
        void skipFromBuffer() throws IOException {
            // given
            ProtobufInput testee = testee(b(0, 1, 2, 3), 4);

            // when
            byte read1 = testee.readRawByte();
            testee.skip(3);
            assertThatThrownBy(() -> testee.skip(1))
                    .isInstanceOf(InputEndedException.class);

            // then
            assertThat(read1).isEqualTo((byte) 0);
        }

        @Test
        void skipWhenBufferEmpty() throws IOException {
            // given
            ProtobufInput testee = testee(b(0, 1, 2, 3), 4);

            // when
            testee.skip(3);
            byte read1 = testee.readRawByte();
            assertThatThrownBy(() -> testee.skip(1))
                    .isInstanceOf(InputEndedException.class);

            // then
            assertThat(read1).isEqualTo((byte) 3);
        }

        @Test
        void skipOverMultipleBuffers() throws IOException {
            // given
            ProtobufInput testee = testee(b(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10), 4);

            // when
            byte read1 = testee.readRawByte();
            testee.skip(9);
            byte read2 = testee.readRawByte();

            assertThatThrownBy(() -> testee.skip(1))
                    .isInstanceOf(InputEndedException.class);

            // then
            assertThat(read1).isEqualTo((byte) 0);
            assertThat(read2).isEqualTo((byte) 10);
        }

        @Test
        void skipOverCapacity() {
            // given
            ProtobufInput testee = testee(b(0, 1, 2), 4);

            // when
            assertThatThrownBy(() -> testee.skip(4))
                    .isInstanceOf(InputEndedException.class);
        }

        @Test
        void skipOverLimit() throws IOException {
            // given
            ProtobufInput testee = testee(b(0, 1, 2, 3), 4);
            testee.setLimit(3);

            // when
            assertThatThrownBy(() -> testee.skip(4))
                    .isInstanceOf(LimitExceededException.class);
        }

        @Test
        void skipDecreasesLimit() throws IOException {
            // given
            ProtobufInput testee = testee(b(0, 1, 2, 3, 4, 5, 6, 7), 4);
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
            ProtobufInput testee = testee(b(
                    0, 1, 2,
                    3,
                    4,
                    'f', 'o', 'o',
                    't', 'e', 's', 't', 's',
                    13, 14, 15, 16, 17, 18, 19, 20, 21,
                    22, 23, 24, 25,
                    26,
                    27, 28
            ), 4);
            testee.setLimit(27);

            // when
            byte[] readArray1 = testee.readRawBytes(3);
            byte read1 = testee.readRawByte();
            byte read2 = testee.readRawByte();

            String readString1 = testee.readRawString(3);
            String readString2 = testee.readRawString(5);

            testee.skip(9);

            byte[] readArray2 = testee.readRawBytes(4);
            byte read3 = testee.readRawByte();

            assertThatThrownBy(() -> testee.readRawBytes(1))
                    .isInstanceOf(LimitExceededException.class);
            testee.setLimit(10);

            byte read4 = testee.readRawByte();
            byte read5 = testee.readRawByte();

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

    private static ProtobufInput testee(byte[] bytes, int bufferSize) {
        return ProtobufInput.from(new ByteArrayInputStream(bytes), bufferSize);
    }
}