package com.protobuf.performance;

import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;

@BenchmarkMode(Mode.Throughput)
@Fork(3)
public class TestBase {
}
