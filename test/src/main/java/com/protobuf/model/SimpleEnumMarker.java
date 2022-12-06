package com.protobuf.model;

import com.github.pcimcioch.protobuf.annotation.Enumeration;
import com.github.pcimcioch.protobuf.annotation.EnumerationElement;

@Enumeration(
        name = "SimpleEnum",
        elements = {
                @EnumerationElement(name = "FIRST", number = 0),
                @EnumerationElement(name = "SECOND", number = 1),
                @EnumerationElement(name = "THIRD", number = 2)
        }
)
public class SimpleEnumMarker {
}
