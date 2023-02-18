package com.protobuf.performance;

import com.github.pcimcioch.protobuf.annotation.Field;
import com.github.pcimcioch.protobuf.annotation.Message;

@Message(
        name = "RepeatedScalar",
        fields = {
                @Field(type = "double", name = "_double", number = 1, repeated = true),
                @Field(type = "float", name = "_float", number = 2, repeated = true),
                @Field(type = "int32", name = "int32", number = 3, repeated = true),
                @Field(type = "int64", name = "int64", number = 4, repeated = true),
                @Field(type = "uint32", name = "uint32", number = 5, repeated = true),
                @Field(type = "uint64", name = "uint64", number = 6, repeated = true),
                @Field(type = "sint32", name = "sint32", number = 7, repeated = true),
                @Field(type = "sint64", name = "sint64", number = 8, repeated = true),
                @Field(type = "fixed32", name = "fixed32", number = 9, repeated = true),
                @Field(type = "fixed64", name = "fixed64", number = 10, repeated = true),
                @Field(type = "sfixed32", name = "sfixed32", number = 11, repeated = true),
                @Field(type = "sfixed64", name = "sfixed64", number = 12, repeated = true),
        }
)
class RepeatedScalarMarker {
}
