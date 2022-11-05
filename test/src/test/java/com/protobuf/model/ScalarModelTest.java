package com.protobuf.model;

import com.github.pcimcioch.protobuf.test.FullRecord;
import com.github.pcimcioch.protobuf.test.SimpleRecord;
import org.junit.jupiter.api.Test;

import static com.protobuf.Utils.ba;
import static org.assertj.core.api.Assertions.assertThat;

class ScalarModelTest {

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
    void fullRecord() {
        // when
        FullRecord record = new FullRecord(
                10d, 20f,
                30, 40L, 50, 60L, 70, 80L, 90, 100L, 110, 120L,
                true, "test", ba(1, 20, 3)
        );

        // then
        assertThat(record._double()).isEqualTo(10d);
        assertThat(record._float()).isEqualTo(20f);
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
}
