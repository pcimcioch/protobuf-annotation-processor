package com.github.pcimcioch.protobuf.model.message;

import com.github.pcimcioch.protobuf.model.type.TypeName;

import java.util.List;
import java.util.Objects;

import static com.github.pcimcioch.protobuf.model.validation.Assertions.assertContains;
import static com.github.pcimcioch.protobuf.model.validation.Assertions.assertContainsNoNulls;
import static com.github.pcimcioch.protobuf.model.validation.Assertions.assertNoDuplicates;
import static com.github.pcimcioch.protobuf.model.validation.Assertions.assertNonEmpty;
import static com.github.pcimcioch.protobuf.model.validation.Assertions.assertNonNull;

/**
 * Enumeration Definition
 */
public class EnumerationDefinition {
    private final TypeName name;
    private final List<EnumerationElementDefinition> elements;

    /**
     * Constructor
     *
     * @param name       name of the enumeration
     * @param elements   elements of the enumeration
     * @param allowAlias whether to allow aliases
     * @param reserved   reserved elements
     */
    public EnumerationDefinition(TypeName name, List<EnumerationElementDefinition> elements, boolean allowAlias, ReservedDefinition reserved) {
        this.name = Valid.name(name);
        this.elements = Valid.elements(elements, allowAlias, reserved);
    }

    /**
     * Returns name of the enumeration
     *
     * @return name of the enumeration
     */
    public TypeName name() {
        return name;
    }

    /**
     * Returns elements of this enumeration
     *
     * @return elements
     */
    public List<EnumerationElementDefinition> elements() {
        return elements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnumerationDefinition that = (EnumerationDefinition) o;
        return name.equals(that.name) && elements.equals(that.elements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, elements);
    }

    private static final class Valid {

        private static TypeName name(TypeName name) {
            assertNonNull(name, "Enum name cannot be null");
            return name;
        }

        private static List<EnumerationElementDefinition> elements(List<EnumerationElementDefinition> elements, boolean allowAlias, ReservedDefinition reserved) {
            assertNonEmpty(elements, "Enum must have at least one element");
            assertContainsNoNulls(elements, "Null element in enumeration");
            assertContains(elements, EnumerationElementDefinition::number, 0, "Enum must contain element with number 0");
            assertNoDuplicates(elements, EnumerationElementDefinition::name, "Duplicated element name: %s");
            if (!allowAlias) {
                assertNoDuplicates(elements, EnumerationElementDefinition::number, "Duplicated element number: %s");
            }

            return reserved.validElements(elements);
        }
    }
}
