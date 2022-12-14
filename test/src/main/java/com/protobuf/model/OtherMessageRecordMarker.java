package com.protobuf.model;

import com.github.pcimcioch.protobuf.annotation.Field;
import com.github.pcimcioch.protobuf.annotation.Message;

@Message(
        name = "OtherMessageAddress",
        fields = {
                @Field(name = "street", type = Field.string, number = 1),
                @Field(name = "number", type = Field.int32, number = 2)
        }
)
@Message(
        name = "OtherMessageWork",
        fields = {
                @Field(name = "address", type = "OtherMessageAddress", number = 1),
                @Field(name = "name", type = Field.string, number = 2),
                @Field(name = "year", type = Field.fixed32, number = 3)
        }
)
@Message(
        name = "OtherMessageRecord",
        fields = {
                @Field(name = "name", type = Field.string, number = 1),
                @Field(name = "age", type = Field.int32, number = 2),
                @Field(name = "address", type = "OtherMessageAddress", number = 3),
                @Field(name = "work", type = "OtherMessageWork", number = 4)
        }
)
class OtherMessageRecordMarker {
}
