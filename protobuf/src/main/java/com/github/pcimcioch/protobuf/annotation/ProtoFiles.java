package com.github.pcimcioch.protobuf.annotation;

import java.util.List;

/**
 * Set of all protobuf files
 *
 * @param files files
 */
public record ProtoFiles(List<ProtoFile> files) {

    /**
     * Represents protobuf file that contains multiple protobuf structures
     *
     * @param javaPackage        java_package option
     * @param javaOuterClassName java_outer_classname option
     * @param messages           message annotations
     * @param enumerations       enumerations
     */
    public record ProtoFile(String javaPackage, String javaOuterClassName, List<Message> messages,
                            List<Enumeration> enumerations) {
    }
}
