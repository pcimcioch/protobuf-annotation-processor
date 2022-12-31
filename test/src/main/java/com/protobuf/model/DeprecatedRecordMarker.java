package com.protobuf.model;

import com.github.pcimcioch.protobuf.annotation.Enumeration;
import com.github.pcimcioch.protobuf.annotation.Enumeration.Element;
import com.github.pcimcioch.protobuf.annotation.Field;
import com.github.pcimcioch.protobuf.annotation.Message;

@Enumeration(
        name = "DeprecatedEnum",
        elements = {
                @Element(name = "FIRST", number = 0),
                @Element(name = "SECOND", number = 1)
        }
)
@Message(
        name = "DeprecatedSubRecord",
        fields = {
                @Field(type = Field.int32, name = "someField", number = 1)
        }
)
@Message(
        name = "DeprecatedRecord",
        fields = {
                @Field(type = Field.int32, name = "someField", number = 1),
                @Field(type = "DeprecatedEnum", name = "someEnum", number = 2),
                @Field(type = "DeprecatedSubRecord", name = "someOther", number = 3),
                @Field(type = Field.int32, name = "deprecatedField", number = 4, deprecated = true),
                @Field(type = "DeprecatedEnum", name = "deprecatedEnum", number = 5, deprecated = true),
                @Field(type = "DeprecatedSubRecord", name = "deprecatedOther", number = 6, deprecated = true)
        }
)
class DeprecatedRecordMarker {
}
