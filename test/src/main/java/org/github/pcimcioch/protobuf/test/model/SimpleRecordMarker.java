package org.github.pcimcioch.protobuf.test.model;

import org.github.pcimcioch.protobuf.annotation.Field;
import org.github.pcimcioch.protobuf.annotation.Message;

@Message(
        packageName = "org.github.pcimcioch.protobuf.test",
        name = "SimpleRecord",
        fields = {
                @Field(type = "int32", name = "amount", number = 0),
                @Field(type = "double", name = "latitude", number = 1),
                @Field(type = "double", name = "longitude", number = 2)
        }
)
class SimpleRecordMarker {
}
