package com.protobuf.performance;

import com.protobuf.performance.data.RepeatedData;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

public class WriteRepeatedTest extends TestBase {

    @Benchmark
    public void bytes(Blackhole bh, RepeatedData data) throws Exception {
        bh.consume(data.algorithm.serializeBytes());
    }

    @Benchmark
    public void stream(Blackhole bh, RepeatedData data) throws Exception {
        bh.consume(data.algorithm.serializeStream());
    }
}
