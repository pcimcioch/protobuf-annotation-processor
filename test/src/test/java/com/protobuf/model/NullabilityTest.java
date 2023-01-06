package com.protobuf.model;

import com.github.pcimcioch.protobuf.dto.ByteArray;
import org.junit.jupiter.api.Test;

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
}
