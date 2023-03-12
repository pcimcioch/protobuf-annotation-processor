package com.protobuf.performance;

import com.protobuf.performance.data.RepeatedPackedData;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

public class WriteRepeatedPackedTest extends TestBase {

    @Benchmark
    public void bytes(Blackhole bh, RepeatedPackedData data) throws Exception {
        bh.consume(data.algorithm.serializeBytes());
    }

    @Benchmark
    public void stream(Blackhole bh, RepeatedPackedData data) throws Exception {
        bh.consume(data.algorithm.serializeStream());
    }
}
