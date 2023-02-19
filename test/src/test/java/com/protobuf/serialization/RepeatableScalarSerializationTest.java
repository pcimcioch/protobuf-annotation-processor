package com.protobuf.serialization;

import com.github.pcimcioch.protobuf.dto.ByteArray;
import com.github.pcimcioch.protobuf.io.ProtobufWriter;
import com.google.protobuf.ByteString;
import com.protobuf.model.RepeatableScalar;
import com.protobuf.model.RepeatableScalarProto;
import org.assertj.core.api.ThrowingConsumer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.protobuf.ByteUtils.b;
import static com.protobuf.ByteUtils.ba;
import static com.protobuf.ByteUtils.bs;
import static com.protobuf.ProtobufAssertion.assertProto;
import static org.assertj.core.api.Assertions.assertThat;

class RepeatableScalarSerializationTest extends SerializationTestBase {

    @Nested
    class Serialization {

        @Test
        void fullObject() throws IOException {
            // given
            RepeatableScalar record = new RepeatableScalar(
                    List.of(10d, 11d),
                    List.of(20f, 21f),
                    List.of(30, 31),
                    List.of(40L, 41L),
                    List.of(50, 51),
                    List.of(60L, 61L),
                    List.of(70, 71),
                    List.of(80L, 81L),
                    List.of(90, 91),
                    List.of(100L, 101L),
                    List.of(110, 111),
                    List.of(120L, 121L),
                    List.of(true, false),
                    List.of("test1", "test2"),
                    List.of(ba(1, 2, 3), ba(20, 30, 40))
            );

            // when then
            assertProto(serialize(record))
                    .double_(1, 10d)
                    .double_(1, 11d)
                    .float_(2, 20f)
                    .float_(2, 21f)
                    .int32(3, 30)
                    .int32(3, 31)
                    .int64(4, 40L)
                    .int64(4, 41L)
                    .uint32(5, 50)
                    .uint32(5, 51)
                    .uint64(6, 60L)
                    .uint64(6, 61L)
                    .sint32(7, 70)
                    .sint32(7, 71)
                    .sint64(8, 80L)
                    .sint64(8, 81L)
                    .fixed32(9, 90)
                    .fixed32(9, 91)
                    .fixed64(10, 100L)
                    .fixed64(10, 101L)
                    .sfixed32(11, 110)
                    .sfixed32(11, 111)
                    .sfixed64(12, 120L)
                    .sfixed64(12, 121L)
                    .bool(13, true)
                    .bool(13, false)
                    .string(14, "test1")
                    .string(14, "test2")
                    .bytes(15, b(1, 2, 3))
                    .bytes(15, b(20, 30, 40))
                    .end();
        }

        @Test
        void emptyObject() throws IOException {
            // given
            RepeatableScalar record = RepeatableScalar.empty();

            // when then
            assertProto(serialize(record))
                    .end();
        }

        @Test
        void partialObject() throws IOException {
            // given
            RepeatableScalar record = RepeatableScalar.builder()
                    .addDoubles(10d)
                    .int64s(List.of(40L, 41L, 42L))
                    .addAllSint32s(List.of(70, 71))
                    .sfixed32s(List.of(110))
                    .bools(List.of(false, true, true))
                    .strings(List.of("test", "test"))
                    .build();

            // when then
            assertProto(serialize(record))
                    .double_(1, 10d)
                    .int64(4, 40L)
                    .int64(4, 41L)
                    .int64(4, 42L)
                    .sint32(7, 70)
                    .sint32(7, 71)
                    .sfixed32(11, 110)
                    .bool(13, false)
                    .bool(13, true)
                    .bool(13, true)
                    .string(14, "test")
                    .string(14, "test")
                    .end();
        }

        @Test
        void defaultValues() throws IOException {
            // given
            RepeatableScalar record = new RepeatableScalar(
                    List.of(0d),
                    List.of(0f),
                    List.of(0),
                    List.of(0L),
                    List.of(0),
                    List.of(0L),
                    List.of(0),
                    List.of(0L),
                    List.of(0),
                    List.of(0L),
                    List.of(0),
                    List.of(0L),
                    List.of(false),
                    List.of(""),
                    List.of(ba())
            );

            // when then
            assertProto(serialize(record))
                    .double_(1, 0d)
                    .float_(2, 0f)
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
        void negativeValues() throws IOException {
            // given
            RepeatableScalar record = new RepeatableScalar(
                    List.of(-10d, -11d),
                    List.of(-20f, -21f),
                    List.of(-30, -31),
                    List.of(-40L, -41L),
                    List.of(50, 51),
                    List.of(60L, 61L),
                    List.of(-70, -71),
                    List.of(-80L, -81L),
                    List.of(-90, -91),
                    List.of(-100L, -101L),
                    List.of(-110, -111),
                    List.of(-120L, -121L),
                    List.of(true, false),
                    List.of("test1", "test2"),
                    List.of(ba(1, 2, 3), ba(20, 30, 40))
            );

            // when then
            assertProto(serialize(record))
                    .double_(1, -10d)
                    .double_(1, -11d)
                    .float_(2, -20f)
                    .float_(2, -21f)
                    .int32(3, -30)
                    .int32(3, -31)
                    .int64(4, -40L)
                    .int64(4, -41L)
                    .uint32(5, 50)
                    .uint32(5, 51)
                    .uint64(6, 60L)
                    .uint64(6, 61L)
                    .sint32(7, -70)
                    .sint32(7, -71)
                    .sint64(8, -80L)
                    .sint64(8, -81L)
                    .fixed32(9, -90)
                    .fixed32(9, -91)
                    .fixed64(10, -100L)
                    .fixed64(10, -101L)
                    .sfixed32(11, -110)
                    .sfixed32(11, -111)
                    .sfixed64(12, -120L)
                    .sfixed64(12, -121L)
                    .bool(13, true)
                    .bool(13, false)
                    .string(14, "test1")
                    .string(14, "test2")
                    .bytes(15, b(1, 2, 3))
                    .bytes(15, b(20, 30, 40))
                    .end();
        }
    }

    @Nested
    class Deserialization {

        @Test
        void emptyObject() throws IOException {
            // when
            RepeatableScalar record = deserialize(new byte[0]);

            // then
            assertThat(record).isEqualTo(RepeatableScalar.empty());
        }

        @Test
        void fullObject() throws IOException {
            // given when
            RepeatableScalar record = deserialize(writer -> writer
                    .double_(1, 10d)
                    .double_(1, 11d)
                    .float_(2, 20f)
                    .float_(2, 21f)
                    .int32(3, 30)
                    .int32(3, 31)
                    .int64(4, 40L)
                    .int64(4, 41L)
                    .uint32(5, 50)
                    .uint32(5, 51)
                    .uint64(6, 60L)
                    .uint64(6, 61L)
                    .sint32(7, 70)
                    .sint32(7, 71)
                    .sint64(8, 80L)
                    .sint64(8, 81L)
                    .fixed32(9, 90)
                    .fixed32(9, 91)
                    .fixed64(10, 100L)
                    .fixed64(10, 101L)
                    .sfixed32(11, 110)
                    .sfixed32(11, 111)
                    .sfixed64(12, 120L)
                    .sfixed64(12, 121L)
                    .bool(13, List.of(true, false))
                    .string(14, "test1")
                    .string(14, "test2")
                    .bytes(15, ba(1, 2, 3))
                    .bytes(15, ba(20, 30, 40))
            );

            // then
            assertThat(record).isEqualTo(RepeatableScalar.builder()
                    .doubles(List.of(10d, 11d))
                    .floats(List.of(20f, 21f))
                    .int32s(List.of(30, 31))
                    .int64s(List.of(40L, 41L))
                    .uint32s(List.of(50, 51))
                    .uint64s(List.of(60L, 61L))
                    .sint32s(List.of(70, 71))
                    .sint64s(List.of(80L, 81L))
                    .fixed32s(List.of(90, 91))
                    .fixed64s(List.of(100L, 101L))
                    .sfixed32s(List.of(110, 111))
                    .sfixed64s(List.of(120L, 121L))
                    .bools(List.of(true, false))
                    .strings(List.of("test1", "test2"))
                    .bytes(List.of(ba(1, 2, 3), ba(20, 30, 40)))
                    .build());
        }

        @Test
        void partialObject() throws IOException {
            // given when
            RepeatableScalar record = deserialize(writer -> writer
                    .double_(1, 10d)
                    .int64(4, 40L)
                    .int64(4, 41L)
                    .int64(4, 42L)
                    .sint32(7, 70)
                    .sint32(7, 71)
                    .sfixed32(11, 110)
                    .bool(13, List.of(false, true, true))
                    .string(14, "test")
                    .string(14, "test")
            );

            // then
            assertThat(record).isEqualTo(RepeatableScalar.builder()
                    .doubles(List.of(10d))
                    .int64s(List.of(40L, 41L, 42L))
                    .sint32s(List.of(70, 71))
                    .sfixed32s(List.of(110))
                    .bools(List.of(false, true, true))
                    .strings(List.of("test", "test"))
                    .build());
        }

        @Test
        void fullObjectReverseOrder() throws IOException {
            // given when
            RepeatableScalar record = deserialize(writer -> writer
                    .double_(1, 10d)
                    .sint32(7, 70)
                    .int32(3, 30)
                    .float_(2, 20f)
                    .sfixed32(11, 110)
                    .int32(3, 31)
                    .fixed64(10, 100L)
                    .int64(4, 40L)
                    .uint32(5, 50)
                    .sint32(7, 71)
                    .fixed32(9, 90)
                    .string(14, "test1")
                    .uint32(5, 51)
                    .uint64(6, 60L)
                    .double_(1, 11d)
                    .float_(2, 21f)
                    .uint64(6, 61L)
                    .int64(4, 41L)
                    .bool(13, List.of(true))
                    .sint64(8, 80L)
                    .bytes(15, ba(1, 2, 3))
                    .sint64(8, 81L)
                    .fixed32(9, 91)
                    .sfixed64(12, 120L)
                    .fixed64(10, 101L)
                    .sfixed32(11, 111)
                    .sfixed64(12, 121L)
                    .bool(13, List.of(false))
                    .string(14, "test2")
                    .bytes(15, ba(20, 30, 40))
            );

            // then
            assertThat(record).isEqualTo(RepeatableScalar.builder()
                    .doubles(List.of(10d, 11d))
                    .floats(List.of(20f, 21f))
                    .int32s(List.of(30, 31))
                    .int64s(List.of(40L, 41L))
                    .uint32s(List.of(50, 51))
                    .uint64s(List.of(60L, 61L))
                    .sint32s(List.of(70, 71))
                    .sint64s(List.of(80L, 81L))
                    .fixed32s(List.of(90, 91))
                    .fixed64s(List.of(100L, 101L))
                    .sfixed32s(List.of(110, 111))
                    .sfixed64s(List.of(120L, 121L))
                    .bools(List.of(true, false))
                    .strings(List.of("test1", "test2"))
                    .bytes(List.of(ba(1, 2, 3), ba(20, 30, 40)))
                    .build());
        }

        @Test
        void unknownFields() throws IOException {
            // given when
            RepeatableScalar record = deserialize(writer -> writer
                    // unknown
                    .double_(21, 110d)
                    .double_(21, 111d)
                    .float_(22, 120f)
                    .float_(22, 121f)
                    .int32(23, 130)
                    .int32(23, 131)
                    .int64(24, 140L)
                    .int64(24, 141L)
                    .uint32(25, 150)
                    .uint32(25, 151)
                    .uint64(26, 160L)
                    .uint64(26, 161L)
                    .sint32(27, 170)
                    .sint32(27, 171)
                    .sint64(28, 180L)
                    .sint64(28, 181L)
                    .fixed32(29, 190)
                    .fixed32(29, 191)
                    .fixed64(30, 1100L)
                    .fixed64(30, 1101L)
                    .sfixed32(31, 1110)
                    .sfixed32(31, 1111)
                    .sfixed64(32, 1120L)
                    .sfixed64(32, 1121L)
                    .bool(33, List.of(true, false))
                    .string(34, "test10")
                    .string(34, "test20")
                    .bytes(35, ba(5, 6, 7))
                    .bytes(35, ba(25, 35, 45))
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
                    .string(14, "test1")
                    .bytes(15, ba(1, 2, 3))
            );

            // then
            assertThat(record).isEqualTo(RepeatableScalar.builder()
                    .doubles(List.of(10d))
                    .floats(List.of(20f))
                    .int32s(List.of(30))
                    .int64s(List.of(40L))
                    .uint32s(List.of(50))
                    .uint64s(List.of(60L))
                    .sint32s(List.of(70))
                    .sint64s(List.of(80L))
                    .fixed32s(List.of(90))
                    .fixed64s(List.of(100L))
                    .sfixed32s(List.of(110))
                    .sfixed64s(List.of(120L))
                    .bools(List.of(true))
                    .strings(List.of("test1"))
                    .bytes(List.of(ba(1, 2, 3)))
                    .build());
        }

        @Test
        void negativeValues() throws IOException {
            // given when
            RepeatableScalar record = deserialize(writer -> writer
                    .double_(1, -10d)
                    .double_(1, -11d)
                    .float_(2, -20f)
                    .float_(2, -21f)
                    .int32(3, -30)
                    .int32(3, -31)
                    .int64(4, -40L)
                    .int64(4, -41L)
                    .uint32(5, 50)
                    .uint32(5, 51)
                    .uint64(6, 60L)
                    .uint64(6, 61L)
                    .sint32(7, -70)
                    .sint32(7, -71)
                    .sint64(8, -80L)
                    .sint64(8, -81L)
                    .fixed32(9, -90)
                    .fixed32(9, -91)
                    .fixed64(10, -100L)
                    .fixed64(10, -101L)
                    .sfixed32(11, -110)
                    .sfixed32(11, -111)
                    .sfixed64(12, -120L)
                    .sfixed64(12, -121L)
                    .bool(13, List.of(true, false))
                    .string(14, "test1")
                    .string(14, "test2")
                    .bytes(15, ba(1, 2, 3))
                    .bytes(15, ba(20, 30, 40))
            );

            // then
            assertThat(record).isEqualTo(RepeatableScalar.builder()
                    .doubles(List.of(-10d, -11d))
                    .floats(List.of(-20f, -21f))
                    .int32s(List.of(-30, -31))
                    .int64s(List.of(-40L, -41L))
                    .uint32s(List.of(50, 51))
                    .uint64s(List.of(60L, 61L))
                    .sint32s(List.of(-70, -71))
                    .sint64s(List.of(-80L, -81L))
                    .fixed32s(List.of(-90, -91))
                    .fixed64s(List.of(-100L, -101L))
                    .sfixed32s(List.of(-110, -111))
                    .sfixed64s(List.of(-120L, -121L))
                    .bools(List.of(true, false))
                    .strings(List.of("test1", "test2"))
                    .bytes(List.of(ba(1, 2, 3), ba(20, 30, 40)))
                    .build());
        }
    }

    @Nested
    class InternalCompatibility {

        @Test
        void emptyObject() throws IOException {
            // given
            RepeatableScalar record = RepeatableScalar.empty();

            // when
            RepeatableScalar deserialized = deserialize(serialize(record));

            // then
            assertThat(deserialized).isEqualTo(record);
        }

        @Test
        void fullObject() throws IOException {
            // given
            RepeatableScalar record = new RepeatableScalar(
                    List.of(10d, 11d),
                    List.of(20f, 21f),
                    List.of(30, 31),
                    List.of(40L, 41L),
                    List.of(50, 51),
                    List.of(60L, 61L),
                    List.of(70, 71),
                    List.of(80L, 81L),
                    List.of(90, 91),
                    List.of(100L, 101L),
                    List.of(110, 111),
                    List.of(120L, 121L),
                    List.of(true, false),
                    List.of("test1", "test2"),
                    List.of(ba(1, 2, 3), ba(20, 30, 40))
            );

            // when
            RepeatableScalar deserialized = deserialize(serialize(record));

            // then
            assertThat(deserialized).isEqualTo(record);
        }

        @Test
        void partialObject() throws IOException {
            // given
            RepeatableScalar record = RepeatableScalar.builder()
                    .addDoubles(10d)
                    .int64s(List.of(40L, 41L, 42L))
                    .addAllSint32s(List.of(70, 71))
                    .sfixed32s(List.of(110))
                    .bools(List.of(false, true, true))
                    .strings(List.of("test", "test"))
                    .build();

            // when
            RepeatableScalar deserialized = deserialize(serialize(record));

            // then
            assertThat(deserialized).isEqualTo(record);
        }

        @Test
        void negativeValues() throws IOException {
            // given
            RepeatableScalar record = new RepeatableScalar(
                    List.of(-10d, -11d),
                    List.of(-20f, -21f),
                    List.of(-30, -31),
                    List.of(-40L, -41L),
                    List.of(50, 51),
                    List.of(60L, 61L),
                    List.of(-70, -71),
                    List.of(-80L, -81L),
                    List.of(-90, -91),
                    List.of(-100L, -101L),
                    List.of(-110, -111),
                    List.of(-120L, -121L),
                    List.of(true, false),
                    List.of("test1", "test2"),
                    List.of(ba(1, 2, 3), ba(20, 30, 40))
            );

            // when
            RepeatableScalar deserialized = deserialize(serialize(record));

            // then
            assertThat(deserialized).isEqualTo(record);
        }
    }

    @Nested
    class ExternalCompatibility {

        @Test
        void emptyObject() throws IOException {
            // given
            RepeatableScalar our = RepeatableScalar.empty();
            RepeatableScalarProto proto = RepeatableScalarProto.newBuilder().build();
            byte[] ourBytes = our.toByteArray();
            byte[] protoBytes = proto.toByteArray();

            // when then
            assertProtoEqual(our, RepeatableScalarProto.parseFrom(ourBytes));
            assertProtoEqual(RepeatableScalar.parse(protoBytes), proto);
        }

        @Test
        void partialObject() throws IOException {
            // given
            RepeatableScalar our = RepeatableScalar.builder()
                    .doubles(List.of(10d))
                    .int64s(List.of(40L, 41L, 42L))
                    .sint32s(List.of(70, 71))
                    .sfixed32s(List.of(110))
                    .bools(List.of(false, true, true))
                    .strings(List.of("test", "test"))
                    .build();
            RepeatableScalarProto proto = RepeatableScalarProto.newBuilder()
                    .addAllDoubles(List.of(10d))
                    .addAllInt64S(List.of(40L, 41L, 42L))
                    .addAllSint32S(List.of(70, 71))
                    .addAllSfixed32S(List.of(110))
                    .addAllBools(List.of(false, true, true))
                    .addAllStrings(List.of("test", "test"))
                    .build();
            byte[] ourBytes = our.toByteArray();
            byte[] protoBytes = proto.toByteArray();

            // when then
            assertProtoEqual(our, RepeatableScalarProto.parseFrom(ourBytes));
            assertProtoEqual(RepeatableScalar.parse(protoBytes), proto);
        }

        @Test
        void fullObject() throws IOException {
            // given
            RepeatableScalar our = RepeatableScalar.builder()
                    .doubles(List.of(10d, 11d))
                    .floats(List.of(20f, 21f))
                    .int32s(List.of(30, 31))
                    .int64s(List.of(40L, 41L))
                    .uint32s(List.of(50, 51))
                    .uint64s(List.of(60L, 61L))
                    .sint32s(List.of(70, 71))
                    .sint64s(List.of(80L, 81L))
                    .fixed32s(List.of(90, 91))
                    .fixed64s(List.of(100L, 101L))
                    .sfixed32s(List.of(110, 111))
                    .sfixed64s(List.of(120L, 121L))
                    .bools(List.of(true, false))
                    .strings(List.of("test1", "test2"))
                    .bytes(List.of(ba(1, 2, 3), ba(20, 30, 40)))
                    .build();
            RepeatableScalarProto proto = RepeatableScalarProto.newBuilder()
                    .addAllDoubles(List.of(10d, 11d))
                    .addAllFloats(List.of(20f, 21f))
                    .addAllInt32S(List.of(30, 31))
                    .addAllInt64S(List.of(40L, 41L))
                    .addAllUint32S(List.of(50, 51))
                    .addAllUint64S(List.of(60L, 61L))
                    .addAllSint32S(List.of(70, 71))
                    .addAllSint64S(List.of(80L, 81L))
                    .addAllFixed32S(List.of(90, 91))
                    .addAllFixed64S(List.of(100L, 101L))
                    .addAllSfixed32S(List.of(110, 111))
                    .addAllSfixed64S(List.of(120L, 121L))
                    .addAllBools(List.of(true, false))
                    .addAllStrings(List.of("test1", "test2"))
                    .addAllBytes(List.of(bs(1, 2, 3), bs(20, 30, 40)))
                    .build();
            byte[] ourBytes = our.toByteArray();
            byte[] protoBytes = proto.toByteArray();

            // when then
            assertProtoEqual(our, RepeatableScalarProto.parseFrom(ourBytes));
            assertProtoEqual(RepeatableScalar.parse(protoBytes), proto);
        }

        @Test
        void negativeValues() throws IOException {
            // given
            RepeatableScalar our = RepeatableScalar.builder()
                    .doubles(List.of(-10d, -11d))
                    .floats(List.of(-20f, -21f))
                    .int32s(List.of(-30, -31))
                    .int64s(List.of(-40L, -41L))
                    .uint32s(List.of(50, 51))
                    .uint64s(List.of(60L, 61L))
                    .sint32s(List.of(-70, -71))
                    .sint64s(List.of(-80L, -81L))
                    .fixed32s(List.of(-90, -91))
                    .fixed64s(List.of(-100L, -101L))
                    .sfixed32s(List.of(-110, -111))
                    .sfixed64s(List.of(-120L, -121L))
                    .bools(List.of(true, false))
                    .strings(List.of("test1", "test2"))
                    .bytes(List.of(ba(1, 2, 3), ba(20, 30, 40)))
                    .build();
            RepeatableScalarProto proto = RepeatableScalarProto.newBuilder()
                    .addAllDoubles(List.of(-10d, -11d))
                    .addAllFloats(List.of(-20f, -21f))
                    .addAllInt32S(List.of(-30, -31))
                    .addAllInt64S(List.of(-40L, -41L))
                    .addAllUint32S(List.of(50, 51))
                    .addAllUint64S(List.of(60L, 61L))
                    .addAllSint32S(List.of(-70, -71))
                    .addAllSint64S(List.of(-80L, -81L))
                    .addAllFixed32S(List.of(-90, -91))
                    .addAllFixed64S(List.of(-100L, -101L))
                    .addAllSfixed32S(List.of(-110, -111))
                    .addAllSfixed64S(List.of(-120L, -121L))
                    .addAllBools(List.of(true, false))
                    .addAllStrings(List.of("test1", "test2"))
                    .addAllBytes(List.of(bs(1, 2, 3), bs(20, 30, 40)))
                    .build();
            byte[] ourBytes = our.toByteArray();
            byte[] protoBytes = proto.toByteArray();

            // when then
            assertProtoEqual(our, RepeatableScalarProto.parseFrom(ourBytes));
            assertProtoEqual(RepeatableScalar.parse(protoBytes), proto);
        }

        private void assertProtoEqual(RepeatableScalar our, RepeatableScalarProto proto) {
            assertThat(our.doubles()).isEqualTo(proto.getDoublesList());
            assertThat(our.floats()).isEqualTo(proto.getFloatsList());
            assertThat(our.int32s()).isEqualTo(proto.getInt32SList());
            assertThat(our.int64s()).isEqualTo(proto.getInt64SList());
            assertThat(our.uint32s()).isEqualTo(proto.getUint32SList());
            assertThat(our.uint64s()).isEqualTo(proto.getUint64SList());
            assertThat(our.sint32s()).isEqualTo(proto.getSint32SList());
            assertThat(our.sint64s()).isEqualTo(proto.getSint64SList());
            assertThat(our.fixed32s()).isEqualTo(proto.getFixed32SList());
            assertThat(our.fixed64s()).isEqualTo(proto.getFixed64SList());
            assertThat(our.sfixed32s()).isEqualTo(proto.getSfixed32SList());
            assertThat(our.sfixed64s()).isEqualTo(proto.getSfixed64SList());
            assertThat(our.bools()).isEqualTo(proto.getBoolsList());
            assertThat(our.strings()).isEqualTo(proto.getStringsList());

            List<byte[]> ourBytes = our.bytes().stream().map(ByteArray::toByteArray).toList();
            List<byte[]> protoBytes = proto.getBytesList().stream().map(ByteString::toByteArray).toList();
            assertThat(ourBytes)
                    .usingElementComparator(Arrays::compare)
                    .isEqualTo(protoBytes);
        }
    }

    private RepeatableScalar deserialize(ThrowingConsumer<ProtobufWriter> writerAction) throws IOException {
        return deserialize(RepeatableScalar::parse, RepeatableScalar::parse, writerAction);
    }

    private RepeatableScalar deserialize(byte[] data) throws IOException {
        return deserialize(RepeatableScalar::parse, RepeatableScalar::parse, data);
    }
}
