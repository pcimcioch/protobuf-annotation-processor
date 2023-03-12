package com.protobuf.performance;

import com.protobuf.performance.data.RepeatedData;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

public class ReadRepeatedTest extends TestBase {

    @Benchmark
    public void bytes(Blackhole bh, RepeatedData data) throws Exception {
        bh.consume(data.algorithm.parseBytes());
    }

    @Benchmark
    public void stream(Blackhole bh, RepeatedData data) throws Exception {
        bh.consume(data.algorithm.parseStream());
    }
}
