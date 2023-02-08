package com.protobuf.model;

import com.github.pcimcioch.protobuf.annotation.Field;
import com.github.pcimcioch.protobuf.annotation.Message;

@Message(
        name = "RepeatableOtherAddress",
        fields = {
                @Field(name = "street", type = Field.string, number = 1),
                @Field(name = "number", type = Field.int32, number = 2)
        }
)
@Message(
        name = "RepeatableOtherWork",
        fields = {
                @Field(name = "address", type = "RepeatableOtherAddress", number = 1, repeated = true)
        }
)
class RepeatableOtherMarker {
}
