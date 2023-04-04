package com.protobuf.serialization;

import com.github.pcimcioch.protobuf.dto.ProtobufMessage;
import com.github.pcimcioch.protobuf.io.ProtobufEncoder;
import org.assertj.core.api.ThrowingConsumer;
import org.assertj.core.api.iterable.ThrowingExtractor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

abstract class SerializationTestBase {

    protected <T> T deserialize(ThrowingExtractor<byte[], T, IOException> bytesParser,
                                ThrowingExtractor<InputStream, T, IOException> inputParser,
                                ThrowingConsumer<ProtobufEncoder> writerAction) throws IOException {
        return deserialize(bytesParser, inputParser, serialize(writerAction));
    }

    protected <T> T deserialize(ThrowingExtractor<byte[], T, IOException> bytesParser,
                                ThrowingExtractor<InputStream, T, IOException> inputParser,
                                byte[] data) throws IOException {
        T recordRaw = bytesParser.extractThrows(data);
        T recordStream;

        try (ByteArrayInputStream in = new ByteArrayInputStream(data)) {
            recordStream = inputParser.extractThrows(in);
        }

        assertThat(recordRaw).isEqualTo(recordStream);

        return recordRaw;
    }

    protected byte[] serialize(ThrowingConsumer<ProtobufEncoder> writerAction) throws IOException {
        ProtobufEncoder encoder = new ProtobufEncoder();
        writerAction.accept(encoder);
        return encoder.data();
    }

    protected byte[] serialize(ProtobufMessage<?> record) throws IOException {
        byte[] rawData = record.toByteArray();
        byte[] streamData;

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            record.writeTo(out);
            streamData = out.toByteArray();
        }

        assertThat(rawData).isEqualTo(streamData);
        assertThat(rawData.length).isEqualTo(record.protobufSize());

        return rawData;
    }
}
