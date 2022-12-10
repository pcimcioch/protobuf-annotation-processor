package com.protobuf.model;

import com.github.pcimcioch.protobuf.annotation.Enumeration;
import com.github.pcimcioch.protobuf.annotation.EnumerationElement;
import com.github.pcimcioch.protobuf.annotation.Field;
import com.github.pcimcioch.protobuf.annotation.Message;

@Enumeration(
        name = "DeprecatedEnum",
        elements = {
                @EnumerationElement(name = "FIRST", number = 0),
                @EnumerationElement(name = "SECOND", number = 1)
        }
)
@Message(
        name = "DeprecatedRecord",
        fields = {
                @Field(type = Field.int32, name = "someField", number = 1),
                @Field(type = "DeprecatedEnum", name = "someEnum", number = 2),
                @Field(type = Field.int32, name = "deprecatedField", number = 3, deprecated = true),
                @Field(type = "DeprecatedEnum", name = "deprecatedEnum", number = 4, deprecated = true),
        }
)
class DeprecatedRecordMarker {
}
