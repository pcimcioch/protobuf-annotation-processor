package com.protobuf.model;

import com.github.pcimcioch.protobuf.annotation.Field;
import com.github.pcimcioch.protobuf.annotation.Message;

@Message(
        name = "UnknownFieldsRecord",
        supportUnknownFields = true,
        fields = {
                @Field(type = "int32", name = "amount", number = 1),
        }
)
class UnknownFieldsRecordMarker {
}
