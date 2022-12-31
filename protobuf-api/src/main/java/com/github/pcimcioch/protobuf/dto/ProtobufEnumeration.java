package com.github.pcimcioch.protobuf.dto;

/**
 * Protobuf enumeration type
 */
@FunctionalInterface
public interface ProtobufEnumeration {

    /**
     * Returns number of this enumeration element
     *
     * @return number of this element
     */
    int number();
}
