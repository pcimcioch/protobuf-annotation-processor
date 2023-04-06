package com.protobuf.model;

import com.github.pcimcioch.protobuf.io.UnknownField.BytesField;
import com.github.pcimcioch.protobuf.io.UnknownField.I32Field;
import com.github.pcimcioch.protobuf.io.UnknownField.I64Field;
import com.github.pcimcioch.protobuf.io.UnknownField.VarintField;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.protobuf.ByteUtils.ba;
import static org.assertj.core.api.Assertions.assertThat;

class UnknownFieldsRecordTest {

    @Test
    void defaultValues() {
        // when
        UnknownFieldsRecord model = UnknownFieldsRecord.empty();

        // then
        assertThat(model.amount()).isEqualTo(0);
        assertThat(model.unknownFields()).isEmpty();
        assertThat(model.protobufSize()).isEqualTo(0);
    }

    @Test
    void unknownValues() {
        // when
        UnknownFieldsRecord model = UnknownFieldsRecord.builder()
                .amount(10)
                .addUnknownFields(new I32Field(10, 20))
                .addUnknownFields(new I64Field(11, 30L))
                .addAllUnknownFields(List.of(
                        new VarintField(12, 40L),
                        new BytesField(13, ba(50, 60, 70))
                ))
                .build();

        // then
        assertThat(model.amount()).isEqualTo(10);
        assertThat(model.unknownFields()).containsExactly(
                new I32Field(10, 20),
                new I64Field(11, 30L),
                new VarintField(12, 40L),
                new BytesField(13, ba(50, 60, 70))
        );
        assertThat(model.protobufSize()).isEqualTo(23);
    }
}
