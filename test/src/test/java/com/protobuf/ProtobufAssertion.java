package com.protobuf;

import com.github.pcimcioch.protobuf.io.ProtobufInput;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public final class ProtobufAssertion {

    private final ProtobufInput input;

    private ProtobufAssertion(InputStream inputStream) {
        this.input = new ProtobufInput(inputStream);
    }

    public static ProtobufAssertion assertProto(byte[] data) throws IOException {
        return new ProtobufAssertion(new ByteArrayInputStream(data));
    }

    public ProtobufAssertion double_(long number, double expectedValue) {
        long expectedTag = number << 3 | 1;

        try {
            long tag = input.readVarint();
            double value = input.readDouble();

            assertThat(tag).isEqualTo(expectedTag);
            assertThat(value).isEqualTo(expectedValue);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion float_(long number, float expectedValue) {
        long expectedTag = number << 3 | 5;

        try {
            long tag = input.readVarint();
            float value = input.readFloat();

            assertThat(tag).isEqualTo(expectedTag);
            assertThat(value).isEqualTo(expectedValue);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion int32(long number, int expectedValue) {
        long expectedTag = number << 3;

        try {
            long tag = input.readVarint();
            int value = (int) input.readVarint();

            assertThat(tag).isEqualTo(expectedTag);
            assertThat(value).isEqualTo(expectedValue);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion int64(long number, long expectedValue) {
        long expectedTag = number << 3;

        try {
            long tag = input.readVarint();
            long value = input.readVarint();

            assertThat(tag).isEqualTo(expectedTag);
            assertThat(value).isEqualTo(expectedValue);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion uint32(long number, int expectedValue) {
        long expectedTag = number << 3;

        try {
            long tag = input.readVarint();
            int value = (int) input.readVarint();

            assertThat(tag).isEqualTo(expectedTag);
            assertThat(value).isEqualTo(expectedValue);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion uint64(long number, long expectedValue) {
        long expectedTag = number << 3;

        try {
            long tag = input.readVarint();
            long value = input.readVarint();

            assertThat(tag).isEqualTo(expectedTag);
            assertThat(value).isEqualTo(expectedValue);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion sint32(long number, int expectedValue) {
        long expectedTag = number << 3;

        try {
            long tag = input.readVarint();
            int value = (int) input.readZigZag();

            assertThat(tag).isEqualTo(expectedTag);
            assertThat(value).isEqualTo(expectedValue);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion sint64(long number, long expectedValue) {
        long expectedTag = number << 3;

        try {
            long tag = input.readVarint();
            long value = input.readZigZag();

            assertThat(tag).isEqualTo(expectedTag);
            assertThat(value).isEqualTo(expectedValue);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion fixed32(long number, int expectedValue) {
        long expectedTag = number << 3 | 5;

        try {
            long tag = input.readVarint();
            int value = input.readFixedInt();

            assertThat(tag).isEqualTo(expectedTag);
            assertThat(value).isEqualTo(expectedValue);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion fixed64(long number, long expectedValue) {
        long expectedTag = number << 3 | 1;

        try {
            long tag = input.readVarint();
            long value = input.readFixedLong();

            assertThat(tag).isEqualTo(expectedTag);
            assertThat(value).isEqualTo(expectedValue);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion sfixed32(long number, int expectedValue) {
        long expectedTag = number << 3 | 5;

        try {
            long tag = input.readVarint();
            int value = input.readFixedInt();

            assertThat(tag).isEqualTo(expectedTag);
            assertThat(value).isEqualTo(expectedValue);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion sfixed64(long number, long expectedValue) {
        long expectedTag = number << 3 | 1;

        try {
            long tag = input.readVarint();
            long value = input.readFixedLong();

            assertThat(tag).isEqualTo(expectedTag);
            assertThat(value).isEqualTo(expectedValue);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion bool(long number, boolean expectedValue) {
        long expectedTag = number << 3;

        try {
            long tag = input.readVarint();
            boolean value = input.readBoolean();

            assertThat(tag).isEqualTo(expectedTag);
            assertThat(value).isEqualTo(expectedValue);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion string(long number, String expectedValue) {
        long expectedTag = number << 3 | 2;

        try {
            long tag = input.readVarint();
            String value = input.readString();

            assertThat(tag).isEqualTo(expectedTag);
            assertThat(value).isEqualTo(expectedValue);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion bytes(long number, byte[] expectedValue) {
        long expectedTag = number << 3 | 2;

        try {
            long tag = input.readVarint();
            byte[] value = input.readBytes();

            assertThat(tag).isEqualTo(expectedTag);
            assertThat(value).isEqualTo(expectedValue);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion message(long number, Consumer<ProtobufAssertion> messageAssertions) {
        long expectedTag = number << 3 | 2;

        try {
            long tag = input.readVarint();
            byte[] value = input.readBytes();

            assertThat(tag).isEqualTo(expectedTag);
            messageAssertions.accept(assertProto(value));
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public void end() {
        assertThatThrownBy(input::readBoolean).isInstanceOf(EOFException.class);
    }
}
