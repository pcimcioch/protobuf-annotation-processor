package com.protobuf.serialization;

import com.github.pcimcioch.protobuf.dto.ObjectList;
import com.github.pcimcioch.protobuf.io.ProtobufEncoder;
import com.github.pcimcioch.protobuf.io.UnknownField;
import com.github.pcimcioch.protobuf.io.UnknownField.BytesField;
import com.github.pcimcioch.protobuf.io.UnknownField.I32Field;
import com.github.pcimcioch.protobuf.io.UnknownField.I64Field;
import com.github.pcimcioch.protobuf.io.UnknownField.VarintField;
import com.google.protobuf.UnknownFieldSet;
import com.protobuf.model.UnknownFieldsRecord;
import com.protobuf.model.UnknownFieldsRecordProto;
import org.assertj.core.api.ThrowingConsumer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.github.pcimcioch.protobuf.io.ProtobufAssertion.assertProto;
import static com.protobuf.ByteUtils.b;
import static com.protobuf.ByteUtils.ba;
import static com.protobuf.ByteUtils.bs;
import static org.assertj.core.api.Assertions.assertThat;

class UnknownFieldsSerializationTest extends SerializationTestBase {

    @Nested
    class Serialization {

        @Test
        void noUnknownFields() throws IOException {
            // given
            UnknownFieldsRecord record = new UnknownFieldsRecord(10, ObjectList.of());

            // when then
            assertProto(serialize(record))
                    .int32(1, 10)
                    .end();
        }

        @Test
        void emptyObject() throws IOException {
            // given
            UnknownFieldsRecord record = new UnknownFieldsRecord(0, ObjectList.of());

            // when then
            assertProto(serialize(record))
                    .end();
        }

        @Test
        void unknownFields() throws IOException {
            // given
            UnknownFieldsRecord record = UnknownFieldsRecord.builder()
                    .amount(10)
                    // fields
                    .addUnknownFields(new I32Field(5, 20))
                    .addUnknownFields(new I64Field(6, 30L))
                    .addUnknownFields(new VarintField(7, 40L))
                    .addUnknownFields(new BytesField(8, ba(50, 51)))
                    // repeated
                    .addUnknownFields(new I32Field(5, 21))
                    .addUnknownFields(new I32Field(5, 22))
                    // default
                    .addUnknownFields(new I32Field(5, 0))
                    .addUnknownFields(new I64Field(6, 0L))
                    .addUnknownFields(new VarintField(7, 0L))
                    .addUnknownFields(new BytesField(8, ba()))
                    // packed
                    .addUnknownFields(new BytesField(7, ba(23, 24)))
                    .build();

            // when then
            assertProto(serialize(record))
                    .int32(1, 10)
                    // fields
                    .fixed32(5, 20)
                    .fixed64(6, 30L)
                    .int32(7, 40)
                    .bytes(8, b(50, 51))
                    // repeated
                    .fixed32(5, 21)
                    .fixed32(5, 22)
                    // default
                    .fixed32(5, 0)
                    .fixed64(6, 0L)
                    .int32(7, 0)
                    .bytes(8, b())
                    // packed
                    .int32Packed(7, 23, 24)
                    .end();
        }
    }

    @Nested
    class Deserialization {

        @Test
        void emptyObject() throws IOException {
            // when
            UnknownFieldsRecord record = deserialize(new byte[0]);

            // then
            assertThat(record).isEqualTo(UnknownFieldsRecord.empty());
        }

        @Test
        void noUnknownFields() throws IOException {
            // when
            UnknownFieldsRecord record = deserialize(writer -> writer
                    .writeInt32(1, 10)
            );

            // then
            assertThat(record).isEqualTo(UnknownFieldsRecord.builder()
                    .amount(10)
                    .build());
        }

        @Test
        void unknownFields() throws IOException {
            // when
            UnknownFieldsRecord record = deserialize(writer -> writer
                    .writeInt32(1, 10)
                    // fields
                    .writeFixed32(5, 20)
                    .writeFixed64(6, 30L)
                    .writeInt32(7, 40)
                    .writeBytes(8, b(50, 51))
                    // repeated
                    .writeFixed32(5, 21)
                    .writeFixed32(5, 22)
                    // default
                    .writeFixed32(5, 0)
                    .writeFixed64(6, 0L)
                    .writeInt32(7, 0)
                    .writeBytes(8, b())
                    // packed
                    .writeInt32Packed(7, 23, 24)
            );

            // then
            assertThat(record).isEqualTo(UnknownFieldsRecord.builder()
                    .amount(10)
                    // fields
                    .addUnknownFields(new I32Field(5, 20))
                    .addUnknownFields(new I64Field(6, 30L))
                    .addUnknownFields(new VarintField(7, 40L))
                    .addUnknownFields(new BytesField(8, ba(50, 51)))
                    // repeated
                    .addUnknownFields(new I32Field(5, 21))
                    .addUnknownFields(new I32Field(5, 22))
                    // default
                    .addUnknownFields(new I32Field(5, 0))
                    .addUnknownFields(new I64Field(6, 0L))
                    .addUnknownFields(new VarintField(7, 0L))
                    .addUnknownFields(new BytesField(8, ba()))
                    // packed
                    .addUnknownFields(new BytesField(7, ba(23, 24)))
                    .build());
        }
    }

    @Nested
    class InternalCompatibility {

        @Test
        void emptyObject() throws IOException {
            // given
            UnknownFieldsRecord record = UnknownFieldsRecord.empty();

            // when
            UnknownFieldsRecord deserialized = deserialize(serialize(record));

            // then
            assertThat(deserialized).isEqualTo(record);
        }

        @Test
        void noUnknownFields() throws IOException {
            // given
            UnknownFieldsRecord record = new UnknownFieldsRecord(10, ObjectList.of());

            // when
            UnknownFieldsRecord deserialized = deserialize(serialize(record));

            // then
            assertThat(deserialized).isEqualTo(record);
        }

        @Test
        void unknownFields() throws IOException {
            // given
            UnknownFieldsRecord record = UnknownFieldsRecord.builder()
                    .amount(10)
                    // fields
                    .addUnknownFields(new I32Field(5, 20))
                    .addUnknownFields(new I64Field(6, 30L))
                    .addUnknownFields(new VarintField(7, 40L))
                    .addUnknownFields(new BytesField(8, ba(50, 51)))
                    // repeated
                    .addUnknownFields(new I32Field(5, 21))
                    .addUnknownFields(new I32Field(5, 22))
                    // default
                    .addUnknownFields(new I32Field(5, 0))
                    .addUnknownFields(new I64Field(6, 0L))
                    .addUnknownFields(new VarintField(7, 0L))
                    .addUnknownFields(new BytesField(8, ba()))
                    // packed
                    .addUnknownFields(new BytesField(7, ba(23, 24)))
                    .build();

            // when
            UnknownFieldsRecord deserialized = deserialize(serialize(record));

            // then
            assertThat(deserialized).isEqualTo(record);
        }
    }

    @Nested
    class ExternalCompatibility {

        @Test
        void emptyObject() throws IOException {
            // given
            UnknownFieldsRecord our = UnknownFieldsRecord.empty();
            UnknownFieldsRecordProto proto = UnknownFieldsRecordProto.newBuilder().build();
            byte[] ourBytes = our.toByteArray();
            byte[] protoBytes = proto.toByteArray();

            // when then
            assertProtoEqual(our, UnknownFieldsRecordProto.parseFrom(ourBytes));
            assertProtoEqual(UnknownFieldsRecord.parse(protoBytes), proto);
        }

        @Test
        void noUnknownFields() throws IOException {
            // given
            UnknownFieldsRecord our = UnknownFieldsRecord.builder()
                    .amount(10)
                    .build();
            UnknownFieldsRecordProto proto = UnknownFieldsRecordProto.newBuilder()
                    .setAmount(10)
                    .build();
            byte[] ourBytes = our.toByteArray();
            byte[] protoBytes = proto.toByteArray();

            // when then
            assertProtoEqual(our, UnknownFieldsRecordProto.parseFrom(ourBytes));
            assertProtoEqual(UnknownFieldsRecord.parse(protoBytes), proto);
        }

        @Test
        void unknownFields() throws IOException {
            // given
            UnknownFieldsRecord our = UnknownFieldsRecord.builder()
                    .amount(10)
                    // fields
                    .addUnknownFields(new I32Field(5, 20))
                    .addUnknownFields(new I64Field(6, 30L))
                    .addUnknownFields(new VarintField(7, 40L))
                    .addUnknownFields(new BytesField(8, ba(50, 51)))
                    // repeated
                    .addUnknownFields(new I32Field(5, 21))
                    .addUnknownFields(new I32Field(5, 22))
                    // default
                    .addUnknownFields(new I32Field(5, 0))
                    .addUnknownFields(new I64Field(6, 0L))
                    .addUnknownFields(new VarintField(7, 0L))
                    .addUnknownFields(new BytesField(8, ba()))
                    // packed
                    .addUnknownFields(new BytesField(7, ba(23, 24)))
                    .build();
            UnknownFieldsRecordProto proto = UnknownFieldsRecordProto.newBuilder()
                    .setAmount(10)
                    .mergeUnknownFields(UnknownFieldSet.newBuilder()
                            // fields
                            .mergeField(5, UnknownFieldSet.Field.newBuilder().addFixed32(20).build())
                            .mergeField(6, UnknownFieldSet.Field.newBuilder().addFixed64(30L).build())
                            .mergeField(7, UnknownFieldSet.Field.newBuilder().addVarint(40L).build())
                            .mergeField(8, UnknownFieldSet.Field.newBuilder().addLengthDelimited(bs(50, 51)).build())
                            // repeated
                            .mergeField(5, UnknownFieldSet.Field.newBuilder().addFixed32(21).addFixed32(22).build())
                            // default
                            .mergeField(5, UnknownFieldSet.Field.newBuilder().addFixed32(0).build())
                            .mergeField(6, UnknownFieldSet.Field.newBuilder().addFixed64(0L).build())
                            .mergeField(7, UnknownFieldSet.Field.newBuilder().addVarint(0L).build())
                            .mergeField(8, UnknownFieldSet.Field.newBuilder().addLengthDelimited(bs()).build())
                            // packed
                            .mergeField(7, UnknownFieldSet.Field.newBuilder().addLengthDelimited(bs(23, 24)).build())
                            .build())
                    .build();
            byte[] ourBytes = our.toByteArray();
            byte[] protoBytes = proto.toByteArray();

            // when then
            assertProtoEqual(our, UnknownFieldsRecordProto.parseFrom(ourBytes));
            assertProtoEqual(UnknownFieldsRecord.parse(protoBytes), proto);
        }

        private void assertProtoEqual(UnknownFieldsRecord our, UnknownFieldsRecordProto proto) {
            assertThat(our.amount()).isEqualTo(proto.getAmount());
            assertProtoEqual(our.unknownFields(), proto.getUnknownFields());
        }

        private void assertProtoEqual(ObjectList<UnknownField> our, UnknownFieldSet proto) {
            UnknownFieldSet.Builder set = UnknownFieldSet.newBuilder();

            for (UnknownField field : our) {
                if (field instanceof I32Field f) {
                    set.mergeField(f.number(), UnknownFieldSet.Field.newBuilder().addFixed32(f.value()).build());
                }
                if (field instanceof I64Field f) {
                    set.mergeField(f.number(), UnknownFieldSet.Field.newBuilder().addFixed64(f.value()).build());
                }
                if (field instanceof VarintField f) {
                    set.mergeField(f.number(), UnknownFieldSet.Field.newBuilder().addVarint(f.value()).build());
                }
                if (field instanceof BytesField f) {
                    set.mergeField(f.number(), UnknownFieldSet.Field.newBuilder().addLengthDelimited(bs(f.value())).build());
                }
            }

            assertThat(set.build()).isEqualTo(proto);
        }
    }

    private UnknownFieldsRecord deserialize(ThrowingConsumer<ProtobufEncoder> writerAction) throws IOException {
        return deserialize(UnknownFieldsRecord::parse, UnknownFieldsRecord::parse, writerAction);
    }

    private UnknownFieldsRecord deserialize(byte[] data) throws IOException {
        return deserialize(UnknownFieldsRecord::parse, UnknownFieldsRecord::parse, data);
    }
}
