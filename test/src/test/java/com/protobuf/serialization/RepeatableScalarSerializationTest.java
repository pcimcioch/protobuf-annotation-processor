package com.protobuf.serialization;

import com.github.pcimcioch.protobuf.dto.BooleanList;
import com.github.pcimcioch.protobuf.dto.ByteArray;
import com.github.pcimcioch.protobuf.dto.DoubleList;
import com.github.pcimcioch.protobuf.dto.FloatList;
import com.github.pcimcioch.protobuf.dto.IntList;
import com.github.pcimcioch.protobuf.dto.LongList;
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

import static com.github.pcimcioch.protobuf.io.ProtobufAssertion.assertProto;
import static com.protobuf.ByteUtils.b;
import static com.protobuf.ByteUtils.ba;
import static com.protobuf.ByteUtils.bs;
import static org.assertj.core.api.Assertions.assertThat;

class RepeatableScalarSerializationTest extends SerializationTestBase {

    @Nested
    class Serialization {

        @Test
        void fullObject() throws IOException {
            // given
            RepeatableScalar record = new RepeatableScalar(
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
                    .writeDouble(1, 10d)
                    .writeDouble(1, 11d)
                    .writeFloat(2, 20f)
                    .writeFloat(2, 21f)
                    .writeInt32(3, 30)
                    .writeInt32(3, 31)
                    .writeInt64(4, 40L)
                    .writeInt64(4, 41L)
                    .writeUint32(5, 50)
                    .writeUint32(5, 51)
                    .writeUint64(6, 60L)
                    .writeUint64(6, 61L)
                    .writeSint32(7, 70)
                    .writeSint32(7, 71)
                    .writeSint64(8, 80L)
                    .writeSint64(8, 81L)
                    .writeFixed32(9, 90)
                    .writeFixed32(9, 91)
                    .writeFixed64(10, 100L)
                    .writeFixed64(10, 101L)
                    .writeSfixed32(11, 110)
                    .writeSfixed32(11, 111)
                    .writeSfixed64(12, 120L)
                    .writeSfixed64(12, 121L)
                    .writeBoolUnpacked(13, BooleanList.of(true, false))
                    .writeString(14, "test1")
                    .writeString(14, "test2")
                    .writeBytes(15, ba(1, 2, 3))
                    .writeBytes(15, ba(20, 30, 40))
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
                    .writeDouble(1, 10d)
                    .writeInt64(4, 40L)
                    .writeInt64(4, 41L)
                    .writeInt64(4, 42L)
                    .writeSint32(7, 70)
                    .writeSint32(7, 71)
                    .writeSfixed32(11, 110)
                    .writeBoolUnpacked(13, BooleanList.of(false, true, true))
                    .writeString(14, "test")
                    .writeString(14, "test")
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
                    .writeDouble(1, 10d)
                    .writeSint32(7, 70)
                    .writeInt32(3, 30)
                    .writeFloat(2, 20f)
                    .writeSfixed32(11, 110)
                    .writeInt32(3, 31)
                    .writeFixed64(10, 100L)
                    .writeInt64(4, 40L)
                    .writeUint32(5, 50)
                    .writeSint32(7, 71)
                    .writeFixed32(9, 90)
                    .writeString(14, "test1")
                    .writeUint32(5, 51)
                    .writeUint64(6, 60L)
                    .writeDouble(1, 11d)
                    .writeFloat(2, 21f)
                    .writeUint64(6, 61L)
                    .writeInt64(4, 41L)
                    .writeBoolUnpacked(13, BooleanList.of(true))
                    .writeSint64(8, 80L)
                    .writeBytes(15, ba(1, 2, 3))
                    .writeSint64(8, 81L)
                    .writeFixed32(9, 91)
                    .writeSfixed64(12, 120L)
                    .writeFixed64(10, 101L)
                    .writeSfixed32(11, 111)
                    .writeSfixed64(12, 121L)
                    .writeBoolUnpacked(13, BooleanList.of(false))
                    .writeString(14, "test2")
                    .writeBytes(15, ba(20, 30, 40))
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
                    .writeDouble(21, 110d)
                    .writeDouble(21, 111d)
                    .writeFloat(22, 120f)
                    .writeFloat(22, 121f)
                    .writeInt32(23, 130)
                    .writeInt32(23, 131)
                    .writeInt64(24, 140L)
                    .writeInt64(24, 141L)
                    .writeUint32(25, 150)
                    .writeUint32(25, 151)
                    .writeUint64(26, 160L)
                    .writeUint64(26, 161L)
                    .writeSint32(27, 170)
                    .writeSint32(27, 171)
                    .writeSint64(28, 180L)
                    .writeSint64(28, 181L)
                    .writeFixed32(29, 190)
                    .writeFixed32(29, 191)
                    .writeFixed64(30, 1100L)
                    .writeFixed64(30, 1101L)
                    .writeSfixed32(31, 1110)
                    .writeSfixed32(31, 1111)
                    .writeSfixed64(32, 1120L)
                    .writeSfixed64(32, 1121L)
                    .writeBoolUnpacked(33, BooleanList.of(true, false))
                    .writeString(34, "test10")
                    .writeString(34, "test20")
                    .writeBytes(35, ba(5, 6, 7))
                    .writeBytes(35, ba(25, 35, 45))
                    // record
                    .writeDouble(1, 10d)
                    .writeFloat(2, 20f)
                    .writeInt32(3, 30)
                    .writeInt64(4, 40L)
                    .writeUint32(5, 50)
                    .writeUint64(6, 60L)
                    .writeSint32(7, 70)
                    .writeSint64(8, 80L)
                    .writeFixed32(9, 90)
                    .writeFixed64(10, 100L)
                    .writeSfixed32(11, 110)
                    .writeSfixed64(12, 120L)
                    .writeBool(13, true)
                    .writeString(14, "test1")
                    .writeBytes(15, ba(1, 2, 3))
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
                    .writeInt32(14, 1001)
                    .writeInt32(15, 1002)
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
                    .writeDouble(1, -10d)
                    .writeDouble(1, -11d)
                    .writeFloat(2, -20f)
                    .writeFloat(2, -21f)
                    .writeInt32(3, -30)
                    .writeInt32(3, -31)
                    .writeInt64(4, -40L)
                    .writeInt64(4, -41L)
                    .writeUint32(5, 50)
                    .writeUint32(5, 51)
                    .writeUint64(6, 60L)
                    .writeUint64(6, 61L)
                    .writeSint32(7, -70)
                    .writeSint32(7, -71)
                    .writeSint64(8, -80L)
                    .writeSint64(8, -81L)
                    .writeFixed32(9, -90)
                    .writeFixed32(9, -91)
                    .writeFixed64(10, -100L)
                    .writeFixed64(10, -101L)
                    .writeSfixed32(11, -110)
                    .writeSfixed32(11, -111)
                    .writeSfixed64(12, -120L)
                    .writeSfixed64(12, -121L)
                    .writeBoolUnpacked(13, BooleanList.of(true, false))
                    .writeString(14, "test1")
                    .writeString(14, "test2")
                    .writeBytes(15, ba(1, 2, 3))
                    .writeBytes(15, ba(20, 30, 40))
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

        @Test
        void packedFields() throws IOException {
            // given when
            RepeatableScalar record = deserialize(writer -> writer
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
            );

            // then
            assertThat(record).isEqualTo(RepeatableScalar.builder()
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
