package com.protobuf.model;

import org.junit.jupiter.api.Test;

import static com.protobuf.ByteUtils.ba;
import static org.assertj.core.api.Assertions.assertThat;

class MergeTest {

    @Test
    void mergeAll() {
        // given
        FullRecord base = new FullRecord(
                10d, 20f,
                30, 40L, 50, 60L, 70, 80L, 90, 100L, 110, 120L,
                false, "test", ba(1, 20, 3)
        );
        FullRecord toMerge = new FullRecord(
                11d, 21f,
                31, 41L, 51, 61L, 71, 81L, 91, 101L, 111, 121L,
                true, "other", ba(5, 10)
        );

        // when
        FullRecord merged = base.merge(toMerge);

        // then
        assertThat(merged).isEqualTo(toMerge);
    }

    @Test
    void mergeSome() {
        // given
        FullRecord base = new FullRecord(
                10d, 20f,
                30, 40L, 50, 60L, 70, 80L, 90, 100L, 110, 120L,
                true, "test", ba(1, 20, 3)
        );
        FullRecord toMerge = new FullRecord(
                11d, 0f,
                0, 41L, 51, 0L, 71, 0L, 0, 101L, 111, 0L,
                false, "", ba(5, 10)
        );

        // when
        FullRecord merged = base.merge(toMerge);

        // then
        assertThat(merged).isEqualTo(new FullRecord(
                11d, 20f,
                30, 41L, 51, 60L, 71, 80L, 90, 101L, 111, 120L,
                true, "test", ba(5, 10)
        ));
    }

    @Test
    void mergeNone() {
        // given
        FullRecord base = new FullRecord(
                10d, 20f,
                30, 40L, 50, 60L, 70, 80L, 90, 100L, 110, 120L,
                true, "test", ba(1, 20, 3)
        );
        FullRecord toMerge = FullRecord.empty();

        // when
        FullRecord merged = base.merge(toMerge);

        // then
        assertThat(merged).isEqualTo(base);
    }

    @Test
    void mergeSubMessage() {
        // given
        OtherMessageRecord base = new OtherMessageRecord(
                "test",
                10,
                new OtherMessageAddress("", 0),
                new OtherMessageWork(
                        new OtherMessageAddress("Test", 20),
                        "Work",
                        1999
                )
        );
        OtherMessageRecord toMerge = new OtherMessageRecord(
                "test2",
                0,
                new OtherMessageAddress("Sun Street", 100),
                new OtherMessageWork(
                        new OtherMessageAddress("", 200),
                        "",
                        2022
                )
        );

        // when
        OtherMessageRecord merged = base.merge(toMerge);

        // then
        assertThat(merged).isEqualTo(new OtherMessageRecord(
                "test2",
                10,
                new OtherMessageAddress("Sun Street", 100),
                new OtherMessageWork(
                        new OtherMessageAddress("Test", 200),
                        "Work",
                        2022
                )
        ));
    }
}
