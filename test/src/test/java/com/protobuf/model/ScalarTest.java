package com.protobuf.model;

import org.junit.jupiter.api.Test;

import static com.protobuf.ByteUtils.ba;
import static org.assertj.core.api.Assertions.assertThat;

class ScalarTest {

    @Test
    void simpleRecord() {
        // when
        SimpleRecord record = new SimpleRecord(10, 20.0, 30.0);

        // then
        assertThat(record.amount()).isEqualTo(10);
        assertThat(record.latitude()).isEqualTo(20.0);
        assertThat(record.longitude()).isEqualTo(30.0);
    }

    @Test
    void simpleRecordBuilder() {
        // when
        SimpleRecord record = SimpleRecord.builder()
                .amount(10)
                .latitude(20.0)
                .longitude(30.0)
                .build();

        // then
        assertThat(record.amount()).isEqualTo(10);
        assertThat(record.latitude()).isEqualTo(20.0);
        assertThat(record.longitude()).isEqualTo(30.0);
    }

    @Test
    void simpleRecordDefault() {
        // when
        SimpleRecord record = SimpleRecord.empty();

        // then
        assertThat(record.amount()).isEqualTo(0);
        assertThat(record.latitude()).isEqualTo(0d);
        assertThat(record.longitude()).isEqualTo(0d);
    }

    @Test
    void fullRecord() {
        // when
        FullRecord record = new FullRecord(
                10d, 20f,
                30, 40L, 50, 60L, 70, 80L, 90, 100L, 110, 120L,
                true, "test", ba(1, 20, 3)
        );

        // then
        assertThat(record.double_()).isEqualTo(10d);
        assertThat(record.float_()).isEqualTo(20f);
        assertThat(record.int32()).isEqualTo(30);
        assertThat(record.int64()).isEqualTo(40L);
        assertThat(record.uint32()).isEqualTo(50);
        assertThat(record.uint64()).isEqualTo(60L);
        assertThat(record.sint32()).isEqualTo(70);
        assertThat(record.sint64()).isEqualTo(80L);
        assertThat(record.fixed32()).isEqualTo(90);
        assertThat(record.fixed64()).isEqualTo(100L);
        assertThat(record.sfixed32()).isEqualTo(110);
        assertThat(record.sfixed64()).isEqualTo(120L);
        assertThat(record.bool()).isTrue();
        assertThat(record.string()).isEqualTo("test");
        assertThat(record.bytes()).isEqualTo(ba(1, 20, 3));
    }

    @Test
    void fullRecordBuilder() {
        // when
        FullRecord record = FullRecord.builder()
                .double_(10d)
                .float_(20f)
                .int32(30)
                .int64(40L)
                .uint32(50)
                .uint64(60L)
                .sint32(70)
                .sint64(80L)
                .fixed32(90)
                .fixed64(100L)
                .sfixed32(110)
                .sfixed64(120L)
                .bool(true)
                .string("test")
                .bytes(ba(1, 20, 3))
                .build();

        // then
        assertThat(record.double_()).isEqualTo(10d);
        assertThat(record.float_()).isEqualTo(20f);
        assertThat(record.int32()).isEqualTo(30);
        assertThat(record.int64()).isEqualTo(40L);
        assertThat(record.uint32()).isEqualTo(50);
        assertThat(record.uint64()).isEqualTo(60L);
        assertThat(record.sint32()).isEqualTo(70);
        assertThat(record.sint64()).isEqualTo(80L);
        assertThat(record.fixed32()).isEqualTo(90);
        assertThat(record.fixed64()).isEqualTo(100L);
        assertThat(record.sfixed32()).isEqualTo(110);
        assertThat(record.sfixed64()).isEqualTo(120L);
        assertThat(record.bool()).isTrue();
        assertThat(record.string()).isEqualTo("test");
        assertThat(record.bytes()).isEqualTo(ba(1, 20, 3));
    }

    @Test
    void fullRecordDefault() {
        // when
        FullRecord record = FullRecord.empty();

        // then
        assertThat(record.double_()).isEqualTo(0d);
        assertThat(record.float_()).isEqualTo(0f);
        assertThat(record.int32()).isEqualTo(0);
        assertThat(record.int64()).isEqualTo(0L);
        assertThat(record.uint32()).isEqualTo(0);
        assertThat(record.uint64()).isEqualTo(0L);
        assertThat(record.sint32()).isEqualTo(0);
        assertThat(record.sint64()).isEqualTo(0L);
        assertThat(record.fixed32()).isEqualTo(0);
        assertThat(record.fixed64()).isEqualTo(0L);
        assertThat(record.sfixed32()).isEqualTo(0);
        assertThat(record.sfixed64()).isEqualTo(0L);
        assertThat(record.bool()).isFalse();
        assertThat(record.string()).isEqualTo("");
        assertThat(record.bytes()).isEqualTo(ba());
    }
}
