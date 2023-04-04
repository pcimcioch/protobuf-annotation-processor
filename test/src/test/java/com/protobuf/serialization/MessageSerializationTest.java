package com.protobuf.serialization;

import com.github.pcimcioch.protobuf.io.ProtobufEncoder;
import com.protobuf.model.OtherMessageAddress;
import com.protobuf.model.OtherMessageAddressProto;
import com.protobuf.model.OtherMessageRecord;
import com.protobuf.model.OtherMessageRecordProto;
import com.protobuf.model.OtherMessageWork;
import com.protobuf.model.OtherMessageWorkProto;
import org.assertj.core.api.ThrowingConsumer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.github.pcimcioch.protobuf.io.ProtobufAssertion.assertProto;
import static com.protobuf.ByteUtils.b;
import static com.protobuf.ByteUtils.concatenate;
import static org.assertj.core.api.Assertions.assertThat;

class MessageSerializationTest extends SerializationTestBase {

    @Nested
    class Serialization {

        @Test
        void fullObject() throws IOException {
            // given
            OtherMessageRecord record = new OtherMessageRecord(
                    "Tomas",
                    40,
                    new OtherMessageAddress("Java St.", 12),
                    new OtherMessageWork(
                            new OtherMessageAddress("Test Al.", 34000),
                            "Software House inc.",
                            2001
                    )
            );

            // when then
            assertProto(serialize(record))
                    .string(1, "Tomas")
                    .int32(2, 40)
                    .message(3, address -> address
                            .string(1, "Java St.")
                            .int32(2, 12)
                            .end())
                    .message(4, work -> work
                            .message(1, address -> address
                                    .string(1, "Test Al.")
                                    .int32(2, 34000)
                                    .end())
                            .string(2, "Software House inc.")
                            .fixed32(3, 2001)
                            .end())
                    .end();
        }

        @Test
        void emptyObject() throws IOException {
            // given
            OtherMessageRecord record = OtherMessageRecord.empty();

            // when then
            assertProto(serialize(record))
                    .end();
        }

        @Test
        void partialObject() throws IOException {
            // given
            OtherMessageRecord record = OtherMessageRecord.builder()
                    .name("Tomas")
                    .age(40)
                    .work(OtherMessageWork.builder()
                            .name("Software House inc.")
                            .build()
                    )
                    .build();

            // when then
            assertProto(serialize(record))
                    .string(1, "Tomas")
                    .int32(2, 40)
                    .message(4, work -> work
                            .string(2, "Software House inc.")
                            .end())
                    .end();
        }
    }

    @Nested
    class Deserialization {

        @Test
        void emptyObject() throws IOException {
            // given when
            OtherMessageRecord record = deserialize(new byte[0]);

            // then
            assertThat(record).isEqualTo(OtherMessageRecord.empty());
        }

        @Test
        void fullObject() throws IOException {
            // given when
            OtherMessageRecord record = deserialize(writer -> writer
                    .writeString(1, "Tomas")
                    .writeInt32(2, 40)
                    .writeBytes(3, serialize(address -> address
                            .writeString(1, "Java St.")
                            .writeInt32(2, 12)
                    ))
                    .writeBytes(4, serialize(work -> work
                            .writeBytes(1, serialize(address -> address
                                    .writeString(1, "Test Al.")
                                    .writeInt32(2, 34000)
                            ))
                            .writeString(2, "Software House inc.")
                            .writeFixed32(3, 2001)
                    ))
            );

            // then
            assertThat(record).isEqualTo(new OtherMessageRecord(
                    "Tomas",
                    40,
                    new OtherMessageAddress("Java St.", 12),
                    new OtherMessageWork(
                            new OtherMessageAddress("Test Al.", 34000),
                            "Software House inc.",
                            2001
                    )
            ));
        }

        @Test
        void partialObject() throws IOException {
            // given when
            OtherMessageRecord record = deserialize(writer -> writer
                    .writeString(1, "Tomas")
                    .writeInt32(2, 40)
                    .writeBytes(4, serialize(work -> work
                            .writeString(2, "Software House inc.")
                    ))
            );

            // then
            assertThat(record).isEqualTo(OtherMessageRecord.builder()
                    .name("Tomas")
                    .age(40)
                    .work(OtherMessageWork.builder()
                            .name("Software House inc.")
                            .build()
                    )
                    .build()
            );
        }

        @Test
        void fullObjectReverseOrder() throws IOException {
            // given when
            OtherMessageRecord record = deserialize(writer -> writer
                    .writeBytes(4, serialize(work -> work
                            .writeFixed32(3, 2001)
                            .writeString(2, "Software House inc.")
                            .writeBytes(1, serialize(address -> address
                                    .writeString(1, "Test Al.")
                                    .writeInt32(2, 34000)
                            ))
                    ))
                    .writeBytes(3, serialize(address -> address
                            .writeInt32(2, 12)
                            .writeString(1, "Java St.")
                    ))
                    .writeInt32(2, 40)
                    .writeString(1, "Tomas")
            );

            // then
            assertThat(record).isEqualTo(new OtherMessageRecord(
                    "Tomas",
                    40,
                    new OtherMessageAddress("Java St.", 12),
                    new OtherMessageWork(
                            new OtherMessageAddress("Test Al.", 34000),
                            "Software House inc.",
                            2001
                    )
            ));
        }

        @Test
        void unknownFields() throws IOException {
            // given when
            OtherMessageRecord record = deserialize(writer -> writer
                    .writeBytes(10, b(10, 20, 30, 40))
                    .writeString(1, "Tomas")
                    .writeInt32(2, 40)
                    .writeBytes(3, serialize(address -> address
                            .writeString(1, "Java St.")
                            .writeFixed32(11, 500)
                            .writeInt32(2, 12)
                    ))
                    .writeBytes(4, serialize(work -> work
                            .writeBytes(1, serialize(address -> address
                                    .writeFixed64(12, 123456L)
                                    .writeString(1, "Test Al.")
                                    .writeInt32(2, 34000)
                            ))
                            .writeString(2, "Software House inc.")
                            .writeFixed32(3, 2001)
                            .writeString(13, "some string")
                    ))
            );

            // then
            assertThat(record).isEqualTo(new OtherMessageRecord(
                    "Tomas",
                    40,
                    new OtherMessageAddress("Java St.", 12),
                    new OtherMessageWork(
                            new OtherMessageAddress("Test Al.", 34000),
                            "Software House inc.",
                            2001
                    )
            ));
        }

        @Test
        void merge() throws IOException {
            // given when
            OtherMessageRecord record = deserialize(writer -> writer
                    // first message
                    .writeString(1, "test")
                    .writeInt32(2, 10)
                    .writeBytes(4, serialize(work -> work
                            .writeBytes(1, serialize(address -> address
                                    .writeString(1, "Test")
                                    .writeInt32(2, 20)
                            ))
                            .writeString(2, "Work")
                            .writeFixed32(3, 1999)
                    ))
                    // second message
                    .writeString(1, "test2")
                    .writeBytes(3, serialize(address -> address
                            .writeString(1, "Sun Street")
                            .writeInt32(2, 100)
                    ))
                    .writeBytes(4, serialize(work -> work
                            .writeBytes(1, serialize(address -> address
                                    .writeInt32(2, 200)
                            ))
                            .writeFixed32(3, 2022)
                    ))
            );

            // then
            assertThat(record).isEqualTo(new OtherMessageRecord(
                    "test2",
                    10,
                    new OtherMessageAddress("Sun Street", 100),
                    new OtherMessageWork(
                            new OtherMessageAddress("Test", 200),
                            "Work",
                            2022
                    )
            ));
        }
    }

    @Nested
    class InternalCompatibility {

        @Test
        void emptyObject() throws IOException {
            // given
            OtherMessageRecord record = OtherMessageRecord.empty();

            // when
            OtherMessageRecord deserialized = deserialize(serialize(record));

            // then
            assertThat(deserialized).isEqualTo(record);
        }

        @Test
        void fullObject() throws IOException {
            // given
            OtherMessageRecord record = new OtherMessageRecord(
                    "Tomas",
                    40,
                    new OtherMessageAddress("Java St.", 12),
                    new OtherMessageWork(
                            new OtherMessageAddress("Test Al.", 34000),
                            "Software House inc.",
                            2001
                    )
            );

            // when
            OtherMessageRecord deserialized = deserialize(serialize(record));

            // then
            assertThat(deserialized).isEqualTo(record);
        }

        @Test
        void partialObject() throws IOException {
            // given
            OtherMessageRecord record = OtherMessageRecord.builder()
                    .name("Tomas")
                    .age(40)
                    .work(OtherMessageWork.builder()
                            .name("Software House inc.")
                            .build()
                    )
                    .build();

            // when
            OtherMessageRecord deserialized = deserialize(serialize(record));

            // then
            assertThat(deserialized).isEqualTo(record);
        }
    }

    @Nested
    class ExternalCompatibility {

        @Test
        void emptyObject() throws IOException {
            // given
            OtherMessageRecord our = OtherMessageRecord.empty();
            OtherMessageRecordProto proto = OtherMessageRecordProto.newBuilder().build();
            byte[] ourBytes = our.toByteArray();
            byte[] protoBytes = proto.toByteArray();

            // when then
            assertProtoEqual(our, OtherMessageRecordProto.parseFrom(ourBytes));
            assertProtoEqual(OtherMessageRecord.parse(protoBytes), proto);
        }

        @Test
        void fullObject() throws IOException {
            // given
            OtherMessageRecord our = new OtherMessageRecord(
                    "Tomas",
                    40,
                    new OtherMessageAddress("Java St.", 12),
                    new OtherMessageWork(
                            new OtherMessageAddress("Test Al.", 34000),
                            "Software House inc.",
                            2001
                    )
            );
            OtherMessageRecordProto proto = OtherMessageRecordProto.newBuilder()
                    .setName("Tomas")
                    .setAge(40)
                    .setAddress(OtherMessageAddressProto.newBuilder()
                            .setStreet("Java St.")
                            .setNumber(12)
                            .build())
                    .setWork(OtherMessageWorkProto.newBuilder()
                            .setAddress(OtherMessageAddressProto.newBuilder()
                                    .setStreet("Test Al.")
                                    .setNumber(34000)
                                    .build())
                            .setName("Software House inc.")
                            .setYear(2001)
                            .build())
                    .build();
            byte[] ourBytes = our.toByteArray();
            byte[] protoBytes = proto.toByteArray();

            // when then
            assertProtoEqual(our, OtherMessageRecordProto.parseFrom(ourBytes));
            assertProtoEqual(OtherMessageRecord.parse(protoBytes), proto);
        }

        @Test
        void partialObject() throws IOException {
            // given
            OtherMessageRecord our = OtherMessageRecord.builder()
                    .name("Tomas")
                    .age(40)
                    .work(OtherMessageWork.builder()
                            .name("Software House inc.")
                            .build())
                    .build();
            OtherMessageRecordProto proto = OtherMessageRecordProto.newBuilder()
                    .setName("Tomas")
                    .setAge(40)
                    .setWork(OtherMessageWorkProto.newBuilder()
                            .setName("Software House inc.")
                            .build())
                    .build();
            byte[] ourBytes = our.toByteArray();
            byte[] protoBytes = proto.toByteArray();

            // when then
            assertProtoEqual(our, OtherMessageRecordProto.parseFrom(ourBytes));
            assertProtoEqual(OtherMessageRecord.parse(protoBytes), proto);
        }

        @Test
        void merge() throws IOException {
            // given
            OtherMessageRecord ourBase = OtherMessageRecord.builder()
                    .name("test")
                    .age(10)
                    .work(OtherMessageWork.builder()
                            .address(OtherMessageAddress.builder()
                                    .street("Test")
                                    .number(20)
                                    .build())
                            .name("Work")
                            .year(1999)
                            .build())
                    .build();
            OtherMessageRecord ourToMerge = OtherMessageRecord.builder()
                    .name("test2")
                    .address(OtherMessageAddress.builder()
                            .street("Sun Street")
                            .number(100)
                            .build())
                    .work(OtherMessageWork.builder()
                            .address(OtherMessageAddress.builder()
                                    .number(200)
                                    .build())
                            .year(2022)
                            .build())
                    .build();
            OtherMessageRecordProto protoBase = OtherMessageRecordProto.newBuilder()
                    .setName("test")
                    .setAge(10)
                    .setWork(OtherMessageWorkProto.newBuilder()
                            .setAddress(OtherMessageAddressProto.newBuilder()
                                    .setStreet("Test")
                                    .setNumber(20)
                                    .build())
                            .setName("Work")
                            .setYear(1999)
                            .build())
                    .build();
            OtherMessageRecordProto protoToMerge = OtherMessageRecordProto.newBuilder()
                    .setName("test2")
                    .setAddress(OtherMessageAddressProto.newBuilder()
                            .setStreet("Sun Street")
                            .setNumber(100)
                            .build())
                    .setWork(OtherMessageWorkProto.newBuilder()
                            .setAddress(OtherMessageAddressProto.newBuilder()
                                    .setNumber(200)
                                    .build())
                            .setYear(2022)
                            .build())
                    .build();
            byte[] ourBytes = concatenate(ourBase.toByteArray(), ourToMerge.toByteArray());
            byte[] protoBytes = concatenate(protoBase.toByteArray(), protoToMerge.toByteArray());

            // when then
            OtherMessageRecord ourExpected = OtherMessageRecord.builder()
                    .name("test2")
                    .age(10)
                    .address(OtherMessageAddress.builder()
                            .street("Sun Street")
                            .number(100)
                            .build())
                    .work(OtherMessageWork.builder()
                            .address(OtherMessageAddress.builder()
                                    .street("Test")
                                    .number(200)
                                    .build())
                            .name("Work")
                            .year(2022)
                            .build())
                    .build();
            OtherMessageRecordProto protoExpected = OtherMessageRecordProto.newBuilder()
                    .setName("test2")
                    .setAge(10)
                    .setAddress(OtherMessageAddressProto.newBuilder()
                            .setStreet("Sun Street")
                            .setNumber(100)
                            .build())
                    .setWork(OtherMessageWorkProto.newBuilder()
                            .setAddress(OtherMessageAddressProto.newBuilder()
                                    .setStreet("Test")
                                    .setNumber(200)
                                    .build())
                            .setName("Work")
                            .setYear(2022)
                            .build())
                    .build();

            assertProtoEqual(ourExpected, OtherMessageRecordProto.parseFrom(ourBytes));
            assertProtoEqual(OtherMessageRecord.parse(protoBytes), protoExpected);
        }

        private void assertProtoEqual(OtherMessageRecord our, OtherMessageRecordProto proto) {
            assertThat(our.name()).isEqualTo(proto.getName());
            assertThat(our.age()).isEqualTo(proto.getAge());
            assertProtoEqual(our.address(), proto.getAddress());
            assertProtoEqual(our.work(), proto.getWork());
        }

        private void assertProtoEqual(OtherMessageWork our, OtherMessageWorkProto proto) {
            assertThat(our.name()).isEqualTo(proto.getName());
            assertThat(our.year()).isEqualTo(proto.getYear());
            assertProtoEqual(our.address(), proto.getAddress());
        }

        private void assertProtoEqual(OtherMessageAddress our, OtherMessageAddressProto proto) {
            assertThat(our.street()).isEqualTo(proto.getStreet());
            assertThat(our.number()).isEqualTo(proto.getNumber());
        }
    }

    private OtherMessageRecord deserialize(ThrowingConsumer<ProtobufEncoder> writerAction) throws IOException {
        return deserialize(OtherMessageRecord::parse, OtherMessageRecord::parse, writerAction);
    }

    private OtherMessageRecord deserialize(byte[] data) throws IOException {
        return deserialize(OtherMessageRecord::parse, OtherMessageRecord::parse, data);
    }
}
