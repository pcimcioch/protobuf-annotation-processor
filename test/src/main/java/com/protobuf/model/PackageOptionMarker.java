package com.protobuf.model;

import com.github.pcimcioch.protobuf.annotation.Field;
import com.github.pcimcioch.protobuf.annotation.JavaPackage;
import com.github.pcimcioch.protobuf.annotation.Message;

import static com.github.pcimcioch.protobuf.annotation.Field.int32;

@JavaPackage("com.protobuf.test")
@Message(
        name = "PackageOptionSimple",
        fields = @Field(type = int32, name = "test", number = 1)
)
@Message(
        name = "com.test.PackageOptionFull",
        fields = @Field(type = int32, name = "test", number = 1)
)
@Message(
        name = ".test.PackageOptionPartial",
        fields = @Field(type = int32, name = "test", number = 1)
)
class PackageOptionMarker {
}
