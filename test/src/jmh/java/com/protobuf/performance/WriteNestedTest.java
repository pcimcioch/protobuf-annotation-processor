package com.protobuf.performance;

import com.protobuf.performance.data.NestedData;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

public class WriteNestedTest extends TestBase {

    @Benchmark
    public void bytes(Blackhole bh, NestedData data) throws Exception {
        bh.consume(data.algorithm.serializeBytes());
    }

    @Benchmark
    public void stream(Blackhole bh, NestedData data) throws Exception {
        bh.consume(data.algorithm.serializeStream());
    }
}
