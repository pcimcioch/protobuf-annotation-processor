package com.protobuf.model;


import com.github.pcimcioch.protobuf.annotation.Field;
import com.github.pcimcioch.protobuf.annotation.Message;

import static com.github.pcimcioch.protobuf.annotation.Field.int32;

@Message(
        name = "com.protobuf.model.one.NameOne",
        fields = {
                @Field(name = "leftUp", type = int32, number = 1),
                @Field(name = "leftDown", type = "com.protobuf.model.one.NameOne.NameTwo", number = 2),
                @Field(name = "rightUp", type = "com.protobuf.model.two.NameOne", number = 3),
                @Field(name = "rightDown", type = "com.protobuf.model.two.NameOne.NameTwo", number = 4)
        })
@Message(
        name = "com.protobuf.model.one.NameOne.NameTwo",
        fields = {
                @Field(name = "leftUp", type = "com.protobuf.model.one.NameOne", number = 1),
                @Field(name = "leftDown", type = int32, number = 2),
                @Field(name = "rightUp", type = "com.protobuf.model.two.NameOne", number = 3),
                @Field(name = "rightDown", type = "com.protobuf.model.two.NameOne.NameTwo", number = 4)
        })
@Message(
        name = "com.protobuf.model.two.NameOne",
        fields = {
                @Field(name = "leftUp", type = "com.protobuf.model.one.NameOne", number = 1),
                @Field(name = "leftDown", type = "com.protobuf.model.one.NameOne.NameTwo", number = 2),
                @Field(name = "rightUp", type = int32, number = 3),
                @Field(name = "rightDown", type = "com.protobuf.model.two.NameOne.NameTwo", number = 4)
        })
@Message(
        name = "com.protobuf.model.two.NameOne.NameTwo",
        fields = {
                @Field(name = "leftUp", type = "com.protobuf.model.one.NameOne", number = 1),
                @Field(name = "leftDown", type = "com.protobuf.model.one.NameOne.NameTwo", number = 2),
                @Field(name = "rightUp", type = "com.protobuf.model.two.NameOne", number = 3),
                @Field(name = "rightDown", type = int32, number = 4)
        })
class SameNameMarker {
}
