package com.protobuf.serialization;

import com.protobuf.model.OtherMessageAddress;
import com.protobuf.model.OtherMessageAddressProto;
import com.protobuf.model.OtherMessageRecord;
import com.protobuf.model.OtherMessageRecordProto;
import com.protobuf.model.OtherMessageWork;
import com.protobuf.model.OtherMessageWorkProto;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.protobuf.ByteUtils.ba;
import static com.protobuf.ProtobufAssertion.assertProto;
import static org.assertj.core.api.Assertions.assertThat;

class MessageSerializationTest extends SerializationTest {

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
            OtherMessageRecord record = deserialize(OtherMessageRecord::parse, OtherMessageRecord::parse, new byte[0]);

            // then
            assertThat(record).isEqualTo(OtherMessageRecord.empty());
        }

        @Test
        void fullObject() throws IOException {
            // given when
            OtherMessageRecord record = deserialize(OtherMessageRecord::parse, OtherMessageRecord::parse, writer -> writer
                    .string(1, "Tomas")
                    .int32(2, 40)
                    .bytes(3, deserialize(address -> address
                            .string(1, "Java St.")
                            .int32(2, 12)
                    ))
                    .bytes(4, deserialize(work -> work
                            .bytes(1, deserialize(address -> address
                                    .string(1, "Test Al.")
                                    .int32(2, 34000)
                            ))
                            .string(2, "Software House inc.")
                            .fixed32(3, 2001)
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
            OtherMessageRecord record = deserialize(OtherMessageRecord::parse, OtherMessageRecord::parse, writer -> writer
                    .string(1, "Tomas")
                    .int32(2, 40)
                    .bytes(4, deserialize(work -> work
                            .string(2, "Software House inc.")
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
            OtherMessageRecord record = deserialize(OtherMessageRecord::parse, OtherMessageRecord::parse, writer -> writer
                    .bytes(4, deserialize(work -> work
                            .fixed32(3, 2001)
                            .string(2, "Software House inc.")
                            .bytes(1, deserialize(address -> address
                                    .string(1, "Test Al.")
                                    .int32(2, 34000)
                            ))
                    ))
                    .bytes(3, deserialize(address -> address
                            .int32(2, 12)
                            .string(1, "Java St.")
                    ))
                    .int32(2, 40)
                    .string(1, "Tomas")
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
            OtherMessageRecord record = deserialize(OtherMessageRecord::parse, OtherMessageRecord::parse, writer -> writer
                    .bytes(10, ba(10, 20, 30, 40))
                    .string(1, "Tomas")
                    .int32(2, 40)
                    .bytes(3, deserialize(address -> address
                            .string(1, "Java St.")
                            .fixed32(11, 500)
                            .int32(2, 12)
                    ))
                    .bytes(4, deserialize(work -> work
                            .bytes(1, deserialize(address -> address
                                    .fixed64(12, 123456L)
                                    .string(1, "Test Al.")
                                    .int32(2, 34000)
                            ))
                            .string(2, "Software House inc.")
                            .fixed32(3, 2001)
                            .string(13, "some string")
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
    }

    @Nested
    class InternalCompatibility {

        @Test
        void emptyObject() throws IOException {
            // given
            OtherMessageRecord record = OtherMessageRecord.empty();

            // when
            OtherMessageRecord deserialized = deserialize(OtherMessageRecord::parse, OtherMessageRecord::parse, serialize(record));

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
            OtherMessageRecord deserialized = deserialize(OtherMessageRecord::parse, OtherMessageRecord::parse, serialize(record));

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
            OtherMessageRecord deserialized = deserialize(OtherMessageRecord::parse, OtherMessageRecord::parse, serialize(record));

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
                            .build()
                    )
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
}
