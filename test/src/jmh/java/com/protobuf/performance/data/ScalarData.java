package com.protobuf.performance.data;

import com.protobuf.performance.Scalar;
import com.protobuf.performance.ScalarProto;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import static com.protobuf.performance.data.Algorithm.OUR;
import static com.protobuf.performance.data.Algorithm.PROTO;

@State(Scope.Benchmark)
public class ScalarData {
    @Param({OUR, PROTO})
    public String type;

    public Algorithm<?> algorithm;

    @Setup(Level.Trial)
    public void setUp() {
        this.algorithm = switch (type) {
            case OUR -> new Algorithm<>(our(), Scalar::toByteArray, Scalar::writeTo, Scalar::parse, Scalar::parse);
            case PROTO ->
                    new Algorithm<>(proto(), ScalarProto::toByteArray, ScalarProto::writeTo, ScalarProto::parseFrom, ScalarProto::parseFrom);
            default -> null;
        };
    }

    private Scalar our() {
        return new Scalar(10d, 20f, 30, 40L, 50, 60L, 70, 80L, 90, 100L, 110, 120L);
    }

    private ScalarProto proto() {
        return ScalarProto.newBuilder()
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
    }
}
