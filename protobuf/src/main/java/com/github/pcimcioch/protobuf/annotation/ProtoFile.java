package com.github.pcimcioch.protobuf.annotation;

import java.util.List;

/**
 * Represents protobuf file that contains multiple protobuf structures
 *
 * @param javaPackage  java_package option
 * @param messages     message annotations
 * @param enumerations enumerations
 */
public record ProtoFile(String javaPackage, List<Message> messages, List<Enumeration> enumerations) {
}
