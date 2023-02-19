package com.protobuf.performance;

import com.protobuf.performance.data.RepeatableData;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

public class WriteRepeatableTest extends TestBase {

    @Benchmark
    public void bytes(Blackhole bh, RepeatableData data) throws Exception {
        bh.consume(data.algorithm.serializeBytes());
    }

    @Benchmark
    public void stream(Blackhole bh, RepeatableData data) throws Exception {
        bh.consume(data.algorithm.serializeStream());
    }
}
