package com.protobuf.performance;

import com.protobuf.performance.data.ScalarData;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

public class ReadScalarTest extends TestBase {

    //@Benchmark
    public void bytes(Blackhole bh, ScalarData data) throws Exception {
        bh.consume(data.algorithm.parseBytes());
    }

    //@Benchmark
    public void stream(Blackhole bh, ScalarData data) throws Exception {
        bh.consume(data.algorithm.parseStream());
    }
}
