package com.protobuf.serialization;

import com.github.pcimcioch.protobuf.dto.ByteArray;
import com.github.pcimcioch.protobuf.dto.ProtobufMessage;
import com.github.pcimcioch.protobuf.io.ProtobufWriter;
import org.assertj.core.api.ThrowingConsumer;
import org.assertj.core.api.iterable.ThrowingExtractor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

abstract class SerializationTest {

    protected <T> T deserialize(ThrowingExtractor<byte[], T, IOException> bytesParser,
                                ThrowingExtractor<InputStream, T, IOException> inputParser,
                                ThrowingConsumer<ProtobufWriter> writerAction) throws IOException {
        return deserialize(bytesParser, inputParser, deserialize(writerAction).toByteArray());
    }

    protected ByteArray deserialize(ThrowingConsumer<ProtobufWriter> writerAction) {
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        ProtobufWriter proto = new ProtobufWriter(data);

        writerAction.accept(proto);

        return ByteArray.fromByteArray(data.toByteArray());
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

    protected <T extends ProtobufMessage<T>> byte[] serialize(T record) throws IOException {
        byte[] rawData = record.toByteArray();
        byte[] streamData;

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            record.writeTo(out);
            streamData = out.toByteArray();
        }

        assertThat(rawData).isEqualTo(streamData);

        return rawData;
    }
}
