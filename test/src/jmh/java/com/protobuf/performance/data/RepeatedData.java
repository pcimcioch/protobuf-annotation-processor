package com.protobuf.performance.data;

import com.protobuf.performance.RepeatedScalar;
import com.protobuf.performance.RepeatedScalarProto;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.util.List;

import static com.protobuf.performance.data.Algorithm.OUR;
import static com.protobuf.performance.data.Algorithm.PROTO;
import static java.util.stream.IntStream.rangeClosed;

@State(Scope.Benchmark)
public class RepeatedData {
    @Param({OUR, PROTO})
    public String type;

    public Algorithm<?> algorithm;

    @Setup(Level.Trial)
    public void setUp() {
        this.algorithm = switch (type) {
            case OUR ->
                    new Algorithm<>(our(), RepeatedScalar::toByteArray, RepeatedScalar::writeTo, RepeatedScalar::parse, RepeatedScalar::parse);
            case PROTO ->
                    new Algorithm<>(proto(), RepeatedScalarProto::toByteArray, RepeatedScalarProto::writeTo, RepeatedScalarProto::parseFrom, RepeatedScalarProto::parseFrom);
            default -> null;
        };
    }

    private RepeatedScalar our() {
        return RepeatedScalar.builder()
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
                .bool(boolList(true))
                .build();
    }

    private RepeatedScalarProto proto() {
        return RepeatedScalarProto.newBuilder()
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
                .addAllBool(boolList(true))
                .build();
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

    @SuppressWarnings("SameParameterValue")
    private static List<Boolean> boolList(boolean seed) {
        return rangeClosed(1, 100).mapToObj(i -> seed).toList();
    }
}
