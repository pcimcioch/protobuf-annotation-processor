package com.protobuf.model;

import com.github.pcimcioch.protobuf.annotation.Enumeration;
import com.github.pcimcioch.protobuf.annotation.Enumeration.Element;
import com.github.pcimcioch.protobuf.annotation.Field;
import com.github.pcimcioch.protobuf.annotation.JavaMultipleFiles;
import com.github.pcimcioch.protobuf.annotation.Message;

@JavaMultipleFiles(javaOuterClassName = "Multiple")
@Enumeration(
        name = "MultipleEnum",
        elements = @Element(name = "TEST", number = 0))
@Message(
        name = "MultipleMessage",
        fields = {
                @Field(type = "int32", name = "intField", number = 1),
                @Field(type = "MultipleEnum", name = "enumField", number = 2)
        })
class MultipleFilesMarker {
}
