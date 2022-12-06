package com.github.pcimcioch.protobuf.annotation;

import com.github.pcimcioch.protobuf.model.TypeName;

import java.util.List;

/**
 * Set of all protobuf files
 *
 * @param files files
 */
public record ProtoFiles(List<ProtoFile> files) {

    /**
     * Returns whether this proto file set contains enumeration with given name
     *
     * @param typeName type name
     * @return check result
     */
    public boolean containsEnumeration(TypeName typeName) {
        return files.stream()
                .anyMatch(file -> file.containsEnumeration(typeName));
    }

    /**
     * Returns whether this proto file set contains message with given name
     *
     * @param typeName type name
     * @return check result
     */
    public boolean containsMessage(TypeName typeName) {
        return files.stream()
                .anyMatch(file -> file.containsMessage(typeName));
    }
}
