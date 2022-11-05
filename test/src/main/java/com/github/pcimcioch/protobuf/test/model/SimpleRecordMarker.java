package com.github.pcimcioch.protobuf.test.model;

import com.github.pcimcioch.protobuf.annotation.Field;
import com.github.pcimcioch.protobuf.annotation.Message;
import com.github.pcimcioch.protobuf.test.SimpleRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Message(
        packageName = "com.github.pcimcioch.protobuf.test",
        name = "SimpleRecord",
        fields = {
                @Field(type = "int32", name = "amount", number = 0),
                @Field(type = "double", name = "latitude", number = 1),
                @Field(type = "double", name = "longitude", number = 2)
        }
)
class SimpleRecordMarker {
    public static void main(String[] args) throws IOException {
        // Create record
        SimpleRecord r1 = new SimpleRecord(10, 20.0, 30.0);
        SimpleRecord r2 = SimpleRecord.builder()
                .amount(10)
                .latitude(20.0)
                .longitude(30.0)
                .build();

        // Serialize record
        Path file = Paths.get("test.data");

        byte[] data = r1.toByteArray();
        try (OutputStream output = Files.newOutputStream(file)) {
            r1.writeTo(output);
        }

        // Deserialize record
        SimpleRecord r3 = SimpleRecord.parse(data);
        try (InputStream input = Files.newInputStream(file)) {
            SimpleRecord r4 = SimpleRecord.parse(input);
        }

        // read record fields
        System.out.printf("Record [amount=%d, latitude=%f, longitude=%f]%n", r3.amount(), r3.latitude(), r3.longitude());
    }
}
