package com.protobuf.performance;

import com.protobuf.performance.data.RepeatableData;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

public class ReadRepeatableTest extends TestBase {

    //@Benchmark
    public void bytes(Blackhole bh, RepeatableData data) throws Exception {
        bh.consume(data.algorithm.parseBytes());
    }

    //@Benchmark
    public void stream(Blackhole bh, RepeatableData data) throws Exception {
        bh.consume(data.algorithm.parseStream());
    }
}
