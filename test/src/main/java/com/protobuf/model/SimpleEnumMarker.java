package com.protobuf.model;

import com.github.pcimcioch.protobuf.annotation.Enumeration;
import com.github.pcimcioch.protobuf.annotation.Enumeration.Element;
import com.github.pcimcioch.protobuf.annotation.Field;
import com.github.pcimcioch.protobuf.annotation.Message;

@Enumeration(
        name = "SimpleEnum",
        elements = {
                @Element(name = "FIRST", number = 0),
                @Element(name = "SECOND", number = 1),
                @Element(name = "THIRD", number = 2)
        }
)
@Message(
        name = "SimpleEnumMessage",
        fields = {
                @Field(type = "SimpleEnum", name = "order", number = 1)
        }
)
class SimpleEnumMarker {
}
