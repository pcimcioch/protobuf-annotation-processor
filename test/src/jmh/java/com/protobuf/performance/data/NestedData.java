package com.protobuf.performance.data;

import com.protobuf.performance.Chunk;
import com.protobuf.performance.ChunkProto;
import com.protobuf.performance.Data;
import com.protobuf.performance.DataProto;
import com.protobuf.performance.Point;
import com.protobuf.performance.PointProto;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.util.ArrayList;
import java.util.List;

import static com.protobuf.performance.data.Algorithm.OUR;
import static com.protobuf.performance.data.Algorithm.PROTO;

@State(Scope.Benchmark)
public class NestedData {
    @Param({OUR, PROTO})
    public String type;

    public Algorithm<?> algorithm;

    @Setup(Level.Trial)
    public void setUp() {
        this.algorithm = switch (type) {
            case OUR -> new Algorithm<>(our(), Data::toByteArray, Data::writeTo, Data::parse, Data::parse);
            case PROTO ->
                    new Algorithm<>(proto(), DataProto::toByteArray, DataProto::writeTo, DataProto::parseFrom, DataProto::parseFrom);
            default -> null;
        };
    }

    private Data our() {
        return new Data("1.0.0", "This is test data", 1676725565L, ourChunks());
    }

    private static List<Chunk> ourChunks() {
        List<Chunk> chunks = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            chunks.add(new Chunk("Chunk " + i, ourPoints(i)));
        }

        return chunks;
    }

    private static List<Point> ourPoints(int id) {
        List<Point> points = new ArrayList<>(100);
        for (int i = 0; i < 100; i++) {
            points.add(new Point("Point " + id + " " + i, id + i * 0.01, id + i * 0.01));
        }

        return points;
    }

    private DataProto proto() {
        return DataProto.newBuilder()
                .setVersion("1.0.0")
                .setDescription("This is test data")
                .setTimestamp(1676725565L)
                .addAllChunks(protoChunks())
                .build();
    }

    private static List<ChunkProto> protoChunks() {
        List<ChunkProto> chunks = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            chunks.add(ChunkProto.newBuilder()
                    .setId("Chunk " + i)
                    .addAllPoints(protoPoints(i))
                    .build());
        }

        return chunks;
    }

    private static List<PointProto> protoPoints(int id) {
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
}
