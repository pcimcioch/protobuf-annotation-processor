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

import static com.protobuf.ProtobufAssertion.assertProto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class MessageSerializationTest {

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
            assertProto(record)
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
        void emptyObject() {
            // TODO implement
        }

        @Test
        void partialObject() {
            // TODO implement
        }
    }

    @Nested
    class Deserialization {

        @Test
        void emptyObject() {
            // TODO implement
        }

        @Test
        void fullObject() {
            // TODO implement
        }

        @Test
        void partialObject() {
            // TODO implement
        }

        @Test
        void fullObjectReverseOrder() {
            // TODO implement
        }

        @Test
        void unknownFields() {
            // TODO implement
        }
    }

    @Nested
    class InternalCompatibility {

        @Test
        void emptyObject() {
            // TODO implement
        }

        @Test
        void fullObject() {
            // TODO implement
        }

        @Test
        void partialObject() {
            // TODO implement
        }
    }

    @Nested
    class ExternalCompatibility {

        @Test
        void emptyObject() {
            // TODO implement
        }

        @Test
        void fullObject() {
            // TODO implement
        }

        @Test
        void partialObject() {
            // TODO implement
        }

        private void assertProtoEqual(OtherMessageRecord our, OtherMessageRecordProto proto) {
            assertThat(our.name()).isEqualTo(proto.getName());
            assertThat(our.age()).isEqualTo(proto.getAge());
            assertProtoEqual(our.address(), proto.getAddress());
            assertProtoEqual(our.work(), proto.getWork());
        }

        private void assertProtoEqual(OtherMessageWork our, OtherMessageWorkProto proto) {
            if (our == null) {
                assertThat(proto).isNull();
                return;
            }
            if (proto == null) {
                fail("Expected null our");
                return;
            }

            assertThat(our.name()).isEqualTo(proto.getName());
            assertThat(our.year()).isEqualTo(proto.getYear());
            assertProtoEqual(our.address(), proto.getAddress());
        }

        private void assertProtoEqual(OtherMessageAddress our, OtherMessageAddressProto proto) {
            if (our == null) {
                assertThat(proto).isNull();
                return;
            }
            if (proto == null) {
                fail("Expected null our");
                return;
            }

            assertThat(our.street()).isEqualTo(proto.getStreet());
            assertThat(our.number()).isEqualTo(proto.getNumber());
        }
    }
}
