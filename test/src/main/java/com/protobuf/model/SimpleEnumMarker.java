package com.protobuf.model;

import com.github.pcimcioch.protobuf.annotation.Enumeration;
import com.github.pcimcioch.protobuf.annotation.EnumerationElement;
import com.github.pcimcioch.protobuf.annotation.Field;
import com.github.pcimcioch.protobuf.annotation.Message;

@Enumeration(
        name = "SimpleEnum",
        elements = {
                @EnumerationElement(name = "FIRST", number = 0),
                @EnumerationElement(name = "SECOND", number = 1),
                @EnumerationElement(name = "THIRD", number = 2)
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
