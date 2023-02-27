package com.github.pcimcioch.protobuf.io;

import com.github.pcimcioch.protobuf.io.exception.InputEndedException;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public final class ProtobufAssertion {

    private final ProtobufInput input;

    private ProtobufAssertion(byte[] data) {
        this.input = ProtobufInput.from(data);
    }

    public static ProtobufAssertion assertProto(byte[] data) throws IOException {
        return new ProtobufAssertion(data);
    }

    public ProtobufAssertion double_(int number, double expectedValue) {
        int expectedTag = number << 3 | 1;

        try {
            int tag = input.readVarint32();
            double value = input.readDouble();

            assertThat(tag).isEqualTo(expectedTag);
            assertThat(value).isEqualTo(expectedValue);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion float_(int number, float expectedValue) {
        int expectedTag = number << 3 | 5;

        try {
            int tag = input.readVarint32();
            float value = input.readFloat();

            assertThat(tag).isEqualTo(expectedTag);
            assertThat(value).isEqualTo(expectedValue);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion int32(int number, int expectedValue) {
        int expectedTag = number << 3;

        try {
            int tag = input.readVarint32();
            int value = input.readVarint32();

            assertThat(tag).isEqualTo(expectedTag);
            assertThat(value).isEqualTo(expectedValue);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion int64(int number, long expectedValue) {
        int expectedTag = number << 3;

        try {
            int tag = input.readVarint32();
            long value = input.readVarint64();

            assertThat(tag).isEqualTo(expectedTag);
            assertThat(value).isEqualTo(expectedValue);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion uint32(int number, int expectedValue) {
        int expectedTag = number << 3;

        try {
            int tag = input.readVarint32();
            int value = input.readVarint32();

            assertThat(tag).isEqualTo(expectedTag);
            assertThat(value).isEqualTo(expectedValue);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion uint64(int number, long expectedValue) {
        int expectedTag = number << 3;

        try {
            int tag = input.readVarint32();
            long value = input.readVarint64();

            assertThat(tag).isEqualTo(expectedTag);
            assertThat(value).isEqualTo(expectedValue);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion sint32(int number, int expectedValue) {
        int expectedTag = number << 3;

        try {
            int tag = input.readVarint32();
            int value = input.readZigZag32();

            assertThat(tag).isEqualTo(expectedTag);
            assertThat(value).isEqualTo(expectedValue);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion sint64(int number, long expectedValue) {
        int expectedTag = number << 3;

        try {
            int tag = input.readVarint32();
            long value = input.readZigZag64();

            assertThat(tag).isEqualTo(expectedTag);
            assertThat(value).isEqualTo(expectedValue);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion fixed32(int number, int expectedValue) {
        int expectedTag = number << 3 | 5;

        try {
            int tag = input.readVarint32();
            int value = input.readFixedInt();

            assertThat(tag).isEqualTo(expectedTag);
            assertThat(value).isEqualTo(expectedValue);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion fixed64(int number, long expectedValue) {
        int expectedTag = number << 3 | 1;

        try {
            int tag = input.readVarint32();
            long value = input.readFixedLong();

            assertThat(tag).isEqualTo(expectedTag);
            assertThat(value).isEqualTo(expectedValue);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion sfixed32(int number, int expectedValue) {
        int expectedTag = number << 3 | 5;

        try {
            int tag = input.readVarint32();
            int value = input.readFixedInt();

            assertThat(tag).isEqualTo(expectedTag);
            assertThat(value).isEqualTo(expectedValue);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion sfixed64(int number, long expectedValue) {
        int expectedTag = number << 3 | 1;

        try {
            int tag = input.readVarint32();
            long value = input.readFixedLong();

            assertThat(tag).isEqualTo(expectedTag);
            assertThat(value).isEqualTo(expectedValue);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion bool(int number, boolean expectedValue) {
        int expectedTag = number << 3;

        try {
            int tag = input.readVarint32();
            boolean value = input.readBoolean();

            assertThat(tag).isEqualTo(expectedTag);
            assertThat(value).isEqualTo(expectedValue);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion string(int number, String expectedValue) {
        int expectedTag = number << 3 | 2;

        try {
            int tag = input.readVarint32();
            String value = input.readString();

            assertThat(tag).isEqualTo(expectedTag);
            assertThat(value).isEqualTo(expectedValue);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion bytes(int number, byte[] expectedValue) {
        int expectedTag = number << 3 | 2;

        try {
            int tag = input.readVarint32();
            byte[] value = input.readBytes();

            assertThat(tag).isEqualTo(expectedTag);
            assertThat(value).isEqualTo(expectedValue);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion message(int number, Consumer<ProtobufAssertion> messageAssertions) {
        int expectedTag = number << 3 | 2;

        try {
            int tag = input.readVarint32();
            byte[] value = input.readBytes();

            assertThat(tag).isEqualTo(expectedTag);
            messageAssertions.accept(assertProto(value));
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public void end() {
        assertThatThrownBy(input::readBoolean)
                .isInstanceOf(InputEndedException.class);
    }
}
