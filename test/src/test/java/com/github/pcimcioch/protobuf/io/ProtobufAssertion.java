package com.github.pcimcioch.protobuf.io;

import com.github.pcimcioch.protobuf.dto.BooleanList;
import com.github.pcimcioch.protobuf.dto.DoubleList;
import com.github.pcimcioch.protobuf.dto.FloatList;
import com.github.pcimcioch.protobuf.dto.IntList;
import com.github.pcimcioch.protobuf.dto.LongList;
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
            assertThat(input.readVarint32()).isEqualTo(expectedTag);
            assertThat(input.readDouble()).isEqualTo(expectedValue);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion doublePacked(int number, double... expectedValues) {
        int expectedTag = number << 3 | 2;

        try {
            assertThat(input.readVarint32()).isEqualTo(expectedTag);
            assertThat(input.readVarint32()).isEqualTo(Size.ofDoublePacked(DoubleList.of(expectedValues)));

            for (double expectedValue : expectedValues) {
                assertThat(input.readDouble()).isEqualTo(expectedValue);
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion float_(int number, float expectedValue) {
        int expectedTag = number << 3 | 5;

        try {
            assertThat(input.readVarint32()).isEqualTo(expectedTag);
            assertThat(input.readFloat()).isEqualTo(expectedValue);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion floatPacked(int number, float... expectedValues) {
        int expectedTag = number << 3 | 2;

        try {
            assertThat(input.readVarint32()).isEqualTo(expectedTag);
            assertThat(input.readVarint32()).isEqualTo(Size.ofFloatPacked(FloatList.of(expectedValues)));

            for (float expectedValue : expectedValues) {
                assertThat(input.readFloat()).isEqualTo(expectedValue);
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion int32(int number, int expectedValue) {
        int expectedTag = number << 3;

        try {
            assertThat(input.readVarint32()).isEqualTo(expectedTag);
            assertThat(input.readVarint32()).isEqualTo(expectedValue);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion int32Packed(int number, int... expectedValues) {
        int expectedTag = number << 3 | 2;

        try {
            assertThat(input.readVarint32()).isEqualTo(expectedTag);
            assertThat(input.readVarint32()).isEqualTo(Size.ofInt32Packed(IntList.of(expectedValues)));

            for (int expectedValue : expectedValues) {
                assertThat(input.readVarint32()).isEqualTo(expectedValue);
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion int64(int number, long expectedValue) {
        int expectedTag = number << 3;

        try {
            assertThat(input.readVarint32()).isEqualTo(expectedTag);
            assertThat(input.readVarint64()).isEqualTo(expectedValue);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion int64Packed(int number, long... expectedValues) {
        int expectedTag = number << 3 | 2;

        try {
            assertThat(input.readVarint32()).isEqualTo(expectedTag);
            assertThat(input.readVarint32()).isEqualTo(Size.ofInt64Packed(LongList.of(expectedValues)));

            for (long expectedValue : expectedValues) {
                assertThat(input.readVarint64()).isEqualTo(expectedValue);
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion uint32(int number, int expectedValue) {
        int expectedTag = number << 3;

        try {
            assertThat(input.readVarint32()).isEqualTo(expectedTag);
            assertThat(input.readVarint32()).isEqualTo(expectedValue);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion uint32Packed(int number, int... expectedValues) {
        int expectedTag = number << 3 | 2;

        try {
            assertThat(input.readVarint32()).isEqualTo(expectedTag);
            assertThat(input.readVarint32()).isEqualTo(Size.ofUint32Packed(IntList.of(expectedValues)));

            for (int expectedValue : expectedValues) {
                assertThat(input.readVarint32()).isEqualTo(expectedValue);
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion uint64(int number, long expectedValue) {
        int expectedTag = number << 3;

        try {
            assertThat(input.readVarint32()).isEqualTo(expectedTag);
            assertThat(input.readVarint64()).isEqualTo(expectedValue);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion uint64Packed(int number, long... expectedValues) {
        int expectedTag = number << 3 | 2;

        try {
            assertThat(input.readVarint32()).isEqualTo(expectedTag);
            assertThat(input.readVarint32()).isEqualTo(Size.ofUint64Packed(LongList.of(expectedValues)));

            for (long expectedValue : expectedValues) {
                assertThat(input.readVarint64()).isEqualTo(expectedValue);
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion sint32(int number, int expectedValue) {
        int expectedTag = number << 3;

        try {
            assertThat(input.readVarint32()).isEqualTo(expectedTag);
            assertThat(input.readZigZag32()).isEqualTo(expectedValue);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion sint32Packed(int number, int... expectedValues) {
        int expectedTag = number << 3 | 2;

        try {
            assertThat(input.readVarint32()).isEqualTo(expectedTag);
            assertThat(input.readVarint32()).isEqualTo(Size.ofSint32Packed(IntList.of(expectedValues)));

            for (int expectedValue : expectedValues) {
                assertThat(input.readZigZag32()).isEqualTo(expectedValue);
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion sint64(int number, long expectedValue) {
        int expectedTag = number << 3;

        try {
            assertThat(input.readVarint32()).isEqualTo(expectedTag);
            assertThat(input.readZigZag64()).isEqualTo(expectedValue);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion sint64Packed(int number, long... expectedValues) {
        int expectedTag = number << 3 | 2;

        try {
            assertThat(input.readVarint32()).isEqualTo(expectedTag);
            assertThat(input.readVarint32()).isEqualTo(Size.ofSint64Packed(LongList.of(expectedValues)));

            for (long expectedValue : expectedValues) {
                assertThat(input.readZigZag64()).isEqualTo(expectedValue);
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion fixed32(int number, int expectedValue) {
        int expectedTag = number << 3 | 5;

        try {
            assertThat(input.readVarint32()).isEqualTo(expectedTag);
            assertThat(input.readFixedInt()).isEqualTo(expectedValue);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion fixed32Packed(int number, int... expectedValues) {
        int expectedTag = number << 3 | 2;

        try {
            assertThat(input.readVarint32()).isEqualTo(expectedTag);
            assertThat(input.readVarint32()).isEqualTo(Size.ofFixed32Packed(IntList.of(expectedValues)));

            for (int expectedValue : expectedValues) {
                assertThat(input.readFixedInt()).isEqualTo(expectedValue);
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion fixed64(int number, long expectedValue) {
        int expectedTag = number << 3 | 1;

        try {
            assertThat(input.readVarint32()).isEqualTo(expectedTag);
            assertThat(input.readFixedLong()).isEqualTo(expectedValue);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion fixed64Packed(int number, long... expectedValues) {
        int expectedTag = number << 3 | 2;

        try {
            assertThat(input.readVarint32()).isEqualTo(expectedTag);
            assertThat(input.readVarint32()).isEqualTo(Size.ofFixed64Packed(LongList.of(expectedValues)));

            for (long expectedValue : expectedValues) {
                assertThat(input.readFixedLong()).isEqualTo(expectedValue);
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion sfixed32(int number, int expectedValue) {
        int expectedTag = number << 3 | 5;

        try {
            assertThat(input.readVarint32()).isEqualTo(expectedTag);
            assertThat(input.readFixedInt()).isEqualTo(expectedValue);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion sfixed32Packed(int number, int... expectedValues) {
        int expectedTag = number << 3 | 2;

        try {
            assertThat(input.readVarint32()).isEqualTo(expectedTag);
            assertThat(input.readVarint32()).isEqualTo(Size.ofSfixed32Packed(IntList.of(expectedValues)));

            for (int expectedValue : expectedValues) {
                assertThat(input.readFixedInt()).isEqualTo(expectedValue);
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion sfixed64(int number, long expectedValue) {
        int expectedTag = number << 3 | 1;

        try {
            assertThat(input.readVarint32()).isEqualTo(expectedTag);
            assertThat(input.readFixedLong()).isEqualTo(expectedValue);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion sfixed64Packed(int number, long... expectedValues) {
        int expectedTag = number << 3 | 2;

        try {
            assertThat(input.readVarint32()).isEqualTo(expectedTag);
            assertThat(input.readVarint32()).isEqualTo(Size.ofSfixed64Packed(LongList.of(expectedValues)));

            for (long expectedValue : expectedValues) {
                assertThat(input.readFixedLong()).isEqualTo(expectedValue);
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion bool(int number, boolean expectedValue) {
        int expectedTag = number << 3;

        try {
            assertThat(input.readVarint32()).isEqualTo(expectedTag);
            assertThat(input.readBoolean()).isEqualTo(expectedValue);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion boolPacked(int number, boolean... expectedValues) {
        int expectedTag = number << 3 | 2;

        try {
            assertThat(input.readVarint32()).isEqualTo(expectedTag);
            assertThat(input.readVarint32()).isEqualTo(Size.ofBoolPacked(BooleanList.of(expectedValues)));

            for (boolean expectedValue : expectedValues) {
                assertThat(input.readBoolean()).isEqualTo(expectedValue);
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion string(int number, String expectedValue) {
        int expectedTag = number << 3 | 2;

        try {
            assertThat(input.readVarint32()).isEqualTo(expectedTag);
            assertThat(input.readString()).isEqualTo(expectedValue);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion bytes(int number, byte[] expectedValue) {
        int expectedTag = number << 3 | 2;

        try {
            assertThat(input.readVarint32()).isEqualTo(expectedTag);
            assertThat(input.readBytes()).isEqualTo(expectedValue);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        return this;
    }

    public ProtobufAssertion message(int number, Consumer<ProtobufAssertion> messageAssertions) {
        int expectedTag = number << 3 | 2;

        try {
            assertThat(input.readVarint32()).isEqualTo(expectedTag);
            messageAssertions.accept(assertProto(input.readBytes()));
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
