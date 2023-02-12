package com.protobuf.serialization;

import com.github.pcimcioch.protobuf.io.ProtobufWriter;
import com.protobuf.model.RepeatableEnum;
import com.protobuf.model.RepeatableEnumMessage;
import com.protobuf.model.RepeatableEnumMessageProto;
import com.protobuf.model.RepeatableEnumProto;
import com.protobuf.model.SimpleEnum;
import com.protobuf.model.SimpleEnumMessage;
import com.protobuf.model.SimpleEnumMessageProto;
import com.protobuf.model.SimpleEnumProto;
import org.assertj.core.api.ThrowingConsumer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static com.protobuf.ProtobufAssertion.assertProto;
import static org.assertj.core.api.Assertions.assertThat;

class RepeatableEnumSerializationTest extends SerializationTestBase {

    @Nested
    class Serialization {

        @Test
        void fullObject() throws IOException {
            // given
            RepeatableEnumMessage record = new RepeatableEnumMessage(List.of(2, 1));

            // when then
            assertProto(serialize(record))
                    .int32(1, 2)
                    .int32(1, 1)
                    .end();
        }

        @Test
        void emptyObject() throws IOException {
            // given
            RepeatableEnumMessage record = RepeatableEnumMessage.empty();

            // when then
            assertProto(serialize(record))
                    .end();
        }

        @Test
        void defaultValue() throws IOException {
            // given
            RepeatableEnumMessage record = new RepeatableEnumMessage(List.of(0));

            // when then
            assertProto(serialize(record))
                    .int32(1, 0)
                    .end();
        }
    }

    @Nested
    class Deserialization {

        @Test
        void emptyObject() throws IOException {
            // given when
            RepeatableEnumMessage record = deserialize(new byte[0]);

            // then
            assertThat(record).isEqualTo(RepeatableEnumMessage.builder()
                    .ordersValue(List.of())
                    .build());
        }

        @Test
        void fullObject() throws IOException {
            // given when
            RepeatableEnumMessage record = deserialize(writer -> writer
                    .int32(1, List.of(2, 0))
            );

            // then
            assertThat(record).isEqualTo(RepeatableEnumMessage.builder()
                    .ordersValue(List.of(2, 0))
                    .build());
        }

        @Test
        void unknownEnum() throws IOException {
            // given when
            RepeatableEnumMessage record = deserialize(writer -> writer
                    .int32(1, 10)
            );

            // then
            assertThat(record).isEqualTo(RepeatableEnumMessage.builder()
                    .ordersValue(List.of(10))
                    .build());
        }
    }

    @Nested
    class InternalCompatibility {

        @Test
        void emptyObject() throws IOException {
            // given
            RepeatableEnumMessage record = RepeatableEnumMessage.empty();

            // when
            RepeatableEnumMessage deserialized = deserialize(serialize(record));

            // then
            assertThat(deserialized).isEqualTo(record);
        }

        @Test
        void fullObject() throws IOException {
            // given
            RepeatableEnumMessage record = new RepeatableEnumMessage(List.of(2, 0));

            // when
            RepeatableEnumMessage deserialized = deserialize(serialize(record));

            // then
            assertThat(deserialized).isEqualTo(record);
        }

        @Test
        void unknownEnum() throws IOException {
            // given
            RepeatableEnumMessage record = new RepeatableEnumMessage(List.of(10));

            // when
            RepeatableEnumMessage deserialized = deserialize(serialize(record));

            // then
            assertThat(deserialized).isEqualTo(record);
        }
    }

    @Nested
    class ExternalCompatibility {

        @Test
        void emptyObject() throws IOException {
            // given
            RepeatableEnumMessage our = RepeatableEnumMessage.empty();
            RepeatableEnumMessageProto proto = RepeatableEnumMessageProto.newBuilder().build();
            byte[] ourBytes = our.toByteArray();
            byte[] protoBytes = proto.toByteArray();

            // when then
            assertProtoEqual(our, RepeatableEnumMessageProto.parseFrom(ourBytes));
            assertProtoEqual(RepeatableEnumMessage.parse(protoBytes), proto);
        }

        @Test
        void fullObject() throws IOException {
            // given
            RepeatableEnumMessage our = RepeatableEnumMessage.builder()
                    .orders(List.of(RepeatableEnum.FIRST, RepeatableEnum.THIRD))
                    .build();
            RepeatableEnumMessageProto proto = RepeatableEnumMessageProto.newBuilder()
                    .addAllOrders(List.of(RepeatableEnumProto.FIRST_, RepeatableEnumProto.THIRD_))
                    .build();
            byte[] ourBytes = our.toByteArray();
            byte[] protoBytes = proto.toByteArray();

            // when then
            assertProtoEqual(our, RepeatableEnumMessageProto.parseFrom(ourBytes));
            assertProtoEqual(RepeatableEnumMessage.parse(protoBytes), proto);
        }

        @Test
        void unknownEnum() throws IOException {
            // given
            RepeatableEnumMessage our = RepeatableEnumMessage.builder()
                    .ordersValue(List.of(10))
                    .build();
            RepeatableEnumMessageProto proto = RepeatableEnumMessageProto.newBuilder()
                    .addAllOrdersValue(List.of(10))
                    .build();
            byte[] ourBytes = our.toByteArray();
            byte[] protoBytes = proto.toByteArray();

            // when then
            assertProtoEqual(our, RepeatableEnumMessageProto.parseFrom(ourBytes));
            assertProtoEqual(RepeatableEnumMessage.parse(protoBytes), proto);
        }

        private void assertProtoEqual(RepeatableEnumMessage our, RepeatableEnumMessageProto proto) {
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

    private RepeatableEnumMessage deserialize(ThrowingConsumer<ProtobufWriter> writerAction) throws IOException {
        return deserialize(RepeatableEnumMessage::parse, RepeatableEnumMessage::parse, writerAction);
    }

    private RepeatableEnumMessage deserialize(byte[] data) throws IOException {
        return deserialize(RepeatableEnumMessage::parse, RepeatableEnumMessage::parse, data);
    }
}
