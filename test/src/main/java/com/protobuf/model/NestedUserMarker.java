package com.protobuf.model;

import com.github.pcimcioch.protobuf.annotation.Enumeration;
import com.github.pcimcioch.protobuf.annotation.Enumeration.Element;
import com.github.pcimcioch.protobuf.annotation.Field;
import com.github.pcimcioch.protobuf.annotation.Message;

@Message(
        name = "NestedUser",
        fields = {
                @Field(type = "string", name = "name", number = 1),
                @Field(type = "int32", name = "age", number = 2),
                @Field(type = "NestedUser.NestedAddress", name = "address", number = 3),
                @Field(type = "NestedUser.NestedWork", name = "work", number = 4)
        }
)
@Message(
        name = "NestedUser.NestedAddress",
        fields = {
                @Field(type = "string", name = "street", number = 1),
                @Field(type = "int32", name = "number", number = 2)
        }
)
@Message(
        name = "NestedUser.NestedWork",
        fields = {
                @Field(type = "string", name = "name", number = 1),
                @Field(type = "int32", name = "year", number = 2),
                @Field(type = "NestedUser.NestedAddress", name = "address", number = 3),
                @Field(type = "NestedUser.NestedWork.NestedWorkType", name = "type", number = 4)
        }
)
@Enumeration(
        name = "NestedUser.NestedWork.NestedWorkType",
        elements = {
                @Element(name = "OFFICE", number = 0),
                @Element(name = "PHYSICAL", number = 1)
        }
)
class NestedUserMarker {
}
