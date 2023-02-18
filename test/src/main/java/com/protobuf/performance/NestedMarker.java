package com.protobuf.performance;


import com.github.pcimcioch.protobuf.annotation.Field;
import com.github.pcimcioch.protobuf.annotation.Message;

import static com.github.pcimcioch.protobuf.annotation.Field.double_;
import static com.github.pcimcioch.protobuf.annotation.Field.int64;
import static com.github.pcimcioch.protobuf.annotation.Field.string;

@Message(
        name = "Data",
        fields = {
                @Field(name = "version", type = string, number = 1),
                @Field(name = "description", type = string, number = 2),
                @Field(name = "timestamp", type = int64, number = 3),
                @Field(name = "chunks", type = "Chunk", number = 4, repeated = true)
        }
)
@Message(
        name = "Chunk",
        fields = {
                @Field(name = "id", type = string, number = 1),
                @Field(name = "points", type = "Point", number = 2, repeated = true)
        }
)
@Message(
        name = "Point",
        fields = {
                @Field(name = "id", type = string, number = 1),
                @Field(name = "latitude", type = double_, number = 2),
                @Field(name = "longitude", type = double_, number = 3)
        }
)
class NestedMarker {
}

