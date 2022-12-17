package com.protobuf.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OtherMessageTest {

    @Test
    void otherMessage() {
        // when
        OtherMessageRecord record = new OtherMessageRecord(
                "Tomas",
                40,
                new OtherMessageAddress("Java St.", 12),
                new OtherMessageWork(
                        new OtherMessageAddress("Test Al.", 34000),
                        "Software House inc.",
                        2001
                )
        );

        // then
        assertThat(record.name()).isEqualTo("Tomas");
        assertThat(record.age()).isEqualTo(40);
        assertThat(record.address().street()).isEqualTo("Java St.");
        assertThat(record.address().number()).isEqualTo(12);
        assertThat(record.work().address().street()).isEqualTo("Test Al.");
        assertThat(record.work().address().number()).isEqualTo(34000);
        assertThat(record.work().name()).isEqualTo("Software House inc.");
        assertThat(record.work().year()).isEqualTo(2001);
    }

    @Test
    void otherMessageBuilder() {
        // when
        OtherMessageRecord record = OtherMessageRecord.builder()
                .name("Tomas")
                .age(40)
                .address(OtherMessageAddress.builder()
                        .street("Java St.")
                        .number(12)
                        .build())
                .work(OtherMessageWork.builder()
                        .name("Software House inc.")
                        .year(2001)
                        .address(OtherMessageAddress.builder()
                                .street("Test Al.")
                                .number(34000)
                                .build())
                        .build())
                .build();

        // then
        assertThat(record.name()).isEqualTo("Tomas");
        assertThat(record.age()).isEqualTo(40);
        assertThat(record.address().street()).isEqualTo("Java St.");
        assertThat(record.address().number()).isEqualTo(12);
        assertThat(record.work().address().street()).isEqualTo("Test Al.");
        assertThat(record.work().address().number()).isEqualTo(34000);
        assertThat(record.work().name()).isEqualTo("Software House inc.");
        assertThat(record.work().year()).isEqualTo(2001);
    }

    @Test
    void otherMessageDefault() {
        // when
        OtherMessageRecord record = OtherMessageRecord.empty();

        // then
        assertThat(record.name()).isEqualTo("");
        assertThat(record.age()).isEqualTo(0);
        assertThat(record.address().street()).isEqualTo("");
        assertThat(record.address().number()).isEqualTo(0);
        assertThat(record.work().address().street()).isEqualTo("");
        assertThat(record.work().address().number()).isEqualTo(0);
        assertThat(record.work().name()).isEqualTo("");
        assertThat(record.work().year()).isEqualTo(0);
    }
}
