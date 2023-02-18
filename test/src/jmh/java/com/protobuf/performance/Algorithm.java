package com.protobuf.performance;

import com.google.protobuf.InvalidProtocolBufferException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.IntStream.rangeClosed;

public sealed interface Algorithm {

    enum Type {OUR, PROTO}

    enum Payload {SCALAR, REPEATED, NESTED}

    enum IO {ARRAY, IO_STREAM}

    Object parse() throws Exception;

    Object serialize() throws Exception;

    static Algorithm from(Type type, Payload payload, IO io) {
        return switch (type) {
            case OUR -> Our.from(payload, io);
            case PROTO -> Proto.from(payload, io);
        };
    }

    abstract sealed class Our implements Algorithm {
        private abstract static sealed class Scalar extends Our {
            protected final com.protobuf.performance.Scalar data;
            protected final byte[] bytes;

            private Scalar() {
                try {
                    this.data = new com.protobuf.performance.Scalar(10d, 20f, 30, 40L, 50, 60L, 70, 80L, 90, 100L, 110, 120L);
                    this.bytes = this.data.toByteArray();
                } catch (IOException ex) {
                    throw new UncheckedIOException(ex);
                }
            }

            private static final class Array extends Scalar {
                @Override
                public com.protobuf.performance.Scalar parse() throws IOException {
                    return com.protobuf.performance.Scalar.parse(bytes);
                }

                @Override
                public byte[] serialize() throws IOException {
                    return data.toByteArray();
                }
            }

            private static final class IOStream extends Scalar {
                @Override
                public com.protobuf.performance.Scalar parse() throws IOException {
                    return com.protobuf.performance.Scalar.parse(new ByteArrayInputStream(bytes));
                }

                @Override
                public OutputStream serialize() throws IOException {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream(bytes.length);
                    data.writeTo(stream);
                    return stream;
                }
            }

            private static Scalar from(IO io) {
                return switch (io) {
                    case ARRAY -> new Array();
                    case IO_STREAM -> new IOStream();
                };
            }
        }

        private abstract static sealed class Repeated extends Our {
            protected final RepeatedScalar data;
            protected final byte[] bytes;

            private Repeated() {
                try {
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
                } catch (IOException ex) {
                    throw new UncheckedIOException(ex);
                }
            }

            private static final class Array extends Repeated {
                @Override
                public RepeatedScalar parse() throws IOException {
                    return RepeatedScalar.parse(bytes);
                }

                @Override
                public byte[] serialize() throws IOException {
                    return data.toByteArray();
                }
            }

            private static final class IOStream extends Repeated {
                @Override
                public RepeatedScalar parse() throws IOException {
                    return RepeatedScalar.parse(new ByteArrayInputStream(bytes));
                }

                @Override
                public OutputStream serialize() throws IOException {
                    ByteArrayOutputStream out = new ByteArrayOutputStream(bytes.length);
                    data.writeTo(out);
                    return out;
                }
            }

            private static Repeated from(IO io) {
                return switch (io) {
                    case ARRAY -> new Array();
                    case IO_STREAM -> new IOStream();
                };
            }
        }

        private abstract static sealed class Nested extends Our {
            protected final Data data;
            protected final byte[] bytes;

            private Nested() {
                try {
                    this.data = new Data("1.0.0", "This is test data", 1676725565L, chunks());
                    this.bytes = this.data.toByteArray();
                } catch (IOException ex) {
                    throw new UncheckedIOException(ex);
                }
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

            private static final class Array extends Nested {
                @Override
                public Data parse() throws IOException {
                    return Data.parse(bytes);
                }

                @Override
                public byte[] serialize() throws IOException {
                    return data.toByteArray();
                }
            }

            private static final class IOStream extends Nested {
                @Override
                public Data parse() throws IOException {
                    return Data.parse(new ByteArrayInputStream(bytes));
                }

                @Override
                public OutputStream serialize() throws IOException {
                    ByteArrayOutputStream out = new ByteArrayOutputStream(bytes.length);
                    data.toByteArray();
                    return out;
                }
            }

            private static Nested from(IO io) {
                return switch (io) {
                    case ARRAY -> new Array();
                    case IO_STREAM -> new IOStream();
                };
            }
        }

        private static Our from(Algorithm.Payload payload, IO io) {
            return switch (payload) {
                case SCALAR -> Scalar.from(io);
                case REPEATED -> Repeated.from(io);
                case NESTED -> Nested.from(io);
            };
        }
    }

    abstract sealed class Proto implements Algorithm {
        private abstract static sealed class Scalar extends Proto {
            protected final ScalarProto data;
            protected final byte[] bytes;

            private Scalar() {
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

            private static final class Array extends Scalar {
                @Override
                public ScalarProto parse() throws InvalidProtocolBufferException {
                    return ScalarProto.parseFrom(bytes);
                }

                @Override
                public byte[] serialize() {
                    return data.toByteArray();
                }
            }

            private static final class IOStream extends Scalar {
                @Override
                public ScalarProto parse() throws IOException {
                    return ScalarProto.parseFrom(new ByteArrayInputStream(bytes));
                }

                @Override
                public OutputStream serialize() throws IOException {
                    ByteArrayOutputStream out = new ByteArrayOutputStream(bytes.length);
                    data.writeTo(out);
                    return out;
                }
            }

            private static Scalar from(IO io) {
                return switch (io) {
                    case ARRAY -> new Array();
                    case IO_STREAM -> new IOStream();
                };
            }
        }

        private abstract static sealed class Repeated extends Proto {
            protected final RepeatedScalarProto data;
            protected final byte[] bytes;

            private Repeated() {
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

            private static final class Array extends Repeated {
                @Override
                public RepeatedScalarProto parse() throws InvalidProtocolBufferException {
                    return RepeatedScalarProto.parseFrom(bytes);
                }

                @Override
                public byte[] serialize() {
                    return data.toByteArray();
                }
            }

            private static final class IOStream extends Repeated {
                @Override
                public RepeatedScalarProto parse() throws IOException {
                    return RepeatedScalarProto.parseFrom(new ByteArrayInputStream(bytes));
                }

                @Override
                public OutputStream serialize() throws IOException {
                    ByteArrayOutputStream out = new ByteArrayOutputStream(bytes.length);
                    data.writeTo(out);
                    return out;
                }
            }

            private static Repeated from(IO io) {
                return switch (io) {
                    case ARRAY -> new Array();
                    case IO_STREAM -> new IOStream();
                };
            }
        }

        private abstract static sealed class Nested extends Proto {
            protected final DataProto data;
            protected final byte[] bytes;

            private Nested() {
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

            private static final class Array extends Nested {
                @Override
                public DataProto parse() throws InvalidProtocolBufferException {
                    return DataProto.parseFrom(bytes);
                }

                @Override
                public byte[] serialize() {
                    return data.toByteArray();
                }
            }

            private static final class IOStream extends Nested {
                @Override
                public DataProto parse() throws IOException {
                    return DataProto.parseFrom(new ByteArrayInputStream(bytes));
                }

                @Override
                public OutputStream serialize() throws IOException {
                    ByteArrayOutputStream out = new ByteArrayOutputStream(bytes.length);
                    data.writeTo(out);
                    return out;
                }
            }

            private static Nested from(IO io) {
                return switch (io) {
                    case ARRAY -> new Array();
                    case IO_STREAM -> new IOStream();
                };
            }
        }

        private static Proto from(Algorithm.Payload payload, IO io) {
            return switch (payload) {
                case SCALAR -> Scalar.from(io);
                case REPEATED -> Repeated.from(io);
                case NESTED -> Nested.from(io);
            };
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
