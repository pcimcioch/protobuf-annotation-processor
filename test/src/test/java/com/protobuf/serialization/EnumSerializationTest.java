package com.protobuf.serialization;

import com.github.pcimcioch.protobuf.io.ProtobufWriter;
import com.protobuf.model.SimpleEnum;
import com.protobuf.model.SimpleEnumMessage;
import com.protobuf.model.SimpleEnumMessageProto;
import com.protobuf.model.SimpleEnumProto;
import org.assertj.core.api.ThrowingConsumer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.github.pcimcioch.protobuf.io.ProtobufAssertion.assertProto;
import static org.assertj.core.api.Assertions.assertThat;

class EnumSerializationTest extends SerializationTestBase {

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
            SimpleEnumMessage record = SimpleEnumMessage.empty();

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
            SimpleEnumMessage record = deserialize(new byte[0]);

            // then
            assertThat(record).isEqualTo(SimpleEnumMessage.builder()
                    .orderValue(0)
                    .build());
        }

        @Test
        void fullObject() throws IOException {
            // given when
            SimpleEnumMessage record = deserialize(writer -> writer
                    .writeInt32(1, 2)
            );

            // then
            assertThat(record).isEqualTo(SimpleEnumMessage.builder()
                    .orderValue(2)
                    .build());
        }

        @Test
        void unknownEnum() throws IOException {
            // given when
            SimpleEnumMessage record = deserialize(writer -> writer
                    .writeInt32(1, 10)
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
            SimpleEnumMessage record = SimpleEnumMessage.empty();

            // when
            SimpleEnumMessage deserialized = deserialize(serialize(record));

            // then
            assertThat(deserialized).isEqualTo(record);
        }

        @Test
        void fullObject() throws IOException {
            // given
            SimpleEnumMessage record = new SimpleEnumMessage(2);

            // when
            SimpleEnumMessage deserialized = deserialize(serialize(record));

            // then
            assertThat(deserialized).isEqualTo(record);
        }

        @Test
        void unknownEnum() throws IOException {
            // given
            SimpleEnumMessage record = new SimpleEnumMessage(10);

            // when
            SimpleEnumMessage deserialized = deserialize(serialize(record));

            // then
            assertThat(deserialized).isEqualTo(record);
        }
    }

    @Nested
    class ExternalCompatibility {

        @Test
        void emptyObject() throws IOException {
            // given
            SimpleEnumMessage our = SimpleEnumMessage.empty();
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
            assertThat(our.order()).isEqualTo(convert(proto.getOrder()));
        }

        private SimpleEnum convert(SimpleEnumProto order) {
            return switch (order) {
                case FIRST -> SimpleEnum.FIRST;
                case SECOND -> SimpleEnum.SECOND;
                case THIRD -> SimpleEnum.THIRD;
                case UNRECOGNIZED -> SimpleEnum.UNRECOGNIZED;
            };
        }
    }

    private SimpleEnumMessage deserialize(ThrowingConsumer<ProtobufWriter> writerAction) throws IOException {
        return deserialize(SimpleEnumMessage::parse, SimpleEnumMessage::parse, writerAction);
    }

    private SimpleEnumMessage deserialize(byte[] data) throws IOException {
        return deserialize(SimpleEnumMessage::parse, SimpleEnumMessage::parse, data);
    }
}
