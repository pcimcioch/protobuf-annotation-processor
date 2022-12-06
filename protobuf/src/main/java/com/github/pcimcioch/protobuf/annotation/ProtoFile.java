package com.github.pcimcioch.protobuf.annotation;

import com.github.pcimcioch.protobuf.model.TypeName;

import java.util.List;

import static com.github.pcimcioch.protobuf.model.TypeName.canonicalName;

/**
 * Represents protobuf file that contains multiple protobuf structures
 *
 * @param javaPackage  java_package option
 * @param messages     message annotations
 * @param enumerations enumerations
 */
public record ProtoFile(String javaPackage, List<Message> messages, List<Enumeration> enumerations) {

    /**
     * Builds name of object located in this file
     *
     * @param name short name
     * @return full canonical name
     */
    // TODO [improvement] maybe the model could be reworked a bit so that name resolving is done before ModelFactory
    public TypeName nameOf(String name) {
        if (name == null) {
            return canonicalName("");
        }
        if (name.startsWith(".")) {
            return canonicalName(javaPackage + name);
        }
        if (name.contains(".")) {
            return canonicalName(name);
        }
        return canonicalName(javaPackage + "." + name);
    }

    /**
     * Returns whether this proto file contains enumeration with given name
     *
     * @param typeName type name
     * @return check result
     */
    public boolean containsEnumeration(TypeName typeName) {
        return enumerations.stream()
                .map(Enumeration::name)
                .map(this::nameOf)
                .anyMatch(typeName::equals);
    }

    /**
     * Returns whether this proto file contains message with given name
     *
     * @param typeName type name
     * @return check result
     */
    public boolean containsMessage(TypeName typeName) {
        return messages.stream()
                .map(Message::name)
                .map(this::nameOf)
                .anyMatch(typeName::equals);
    }
}
