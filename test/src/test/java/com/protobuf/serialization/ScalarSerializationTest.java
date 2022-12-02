package com.protobuf.serialization;

import com.github.pcimcioch.protobuf.io.ProtobufWriter;
import com.github.pcimcioch.protobuf.test.FullRecord;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.protobuf.ProtobufAssertion.assertProto;
import static com.protobuf.ProtobufAssertion.deserialize;
import static com.protobuf.ProtobufAssertion.serialize;
import static com.protobuf.Utils.b;
import static com.protobuf.Utils.ba;
import static org.assertj.core.api.Assertions.assertThat;


class ScalarSerializationTest {

    @Nested
    class Serialization {

        @Test
        void fullObject() throws IOException {
            // given
            FullRecord record = new FullRecord(
                    10d, 20f,
                    30, 40L, 50, 60L, 70, 80L, 90, 100L, 110, 120L,
                    true, "test", ba(1, 20, 3)
            );

            // when then
            assertProto(record)
                    ._double(1, 10d)
                    ._float(2, 20f)
                    .int32(3, 30)
                    .int64(4, 40L)
                    .uint32(5, 50)
                    .uint64(6, 60L)
                    .sint32(7, 70)
                    .sint64(8, 80L)
                    .fixed32(9, 90)
                    .fixed64(10, 100L)
                    .sfixed32(11, 110)
                    .sfixed64(12, 120L)
                    .bool(13, true)
                    .string(14, "test")
                    .bytes(15, b(1, 20, 3))
                    .end();
        }

        @Test
        void emptyObject() throws IOException {
            // given
            FullRecord record = FullRecord.builder().build();

            // when then
            assertProto(record)
                    ._double(1, 0d)
                    ._float(2, 0f)
                    .int32(3, 0)
                    .int64(4, 0L)
                    .uint32(5, 0)
                    .uint64(6, 0L)
                    .sint32(7, 0)
                    .sint64(8, 0L)
                    .fixed32(9, 0)
                    .fixed64(10, 0L)
                    .sfixed32(11, 0)
                    .sfixed64(12, 0L)
                    .bool(13, false)
                    .string(14, "")
                    .bytes(15, b())
                    .end();
        }

        @Test
        void partialObject() throws IOException {
            // given
            FullRecord record = FullRecord.builder()
                    ._double(10d)
                    .int64(40L)
                    .sint32(70)
                    .sfixed32(110)
                    .bool(true)
                    .string("test")
                    .build();

            // when then
            assertProto(record)
                    ._double(1, 10d)
                    ._float(2, 0f)
                    .int32(3, 0)
                    .int64(4, 40L)
                    .uint32(5, 0)
                    .uint64(6, 0L)
                    .sint32(7, 70)
                    .sint64(8, 0L)
                    .fixed32(9, 0)
                    .fixed64(10, 0L)
                    .sfixed32(11, 110)
                    .sfixed64(12, 0L)
                    .bool(13, true)
                    .string(14, "test")
                    .bytes(15, b())
                    .end();
        }
    }

    @Nested
    class Deserialization {

        private final ByteArrayOutputStream data = new ByteArrayOutputStream();
        private final ProtobufWriter proto = new ProtobufWriter(data);

        @Test
        void emptyObject() throws IOException {
            // given

            // when
            FullRecord record = deserialize(data, FullRecord::parse, FullRecord::parse);

            // then
            assertThat(record).isEqualTo(FullRecord.builder()
                    ._double(0d)
                    ._float(0f)
                    .int32(0)
                    .int64(0L)
                    .uint32(0)
                    .uint64(0L)
                    .sint32(0)
                    .sint64(0L)
                    .fixed32(0)
                    .fixed64(0L)
                    .sfixed32(0)
                    .sfixed64(0L)
                    .bool(false)
                    .string("")
                    .bytes(ba())
                    .build());
        }

        @Test
        void fullObject() throws IOException {
            // given
            proto._double(1, 10d);
            proto._float(2, 20f);
            proto.int32(3, 30);
            proto.int64(4, 40L);
            proto.uint32(5, 50);
            proto.uint64(6, 60L);
            proto.sint32(7, 70);
            proto.sint64(8, 80L);
            proto.fixed32(9, 90);
            proto.fixed64(10, 100L);
            proto.sfixed32(11, 110);
            proto.sfixed64(12, 120L);
            proto.bool(13, true);
            proto.string(14, "test");
            proto.bytes(15, ba(1, 20, 3));

            // when
            FullRecord record = deserialize(data, FullRecord::parse, FullRecord::parse);

            // then
            assertThat(record).isEqualTo(FullRecord.builder()
                    ._double(10d)
                    ._float(20f)
                    .int32(30)
                    .int64(40L)
                    .uint32(50)
                    .uint64(60L)
                    .sint32(70)
                    .sint64(80L)
                    .fixed32(90)
                    .fixed64(100L)
                    .sfixed32(110)
                    .sfixed64(120L)
                    .bool(true)
                    .string("test")
                    .bytes(ba(1, 20, 3))
                    .build());
        }

        @Test
        void partialObject() throws IOException {
            // given
            proto._double(1, 10d);
            proto.int64(4, 40L);
            proto.sint32(7, 70);
            proto.sfixed32(11, 110);
            proto.bool(13, true);
            proto.string(14, "test");

            // when
            FullRecord record = deserialize(data, FullRecord::parse, FullRecord::parse);

            // then
            assertThat(record).isEqualTo(FullRecord.builder()
                    ._double(10d)
                    ._float(0f)
                    .int32(0)
                    .int64(40L)
                    .uint32(0)
                    .uint64(0L)
                    .sint32(70)
                    .sint64(0L)
                    .fixed32(0)
                    .fixed64(0L)
                    .sfixed32(110)
                    .sfixed64(0L)
                    .bool(true)
                    .string("test")
                    .bytes(ba())
                    .build());
        }

        @Test
        void fullObjectReverseOrder() throws IOException {
            // given
            proto.bytes(15, ba(1, 20, 3));
            proto.string(14, "test");
            proto.bool(13, true);
            proto.sfixed64(12, 120L);
            proto.sfixed32(11, 110);
            proto.fixed64(10, 100L);
            proto.fixed32(9, 90);
            proto.sint64(8, 80L);
            proto.sint32(7, 70);
            proto.uint64(6, 60L);
            proto.uint32(5, 50);
            proto.int64(4, 40L);
            proto.int32(3, 30);
            proto._float(2, 20f);
            proto._double(1, 10d);

            // when
            FullRecord record = deserialize(data, FullRecord::parse, FullRecord::parse);

            // then
            assertThat(record).isEqualTo(FullRecord.builder()
                    ._double(10d)
                    ._float(20f)
                    .int32(30)
                    .int64(40L)
                    .uint32(50)
                    .uint64(60L)
                    .sint32(70)
                    .sint64(80L)
                    .fixed32(90)
                    .fixed64(100L)
                    .sfixed32(110)
                    .sfixed64(120L)
                    .bool(true)
                    .string("test")
                    .bytes(ba(1, 20, 3))
                    .build());
        }

        @Test
        void unknownFields() throws IOException {
            // given
            proto._double(21, 100d);
            proto._float(22, 200f);
            proto.int32(23, 300);
            proto.int64(24, 400L);
            proto.uint32(25, 500);
            proto.uint64(26, 600L);
            proto.sint32(27, 700);
            proto.sint64(28, 800L);
            proto.fixed32(29, 900);
            proto.fixed64(30, 1000L);
            proto.sfixed32(31, 1100);
            proto.sfixed64(32, 1200L);
            proto.bool(33, false);
            proto.string(34, "foobar");
            proto.bytes(35, ba(10, 20, 30, 40, 50));

            proto._double(1, 10d);
            proto._float(2, 20f);
            proto.int32(3, 30);
            proto.int64(4, 40L);
            proto.uint32(5, 50);
            proto.uint64(6, 60L);
            proto.sint32(7, 70);
            proto.sint64(8, 80L);
            proto.fixed32(9, 90);
            proto.fixed64(10, 100L);
            proto.sfixed32(11, 110);
            proto.sfixed64(12, 120L);
            proto.bool(13, true);
            proto.string(14, "test");
            proto.bytes(15, ba(1, 20, 3));

            // when
            FullRecord record = deserialize(data, FullRecord::parse, FullRecord::parse);

            // then
            assertThat(record).isEqualTo(FullRecord.builder()
                    ._double(10d)
                    ._float(20f)
                    .int32(30)
                    .int64(40L)
                    .uint32(50)
                    .uint64(60L)
                    .sint32(70)
                    .sint64(80L)
                    .fixed32(90)
                    .fixed64(100L)
                    .sfixed32(110)
                    .sfixed64(120L)
                    .bool(true)
                    .string("test")
                    .bytes(ba(1, 20, 3))
                    .build());
        }
    }

    @Nested
    class InternalCompatibility {

        @Test
        void emptyObject() throws IOException {
            // given
            FullRecord record = FullRecord.builder().build();

            // when
            FullRecord deserialized = deserialize(serialize(record), FullRecord::parse, FullRecord::parse);

            // then
            assertThat(deserialized).isEqualTo(record);
        }

        @Test
        void fullObject() throws IOException {
            // given
            FullRecord record = new FullRecord(
                    10d, 20f,
                    30, 40L, 50, 60L, 70, 80L, 90, 100L, 110, 120L,
                    true, "test", ba(1, 20, 3)
            );

            // when
            FullRecord deserialized = deserialize(serialize(record), FullRecord::parse, FullRecord::parse);

            // then
            assertThat(deserialized).isEqualTo(record);
        }

        @Test
        void partialObject() throws IOException {
            // given
            FullRecord record = FullRecord.builder()
                    ._double(10d)
                    .int64(40L)
                    .sint32(70)
                    .sfixed32(110)
                    .bool(true)
                    .string("test")
                    .build();

            // when
            FullRecord deserialized = deserialize(serialize(record), FullRecord::parse, FullRecord::parse);

            // then
            assertThat(deserialized).isEqualTo(record);
        }
    }

    @Nested
    class ExternalCompatibility {
        // TODO implement tests of protoc compatibility
    }
}
