package com.protobuf.performance;

import com.google.protobuf.InvalidProtocolBufferException;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.stream.IntStream.rangeClosed;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Fork(3)
@Measurement(iterations = 3)
@Warmup(iterations = 3)
public class PerformanceTest {

    @Benchmark
    public void write(Blackhole bh, Model model) throws Exception {
        bh.consume(model.algorithm.toByteArray());
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

        public Algorithm algorithm;

        @Setup(Level.Trial)
        public void setUp() throws IOException {
            this.algorithm = Algorithm.from(type, payload);
        }
    }

    public sealed interface Algorithm {
        Object parse() throws Exception;

        byte[] toByteArray() throws Exception;

        enum Type {OUR, PROTO}

        enum Payload {SCALAR, REPEATED, NESTED}

        static Algorithm from(Type type, Payload payload) throws IOException {
            return switch (type) {
                case OUR -> switch (payload) {
                    case SCALAR -> new OurScalarAlgorithm();
                    case REPEATED -> new OurRepeatedScalarAlgorithm();
                    case NESTED -> new OurNestedAlgorithm();
                };
                case PROTO -> switch (payload) {
                    case SCALAR -> new ProtoScalarAlgorithm();
                    case REPEATED -> new ProtoRepeatedScalarAlgorithm();
                    case NESTED -> new ProtoNestedAlgorithm();
                };
            };
        }
    }

    private static final class OurScalarAlgorithm implements Algorithm {
        private final Scalar data;
        private final byte[] bytes;

        private OurScalarAlgorithm() throws IOException {
            this.data = new Scalar(10d, 20f, 30, 40L, 50, 60L, 70, 80L, 90, 100L, 110, 120L);
            this.bytes = this.data.toByteArray();
        }

        @Override
        public Scalar parse() throws IOException {
            return Scalar.parse(bytes);
        }

        @Override
        public byte[] toByteArray() throws IOException {
            return data.toByteArray();
        }
    }

    private static final class OurRepeatedScalarAlgorithm implements Algorithm {
        private final RepeatedScalar data;
        private final byte[] bytes;

        private OurRepeatedScalarAlgorithm() throws IOException {
            this.data = RepeatedScalar.builder()
                    .double_(doubleList(10d))
                    .float_(floatList(20f))
                    .int32(intList(30))
                    .int64(longList(40L))
                    .uint32(intList(50))
                    .uint64(longList(60L))
                    .sint32(intList(70))
                    .sint64(longList(80L))
                    .fixed32(intList(90))
                    .fixed64(longList(100L))
                    .sfixed32(intList(110))
                    .sfixed64(longList(120L))
                    .build();

            this.bytes = this.data.toByteArray();
        }

        @Override
        public RepeatedScalar parse() throws IOException {
            return RepeatedScalar.parse(bytes);
        }

        @Override
        public byte[] toByteArray() throws IOException {
            return data.toByteArray();
        }
    }

    private static final class OurNestedAlgorithm implements Algorithm {
        private final Data data;
        private final byte[] bytes;

        private OurNestedAlgorithm() throws IOException {
            this.data = new Data("1.0.0", "This is test data", 1676725565L, chunks());
            this.bytes = this.data.toByteArray();
        }

        private static List<Chunk> chunks() {
            List<Chunk> chunks = new ArrayList<>(10);
            for (int i = 0; i < 10; i++) {
                chunks.add(new Chunk("Chunk " + i, points(i)));
            }

            return chunks;
        }

        private static List<Point> points(int id) {
            List<Point> points = new ArrayList<>(100);
            for (int i = 0; i < 100; i++) {
                points.add(new Point("Point " + id + " " + i, id + i * 0.01, id + i * 0.01));
            }

            return points;
        }

        @Override
        public Data parse() throws IOException {
            return Data.parse(bytes);
        }

        @Override
        public byte[] toByteArray() throws IOException {
            return data.toByteArray();
        }
    }

    private static final class ProtoScalarAlgorithm implements Algorithm {
        private final ScalarProto data;
        private final byte[] bytes;

        private ProtoScalarAlgorithm() {
            this.data = ScalarProto.newBuilder()
                    .setDouble(10d)
                    .setFloat(20f)
                    .setInt32(30)
                    .setInt64(40L)
                    .setUint32(50)
                    .setUint64(60L)
                    .setSint32(70)
                    .setSint64(80L)
                    .setFixed32(90)
                    .setFixed64(100L)
                    .setSfixed32(110)
                    .setSfixed64(120L)
                    .build();
            this.bytes = this.data.toByteArray();
        }

        @Override
        public ScalarProto parse() throws InvalidProtocolBufferException {
            return ScalarProto.parseFrom(bytes);
        }

        @Override
        public byte[] toByteArray() {
            return data.toByteArray();
        }
    }

    private static final class ProtoRepeatedScalarAlgorithm implements Algorithm {
        private final RepeatedScalarProto data;
        private final byte[] bytes;

        private ProtoRepeatedScalarAlgorithm() {
            this.data = RepeatedScalarProto.newBuilder()
                    .addAllDouble(doubleList(10d))
                    .addAllFloat(floatList(20f))
                    .addAllInt32(intList(30))
                    .addAllInt64(longList(40L))
                    .addAllUint32(intList(50))
                    .addAllUint64(longList(60L))
                    .addAllSint32(intList(70))
                    .addAllSint64(longList(80L))
                    .addAllFixed32(intList(90))
                    .addAllFixed64(longList(100L))
                    .addAllSfixed32(intList(110))
                    .addAllSfixed64(longList(120L))
                    .build();
            this.bytes = this.data.toByteArray();
        }

        @Override
        public RepeatedScalarProto parse() throws InvalidProtocolBufferException {
            return RepeatedScalarProto.parseFrom(bytes);
        }

        @Override
        public byte[] toByteArray() {
            return data.toByteArray();
        }

    }

    private static final class ProtoNestedAlgorithm implements Algorithm {
        private final DataProto data;
        private final byte[] bytes;

        private ProtoNestedAlgorithm() {
            this.data = DataProto.newBuilder()
                    .setVersion("1.0.0")
                    .setDescription("This is test data")
                    .setTimestamp(1676725565L)
                    .addAllChunks(chunks())
                    .build();
            this.bytes = this.data.toByteArray();
        }

        private static List<ChunkProto> chunks() {
            List<ChunkProto> chunks = new ArrayList<>(10);
            for (int i = 0; i < 10; i++) {
                chunks.add(ChunkProto.newBuilder()
                        .setId("Chunk " + i)
                        .addAllPoints(points(i))
                        .build());
            }

            return chunks;
        }

        private static List<PointProto> points(int id) {
            List<PointProto> points = new ArrayList<>(100);
            for (int i = 0; i < 100; i++) {
                points.add(PointProto.newBuilder()
                        .setId("Point " + id + " " + i)
                        .setLatitude(id + i * 0.01)
                        .setLongitude(id + i * 0.01)
                        .build());
            }

            return points;
        }

        @Override
        public DataProto parse() throws InvalidProtocolBufferException {
            return DataProto.parseFrom(bytes);
        }

        @Override
        public byte[] toByteArray() {
            return data.toByteArray();
        }
    }

    @SuppressWarnings("SameParameterValue")
    private static List<Double> doubleList(double seed) {
        return rangeClosed(1, 100).mapToObj(i -> seed * i).toList();
    }

    @SuppressWarnings("SameParameterValue")
    private static List<Float> floatList(float seed) {
        return rangeClosed(1, 100).mapToObj(i -> seed * i).toList();
    }

    private static List<Integer> intList(int seed) {
        return rangeClosed(1, 100).mapToObj(i -> seed * i).toList();
    }

    private static List<Long> longList(long seed) {
        return rangeClosed(1, 100).mapToObj(i -> seed * i).toList();
    }
}
