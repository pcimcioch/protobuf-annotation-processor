package com.github.pcimcioch.protobuf.model.message;

import com.github.pcimcioch.protobuf.model.field.FieldDefinition;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Reserved elements definition
 */
public class ReservedDefinition {
    private final Set<String> names;
    private final Set<Integer> numbers;
    private final Set<Range> ranges;

    /**
     * Constructor
     *
     * @param names   reserved names
     * @param numbers reserved numbers
     * @param ranges  reserved number ranges
     */
    // TODO validate non null
    public ReservedDefinition(Set<String> names, Set<Integer> numbers, Set<Range> ranges) {
        this.names = names;
        this.numbers = numbers;
        this.ranges = ranges;
    }

    /**
     * Validate given message fields
     *
     * @param fields message fields to validate
     * @return fields
     */
    public List<FieldDefinition> validFields(List<FieldDefinition> fields) {
        for (FieldDefinition field : fields) {
            if (nameReserved(field.name())) {
                throw new IllegalArgumentException("Field name " + field.name() + " is reserved");
            }
            if (numberReserved(field.number())) {
                throw new IllegalArgumentException("Field number " + field.number() + " is reserved");
            }
        }

        return fields;
    }

    /**
     * Validate given enum elements
     *
     * @param elements elements to validate
     * @return elements
     */
    public List<EnumerationElementDefinition> validElements(List<EnumerationElementDefinition> elements) {
        for (EnumerationElementDefinition element : elements) {
            if (nameReserved(element.name())) {
                throw new IllegalArgumentException("Element name " + element.name() + " is reserved");
            }
            if (numberReserved(element.number())) {
                throw new IllegalArgumentException("Field number " + element.number() + " is reserved");
            }
        }

        return elements;
    }

    private boolean nameReserved(String name) {
        return names.contains(name);
    }

    private boolean numberReserved(int number) {
        return numbers.contains(number) || ranges.stream().anyMatch(range -> range.matches(number));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservedDefinition that = (ReservedDefinition) o;
        return names.equals(that.names) && numbers.equals(that.numbers) && ranges.equals(that.ranges);
    }

    @Override
    public int hashCode() {
        return Objects.hash(names, numbers, ranges);
    }

    /**
     * Number range
     *
     * @param from lower bound, inclusive
     * @param to   upper bound, inclusive
     */
    public record Range(int from, int to) {
        /**
         * Constructor
         *
         * @param from lower bound, inclusive
         * @param to   upper bound, inclusive
         */
        public Range {
            if (to < from) {
                throw new IllegalArgumentException("Incorrect range [" + from + ", " + to + "]");
            }
        }

        /**
         * Returns true if given value is in range, inclusive
         *
         * @param value value to check
         * @return whether value is in range
         */
        public boolean matches(int value) {
            return value >= from && value <= to;
        }
    }
}
