package com.protobuf.performance.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public final class Algorithm<T> {
    static final String OUR = "OUR";
    static final String PROTO = "PROTO";

    private final T data;
    private final byte[] bytes;
    private final ThrowingFunction<T, byte[]> bytesSerializer;
    private final ThrowingBiConsumer<T, OutputStream> streamSerializer;
    private final ThrowingFunction<byte[], T> bytesParser;
    private final ThrowingFunction<InputStream, T> streamParser;

    @FunctionalInterface
    interface ThrowingFunction<IN, OUT> {
        OUT call(IN in) throws Exception;
    }

    @FunctionalInterface
    interface ThrowingBiConsumer<IN1, IN2> {
        void call(IN1 in1, IN2 in2) throws Exception;
    }

    Algorithm(T data,
              ThrowingFunction<T, byte[]> bytesSerializer,
              ThrowingBiConsumer<T, OutputStream> streamSerializer,
              ThrowingFunction<byte[], T> bytesParser,
              ThrowingFunction<InputStream, T> streamParser
    ) {
        try {
            this.data = data;
            this.bytes = bytesSerializer.call(data);
            this.bytesSerializer = bytesSerializer;
            this.streamSerializer = streamSerializer;
            this.bytesParser = bytesParser;
            this.streamParser = streamParser;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public byte[] serializeBytes() throws Exception {
        return bytesSerializer.call(data);
    }

    public OutputStream serializeStream() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream(bytes.length);
        streamSerializer.call(data, out);
        return out;
    }

    public T parseBytes() throws Exception {
        return bytesParser.call(bytes);
    }

    public T parseStream() throws Exception {
        return streamParser.call(new ByteArrayInputStream(bytes));
    }
}
