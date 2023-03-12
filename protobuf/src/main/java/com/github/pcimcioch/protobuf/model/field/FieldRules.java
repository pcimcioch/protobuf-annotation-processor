package com.github.pcimcioch.protobuf.model.field;

import static com.github.pcimcioch.protobuf.model.validation.Assertions.assertFalse;

/**
 * Rules of the field
 *
 * @param deprecated whether field is deprecated
 * @param repeated   whether field is repeated
 * @param packed     whether repeated field is packed
 */
public record FieldRules(
        boolean deprecated,
        boolean repeated,
        boolean packed
) {

    /**
     * Constructor
     *
     * @param deprecated whether field is deprecated
     * @param repeated   whether field is repeated
     * @param packed     whether repeated field is packed
     */
    public FieldRules {
        Valid.packed(repeated, packed);
    }

    private static final class Valid {

        private static void packed(boolean repeated, boolean packed) {
            assertFalse(!repeated && packed, "Only repeated fields can be packed");
        }
    }
}
