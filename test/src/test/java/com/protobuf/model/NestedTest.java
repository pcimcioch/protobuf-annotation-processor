package com.protobuf.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NestedTest {

    @Test
    void testNested() {
        // given when
        NestedUser.NestedAddress userAddress = new NestedUser.NestedAddress("Test Street", 15);
        NestedUser.NestedAddress workAddress = new NestedUser.NestedAddress("Factorial Street", 123);
        NestedUser.NestedWork work = NestedUser.NestedWork.builder()
                .name("Test Factory")
                .year(1999)
                .address(workAddress)
                .type(NestedUser.NestedWork.NestedWorkType.PHYSICAL)
                .build();
        NestedUser user = new NestedUser("Thomas", 30, userAddress, work);

        // then
        assertThat(user.name()).isEqualTo("Thomas");
        assertThat(user.age()).isEqualTo(30);
        assertThat(user.address().street()).isEqualTo("Test Street");
        assertThat(user.address().number()).isEqualTo(15);
        assertThat(user.work().name()).isEqualTo("Test Factory");
        assertThat(user.work().year()).isEqualTo(1999);
        assertThat(user.work().type()).isEqualTo(NestedUser.NestedWork.NestedWorkType.PHYSICAL);
        assertThat(user.work().typeValue()).isEqualTo(1);
        assertThat(user.work().address().street()).isEqualTo("Factorial Street");
        assertThat(user.work().address().number()).isEqualTo(123);
    }
}
