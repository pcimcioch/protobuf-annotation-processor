package com.protobuf.serialization;

import com.github.pcimcioch.protobuf.io.ProtobufWriter;
import com.protobuf.model.FullRecord;
import com.protobuf.model.FullRecordProto;
import org.assertj.core.api.ThrowingConsumer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.Random;
import java.util.stream.Stream;

import static com.protobuf.ByteUtils.b;
import static com.protobuf.ByteUtils.ba;
import static com.protobuf.ByteUtils.bs;
import static com.protobuf.ProtobufAssertion.assertProto;
import static java.util.stream.IntStream.range;
import static org.assertj.core.api.Assertions.assertThat;


class ScalarSerializationTest extends SerializationTestBase {

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
            assertProto(serialize(record))
                    .double_(1, 10d)
                    .float_(2, 20f)
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
            FullRecord record = FullRecord.empty();

            // when then
            assertProto(serialize(record))
                    .end();
        }

        @Test
        void partialObject() throws IOException {
            // given
            FullRecord record = FullRecord.builder()
                    .double_(10d)
                    .int64(40L)
                    .sint32(70)
                    .sfixed32(110)
                    .bool(true)
                    .string("test")
                    .build();

            // when then
            assertProto(serialize(record))
                    .double_(1, 10d)
                    .int64(4, 40L)
                    .sint32(7, 70)
                    .sfixed32(11, 110)
                    .bool(13, true)
                    .string(14, "test")
                    .end();
        }

        @Test
        void negativeValues() throws IOException {
            // given
            FullRecord record = new FullRecord(
                    -10d, -20f,
                    -30, -40L, 50, 60L, -70, -80L, -90, -100L, -110, -120L,
                    true, "test", ba(1, 20, 3)
            );

            // when then
            assertProto(serialize(record))
                    .double_(1, -10d)
                    .float_(2, -20f)
                    .int32(3, -30)
                    .int64(4, -40L)
                    .uint32(5, 50)
                    .uint64(6, 60L)
                    .sint32(7, -70)
                    .sint64(8, -80L)
                    .fixed32(9, -90)
                    .fixed64(10, -100L)
                    .sfixed32(11, -110)
                    .sfixed64(12, -120L)
                    .bool(13, true)
                    .string(14, "test")
                    .bytes(15, b(1, 20, 3))
                    .end();
        }
    }

    @Nested
    class Deserialization {

        @Test
        void emptyObject() throws IOException {
            // when
            FullRecord record = deserialize(new byte[0]);

            // then
            assertThat(record).isEqualTo(FullRecord.builder()
                    .double_(0d)
                    .float_(0f)
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
            // given when
            FullRecord record = deserialize(writer -> writer
                    .double_(1, 10d)
                    .float_(2, 20f)
                    .int32(3, 30)
                    .int64(4, 40L)
                    .uint32(5, 50)
                    .uint64(6, 60L)
                    .sint32(7, 70)
                    .sint64(8, 80L)
                    .fixed32(9, 90)
                    .fixed64(10, 100L)
                    .fixed32(9, 90)
                    .fixed64(10, 100L)
                    .sfixed32(11, 110)
                    .sfixed64(12, 120L)
                    .bool(13, true)
                    .string(14, "test")
                    .bytes(15, ba(1, 20, 3))
            );

            // then
            assertThat(record).isEqualTo(FullRecord.builder()
                    .double_(10d)
                    .float_(20f)
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
            // given when
            FullRecord record = deserialize(writer -> writer
                    .double_(1, 10d)
                    .int64(4, 40L)
                    .sint32(7, 70)
                    .sfixed32(11, 110)
                    .bool(13, true)
                    .string(14, "test")
            );

            // then
            assertThat(record).isEqualTo(FullRecord.builder()
                    .double_(10d)
                    .float_(0f)
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
            // given when
            FullRecord record = deserialize(writer -> writer
                    .bytes(15, ba(1, 20, 3))
                    .string(14, "test")
                    .bool(13, true)
                    .sfixed64(12, 120L)
                    .sfixed32(11, 110)
                    .fixed64(10, 100L)
                    .fixed32(9, 90)
                    .sint64(8, 80L)
                    .sint32(7, 70)
                    .uint64(6, 60L)
                    .uint32(5, 50)
                    .int64(4, 40L)
                    .int32(3, 30)
                    .float_(2, 20f)
                    .double_(1, 10d)
            );

            // then
            assertThat(record).isEqualTo(FullRecord.builder()
                    .double_(10d)
                    .float_(20f)
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
            // given when
            FullRecord record = deserialize(writer -> writer
                    // unknown
                    .double_(21, 100d)
                    .float_(22, 200f)
                    .int32(23, 300)
                    .int64(24, 400L)
                    .uint32(25, 500)
                    .uint64(26, 600L)
                    .sint32(27, 700)
                    .sint64(28, 800L)
                    .fixed32(29, 900)
                    .fixed64(30, 1000L)
                    .sfixed32(31, 1100)
                    .sfixed64(32, 1200L)
                    .bool(33, false)
                    .string(34, "foobar")
                    .bytes(35, ba(10, 20, 30, 40, 50))
                    // record
                    .double_(1, 10d)
                    .float_(2, 20f)
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
                    .bytes(15, ba(1, 20, 3))
                    // same number different type
                    .float_(1, 11f)
                    .double_(2, 21d)
                    .string(3, "30")
                    .string(4, "40L")
                    .string(5, "50")
                    .string(6, "60L")
                    .string(7, "70")
                    .string(8, "80L")
                    .string(9, "90")
                    .string(10, "100L")
                    .string(11, "110")
                    .string(12, "120L")
                    .string(13, "true")
                    .int32(14, 51)
                    .int32(15, 123)
            );

            // then
            assertThat(record).isEqualTo(FullRecord.builder()
                    .double_(10d)
                    .float_(20f)
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
        void negativeValues() throws IOException {
            // given when
            FullRecord record = deserialize(writer -> writer
                    .double_(1, -10d)
                    .float_(2, -20f)
                    .int32(3, -30)
                    .int64(4, -40L)
                    .uint32(5, 50)
                    .uint64(6, 60L)
                    .sint32(7, -70)
                    .sint64(8, -80L)
                    .fixed32(9, -90)
                    .fixed64(10, -100L)
                    .fixed32(9, -90)
                    .fixed64(10, -100L)
                    .sfixed32(11, -110)
                    .sfixed64(12, -120L)
                    .bool(13, true)
                    .string(14, "test")
                    .bytes(15, ba(1, 20, 3))
            );

            // then
            assertThat(record).isEqualTo(FullRecord.builder()
                    .double_(-10d)
                    .float_(-20f)
                    .int32(-30)
                    .int64(-40L)
                    .uint32(50)
                    .uint64(60L)
                    .sint32(-70)
                    .sint64(-80L)
                    .fixed32(-90)
                    .fixed64(-100L)
                    .sfixed32(-110)
                    .sfixed64(-120L)
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
            FullRecord record = FullRecord.empty();

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
                    .double_(10d)
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

        @Test
        void negativeValues() throws IOException {
            // given
            FullRecord record = new FullRecord(
                    -10d, -20f,
                    -30, -40L, 50, 60L, -70, -80L, -90, -100L, -110, -120L,
                    true, "test", ba(1, 20, 3)
            );

            // when
            FullRecord deserialized = deserialize(serialize(record));

            // then
            assertThat(deserialized).isEqualTo(record);
        }
    }

    @Nested
    class ExternalCompatibility {

        @Test
        void emptyObject() throws IOException {
            // given
            FullRecord our = FullRecord.empty();
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
                    .double_(10d)
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
                    .double_(10d)
                    .float_(20f)
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

        @Test
        void negativeValues() throws IOException {
            // given
            FullRecord our = FullRecord.builder()
                    .double_(-10d)
                    .float_(-20f)
                    .int32(-30)
                    .int64(-40L)
                    .uint32(50)
                    .uint64(60L)
                    .sint32(-70)
                    .sint64(-80L)
                    .fixed32(-90)
                    .fixed64(-100L)
                    .sfixed32(-110)
                    .sfixed64(-120L)
                    .bool(true)
                    .string("test")
                    .bytes(ba(1, 20, 3))
                    .build();
            FullRecordProto proto = FullRecordProto.newBuilder()
                    .setDouble(-10d)
                    .setFloat(-20f)
                    .setInt32(-30)
                    .setInt64(-40L)
                    .setUint32(50)
                    .setUint64(60L)
                    .setSint32(-70)
                    .setSint64(-80L)
                    .setFixed32(-90)
                    .setFixed64(-100L)
                    .setSfixed32(-110)
                    .setSfixed64(-120L)
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

        @ParameterizedTest
        @MethodSource("randomArguments")
        void randomValues(double double_, float float_, int int32, long int64, int uint32, long uint64,
                          int sint32, long sint64, int fixed32, long fixed64, int sfixed32, long sfixed64) throws IOException {
            // given
            FullRecord our = FullRecord.builder()
                    .double_(double_)
                    .float_(float_)
                    .int32(int32)
                    .int64(int64)
                    .uint32(uint32)
                    .uint64(uint64)
                    .sint32(sint32)
                    .sint64(sint64)
                    .fixed32(fixed32)
                    .fixed64(fixed64)
                    .sfixed32(sfixed32)
                    .sfixed64(sfixed64)
                    .build();
            FullRecordProto proto = FullRecordProto.newBuilder()
                    .setDouble(double_)
                    .setFloat(float_)
                    .setInt32(int32)
                    .setInt64(int64)
                    .setUint32(uint32)
                    .setUint64(uint64)
                    .setSint32(sint32)
                    .setSint64(sint64)
                    .setFixed32(fixed32)
                    .setFixed64(fixed64)
                    .setSfixed32(sfixed32)
                    .setSfixed64(sfixed64)
                    .build();
            byte[] ourBytes = our.toByteArray();
            byte[] protoBytes = proto.toByteArray();

            // when then
            assertProtoEqual(our, FullRecordProto.parseFrom(ourBytes));
            assertProtoEqual(FullRecord.parse(protoBytes), proto);
        }

        static Stream<Arguments> randomArguments() {
            Random random = new Random(56270356588186L); // random, but deterministic
            return range(0, 1000).mapToObj(i -> Arguments.of(
                    random.nextDouble(),                    // double
                    random.nextFloat(),                     // float
                    random.nextInt(),                       // int32
                    random.nextLong(),                      // int64
                    random.nextInt(Integer.MAX_VALUE),      // uint32
                    random.nextLong(Long.MAX_VALUE),        // uint64
                    random.nextInt(),                       // sint32
                    random.nextLong(),                      // sint64
                    random.nextInt(),                       // fixed32
                    random.nextLong(),                      // fixed64
                    random.nextInt(),                       // sfixed32
                    random.nextLong()                       // sfixed64
            ));
        }

        private void assertProtoEqual(FullRecord our, FullRecordProto proto) {
            assertThat(our.double_()).isEqualTo(proto.getDouble());
            assertThat(our.float_()).isEqualTo(proto.getFloat());
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

    private FullRecord deserialize(ThrowingConsumer<ProtobufWriter> writerAction) throws IOException {
        return deserialize(FullRecord::parse, FullRecord::parse, writerAction);
    }

    private FullRecord deserialize(byte[] data) throws IOException {
        return deserialize(FullRecord::parse, FullRecord::parse, data);
    }
}
