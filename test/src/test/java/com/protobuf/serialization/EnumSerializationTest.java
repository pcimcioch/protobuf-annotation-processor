package com.protobuf.serialization;

import com.protobuf.model.SimpleEnum;
import com.protobuf.model.SimpleEnumMessage;
import com.protobuf.model.SimpleEnumMessageProto;
import com.protobuf.model.SimpleEnumProto;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.protobuf.ProtobufAssertion.assertProto;
import static org.assertj.core.api.Assertions.assertThat;

class EnumSerializationTest extends SerializationTest {

    @Nested
    class Serialization {

        @Test
        void fullObject() throws IOException {
            // given
            SimpleEnumMessage record = new SimpleEnumMessage(2);

            // when then
            assertProto(serialize(record))
                    .int32(1, 2)
                    .end();
        }

        @Test
        void emptyObject() throws IOException {
            // given
            SimpleEnumMessage record = SimpleEnumMessage.builder().build();

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
            SimpleEnumMessage record = deserialize(SimpleEnumMessage::parse, SimpleEnumMessage::parse, new byte[0]);

            // then
            assertThat(record).isEqualTo(SimpleEnumMessage.builder()
                    .orderValue(0)
                    .build());
        }

        @Test
        void fullObject() throws IOException {
            // given when
            SimpleEnumMessage record = deserialize(SimpleEnumMessage::parse, SimpleEnumMessage::parse, writer -> writer
                    .int32(1, 2)
            );

            // then
            assertThat(record).isEqualTo(SimpleEnumMessage.builder()
                    .orderValue(2)
                    .build());
        }

        @Test
        void unknownEnum() throws IOException {
            // given when
            SimpleEnumMessage record = deserialize(SimpleEnumMessage::parse, SimpleEnumMessage::parse, writer -> writer
                    .int32(1, 10)
            );

            // then
            assertThat(record).isEqualTo(SimpleEnumMessage.builder()
                    .orderValue(10)
                    .build());
        }
    }

    @Nested
    class InternalCompatibility {

        @Test
        void emptyObject() throws IOException {
            // given
            SimpleEnumMessage record = SimpleEnumMessage.builder().build();

            // when
            SimpleEnumMessage deserialized = deserialize(SimpleEnumMessage::parse, SimpleEnumMessage::parse, serialize(record));

            // then
            assertThat(deserialized).isEqualTo(record);
        }

        @Test
        void fullObject() throws IOException {
            // given
            SimpleEnumMessage record = new SimpleEnumMessage(2);

            // when
            SimpleEnumMessage deserialized = deserialize(SimpleEnumMessage::parse, SimpleEnumMessage::parse, serialize(record));

            // then
            assertThat(deserialized).isEqualTo(record);
        }

        @Test
        void unknownEnum() throws IOException {
            // given
            SimpleEnumMessage record = new SimpleEnumMessage(10);

            // when
            SimpleEnumMessage deserialized = deserialize(SimpleEnumMessage::parse, SimpleEnumMessage::parse, serialize(record));

            // then
            assertThat(deserialized).isEqualTo(record);
        }
    }

    @Nested
    class ExternalCompatibility {

        @Test
        void emptyObject() throws IOException {
            // given
            SimpleEnumMessage our = SimpleEnumMessage.builder().build();
            SimpleEnumMessageProto proto = SimpleEnumMessageProto.newBuilder().build();
            byte[] ourBytes = our.toByteArray();
            byte[] protoBytes = proto.toByteArray();

            // when then
            assertProtoEqual(our, SimpleEnumMessageProto.parseFrom(ourBytes));
            assertProtoEqual(SimpleEnumMessage.parse(protoBytes), proto);
        }

        @Test
        void fullObject() throws IOException {
            // given
            SimpleEnumMessage our = SimpleEnumMessage.builder()
                    .order(SimpleEnum.FIRST)
                    .build();
            SimpleEnumMessageProto proto = SimpleEnumMessageProto.newBuilder()
                    .setOrder(SimpleEnumProto.FIRST)
                    .build();
            byte[] ourBytes = our.toByteArray();
            byte[] protoBytes = proto.toByteArray();

            // when then
            assertProtoEqual(our, SimpleEnumMessageProto.parseFrom(ourBytes));
            assertProtoEqual(SimpleEnumMessage.parse(protoBytes), proto);
        }

        @Test
        void unknownEnum() throws IOException {
            // given
            SimpleEnumMessage our = SimpleEnumMessage.builder()
                    .orderValue(10)
                    .build();
            SimpleEnumMessageProto proto = SimpleEnumMessageProto.newBuilder()
                    .setOrderValue(10)
                    .build();
            byte[] ourBytes = our.toByteArray();
            byte[] protoBytes = proto.toByteArray();

            // when then
            assertProtoEqual(our, SimpleEnumMessageProto.parseFrom(ourBytes));
            assertProtoEqual(SimpleEnumMessage.parse(protoBytes), proto);
        }

        private void assertProtoEqual(SimpleEnumMessage our, SimpleEnumMessageProto proto) {
            assertThat(our.orderValue()).isEqualTo(proto.getOrderValue());
            assertProtoEquals(our.order(), proto.getOrder());
        }

        private void assertProtoEquals(SimpleEnum our, SimpleEnumProto proto) {
            SimpleEnumProto transformed = switch (our) {
                case FIRST -> SimpleEnumProto.FIRST;
                case SECOND -> SimpleEnumProto.SECOND;
                case THIRD -> SimpleEnumProto.THIRD;
                case UNRECOGNIZED -> SimpleEnumProto.UNRECOGNIZED;
            };

            assertThat(proto).isEqualTo(transformed);
        }
    }
}
