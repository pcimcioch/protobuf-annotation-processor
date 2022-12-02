package com.protobuf.serialization;

import com.github.pcimcioch.protobuf.io.ProtobufWriter;
import com.github.pcimcioch.protobuf.test.FullRecord;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.protobuf.ProtobufAssertion.assertProto;
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

            // when
            byte[] bytes = serialize(record);

            // then
            assertProto(bytes)
                    ._double(0, 10d)
                    ._float(1, 20f)
                    .int32(2, 30)
                    .int64(3, 40L)
                    .uint32(4, 50)
                    .uint64(5, 60L)
                    .sint32(6, 70)
                    .sint64(7, 80L)
                    .fixed32(8, 90)
                    .fixed64(9, 100L)
                    .sfixed32(10, 110)
                    .sfixed64(11, 120L)
                    .bool(12, true)
                    .string(13, "test")
                    .bytes(14, b(1, 20, 3))
                    .end();
        }

        @Test
        void emptyObject() throws IOException {
            // given
            FullRecord record = FullRecord.builder().build();

            // when
            byte[] bytes = serialize(record);

            // then
            assertProto(bytes)
                    ._double(0, 0d)
                    ._float(1, 0f)
                    .int32(2, 0)
                    .int64(3, 0L)
                    .uint32(4, 0)
                    .uint64(5, 0L)
                    .sint32(6, 0)
                    .sint64(7, 0L)
                    .fixed32(8, 0)
                    .fixed64(9, 0L)
                    .sfixed32(10, 0)
                    .sfixed64(11, 0L)
                    .bool(12, false)
                    .string(13, "")
                    .bytes(14, b())
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

            // when
            byte[] bytes = serialize(record);

            // then
            assertProto(bytes)
                    ._double(0, 10d)
                    ._float(1, 0f)
                    .int32(2, 0)
                    .int64(3, 40L)
                    .uint32(4, 0)
                    .uint64(5, 0L)
                    .sint32(6, 70)
                    .sint64(7, 0L)
                    .fixed32(8, 0)
                    .fixed64(9, 0L)
                    .sfixed32(10, 110)
                    .sfixed64(11, 0L)
                    .bool(12, true)
                    .string(13, "test")
                    .bytes(14, b())
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
            FullRecord record = deserialize(data);

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
            proto._double(0, 10d);
            proto._float(1, 20f);
            proto.int32(2, 30);
            proto.int64(3, 40L);
            proto.uint32(4, 50);
            proto.uint64(5, 60L);
            proto.sint32(6, 70);
            proto.sint64(7, 80L);
            proto.fixed32(8, 90);
            proto.fixed64(9, 100L);
            proto.sfixed32(10, 110);
            proto.sfixed64(11, 120L);
            proto.bool(12, true);
            proto.string(13, "test");
            proto.bytes(14, ba(1, 20, 3));

            // when
            FullRecord record = deserialize(data);

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
            proto._double(0, 10d);
            proto.int64(3, 40L);
            proto.sint32(6, 70);
            proto.sfixed32(10, 110);
            proto.bool(12, true);
            proto.string(13, "test");

            // when
            FullRecord record = deserialize(data);

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
            proto.bytes(14, ba(1, 20, 3));
            proto.string(13, "test");
            proto.bool(12, true);
            proto.sfixed64(11, 120L);
            proto.sfixed32(10, 110);
            proto.fixed64(9, 100L);
            proto.fixed32(8, 90);
            proto.sint64(7, 80L);
            proto.sint32(6, 70);
            proto.uint64(5, 60L);
            proto.uint32(4, 50);
            proto.int64(3, 40L);
            proto.int32(2, 30);
            proto._float(1, 20f);
            proto._double(0, 10d);

            // when
            FullRecord record = deserialize(data);

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
            proto.fixed32(20, 1234);
            proto.fixed64(21, 1234L);
            proto.int32(22, 100);
            proto.bytes(23, ba(10, 20, 30, 40, 50));

            proto._double(0, 10d);
            proto._float(1, 20f);
            proto.int32(2, 30);
            proto.int64(3, 40L);
            proto.uint32(4, 50);
            proto.uint64(5, 60L);
            proto.sint32(6, 70);
            proto.sint64(7, 80L);
            proto.fixed32(8, 90);
            proto.fixed64(9, 100L);
            proto.sfixed32(10, 110);
            proto.sfixed64(11, 120L);
            proto.bool(12, true);
            proto.string(13, "test");
            proto.bytes(14, ba(1, 20, 3));

            // when
            FullRecord record = deserialize(data);

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
            FullRecord deserialized = deserialize(serialize(record));

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
            FullRecord deserialized = deserialize(serialize(record));

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
            FullRecord deserialized = deserialize(serialize(record));

            // then
            assertThat(deserialized).isEqualTo(record);
        }
    }

    @Nested
    class ExternalCompatibility {
        // TODO implement tests of protoc compatibility
    }

    // TODO maybe each data record should implement some interface and this logic could be in ProtobufAssertions
    private static byte[] serialize(FullRecord record) throws IOException {
        byte[] rawData = record.toByteArray();
        byte[] streamData;

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            record.writeTo(out);
            streamData = out.toByteArray();
        }

        assertThat(rawData).isEqualTo(streamData);

        return rawData;
    }

    private static FullRecord deserialize(ByteArrayOutputStream output) throws IOException {
        return deserialize(output.toByteArray());
    }

    private static FullRecord deserialize(byte[] data) throws IOException {
        FullRecord recordRaw = FullRecord.parse(data);
        FullRecord recordStream;

        try (ByteArrayInputStream in = new ByteArrayInputStream(data)) {
            recordStream = FullRecord.parse(in);
        }

        assertThat(recordRaw).isEqualTo(recordStream);

        return recordRaw;
    }
}
