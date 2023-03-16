package com.protobuf.model;

import com.github.pcimcioch.protobuf.dto.ByteArray;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
                .strings(null)
                .addAllStrings(null)
                .addStrings(null)
                .build();
        RepeatableScalar repeatableEmpty = RepeatableScalar.builder()
                .strings(List.of())
                .addAllStrings(List.of())
                .addStrings(null)
                .build();

        // when then
        assertThat(repeatableNull).isEqualTo(repeatableEmpty);
        assertThat(repeatableNull.doubles()).isEmpty();
        assertThat(repeatableEmpty.doubles()).isEmpty();
    }

    @Test
    void repeatableScalarNullValues() {
        // given
        RepeatableScalar.Builder builder = RepeatableScalar.builder()
                .strings(asList(null, "test"));

        // when then
        assertThatThrownBy(builder::build)
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void repeatableEnum() {
        // given
        RepeatableEnumMessage repeatableNull = RepeatableEnumMessage.builder()
                .orders(null)
                .addAllOrders(null)
                .addOrders(null)
                .ordersValue(null)
                .addAllOrdersValue(null)
                .build();
        RepeatableEnumMessage repeatableEmpty = RepeatableEnumMessage.builder()
                .orders(List.of())
                .addAllOrders(List.of())
                .addOrders(null)
                .ordersValue(List.of())
                .addAllOrdersValue(List.of())
                .build();

        // when then
        assertThat(repeatableNull).isEqualTo(repeatableEmpty);
        assertThat(repeatableNull.orders()).isEmpty();
        assertThat(repeatableNull.ordersValue()).isEmpty();
        assertThat(repeatableEmpty.orders()).isEmpty();
        assertThat(repeatableEmpty.ordersValue()).isEmpty();
    }

    @Test
    void repeatableEnumNullValues() {
        // when then
        assertThatThrownBy(() -> RepeatableEnumMessage.builder().ordersValue(asList(null, 1)))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void repeatableMessage() {
        // given
        RepeatableOtherWork repeatableNull = RepeatableOtherWork.builder()
                .addresses(null)
                .addAllAddresses(null)
                .addAddresses(null)
                .build();
        RepeatableOtherWork repeatableEmpty = RepeatableOtherWork.builder()
                .addresses(List.of())
                .addAllAddresses(List.of())
                .addAddresses(null)
                .build();

        // when then
        assertThat(repeatableNull).isEqualTo(repeatableEmpty);
        assertThat(repeatableNull.addresses()).isEmpty();
        assertThat(repeatableEmpty.addresses()).isEmpty();
    }

    @Test
    void repeatableMessageNullValues() {
        // given
        RepeatableOtherWork.Builder builder = RepeatableOtherWork.builder()
                .addresses(asList(null, null));

        // when then
        assertThatThrownBy(builder::build)
                .isInstanceOf(NullPointerException.class);
    }
}
