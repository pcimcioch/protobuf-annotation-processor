package com.github.pcimcioch.protobuf.model.validation;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Model data assertions
 */
public final class Assertions {
    private Assertions() {
    }

    /**
     * Assert that given value is not null
     *
     * @param value   value to check
     * @param message exception message
     */
    public static void assertNonNull(Object value, String message) {
        if (value == null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Assert that given condition is met
     *
     * @param condition condition to check
     * @param message   exception message
     */
    public static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Assert that given condition is not met
     *
     * @param condition condition to check
     * @param message   exception message
     */
    public static void assertFalse(boolean condition, String message) {
        if (condition) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Assert that given collection is nether null or empty
     *
     * @param collection collection to check
     * @param message    exception message
     */
    public static void assertNonEmpty(Collection<?> collection, String message) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Assert that given collection does not contain any null values
     *
     * @param collection collection to check
     * @param message    exception message
     */
    public static void assertContainsNoNulls(Collection<?> collection, String message) {
        for (Object element : collection) {
            assertNonNull(element, message);
        }
    }

    /**
     * Assert that given collection does not contain any duplicated values
     *
     * @param collection      collection to check
     * @param valueExtractor  function to extract value from the collection element
     * @param messageTemplate exception message template. Extracted value will be passed as a parameter
     * @param <T>             element type
     * @param <K>             value type
     */
    public static <T, K> void assertNoDuplicates(Collection<T> collection, Function<T, K> valueExtractor, String messageTemplate) {
        Set<K> values = new HashSet<>();

        for (T element : collection) {
            K value = valueExtractor.apply(element);

            if (!values.add(value)) {
                throw new IllegalArgumentException(String.format(messageTemplate, value));
            }
        }
    }

    /**
     * Assert that given collection contains given value
     *
     * @param collection     collection to check
     * @param valueExtractor function to extract value from the collection element
     * @param expectedValue  expected value
     * @param message        exception message
     * @param <T>            element type
     * @param <K>            value type
     */
    public static <T, K> void assertContains(Collection<T> collection, Function<T, K> valueExtractor, K expectedValue, String message) {
        for (T element : collection) {
            K value = valueExtractor.apply(element);

            if (expectedValue.equals(value)) {
                return;
            }
        }

        throw new IllegalArgumentException(message);
    }

    /**
     * Assert that all elements in given collection match given predicate
     *
     * @param collection collection to test
     * @param predicate  predicate to check
     * @param message    exception message
     * @param <T>        element type
     */
    public static <T> void assertAllMatches(Collection<T> collection, Predicate<T> predicate, String message) {
        for (T element : collection) {
            if (!predicate.test(element)) {
                throw new IllegalArgumentException(message);
            }
        }
    }
}
