package com.github.pcimcioch.protobuf.code;

import com.github.pcimcioch.protobuf.model.FieldDefinition;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

/**
 * Method body builder
 */
@SuppressWarnings("unchecked")
public class MethodBody {

    private static final Formatters formatters = new Formatters();

    private final StringBuilder builder = new StringBuilder();

    static {
        formatters.register(FieldDefinition.class, FieldDefinition::name);
        formatters.register(Class.class, Class::getCanonicalName);
        formatters.register(TypeName.class, TypeName::canonicalName);
        formatters.register(List.class, list -> list.stream().map(formatters::format).collect(joining(", ")).toString());
    }

    private MethodBody() {
    }

    /**
     * Creates parameter used in body template substitution
     *
     * @param key   substitution key
     * @param value substitution value
     * @return parameter
     */
    public static Parameter param(String key, Object value) {
        return new Parameter(key, value);
    }

    /**
     * Creates body from the template
     *
     * @param sourceTemplate source code template
     * @param parameters     parameters to fill in source code template
     * @return filled method body
     */
    public static MethodBody body(String sourceTemplate, Parameter... parameters) {
        return body().append(sourceTemplate, parameters);
    }

    /**
     * Returns empty body
     *
     * @return body
     */
    public static MethodBody body() {
        return new MethodBody();
    }

    /**
     * Appends source code body template to the body
     *
     * @param sourceTemplate source code template
     * @param parameters     parameters to fill in source code template
     * @return method body with new code appended
     */
    public MethodBody append(String sourceTemplate, Parameter... parameters) {
        Map<String, String> formattedParameters = Arrays.stream(parameters)
                .collect(toMap(Parameter::key, p -> formatters.format(p.value())));
        builder.append(StringSubstitutor.replace(sourceTemplate, formattedParameters));

        return this;
    }

    @Override
    public String toString() {
        return builder.toString();
    }

    /**
     * Source code template parameter
     *
     * @param key   key
     * @param value value
     */
    public record Parameter(String key, Object value) {
    }

    private static final class Formatters {
        private final Map<Class<?>, Function<?, String>> mappers = new HashMap<>();

        private <T> void register(Class<T> type, Function<T, String> mapper) {
            mappers.put(type, mapper);
        }

        private <T> String format(T value) {
            return findMapper(value).apply(value);
        }

        private <T> Function<T, String> findMapper(T value) {
            for (Entry<Class<?>, Function<?, String>> entry : mappers.entrySet()) {
                if (entry.getKey().isAssignableFrom(value.getClass())) {
                    return (Function<T, String>) entry.getValue();
                }
            }

            return Object::toString;
        }
    }
}
