package com.protobuf.model;

import com.github.pcimcioch.protobuf.annotation.Enumeration;
import com.github.pcimcioch.protobuf.annotation.Field;
import com.github.pcimcioch.protobuf.annotation.Message;

@Enumeration(
        name = "RepeatableEnum",
        elements = {
                @Enumeration.Element(name = "FIRST", number = 0),
                @Enumeration.Element(name = "SECOND", number = 1),
                @Enumeration.Element(name = "THIRD", number = 2)
        }
)
@Message(
        name = "RepeatableEnumMessage",
        fields = {
                @Field(type = "RepeatableEnum", name = "orders", number = 1, repeated = true)
        }
)
class RepeatableEnumMarker {
}
