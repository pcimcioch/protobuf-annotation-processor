package com.protobuf.serialization;

import com.protobuf.model.SelfReference;
import com.protobuf.model.SelfReferenceProto;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.protobuf.ProtobufAssertion.assertProto;
import static org.assertj.core.api.Assertions.assertThat;

class SelfReferenceSerializationTest extends SerializationTestBase {

    @Nested
    class Serialization {

        @Test
        void fullObject() throws IOException {
            // given
            SelfReference record = new SelfReference(10, new SelfReference(20, new SelfReference(30, null)));

            // when then
            assertProto(serialize(record))
                    .int32(1, 10)
                    .message(2, second -> second
                            .int32(1, 20)
                            .message(2, third -> third
                                    .int32(1, 30)
                                    .end())
                            .end())
                    .end();
        }

        @Test
        void emptyObject() throws IOException {
            // given
            SelfReference record = SelfReference.empty();

            // when then
            assertProto(serialize(record))
                    .end();
        }
    }

    @Nested
    class Deserialization {

        @Test
        void emptyObject() throws IOException {
            // given when
            SelfReference record = deserialize(SelfReference::parse, SelfReference::parse, new byte[0]);

            // then
            assertThat(record).isEqualTo(SelfReference.empty());
        }

        @Test
        void fullObject() throws IOException {
            // given when
            SelfReference record = deserialize(SelfReference::parse, SelfReference::parse, writer -> writer
                    .int32(1, 10)
                    .bytes(2, deserialize(second -> second
                            .int32(1, 20)
                            .bytes(2, deserialize(third -> third
                                    .int32(1, 30)
                            ))
                    ))
            );

            // then
            assertThat(record).isEqualTo(new SelfReference(
                    10, new SelfReference(
                    20, new SelfReference(
                    30, null)))
            );
        }
    }

    @Nested
    class InternalCompatibility {

        @Test
        void emptyObject() throws IOException {
            // given
            SelfReference record = SelfReference.empty();

            // when
            SelfReference deserialized = deserialize(SelfReference::parse, SelfReference::parse, serialize(record));

            // then
            assertThat(deserialized).isEqualTo(record);
        }

        @Test
        void fullObject() throws IOException {
            // given
            SelfReference record = new SelfReference(10, new SelfReference(20, new SelfReference(30, null)));

            // when
            SelfReference deserialized = deserialize(SelfReference::parse, SelfReference::parse, serialize(record));

            // then
            assertThat(deserialized).isEqualTo(record);
        }
    }

    @Nested
    class ExternalCompatibility {

        @Test
        void emptyObject() throws IOException {
            // given
            SelfReference our = SelfReference.empty();
            SelfReferenceProto proto = SelfReferenceProto.newBuilder().build();
            byte[] ourBytes = our.toByteArray();
            byte[] protoBytes = proto.toByteArray();

            // when then
            assertProtoEqual(our, SelfReferenceProto.parseFrom(ourBytes));
            assertProtoEqual(SelfReference.parse(protoBytes), proto);
        }

        @Test
        void fullObject() throws IOException {
            // given
            SelfReference our = new SelfReference(10, new SelfReference(20 ,new SelfReference(30, null)));
            SelfReferenceProto proto = SelfReferenceProto.newBuilder()
                    .setValue(10)
                    .setNext(SelfReferenceProto.newBuilder()
                            .setValue(20)
                            .setNext(SelfReferenceProto.newBuilder()
                                    .setValue(10)
                                    .build())
                            .build())
                    .build();
            byte[] ourBytes = our.toByteArray();
            byte[] protoBytes = proto.toByteArray();

            // when then
            assertProtoEqual(our, SelfReferenceProto.parseFrom(ourBytes));
            assertProtoEqual(SelfReference.parse(protoBytes), proto);
        }

        private void assertProtoEqual(SelfReference our, SelfReferenceProto proto) {
            if (our.isEmpty()) {
                assertThat(proto).isEqualTo(SelfReferenceProto.newBuilder().build());
                return;
            }

            assertThat(our.value()).isEqualTo(proto.getValue());
            assertProtoEqual(our.next(), proto.getNext());
        }
    }
}
