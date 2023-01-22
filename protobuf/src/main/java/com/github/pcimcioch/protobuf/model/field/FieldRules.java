package com.github.pcimcioch.protobuf.model.field;

/**
 * Rules of the field
 *
 * @param deprecated whether field is deprecated
 * @param repeated   whether field is repeated
 */
public record FieldRules(
        boolean deprecated,
        boolean repeated
) {
}
