package com.protobuf.model;

import com.github.pcimcioch.protobuf.annotation.Field;
import com.github.pcimcioch.protobuf.annotation.Message;

@Message(
        name = "FullRecord",
        fields = {
                @Field(type = "double", name = "double_", number = 1),
                @Field(type = "float", name = "float_", number = 2),
                @Field(type = "int32", name = "int32", number = 3),
                @Field(type = "int64", name = "int64", number = 4),
                @Field(type = "uint32", name = "uint32", number = 5),
                @Field(type = "uint64", name = "uint64", number = 6),
                @Field(type = "sint32", name = "sint32", number = 7),
                @Field(type = "sint64", name = "sint64", number = 8),
                @Field(type = "fixed32", name = "fixed32", number = 9),
                @Field(type = "fixed64", name = "fixed64", number = 10),
                @Field(type = "sfixed32", name = "sfixed32", number = 11),
                @Field(type = "sfixed64", name = "sfixed64", number = 12),
                @Field(type = "bool", name = "bool", number = 13),
                @Field(type = "string", name = "string", number = 14),
                @Field(type = "bytes", name = "bytes", number = 15)
        }
)
class FullRecordMarker {
}
