package com.protobuf.model;

import com.github.pcimcioch.protobuf.annotation.Field;
import com.github.pcimcioch.protobuf.annotation.Message;

import static com.github.pcimcioch.protobuf.annotation.Field.int32;

@Message(
        name = "PackagesSimple",
        fields = @Field(type = int32, name = "test", number = 1)
)
@Message(
        name = "com.test.PackagesFull",
        fields = @Field(type = int32, name = "test", number = 1)
)
@Message(
        name = ".test.PackagesPartial",
        fields = @Field(type = int32, name = "test", number = 1)
)
class PackagesMarker {
}
