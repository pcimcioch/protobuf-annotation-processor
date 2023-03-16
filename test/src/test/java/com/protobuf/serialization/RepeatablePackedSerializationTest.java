package com.protobuf.serialization;

import com.github.pcimcioch.protobuf.dto.BooleanList;
import com.github.pcimcioch.protobuf.dto.DoubleList;
import com.github.pcimcioch.protobuf.dto.FloatList;
import com.github.pcimcioch.protobuf.dto.IntList;
import com.github.pcimcioch.protobuf.dto.LongList;
import com.github.pcimcioch.protobuf.io.ProtobufWriter;
import com.protobuf.model.RepeatableEnum;
import com.protobuf.model.RepeatableEnumProto;
import com.protobuf.model.RepeatablePacked;
import com.protobuf.model.RepeatablePackedProto;
import org.assertj.core.api.ThrowingConsumer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static com.github.pcimcioch.protobuf.io.ProtobufAssertion.assertProto;
import static org.assertj.core.api.Assertions.assertThat;

class RepeatablePackedSerializationTest extends SerializationTestBase {

    @Nested
    class Serialization {

        @Test
        void fullObject() throws IOException {
            // given
            RepeatablePacked record = new RepeatablePacked(
                    DoubleList.of(10d, 11d),
                    FloatList.of(20f, 21f),
                    IntList.of(30, 31),
                    LongList.of(40L, 41L),
                    IntList.of(50, 51),
                    LongList.of(60L, 61L),
                    IntList.of(70, 71),
                    LongList.of(80L, 81L),
                    IntList.of(90, 91),
                    LongList.of(100L, 101L),
                    IntList.of(110, 111),
                    LongList.of(120L, 121L),
                    BooleanList.of(true, false),
                    IntList.of(2, 1)
            );

            // when then
            assertProto(serialize(record))
                    .doublePacked(1, 10d, 11d)
                    .floatPacked(2, 20f, 21f)
                    .int32Packed(3, 30, 31)
                    .int64Packed(4, 40L, 41L)
                    .uint32Packed(5, 50, 51)
                    .uint64Packed(6, 60L, 61L)
                    .sint32Packed(7, 70, 71)
                    .sint64Packed(8, 80L, 81L)
                    .fixed32Packed(9, 90, 91)
                    .fixed64Packed(10, 100L, 101L)
                    .sfixed32Packed(11, 110, 111)
                    .sfixed64Packed(12, 120L, 121L)
                    .boolPacked(13, true, false)
                    .int32Packed(14, 2, 1)
                    .end();
        }

        @Test
        void emptyObject() throws IOException {
            // given
            RepeatablePacked record = RepeatablePacked.empty();

            // when then
            assertProto(serialize(record))
                    .end();
        }

        @Test
        void partialObject() throws IOException {
            // given
            RepeatablePacked record = RepeatablePacked.builder()
                    .addDoubles(10d)
                    .int64s(List.of(40L, 41L, 42L))
                    .addAllSint32s(List.of(70, 71))
                    .sfixed32s(List.of(110))
                    .bools(List.of(false, true, true))
                    .ordersValue(List.of(2, 1))
                    .build();

            // when then
            assertProto(serialize(record))
                    .doublePacked(1, 10d)
                    .int64Packed(4, 40L, 41L, 42L)
                    .sint32Packed(7, 70, 71)
                    .sfixed32Packed(11, 110)
                    .boolPacked(13, false, true, true)
                    .int32Packed(14, 2, 1)
                    .end();
        }

        @Test
        void defaultValues() throws IOException {
            // given
            RepeatablePacked record = new RepeatablePacked(
                    DoubleList.of(0d),
                    FloatList.of(0f),
                    IntList.of(0),
                    LongList.of(0L),
                    IntList.of(0),
                    LongList.of(0L),
                    IntList.of(0),
                    LongList.of(0L),
                    IntList.of(0),
                    LongList.of(0L),
                    IntList.of(0),
                    LongList.of(0L),
                    BooleanList.of(false),
                    IntList.of(0)
            );

            // when then
            assertProto(serialize(record))
                    .doublePacked(1, 0d)
                    .floatPacked(2, 0f)
                    .int32Packed(3, 0)
                    .int64Packed(4, 0L)
                    .uint32Packed(5, 0)
                    .uint64Packed(6, 0L)
                    .sint32Packed(7, 0)
                    .sint64Packed(8, 0L)
                    .fixed32Packed(9, 0)
                    .fixed64Packed(10, 0L)
                    .sfixed32Packed(11, 0)
                    .sfixed64Packed(12, 0L)
                    .boolPacked(13, false)
                    .int32Packed(14, 0)
                    .end();
        }

        @Test
        void negativeValues() throws IOException {
            // given
            RepeatablePacked record = new RepeatablePacked(
                    DoubleList.of(-10d, -11d),
                    FloatList.of(-20f, -21f),
                    IntList.of(-30, -31),
                    LongList.of(-40L, -41L),
                    IntList.of(50, 51),
                    LongList.of(60L, 61L),
                    IntList.of(-70, -71),
                    LongList.of(-80L, -81L),
                    IntList.of(-90, -91),
                    LongList.of(-100L, -101L),
                    IntList.of(-110, -111),
                    LongList.of(-120L, -121L),
                    BooleanList.of(true, false),
                    IntList.of(1, 0)
            );

            // when then
            assertProto(serialize(record))
                    .doublePacked(1, -10d, -11d)
                    .floatPacked(2, -20f, -21f)
                    .int32Packed(3, -30, -31)
                    .int64Packed(4, -40L, -41L)
                    .uint32Packed(5, 50, 51)
                    .uint64Packed(6, 60L, 61L)
                    .sint32Packed(7, -70, -71)
                    .sint64Packed(8, -80L, -81L)
                    .fixed32Packed(9, -90, -91)
                    .fixed64Packed(10, -100L, -101L)
                    .sfixed32Packed(11, -110, -111)
                    .sfixed64Packed(12, -120L, -121L)
                    .boolPacked(13, true, false)
                    .int32Packed(14, 1, 0)
                    .end();
        }
    }

    @Nested
    class Deserialization {

        @Test
        void emptyObject() throws IOException {
            // when
            RepeatablePacked record = deserialize(new byte[0]);

            // then
            assertThat(record).isEqualTo(RepeatablePacked.empty());
        }

        @Test
        void fullObject() throws IOException {
            // given when
            RepeatablePacked record = deserialize(writer -> writer
                    .writeDoublePacked(1, DoubleList.of(10d, 11d))
                    .writeFloatPacked(2, FloatList.of(20f, 21f))
                    .writeInt32Packed(3, IntList.of(30, 31))
                    .writeInt64Packed(4, LongList.of(40L, 41L))
                    .writeUint32Packed(5, IntList.of(50, 51))
                    .writeUint64Packed(6, LongList.of(60L, 61L))
                    .writeSint32Packed(7, IntList.of(70, 71))
                    .writeSint64Packed(8, LongList.of(80L, 81L))
                    .writeFixed32Packed(9, IntList.of(90, 91))
                    .writeFixed64Packed(10, LongList.of(100L, 101L))
                    .writeSfixed32Packed(11, IntList.of(110, 111))
                    .writeSfixed64Packed(12, LongList.of(120L, 121L))
                    .writeBoolPacked(13, BooleanList.of(true, false))
                    .writeInt32Packed(14, IntList.of(2, 0))
            );

            // then
            assertThat(record).isEqualTo(RepeatablePacked.builder()
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
                    .ordersValue(List.of(2, 0))
                    .build());
        }

        @Test
        void partialObject() throws IOException {
            // given when
            RepeatablePacked record = deserialize(writer -> writer
                    .writeDoublePacked(1, DoubleList.of(10d))
                    .writeInt64Packed(4, LongList.of(40L, 41L, 42L))
                    .writeSint32Packed(7, IntList.of(70, 71))
                    .writeSfixed32Packed(11, IntList.of(110))
                    .writeBoolPacked(13, BooleanList.of(false, true, true))
            );

            // then
            assertThat(record).isEqualTo(RepeatablePacked.builder()
                    .doubles(List.of(10d))
                    .int64s(List.of(40L, 41L, 42L))
                    .sint32s(List.of(70, 71))
                    .sfixed32s(List.of(110))
                    .bools(List.of(false, true, true))
                    .build());
        }

        @Test
        void fullObjectReverseOrder() throws IOException {
            // given when
            RepeatablePacked record = deserialize(writer -> writer
                    .writeDoublePacked(1, DoubleList.of(10d))
                    .writeSint32Packed(7, IntList.of(70))
                    .writeInt32Packed(3, IntList.of(30))
                    .writeFloatPacked(2, FloatList.of(20f))
                    .writeSfixed32Packed(11, IntList.of(110))
                    .writeInt32Packed(3, IntList.of(31))
                    .writeFixed64Packed(10, LongList.of(100L))
                    .writeInt64Packed(4, LongList.of(40L))
                    .writeUint32Packed(5, IntList.of(50))
                    .writeSint32Packed(7, IntList.of(71))
                    .writeFixed32Packed(9, IntList.of(90))
                    .writeInt32Packed(14, IntList.of(1))
                    .writeUint32Packed(5, IntList.of(51))
                    .writeUint64Packed(6, LongList.of(60L))
                    .writeDoublePacked(1, DoubleList.of(11d))
                    .writeFloatPacked(2, FloatList.of(21f))
                    .writeUint64Packed(6, LongList.of(61L))
                    .writeInt64Packed(4, LongList.of(41L))
                    .writeBoolPacked(13, BooleanList.of(true))
                    .writeSint64Packed(8, LongList.of(80L))
                    .writeSint64Packed(8, LongList.of(81L))
                    .writeFixed32Packed(9, IntList.of(91))
                    .writeSfixed64Packed(12, LongList.of(120L))
                    .writeFixed64Packed(10, LongList.of(101L))
                    .writeSfixed32Packed(11, IntList.of(111))
                    .writeSfixed64Packed(12, LongList.of(121L))
                    .writeBoolPacked(13, BooleanList.of(false))
                    .writeInt32Packed(14, IntList.of(2))
            );

            // then
            assertThat(record).isEqualTo(RepeatablePacked.builder()
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
                    .ordersValue(List.of(1, 2))
                    .build());
        }

        @Test
        void unknownFields() throws IOException {
            // given when
            RepeatablePacked record = deserialize(writer -> writer
                    // unknown
                    .writeDoublePacked(21, DoubleList.of(110d, 111d))
                    .writeFloatPacked(22, FloatList.of(120f, 121f))
                    .writeInt32Packed(23, IntList.of(130, 131))
                    .writeInt64Packed(24, LongList.of(140L, 141L))
                    .writeUint32Packed(25, IntList.of(150, 151))
                    .writeUint64Packed(26, LongList.of(160L, 161L))
                    .writeSint32Packed(27, IntList.of(170, 171))
                    .writeSint64Packed(28, LongList.of(180L, 181L))
                    .writeFixed32Packed(29, IntList.of(190, 191))
                    .writeFixed64Packed(30, LongList.of(1100L, 1101L))
                    .writeSfixed32Packed(31, IntList.of(1110, 1111))
                    .writeSfixed64Packed(32, LongList.of(1120L, 1121L))
                    .writeBoolPacked(33, BooleanList.of(true, false))
                    // record
                    .writeDoublePacked(1, DoubleList.of(10d))
                    .writeFloatPacked(2, FloatList.of(20f))
                    .writeInt32Packed(3, IntList.of(30))
                    .writeInt64Packed(4, LongList.of(40L))
                    .writeUint32Packed(5, IntList.of(50))
                    .writeUint64Packed(6, LongList.of(60L))
                    .writeSint32Packed(7, IntList.of(70))
                    .writeSint64Packed(8, LongList.of(80L))
                    .writeFixed32Packed(9, IntList.of(90))
                    .writeFixed64Packed(10, LongList.of(100L))
                    .writeSfixed32Packed(11, IntList.of(110))
                    .writeSfixed64Packed(12, LongList.of(120L))
                    .writeBoolPacked(13, BooleanList.of(true))
                    .writeInt32Packed(14, IntList.of(10))
                    // different wire type
                    .writeFloat(1, 11f)
                    .writeDouble(2, 21d)
                    .writeFixed32(3, 30)
                    .writeFixed32(4, 40)
                    .writeFixed32(5, 50)
                    .writeFixed32(6, 60)
                    .writeFixed32(7, 70)
                    .writeFixed32(8, 80)
                    .writeInt32(9, 90)
                    .writeInt32(10, 100)
                    .writeInt32(11, 110)
                    .writeInt32(12, 120)
                    .writeFixed32(13, 1)
                    .writeFloat(14, 1001f)
            );

            // then
            assertThat(record).isEqualTo(RepeatablePacked.builder()
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
                    .ordersValue(List.of(10))
                    .build());
        }

        @Test
        void negativeValues() throws IOException {
            // given when
            RepeatablePacked record = deserialize(writer -> writer
                    .writeDoublePacked(1, DoubleList.of(-10d, -11d))
                    .writeFloatPacked(2, FloatList.of(-20f, -21f))
                    .writeInt32Packed(3, IntList.of(-30, -31))
                    .writeInt64Packed(4, LongList.of(-40L, -41L))
                    .writeUint32Packed(5, IntList.of(50, 51))
                    .writeUint64Packed(6, LongList.of(60L, 61L))
                    .writeSint32Packed(7, IntList.of(-70, -71))
                    .writeSint64Packed(8, LongList.of(-80L, -81L))
                    .writeFixed32Packed(9, IntList.of(-90, -91))
                    .writeFixed64Packed(10, LongList.of(-100L, -101L))
                    .writeSfixed32Packed(11, IntList.of(-110, -111))
                    .writeSfixed64Packed(12, LongList.of(-120L, -121L))
                    .writeBoolPacked(13, BooleanList.of(true, false))
                    .writeInt32Packed(14, IntList.of(1, 2))
            );

            // then
            assertThat(record).isEqualTo(RepeatablePacked.builder()
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
                    .ordersValue(List.of(1, 2))
                    .build());
        }

        @Test
        void unpackedFields() throws IOException {
            // given when
            RepeatablePacked record = deserialize(writer -> writer
                    .writeDouble(1, 10d)
                    .writeDouble(1, 11d)
                    .writeDoublePacked(1, DoubleList.of(12d, 13d, 14d))
                    .writeFloat(2, 20f)
                    .writeFloat(2, 21f)
                    .writeFloatPacked(2, FloatList.of(22f, 23f, 24f))
                    .writeInt32(3, 30)
                    .writeInt32(3, 31)
                    .writeInt32Packed(3, IntList.of(32, 33, 34))
                    .writeInt64(4, 40L)
                    .writeInt64(4, 41L)
                    .writeInt64Packed(4, LongList.of(42L, 43L, 44L))
                    .writeUint32(5, 50)
                    .writeUint32(5, 51)
                    .writeUint32Packed(5, IntList.of(52, 53, 54))
                    .writeUint64(6, 60L)
                    .writeUint64(6, 61L)
                    .writeUint64Packed(6, LongList.of(62L, 63L, 64L))
                    .writeSint32(7, 70)
                    .writeSint32(7, 71)
                    .writeSint32Packed(7, IntList.of(72, 73, 74))
                    .writeSint64(8, 80L)
                    .writeSint64(8, 81L)
                    .writeSint64Packed(8, LongList.of(82L, 83L, 84L))
                    .writeFixed32(9, 90)
                    .writeFixed32(9, 91)
                    .writeFixed32Packed(9, IntList.of(92, 93, 94))
                    .writeFixed64(10, 100L)
                    .writeFixed64(10, 101L)
                    .writeFixed64Packed(10, LongList.of(102L, 103L, 104L))
                    .writeSfixed32(11, 110)
                    .writeSfixed32(11, 111)
                    .writeSfixed32Packed(11, IntList.of(112, 113, 114))
                    .writeSfixed64(12, 120L)
                    .writeSfixed64(12, 121L)
                    .writeSfixed64Packed(12, LongList.of(122L, 123L, 124L))
                    .writeBoolUnpacked(13, BooleanList.of(true, false))
                    .writeBoolPacked(13, BooleanList.of(false, true))
                    .writeInt32(14, 1)
                    .writeInt32Packed(14, IntList.of(2, 0))
            );

            // then
            assertThat(record).isEqualTo(RepeatablePacked.builder()
                    .doubles(List.of(10d, 11d, 12d, 13d, 14d))
                    .floats(List.of(20f, 21f, 22f, 23f, 24f))
                    .int32s(List.of(30, 31, 32, 33, 34))
                    .int64s(List.of(40L, 41L, 42L, 43L, 44L))
                    .uint32s(List.of(50, 51, 52, 53, 54))
                    .uint64s(List.of(60L, 61L, 62L, 63L, 64L))
                    .sint32s(List.of(70, 71, 72, 73, 74))
                    .sint64s(List.of(80L, 81L, 82L, 83L, 84L))
                    .fixed32s(List.of(90, 91, 92, 93, 94))
                    .fixed64s(List.of(100L, 101L, 102L, 103L, 104L))
                    .sfixed32s(List.of(110, 111, 112, 113, 114))
                    .sfixed64s(List.of(120L, 121L, 122L, 123L, 124L))
                    .bools(List.of(true, false, false, true))
                    .ordersValue(List.of(1, 2, 0))
                    .build());
        }
    }

    @Nested
    class InternalCompatibility {

        @Test
        void emptyObject() throws IOException {
            // given
            RepeatablePacked record = RepeatablePacked.empty();

            // when
            RepeatablePacked deserialized = deserialize(serialize(record));

            // then
            assertThat(deserialized).isEqualTo(record);
        }

        @Test
        void fullObject() throws IOException {
            // given
            RepeatablePacked record = new RepeatablePacked(
                    DoubleList.of(10d, 11d),
                    FloatList.of(20f, 21f),
                    IntList.of(30, 31),
                    LongList.of(40L, 41L),
                    IntList.of(50, 51),
                    LongList.of(60L, 61L),
                    IntList.of(70, 71),
                    LongList.of(80L, 81L),
                    IntList.of(90, 91),
                    LongList.of(100L, 101L),
                    IntList.of(110, 111),
                    LongList.of(120L, 121L),
                    BooleanList.of(true, false),
                    IntList.of(1, 2)
            );

            // when
            RepeatablePacked deserialized = deserialize(serialize(record));

            // then
            assertThat(deserialized).isEqualTo(record);
        }

        @Test
        void partialObject() throws IOException {
            // given
            RepeatablePacked record = RepeatablePacked.builder()
                    .addDoubles(10d)
                    .int64s(List.of(40L, 41L, 42L))
                    .addAllSint32s(List.of(70, 71))
                    .sfixed32s(List.of(110))
                    .bools(List.of(false, true, true))
                    .build();

            // when
            RepeatablePacked deserialized = deserialize(serialize(record));

            // then
            assertThat(deserialized).isEqualTo(record);
        }

        @Test
        void negativeValues() throws IOException {
            // given
            RepeatablePacked record = new RepeatablePacked(
                    DoubleList.of(-10d, -11d),
                    FloatList.of(-20f, -21f),
                    IntList.of(-30, -31),
                    LongList.of(-40L, -41L),
                    IntList.of(50, 51),
                    LongList.of(60L, 61L),
                    IntList.of(-70, -71),
                    LongList.of(-80L, -81L),
                    IntList.of(-90, -91),
                    LongList.of(-100L, -101L),
                    IntList.of(-110, -111),
                    LongList.of(-120L, -121L),
                    BooleanList.of(true, false),
                    IntList.of(2, 10)
            );

            // when
            RepeatablePacked deserialized = deserialize(serialize(record));

            // then
            assertThat(deserialized).isEqualTo(record);
        }
    }

    @Nested
    class ExternalCompatibility {

        @Test
        void emptyObject() throws IOException {
            // given
            RepeatablePacked our = RepeatablePacked.empty();
            RepeatablePackedProto proto = RepeatablePackedProto.newBuilder().build();
            byte[] ourBytes = our.toByteArray();
            byte[] protoBytes = proto.toByteArray();

            // when then
            assertProtoEqual(our, RepeatablePackedProto.parseFrom(ourBytes));
            assertProtoEqual(RepeatablePacked.parse(protoBytes), proto);
        }

        @Test
        void partialObject() throws IOException {
            // given
            RepeatablePacked our = RepeatablePacked.builder()
                    .doubles(List.of(10d))
                    .int64s(List.of(40L, 41L, 42L))
                    .sint32s(List.of(70, 71))
                    .sfixed32s(List.of(110))
                    .bools(List.of(false, true, true))
                    .build();
            RepeatablePackedProto proto = RepeatablePackedProto.newBuilder()
                    .addAllDoubles(List.of(10d))
                    .addAllInt64S(List.of(40L, 41L, 42L))
                    .addAllSint32S(List.of(70, 71))
                    .addAllSfixed32S(List.of(110))
                    .addAllBools(List.of(false, true, true))
                    .build();
            byte[] ourBytes = our.toByteArray();
            byte[] protoBytes = proto.toByteArray();

            // when then
            assertProtoEqual(our, RepeatablePackedProto.parseFrom(ourBytes));
            assertProtoEqual(RepeatablePacked.parse(protoBytes), proto);
        }

        @Test
        void fullObject() throws IOException {
            // given
            RepeatablePacked our = RepeatablePacked.builder()
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
                    .orders(List.of(RepeatableEnum.FIRST, RepeatableEnum.SECOND))
                    .build();
            RepeatablePackedProto proto = RepeatablePackedProto.newBuilder()
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
                    .addAllOrders(List.of(RepeatableEnumProto.FIRST_, RepeatableEnumProto.SECOND_))
                    .build();
            byte[] ourBytes = our.toByteArray();
            byte[] protoBytes = proto.toByteArray();

            // when then
            assertProtoEqual(our, RepeatablePackedProto.parseFrom(ourBytes));
            assertProtoEqual(RepeatablePacked.parse(protoBytes), proto);
        }

        @Test
        void negativeValues() throws IOException {
            // given
            RepeatablePacked our = RepeatablePacked.builder()
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
                    .ordersValue(List.of(1, 10))
                    .build();
            RepeatablePackedProto proto = RepeatablePackedProto.newBuilder()
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
                    .addAllOrdersValue(List.of(1, 10))
                    .build();
            byte[] ourBytes = our.toByteArray();
            byte[] protoBytes = proto.toByteArray();

            // when then
            assertProtoEqual(our, RepeatablePackedProto.parseFrom(ourBytes));
            assertProtoEqual(RepeatablePacked.parse(protoBytes), proto);
        }

        private void assertProtoEqual(RepeatablePacked our, RepeatablePackedProto proto) {
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
            assertThat(our.ordersValue()).isEqualTo(proto.getOrdersValueList());
            assertThat(our.orders()).isEqualTo(convert(proto.getOrdersList()));
        }

        private List<RepeatableEnum> convert(List<RepeatableEnumProto> ordersProto) {
            return ordersProto.stream()
                    .map(this::convert)
                    .toList();
        }

        private RepeatableEnum convert(RepeatableEnumProto proto) {
            return switch (proto) {
                case FIRST_ -> RepeatableEnum.FIRST;
                case SECOND_ -> RepeatableEnum.SECOND;
                case THIRD_ -> RepeatableEnum.THIRD;
                case UNRECOGNIZED -> RepeatableEnum.UNRECOGNIZED;
            };
        }
    }

    private RepeatablePacked deserialize(ThrowingConsumer<ProtobufWriter> writerAction) throws IOException {
        return deserialize(RepeatablePacked::parse, RepeatablePacked::parse, writerAction);
    }

    private RepeatablePacked deserialize(byte[] data) throws IOException {
        return deserialize(RepeatablePacked::parse, RepeatablePacked::parse, data);
    }
}
