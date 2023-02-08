package com.protobuf.model;

import com.github.pcimcioch.protobuf.dto.ByteArray;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NullabilityTest {

    @Test
    void scalarTypeString() {
        // given
        FullRecord recordNull = FullRecord.builder().string(null).build();
        FullRecord recordEmpty = FullRecord.builder().string("").build();

        // when then
        assertThat(recordNull).isEqualTo(recordEmpty);
        assertThat(recordNull.string()).isEqualTo("");
        assertThat(recordEmpty.string()).isEqualTo("");
    }

    @Test
    void scalarTypeByteArray() {
        // given
        FullRecord recordNull = FullRecord.builder().bytes(null).build();
        FullRecord recordEmpty = FullRecord.builder().bytes(ByteArray.empty()).build();

        // when then
        assertThat(recordNull).isEqualTo(recordEmpty);
        assertThat(recordNull.bytes()).isEqualTo(ByteArray.empty());
        assertThat(recordEmpty.bytes()).isEqualTo(ByteArray.empty());
    }

    @Test
    void messageType() {
        // given
        OtherMessageRecord recordNull = OtherMessageRecord.builder().address(null).build();
        OtherMessageRecord recordEmpty = OtherMessageRecord.builder().address(OtherMessageAddress.empty()).build();

        // when then
        assertThat(recordNull).isEqualTo(recordEmpty);
        assertThat(recordNull.address()).isEqualTo(OtherMessageAddress.empty());
        assertThat(recordEmpty.address()).isEqualTo(OtherMessageAddress.empty());
    }

    @Test
    void enumType() {
        // given
        SimpleEnumMessage recordNull = SimpleEnumMessage.builder().order(null).build();
        SimpleEnumMessage recordEmpty = SimpleEnumMessage.builder().order(SimpleEnum.defaultValue()).build();

        // when then
        assertThat(recordNull).isEqualTo(recordEmpty);
        assertThat(recordNull.order()).isEqualTo(SimpleEnum.FIRST);
        assertThat(recordNull.orderValue()).isEqualTo(0);
        assertThat(recordEmpty.order()).isEqualTo(SimpleEnum.FIRST);
        assertThat(recordEmpty.orderValue()).isEqualTo(0);
    }

    @Test
    void repeatableScalar() {
        // given
        RepeatableScalar repeatableNull = RepeatableScalar.builder()
                .double_(null)
                .addAllDouble_(null)
                .addDouble_(null)
                .build();
        RepeatableScalar repeatableEmpty = RepeatableScalar.builder()
                .double_(List.of())
                .addAllDouble_(List.of())
                .addDouble_(null)
                .build();

        // when then
        assertThat(repeatableNull).isEqualTo(repeatableEmpty);
        assertThat(repeatableNull.double_()).isEmpty();
        assertThat(repeatableEmpty.double_()).isEmpty();
    }

    @Test
    void repeatableEnum() {
        // given
        RepeatableEnumMessage repeatableNull = RepeatableEnumMessage.builder()
                .order(null)
                .addAllOrder(null)
                .addOrder(null)
                .orderValue(null)
                .addAllOrderValue(null)
                .addOrderValue(null)
                .build();
        RepeatableEnumMessage repeatableEmpty = RepeatableEnumMessage.builder()
                .order(List.of())
                .addAllOrder(List.of())
                .addOrder(null)
                .orderValue(List.of())
                .addAllOrderValue(List.of())
                .addOrderValue(null)
                .build();

        // when then
        assertThat(repeatableNull).isEqualTo(repeatableEmpty);
        assertThat(repeatableNull.order()).isEmpty();
        assertThat(repeatableNull.orderValue()).isEmpty();
        assertThat(repeatableEmpty.order()).isEmpty();
        assertThat(repeatableEmpty.orderValue()).isEmpty();
    }

    @Test
    void repeatableMessage() {
        // given
        RepeatableOtherWork repeatableNull = RepeatableOtherWork.builder()
                .address(null)
                .addAllAddress(null)
                .addAddress(null)
                .build();
        RepeatableOtherWork repeatableEmpty = RepeatableOtherWork.builder()
                .address(List.of())
                .addAllAddress(List.of())
                .addAddress(null)
                .build();

        // when then
        assertThat(repeatableNull).isEqualTo(repeatableEmpty);
        assertThat(repeatableNull.address()).isEmpty();
        assertThat(repeatableEmpty.address()).isEmpty();
    }
}
