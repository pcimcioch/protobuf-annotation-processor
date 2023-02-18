package com.protobuf.performance;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

// TODO Performance improvement: when reading nested object (message, string, bytearray), do not copy any arrays
// TODO Performance improvement: when writing nested object (message, string, bytearray), do not copy any arrays
// TODO Performance improvement: move ByteArray to io Package and make its "data" field package private so io classes can access it directly
// TODO Performance improvement: currently we have one ProtobufInput and ProtobufOutput that operate on the Streams. Maybe we should have separate implementations for streams and separate for raw byte[]
// TODO Performance improvement: do not assert tag wire type on reading. If wire type is incorrect just treat this entry as unknown
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Fork(3)
@Measurement(iterations = 3)
@Warmup(iterations = 3)
public class PerformanceTest {

    @Benchmark
    public void write(Blackhole bh, Model model) throws Exception {
        bh.consume(model.algorithm.serialize());
    }

    @Benchmark
    public void read(Blackhole bh, Model model) throws Exception {
        bh.consume(model.algorithm.parse());
    }

    @State(Scope.Benchmark)
    public static class Model {
        @Param({"OUR", "PROTO"})
        public Algorithm.Type type;
        @Param({"SCALAR", "REPEATED", "NESTED"})
        public Algorithm.Payload payload;

        @Param({"ARRAY", "IO_STREAM"})
        public Algorithm.IO io;

        public Algorithm algorithm;

        @Setup(Level.Trial)
        public void setUp() {
            this.algorithm = Algorithm.from(type, payload, io);
        }
    }
}
