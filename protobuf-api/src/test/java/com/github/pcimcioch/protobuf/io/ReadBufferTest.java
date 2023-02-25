package com.github.pcimcioch.protobuf.io;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReadBufferTest {

    @Nested
    class ReadSingleByte {

        private final ReadBuffer testee = testee(b(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), 4);

        @Test
        void readOneByte() throws IOException {
            // when
            testee.ensureAvailable(1);
            byte read = testee.read();

            // then
            assertThat(read).isEqualTo((byte) 0);
        }

        @Test
        void readMultipleByte() throws IOException {
            // when
            testee.ensureAvailable(3);
            byte read1 = testee.read();
            byte read2 = testee.read();
            byte read3 = testee.read();

            // then
            assertThat(read1).isEqualTo((byte) 0);
            assertThat(read2).isEqualTo((byte) 1);
            assertThat(read3).isEqualTo((byte) 2);
        }

        @Test
        void readBufferSize() throws IOException {
            // when
            testee.ensureAvailable(4);
            byte read1 = testee.read();
            byte read2 = testee.read();
            byte read3 = testee.read();
            byte read4 = testee.read();

            // then
            assertThat(read1).isEqualTo((byte) 0);
            assertThat(read2).isEqualTo((byte) 1);
            assertThat(read3).isEqualTo((byte) 2);
            assertThat(read4).isEqualTo((byte) 3);
        }

        @Test
        void readAllData() throws IOException {
            // when
            testee.ensureAvailable(4);
            byte read1 = testee.read();
            byte read2 = testee.read();
            byte read3 = testee.read();
            byte read4 = testee.read();

            testee.ensureAvailable(3);
            byte read5 = testee.read();
            byte read6 = testee.read();
            byte read7 = testee.read();

            testee.ensureAvailable(2);
            byte read8 = testee.read();
            byte read9 = testee.read();

            testee.ensureAvailable(1);
            byte read10 = testee.read();

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
            testee.ensureAvailable(4);
            byte read1 = testee.read();
            byte read2 = testee.read();
            byte read3 = testee.read();
            byte read4 = testee.read();

            testee.ensureAvailable(4);
            byte read5 = testee.read();
            byte read6 = testee.read();
            byte read7 = testee.read();
            byte read8 = testee.read();

            testee.ensureAvailable(2);
            byte read9 = testee.read();
            byte read10 = testee.read();

            assertThrowsProtobufException(() -> testee.ensureAvailable(1), ProtobufProtocolException.inputEnded());

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
            testee.ensureAvailable(4);
            byte read1 = testee.read();
            byte read2 = testee.read();
            byte read3 = testee.read();
            byte read4 = testee.read();

            testee.ensureAvailable(4);
            byte read5 = testee.read();
            byte read6 = testee.read();
            byte read7 = testee.read();
            byte read8 = testee.read();

            assertThrowsProtobufException(() -> testee.ensureAvailable(3), ProtobufProtocolException.inputEnded());

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
            testee.ensureAvailable(3);
            byte read1 = testee.read();
            byte read2 = testee.read();
            byte read3 = testee.read();

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
            testee.ensureAvailable(3);
            byte read1 = testee.read();
            byte read2 = testee.read();
            byte read3 = testee.read();

            assertThrowsProtobufException(() -> testee.ensureAvailable(1), ProtobufProtocolException.limitExceeded());

            // then
            assertThat(read1).isEqualTo((byte) 0);
            assertThat(read2).isEqualTo((byte) 1);
            assertThat(read3).isEqualTo((byte) 2);
        }

        @Test
        void readOverLimit() {
            // given
            testee.setLimit(3);

            // when
            assertThrowsProtobufException(() -> testee.ensureAvailable(4), ProtobufProtocolException.limitExceeded());
        }
    }

    @Nested
    class ReadByteArray {

        @Test
        void readByteArrayFromBuffer() throws IOException {
            // given
            ReadBuffer testee = testee(b(0, 1, 2, 3), 4);

            // when
            testee.ensureAvailable(4);

            byte[] readArray = testee.read(3);
            byte read1 = testee.read();
            assertThrowsProtobufException(() -> testee.read(1), ProtobufProtocolException.inputEnded());

            // then
            assertThat(readArray).containsExactly(0, 1, 2);
            assertThat(read1).isEqualTo((byte) 3);
        }

        @Test
        void readByteArrayWhenBufferEmpty() throws IOException {
            // given
            ReadBuffer testee = testee(b(0, 1, 2, 3), 4);

            // when
            byte[] readArray = testee.read(3);
            testee.ensureAvailable(1);
            byte read1 = testee.read();
            assertThrowsProtobufException(() -> testee.read(1), ProtobufProtocolException.inputEnded());

            // then
            assertThat(readArray).containsExactly(0, 1, 2);
            assertThat(read1).isEqualTo((byte) 3);
        }

        @Test
        void readByteArrayOverMultipleBuffers() throws IOException {
            // given
            ReadBuffer testee = testee(b(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10), 4);

            // when
            testee.ensureAvailable(1);
            byte read1 = testee.read();
            byte[] readArray = testee.read(9);
            testee.ensureAvailable(1);
            byte read2 = testee.read();

            assertThrowsProtobufException(() -> testee.read(1), ProtobufProtocolException.inputEnded());

            // then
            assertThat(read1).isEqualTo((byte) 0);
            assertThat(readArray).containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9);
            assertThat(read2).isEqualTo((byte) 10);
        }

        @Test
        void readOverCapacity() {
            // given
            ReadBuffer testee = testee(b(0, 1, 2), 4);

            // when
            assertThrowsProtobufException(() -> testee.read(4), ProtobufProtocolException.inputEnded());
        }

        @Test
        void readOverLimit() {
            // given
            ReadBuffer testee = testee(b(0, 1, 2, 4), 4);
            testee.setLimit(3);

            // when
            assertThrowsProtobufException(() -> testee.read(4), ProtobufProtocolException.limitExceeded());
        }

        @Test
        void readArrayDecreasesLimit() throws IOException {
            // given
            ReadBuffer testee = testee(b(0, 1, 2, 3, 4, 5, 6, 7), 4);
            testee.setLimit(6);

            // when
            byte[] readArray = testee.read(6);
            assertThrowsProtobufException(() -> testee.read(1), ProtobufProtocolException.limitExceeded());

            // then
            assertThat(readArray).containsExactly(0, 1, 2, 3, 4, 5);
        }
    }

    @Nested
    class ReadString {

        @Test
        void readStringFromBuffer() throws IOException {
            // given
            ReadBuffer testee = testee(b('b', 'a', 'r', 3), 4);

            // when
            testee.ensureAvailable(4);

            String readString = testee.readString(3);
            byte read1 = testee.read();
            assertThrowsProtobufException(() -> testee.readString(1), ProtobufProtocolException.inputEnded());

            // then
            assertThat(readString).isEqualTo("bar");
            assertThat(read1).isEqualTo((byte) 3);
        }

        @Test
        void readStringWhenBufferEmpty() throws IOException {
            // given
            ReadBuffer testee = testee(b('b', 'a', 'r', 3), 4);

            // when
            String readString = testee.readString(3);
            testee.ensureAvailable(1);
            byte read1 = testee.read();
            assertThrowsProtobufException(() -> testee.readString(1), ProtobufProtocolException.inputEnded());

            // then
            assertThat(readString).isEqualTo("bar");
            assertThat(read1).isEqualTo((byte) 3);
        }

        @Test
        void readStringOverMultipleBuffers() throws IOException {
            // given
            ReadBuffer testee = testee(b(0, 't', 'e', 's', 't', ' ', 'f', 'o', 'o', '!', 10), 4);

            // when
            testee.ensureAvailable(1);
            byte read1 = testee.read();
            String readString = testee.readString(9);
            testee.ensureAvailable(1);
            byte read2 = testee.read();

            assertThrowsProtobufException(() -> testee.readString(1), ProtobufProtocolException.inputEnded());

            // then
            assertThat(read1).isEqualTo((byte) 0);
            assertThat(readString).isEqualTo("test foo!");
            assertThat(read2).isEqualTo((byte) 10);
        }

        @Test
        void readOverCapacity() {
            // given
            ReadBuffer testee = testee(b('b', 'a', 'r'), 4);

            // when
            assertThrowsProtobufException(() -> testee.readString(4), ProtobufProtocolException.inputEnded());
        }

        @Test
        void readOverLimit() {
            // given
            ReadBuffer testee = testee(b('f', 'o', 'o', '!'), 4);
            testee.setLimit(3);

            // when
            assertThrowsProtobufException(() -> testee.readString(4), ProtobufProtocolException.limitExceeded());
        }

        @Test
        void readStringDecreasesLimit() throws IOException {
            // given
            ReadBuffer testee = testee(b('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'), 4);
            testee.setLimit(6);

            // when
            String readString = testee.readString(6);
            assertThrowsProtobufException(() -> testee.readString(1), ProtobufProtocolException.limitExceeded());

            // then
            assertThat(readString).isEqualTo("abcdef");
        }
    }

    @Nested
    class Skip {

        @Test
        void skipFromBuffer() throws IOException {
            // given
            ReadBuffer testee = testee(b(0, 1, 2, 3), 4);

            // when
            testee.ensureAvailable(4);

            testee.skip(3);
            byte read1 = testee.read();
            assertThrowsProtobufException(() -> testee.skip(1), ProtobufProtocolException.inputEnded());

            // then
            assertThat(read1).isEqualTo((byte) 3);
        }

        @Test
        void skipWhenBufferEmpty() throws IOException {
            // given
            ReadBuffer testee = testee(b(0, 1, 2, 3), 4);

            // when
            testee.skip(3);
            testee.ensureAvailable(1);
            byte read1 = testee.read();
            assertThrowsProtobufException(() -> testee.skip(1), ProtobufProtocolException.inputEnded());

            // then
            assertThat(read1).isEqualTo((byte) 3);
        }

        @Test
        void skipOverMultipleBuffers() throws IOException {
            // given
            ReadBuffer testee = testee(b(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10), 4);

            // when
            testee.ensureAvailable(1);
            byte read1 = testee.read();
            testee.skip(9);
            testee.ensureAvailable(1);
            byte read2 = testee.read();

            assertThrowsProtobufException(() -> testee.skip(1), ProtobufProtocolException.inputEnded());

            // then
            assertThat(read1).isEqualTo((byte) 0);
            assertThat(read2).isEqualTo((byte) 10);
        }

        @Test
        void skipOverCapacity() {
            // given
            ReadBuffer testee = testee(b(0, 1, 2), 4);

            // when
            assertThrowsProtobufException(() -> testee.skip(4), ProtobufProtocolException.inputEnded());
        }

        @Test
        void skipOverLimit() {
            // given
            ReadBuffer testee = testee(b(0, 1, 2, 4), 4);
            testee.setLimit(3);

            // when
            assertThrowsProtobufException(() -> testee.skip(4), ProtobufProtocolException.limitExceeded());
        }

        @Test
        void skipDecreasesLimit() throws IOException {
            // given
            ReadBuffer testee = testee(b(0, 1, 2, 3, 4, 5, 6, 7), 4);
            testee.setLimit(6);

            // when
            testee.skip(6);
            assertThrowsProtobufException(() -> testee.skip(1), ProtobufProtocolException.limitExceeded());
        }
    }

    @Nested
    class Mixed {

        @Test
        void mixedCalls() throws IOException {
            // given
            ReadBuffer testee = testee(b(
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
            byte[] readArray1 = testee.read(3);

            testee.ensureAvailable(2);
            byte read1 = testee.read();
            byte read2 = testee.read();

            String readString1 = testee.readString(3);
            String readString2 = testee.readString(5);

            testee.skip(9);

            byte[] readArray2 = testee.read(4);

            testee.ensureAvailable(1);
            byte read3 = testee.read();

            assertThrowsProtobufException(() -> testee.ensureAvailable(1), ProtobufProtocolException.limitExceeded());
            testee.setLimit(10);
            testee.ensureAvailable(2);
            byte read4 = testee.read();
            byte read5 = testee.read();

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

    private static ReadBuffer testee(byte[] bytes, int bufferSize) {
        return ReadBuffer.from(new ByteArrayInputStream(bytes), bufferSize);
    }

    private static byte[] b(int... values) {
        byte[] bytes = new byte[values.length];
        for (int i = 0; i < values.length; i++) {
            bytes[i] = (byte) values[i];
        }
        return bytes;
    }

    private void assertThrowsProtobufException(ThrowableAssert.ThrowingCallable shouldRaiseThrowable, ProtobufProtocolException exception) {
        assertThatThrownBy(shouldRaiseThrowable)
                .isInstanceOf(ProtobufProtocolException.class)
                .hasMessage(exception.getMessage());
    }
}