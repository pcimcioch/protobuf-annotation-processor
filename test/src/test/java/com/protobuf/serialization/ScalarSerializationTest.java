package com.protobuf.serialization;

import com.github.pcimcioch.protobuf.io.ProtobufWriter;
import com.protobuf.model.FullRecord;
import com.protobuf.model.FullRecordProto;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.protobuf.ProtobufAssertion.assertProto;
import static com.protobuf.ProtobufAssertion.deserialize;
import static com.protobuf.ProtobufAssertion.serialize;
import static com.protobuf.ByteUtils.b;
import static com.protobuf.ByteUtils.ba;
import static com.protobuf.ByteUtils.bs;
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
                    .int64(4, 40L)
                    .sint32(7, 70)
                    .sfixed32(11, 110)
                    .bool(13, true)
                    .string(14, "test")
                    .end();
        }
    }

    @Nested
    class Deserialization {

        private final ByteArrayOutputStream data = new ByteArrayOutputStream();
        private final ProtobufWriter proto = new ProtobufWriter(data);

        @Test
        void emptyObject() throws IOException {
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

        @Test
        void emptyObject() throws IOException {
            // given
            FullRecord our = FullRecord.builder().build();
            FullRecordProto proto = FullRecordProto.newBuilder().build();
            byte[] ourBytes = our.toByteArray();
            byte[] protoBytes = proto.toByteArray();

            // when then
            assertProtoEqual(our, FullRecordProto.parseFrom(ourBytes));
            assertProtoEqual(FullRecord.parse(protoBytes), proto);
        }

        @Test
        void partialObject() throws IOException {
            // given
            FullRecord our = FullRecord.builder()
                    ._double(10d)
                    .int64(40L)
                    .sint32(70)
                    .sfixed32(110)
                    .bool(true)
                    .string("test")
                    .build();
            FullRecordProto proto = FullRecordProto.newBuilder()
                    .setDouble(10d)
                    .setInt64(40L)
                    .setSint32(70)
                    .setSfixed32(110)
                    .setBool(true)
                    .setString("test")
                    .build();
            byte[] ourBytes = our.toByteArray();
            byte[] protoBytes = proto.toByteArray();

            // when then
            assertProtoEqual(our, FullRecordProto.parseFrom(ourBytes));
            assertProtoEqual(FullRecord.parse(protoBytes), proto);
        }

        @Test
        void fullObject() throws IOException {
            // given
            FullRecord our = FullRecord.builder()
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
                    .build();
            FullRecordProto proto = FullRecordProto.newBuilder()
                    .setDouble(10d)
                    .setFloat(20f)
                    .setInt32(30)
                    .setInt64(40L)
                    .setUint32(50)
                    .setUint64(60L)
                    .setSint32(70)
                    .setSint64(80L)
                    .setFixed32(90)
                    .setFixed64(100L)
                    .setSfixed32(110)
                    .setSfixed64(120L)
                    .setBool(true)
                    .setString("test")
                    .setBytes(bs(1, 20, 3))
                    .build();
            byte[] ourBytes = our.toByteArray();
            byte[] protoBytes = proto.toByteArray();

            // when then
            assertProtoEqual(our, FullRecordProto.parseFrom(ourBytes));
            assertProtoEqual(FullRecord.parse(protoBytes), proto);
        }

        @Test
        void utf8String() throws IOException {
            // given
            FullRecord our = FullRecord.builder()
                    .string("test ąęść \t\n\r")
                    .build();
            FullRecordProto proto = FullRecordProto.newBuilder()
                    .setString("test ąęść \t\n\r")
                    .build();
            byte[] ourBytes = our.toByteArray();
            byte[] protoBytes = proto.toByteArray();

            // when then
            assertProtoEqual(our, FullRecordProto.parseFrom(ourBytes));
            assertProtoEqual(FullRecord.parse(protoBytes), proto);
        }

        void assertProtoEqual(FullRecord our, FullRecordProto proto) {
            assertThat(our._double()).isEqualTo(proto.getDouble());
            assertThat(our._float()).isEqualTo(proto.getFloat());
            assertThat(our.int32()).isEqualTo(proto.getInt32());
            assertThat(our.int64()).isEqualTo(proto.getInt64());
            assertThat(our.uint32()).isEqualTo(proto.getUint32());
            assertThat(our.uint64()).isEqualTo(proto.getUint64());
            assertThat(our.sint32()).isEqualTo(proto.getSint32());
            assertThat(our.sint64()).isEqualTo(proto.getSint64());
            assertThat(our.fixed32()).isEqualTo(proto.getFixed32());
            assertThat(our.fixed64()).isEqualTo(proto.getFixed64());
            assertThat(our.sfixed32()).isEqualTo(proto.getSfixed32());
            assertThat(our.sfixed64()).isEqualTo(proto.getSfixed64());
            assertThat(our.bool()).isEqualTo(proto.getBool());
            assertThat(our.string()).isEqualTo(proto.getString());
            assertThat(our.bytes().toByteArray()).containsExactly(proto.getBytes().toByteArray());
        }
    }
}
