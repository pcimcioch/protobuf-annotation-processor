package com.github.pcimcioch.protobuf.io;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.github.pcimcioch.protobuf.io.ByteUtils.b;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ArrayProtobufOutputTest {

    @Nested
    class WriteByte {
        private final byte[] data = new byte[5];
        private final ProtobufOutput testee = ProtobufOutput.from(data);

        @Test
        void writeOneByte() throws IOException {
            // when
            testee.writeRawByte((byte) 1);

            // then
            assertThat(data).containsExactly(1, 0, 0, 0, 0);
        }

        @Test
        void writeMultipleBytes() throws IOException {
            // when
            testee.writeRawByte((byte) 1);
            testee.writeRawByte((byte) 2);
            testee.writeRawByte((byte) 3);

            // then
            assertThat(data).containsExactly(1, 2, 3, 0, 0);
        }

        @Test
        void writeAllData() throws IOException {
            // when
            testee.writeRawByte((byte) 1);
            testee.writeRawByte((byte) 2);
            testee.writeRawByte((byte) 3);
            testee.writeRawByte((byte) 4);
            testee.writeRawByte((byte) 5);

            // then
            assertThat(data).containsExactly(1, 2, 3, 4, 5);
        }

        @Test
        void writeOverCapacity() throws IOException {
            // when
            testee.writeRawByte((byte) 1);
            testee.writeRawByte((byte) 2);
            testee.writeRawByte((byte) 3);
            testee.writeRawByte((byte) 4);
            testee.writeRawByte((byte) 5);

            assertThatThrownBy(() -> testee.writeRawByte((byte) 6))
                    .isInstanceOf(IndexOutOfBoundsException.class);

            // then
            assertThat(data).containsExactly(1, 2, 3, 4, 5);
        }
    }

    @Nested
    class WriteByteArray {
        private final byte[] data = new byte[5];
        private final ProtobufOutput testee = ProtobufOutput.from(data);

        @Test
        void writeByteArray() throws IOException {
            // when
            testee.writeRawByte((byte) 1);
            testee.writeRawBytes(b(2, 3, 4));
            testee.writeRawByte((byte) 5);

            // then
            assertThat(data).containsExactly(1, 2, 3, 4, 5);
        }

        @Test
        void writeAllData() throws IOException {
            // when
            testee.writeRawBytes(b(1, 2, 3, 4, 5));

            // then
            assertThat(data).containsExactly(1, 2, 3, 4, 5);
        }

        @Test
        void writeOverCapacity() {
            // when
            assertThatThrownBy(() -> testee.writeBytes(b(1, 2, 3, 4, 5, 6)))
                    .isInstanceOf(IndexOutOfBoundsException.class);
        }

        @Test
        void writeJustOverCapacity() throws IOException {
            // when
            testee.writeRawBytes(b(1, 2, 3, 4, 5));
            assertThatThrownBy(() -> testee.writeBytes(b(6)))
                    .isInstanceOf(IndexOutOfBoundsException.class);

            // then
            assertThat(data).containsExactly(1, 2, 3, 4, 5);
        }
    }

    @Nested
    class Mixed {
        private final byte[] data = new byte[12];
        private final ProtobufOutput testee = ProtobufOutput.from(data);

        @Test
        void mixedCalls() throws IOException {
            // when
            testee.writeRawByte((byte) 1);
            testee.writeRawByte((byte) 2);
            testee.writeRawBytes(b(3, 4, 5));
            testee.writeRawBytes(b(6, 7, 8, 9));
            testee.writeRawByte((byte) 10);
            testee.writeRawBytes(b(11, 12));
            assertThatThrownBy(() -> testee.writeRawByte((byte) 13))
                    .isInstanceOf(IndexOutOfBoundsException.class);

            // then
            assertThat(data).containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        }
    }
}
