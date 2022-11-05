package com.github.pcimcioch.protobuf.test;

import com.github.pcimcioch.protobuf.io.ProtobufInput;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.*;

public class ProtobufAssertion {

    private final ProtobufInput input;

    private ProtobufAssertion(InputStream inputStream) {
        this.input = new ProtobufInput(inputStream);
    }

    public ProtobufAssertion _double(long number, double expectedValue) throws IOException {
        long expectedTag = number << 3 | 1;

        long tag = input.readVarint();
        double value = input.readDouble();

        assertThat(tag).isEqualTo(expectedTag);
        assertThat(value).isEqualTo(expectedValue);

        return this;
    }

    public ProtobufAssertion _float(long number, float expectedValue) throws IOException {
        long expectedTag = number << 3 | 5;

        long tag = input.readVarint();
        float value = input.readFloat();

        assertThat(tag).isEqualTo(expectedTag);
        assertThat(value).isEqualTo(expectedValue);

        return this;
    }

    public ProtobufAssertion int32(long number, int expectedValue) throws IOException {
        long expectedTag = number << 3;

        long tag = input.readVarint();
        int value = (int) input.readVarint();

        assertThat(tag).isEqualTo(expectedTag);
        assertThat(value).isEqualTo(expectedValue);

        return this;
    }

    public ProtobufAssertion int64(long number, long expectedValue) throws IOException {
        long expectedTag = number << 3;

        long tag = input.readVarint();
        long value = input.readVarint();

        assertThat(tag).isEqualTo(expectedTag);
        assertThat(value).isEqualTo(expectedValue);

        return this;
    }

    public ProtobufAssertion uint32(long number, int expectedValue) throws IOException {
        long expectedTag = number << 3;

        long tag = input.readVarint();
        int value = (int) input.readVarint();

        assertThat(tag).isEqualTo(expectedTag);
        assertThat(value).isEqualTo(expectedValue);

        return this;
    }

    public ProtobufAssertion uint64(long number, long expectedValue) throws IOException {
        long expectedTag = number << 3;

        long tag = input.readVarint();
        long value = input.readVarint();

        assertThat(tag).isEqualTo(expectedTag);
        assertThat(value).isEqualTo(expectedValue);

        return this;
    }

    public ProtobufAssertion sint32(long number, int expectedValue) throws IOException {
        long expectedTag = number << 3;

        long tag = input.readVarint();
        int value = (int) input.readZigZag();

        assertThat(tag).isEqualTo(expectedTag);
        assertThat(value).isEqualTo(expectedValue);

        return this;
    }

    public ProtobufAssertion sint64(long number, long expectedValue) throws IOException {
        long expectedTag = number << 3;

        long tag = input.readVarint();
        long value = input.readZigZag();

        assertThat(tag).isEqualTo(expectedTag);
        assertThat(value).isEqualTo(expectedValue);

        return this;
    }

    public ProtobufAssertion fixed32(long number, int expectedValue) throws IOException {
        long expectedTag = number << 3 | 5;

        long tag = input.readVarint();
        int value = input.readFixedInt();

        assertThat(tag).isEqualTo(expectedTag);
        assertThat(value).isEqualTo(expectedValue);

        return this;
    }

    public ProtobufAssertion fixed64(long number, long expectedValue) throws IOException {
        long expectedTag = number << 3 | 1;

        long tag = input.readVarint();
        long value = input.readFixedLong();

        assertThat(tag).isEqualTo(expectedTag);
        assertThat(value).isEqualTo(expectedValue);

        return this;
    }

    public ProtobufAssertion sfixed32(long number, int expectedValue) throws IOException {
        long expectedTag = number << 3 | 5;

        long tag = input.readVarint();
        int value = input.readFixedInt();

        assertThat(tag).isEqualTo(expectedTag);
        assertThat(value).isEqualTo(expectedValue);

        return this;
    }

    public ProtobufAssertion sfixed64(long number, long expectedValue) throws IOException {
        long expectedTag = number << 3 | 1;

        long tag = input.readVarint();
        long value = input.readFixedLong();

        assertThat(tag).isEqualTo(expectedTag);
        assertThat(value).isEqualTo(expectedValue);

        return this;
    }

    public ProtobufAssertion bool(long number, boolean expectedValue) throws IOException {
        long expectedTag = number << 3;

        long tag = input.readVarint();
        boolean value = input.readBoolean();

        assertThat(tag).isEqualTo(expectedTag);
        assertThat(value).isEqualTo(expectedValue);

        return this;
    }

    public ProtobufAssertion string(long number, String expectedValue) throws IOException {
        long expectedTag = number << 3 | 2;

        long tag = input.readVarint();
        String value = input.readString();

        assertThat(tag).isEqualTo(expectedTag);
        assertThat(value).isEqualTo(expectedValue);

        return this;
    }

    public ProtobufAssertion bytes(long number, byte[] expectedValue) throws IOException {
        long expectedTag = number << 3 | 2;

        long tag = input.readVarint();
        byte[] value = input.readBytes();

        assertThat(tag).isEqualTo(expectedTag);
        assertThat(value).isEqualTo(expectedValue);

        return this;
    }

    public void end() {
        assertThatThrownBy(input::readBoolean).isInstanceOf(EOFException.class);
    }

    public static ProtobufAssertion assertProto(byte[] bytes) {
        return new ProtobufAssertion(new ByteArrayInputStream(bytes));
    }
}
