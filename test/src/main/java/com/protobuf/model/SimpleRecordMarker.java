package com.protobuf.model;

import com.github.pcimcioch.protobuf.annotation.Field;
import com.github.pcimcioch.protobuf.annotation.Message;

@Message(
        name = "com.github.pcimcioch.protobuf.test.SimpleRecord",
        fields = {
                @Field(type = "int32", name = "amount", number = 0),
                @Field(type = "double", name = "latitude", number = 1),
                @Field(type = "double", name = "longitude", number = 2)
        }
)
class SimpleRecordMarker {
}
