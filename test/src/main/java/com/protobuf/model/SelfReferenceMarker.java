package com.protobuf.model;

import com.github.pcimcioch.protobuf.annotation.Field;
import com.github.pcimcioch.protobuf.annotation.Message;

@Message(
        name = "SelfReference",
        fields = {
                @Field(type = "int32", name = "value", number = 1),
                @Field(type = "SelfReference", name = "next", number = 2)
        }
)
class SelfReferenceMarker {
}
