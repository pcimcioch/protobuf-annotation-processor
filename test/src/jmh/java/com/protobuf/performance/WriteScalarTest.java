package com.protobuf.performance;

import com.protobuf.performance.data.ScalarData;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

public class WriteScalarTest extends TestBase {

    @Benchmark
    public void bytes(Blackhole bh, ScalarData data) throws Exception {
        bh.consume(data.algorithm.serializeBytes());
    }

    @Benchmark
    public void stream(Blackhole bh, ScalarData data) throws Exception {
        bh.consume(data.algorithm.serializeStream());
    }
}
