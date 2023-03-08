package com.github.pcimcioch.protobuf.io;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;

import static com.github.pcimcioch.protobuf.io.ByteUtils.b;
import static org.assertj.core.api.Assertions.assertThat;

class StreamProtobufOutputTest {

    private final ByteArrayOutputStream stream = new ByteArrayOutputStream();
    private final ProtobufOutput testee = ProtobufOutput.from(stream, 4);

    @Nested
    class WriteByte {

        @Test
        void writeOneByte() throws Exception {
            // when
            testee.writeRawByte((byte) 1);
            testee.close();

            // then
            assertThat(stream.toByteArray()).containsExactly(1);
        }

        @Test
        void writeMultipleBytes() throws Exception {
            // when
            testee.writeRawByte((byte) 1);
            testee.writeRawByte((byte) 2);
            testee.writeRawByte((byte) 3);
            testee.close();

            // then
            assertThat(stream.toByteArray()).containsExactly(1, 2, 3);
        }

        @Test
        void writeBufferSize() throws Exception {
            // when
            testee.writeRawByte((byte) 1);
            testee.writeRawByte((byte) 2);
            testee.writeRawByte((byte) 3);
            testee.writeRawByte((byte) 4);
            testee.close();

            // then
            assertThat(stream.toByteArray()).containsExactly(1, 2, 3, 4);
        }

        @Test
        void writeOverBufferSize() throws Exception {
            // when
            testee.writeRawByte((byte) 1);
            testee.writeRawByte((byte) 2);
            testee.writeRawByte((byte) 3);
            testee.writeRawByte((byte) 4);
            testee.writeRawByte((byte) 5);
            testee.writeRawByte((byte) 6);
            testee.writeRawByte((byte) 7);
            testee.writeRawByte((byte) 8);
            testee.writeRawByte((byte) 9);
            testee.writeRawByte((byte) 10);
            testee.close();

            // then
            assertThat(stream.toByteArray()).containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        }
    }

    @Nested
    class WriteByteArray {

        @Test
        void writeBelowBuffer() throws Exception {
            // when
            testee.writeRawBytes(b(1, 2, 3));
            testee.close();

            // then
            assertThat(stream.toByteArray()).containsExactly(1, 2, 3);
        }

        @Test
        void writeExactlyBuffer() throws Exception {
            // when
            testee.writeRawBytes(b(1, 2, 3, 4));
            testee.close();

            // then
            assertThat(stream.toByteArray()).containsExactly(1, 2, 3, 4);
        }

        @Test
        void writeWhenBufferNotEmpty() throws Exception {
            // when
            testee.writeRawByte((byte) 1);
            testee.writeRawBytes(b(2, 3, 4, 5));
            testee.close();

            // then
            assertThat(stream.toByteArray()).containsExactly(1, 2, 3, 4, 5);
        }

        @Test
        void writeOverMultipleBuffers() throws Exception {
            // when
            testee.writeRawBytes(b(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
            testee.close();

            // then
            assertThat(stream.toByteArray()).containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        }
    }

    @Nested
    class Mixed {

        @Test
        void mixedCalls() throws Exception {
            // when
            testee.writeRawByte((byte) 1);
            testee.writeRawByte((byte) 2);
            testee.writeRawBytes(b(3, 4, 5, 6, 7, 8, 9));
            testee.writeRawBytes(b(10, 11));
            testee.writeRawByte((byte) 12);
            testee.writeRawByte((byte) 13);
            testee.close();

            // then
            assertThat(stream.toByteArray()).containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13);
        }
    }
}
