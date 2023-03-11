package com.protobuf.serialization;

import com.github.pcimcioch.protobuf.io.ProtobufWriter;
import com.github.pcimcioch.protobuf.io.ProtobufAssertion;
import com.protobuf.model.RepeatableOtherAddress;
import com.protobuf.model.RepeatableOtherAddressProto;
import com.protobuf.model.RepeatableOtherWork;
import com.protobuf.model.RepeatableOtherWorkProto;
import org.assertj.core.api.ThrowingConsumer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static com.github.pcimcioch.protobuf.io.ProtobufAssertion.assertProto;
import static org.assertj.core.api.Assertions.assertThat;

class RepeatableMessageSerializationTest extends SerializationTestBase {

    @Nested
    class Serialization {

        @Test
        void fullObject() throws IOException {
            // given
            RepeatableOtherWork record = new RepeatableOtherWork(
                    List.of(
                            new RepeatableOtherAddress("Street", 10),
                            new RepeatableOtherAddress("Alley", 20)
                    )
            );

            // when then
            assertProto(serialize(record))
                    .message(1, address -> address
                            .string(1, "Street")
                            .int32(2, 10)
                            .end())
                    .message(1, address -> address
                            .string(1, "Alley")
                            .int32(2, 20)
                            .end())
                    .end();
        }

        @Test
        void emptyObject() throws IOException {
            // given
            RepeatableOtherWork record = RepeatableOtherWork.empty();

            // when then
            assertProto(serialize(record))
                    .end();
        }

        @Test
        void defaultValues() throws IOException {
            // given
            RepeatableOtherWork record = new RepeatableOtherWork(
                    List.of(
                            RepeatableOtherAddress.empty(),
                            RepeatableOtherAddress.empty()
                    )
            );

            // when then
            assertProto(serialize(record))
                    .message(1, ProtobufAssertion::end)
                    .message(1, ProtobufAssertion::end)
                    .end();
        }
    }

    @Nested
    class Deserialization {

        @Test
        void emptyObject() throws IOException {
            // given when
            RepeatableOtherWork record = deserialize(new byte[0]);

            // then
            assertThat(record).isEqualTo(RepeatableOtherWork.empty());
        }

        @Test
        void fullObject() throws IOException {
            // given when
            RepeatableOtherWork record = deserialize(writer -> writer
                    .writeBytes(1, serialize(address -> address
                            .writeString(1, "Street")
                            .writeInt32(2, 10)
                    ))
                    .writeBytes(1, serialize(address -> address
                            .writeString(1, "Alley")
                            .writeInt32(2, 20)
                    ))
            );

            // then
            assertThat(record).isEqualTo(new RepeatableOtherWork(
                    List.of(
                            new RepeatableOtherAddress("Street", 10),
                            new RepeatableOtherAddress("Alley", 20)
                    )
            ));
        }

        @Test
        void unknownFields() throws IOException {
            // given when
            RepeatableOtherWork record = deserialize(writer -> writer
                    .writeInt32(10, 123)
                    .writeBytes(1, serialize(address -> address
                            .writeString(1, "Street")
                            .writeInt32(2, 10)
                    ))
                    .writeBytes(2, serialize(address -> address
                            .writeString(1, "Other")
                            .writeInt32(2, 11)
                    ))
                    .writeBytes(1, serialize(address -> address
                            .writeString(1, "Alley")
                            .writeInt32(2, 20)
                    ))
            );

            // then
            assertThat(record).isEqualTo(new RepeatableOtherWork(
                    List.of(
                            new RepeatableOtherAddress("Street", 10),
                            new RepeatableOtherAddress("Alley", 20)
                    )
            ));
        }
    }

    @Nested
    class InternalCompatibility {

        @Test
        void emptyObject() throws IOException {
            // given
            RepeatableOtherWork record = RepeatableOtherWork.empty();

            // when
            RepeatableOtherWork deserialized = deserialize(serialize(record));

            // then
            assertThat(deserialized).isEqualTo(record);
        }

        @Test
        void fullObject() throws IOException {
            // given
            RepeatableOtherWork record = new RepeatableOtherWork(
                    List.of(
                            new RepeatableOtherAddress("Street", 10),
                            new RepeatableOtherAddress("Alley", 20)
                    )
            );

            // when
            RepeatableOtherWork deserialized = deserialize(serialize(record));

            // then
            assertThat(deserialized).isEqualTo(record);
        }
    }

    @Nested
    class ExternalCompatibility {

        @Test
        void emptyObject() throws IOException {
            // given
            RepeatableOtherWork our = RepeatableOtherWork.empty();
            RepeatableOtherWorkProto proto = RepeatableOtherWorkProto.newBuilder().build();
            byte[] ourBytes = our.toByteArray();
            byte[] protoBytes = proto.toByteArray();

            // when then
            assertProtoEqual(our, RepeatableOtherWorkProto.parseFrom(ourBytes));
            assertProtoEqual(RepeatableOtherWork.parse(protoBytes), proto);
        }

        @Test
        void fullObject() throws IOException {
            // given
            RepeatableOtherWork our = new RepeatableOtherWork(
                    List.of(
                            new RepeatableOtherAddress("Street", 10),
                            new RepeatableOtherAddress("Alley", 20)
                    )
            );
            RepeatableOtherWorkProto proto = RepeatableOtherWorkProto.newBuilder()
                    .addAddresses(RepeatableOtherAddressProto.newBuilder()
                            .setStreet("Street")
                            .setNumber(10)
                            .build())
                    .addAddresses(RepeatableOtherAddressProto.newBuilder()
                            .setStreet("Alley")
                            .setNumber(20)
                            .build())
                    .build();
            byte[] ourBytes = our.toByteArray();
            byte[] protoBytes = proto.toByteArray();

            // when then
            assertProtoEqual(our, RepeatableOtherWorkProto.parseFrom(ourBytes));
            assertProtoEqual(RepeatableOtherWork.parse(protoBytes), proto);
        }

        private void assertProtoEqual(RepeatableOtherWork our, RepeatableOtherWorkProto proto) {
            assertThat(our.addresses().size()).isEqualTo(proto.getAddressesCount());
            for (int i = 0; i < our.addresses().size(); i++) {
                assertProtoEquals(our.addresses().get(i), proto.getAddresses(i));
            }
        }

        private void assertProtoEquals(RepeatableOtherAddress our, RepeatableOtherAddressProto proto) {
            assertThat(our.street()).isEqualTo(proto.getStreet());
            assertThat(our.number()).isEqualTo(proto.getNumber());
        }
    }

    private RepeatableOtherWork deserialize(ThrowingConsumer<ProtobufWriter> writerAction) throws IOException {
        return deserialize(RepeatableOtherWork::parse, RepeatableOtherWork::parse, writerAction);
    }

    private RepeatableOtherWork deserialize(byte[] data) throws IOException {
        return deserialize(RepeatableOtherWork::parse, RepeatableOtherWork::parse, data);
    }
}
