package com.protobuf.model;

import com.github.pcimcioch.protobuf.annotation.Field;
import com.github.pcimcioch.protobuf.annotation.Message;

@Message(
        name = "RepeatablePacked",
        fields = {
                @Field(type = "double", name = "doubles", number = 1, repeated = true, packed = true),
                @Field(type = "float", name = "floats", number = 2, repeated = true, packed = true),
                @Field(type = "int32", name = "int32s", number = 3, repeated = true, packed = true),
                @Field(type = "int64", name = "int64s", number = 4, repeated = true, packed = true),
                @Field(type = "uint32", name = "uint32s", number = 5, repeated = true, packed = true),
                @Field(type = "uint64", name = "uint64s", number = 6, repeated = true, packed = true),
                @Field(type = "sint32", name = "sint32s", number = 7, repeated = true, packed = true),
                @Field(type = "sint64", name = "sint64s", number = 8, repeated = true, packed = true),
                @Field(type = "fixed32", name = "fixed32s", number = 9, repeated = true, packed = true),
                @Field(type = "fixed64", name = "fixed64s", number = 10, repeated = true, packed = true),
                @Field(type = "sfixed32", name = "sfixed32s", number = 11, repeated = true, packed = true),
                @Field(type = "sfixed64", name = "sfixed64s", number = 12, repeated = true, packed = true),
                @Field(type = "bool", name = "bools", number = 13, repeated = true, packed = true),
                @Field(type = "RepeatableEnum", name = "orders", number = 14, repeated = true, packed = true)
        }
)
class RepeatablePackedMarker {
}
