package com.github.pcimcioch.protobuf.model;

/**
 * Represents java type name
 *
 * @param packageName java package
 * @param simpleName  java simple name
 */
public record TypeName(
        String packageName,
        String simpleName
) {
}
