package org.github.pcimcioch.protobuf.test.model;

import org.github.pcimcioch.protobuf.annotation.Field;
import org.github.pcimcioch.protobuf.annotation.Message;

@Message(
        packageName = "org.github.pcimcioch.protobuf.test",
        name = "FullRecord",
        fields = {
                @Field(type = "double", name = "_double", number = 0),
                @Field(type = "float", name = "_float", number = 1),
                @Field(type = "int32", name = "int32", number = 2),
                @Field(type = "int64", name = "int64", number = 3),
                @Field(type = "uint32", name = "uint32", number = 4),
                @Field(type = "uint64", name = "uint64", number = 5),
                @Field(type = "sint32", name = "sint32", number = 6),
                @Field(type = "sint64", name = "sint64", number = 7),
                @Field(type = "fixed32", name = "fixed32", number = 8),
                @Field(type = "fixed64", name = "fixed64", number = 9),
                @Field(type = "sfixed32", name = "sfixed32", number = 10),
                @Field(type = "sfixed64", name = "sfixed64", number = 11),
                @Field(type = "bool", name = "bool", number = 12),
                @Field(type = "string", name = "string", number = 13),
                @Field(type = "bytes", name = "bytes", number = 14)
        }
)
class FullRecordMarker {
}
