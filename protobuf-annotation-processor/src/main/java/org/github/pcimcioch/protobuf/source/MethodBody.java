package org.github.pcimcioch.protobuf.source;

import org.apache.commons.text.StringSubstitutor;
import org.github.pcimcioch.protobuf.model.FieldDefinition;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

class MethodBody {

    private static final Formatters formatters = new Formatters();

    private final StringBuilder builder = new StringBuilder();

    static {
        formatters.register(FieldDefinition.class, FieldDefinition::name);
        formatters.register(Class.class, Class::getCanonicalName);
        formatters.register(List.class, list -> list.stream().map(formatters::format).collect(joining(", ")));
    }

    private MethodBody() {
    }

    static Parameter param(String key, Object value) {
        return new Parameter(key, value);
    }

    static MethodBody body(String sourcePattern, Parameter... parameters) {
        return body().append(sourcePattern, parameters);
    }

    static MethodBody body() {
        return new MethodBody();
    }

    MethodBody append(String sourcePattern, Parameter... parameters) {
        Map<String, Object> formattedParameters = Arrays.stream(parameters)
                .collect(toMap(Parameter::key, p -> formatters.format(p.value())));
        builder.append(StringSubstitutor.replace(sourcePattern, formattedParameters));

        return this;
    }

    @Override
    public String toString() {
        return builder.toString();
    }

    record Parameter(String key, Object value) {
    }

    private static final class Formatters {
        private final Map<Class<?>, Function<?, Object>> mappers = new HashMap<>();

        private <T> void register(Class<T> type, Function<T, Object> mapper) {
            mappers.put(type, mapper);
        }

        private <T> Object format(T value) {
            return findMapper(value).apply(value);
        }

        private <T> Function<T, Object> findMapper(T value) {
            for (Entry<Class<?>, Function<?, Object>> entry : mappers.entrySet()) {
                if (entry.getKey().isAssignableFrom(value.getClass())) {
                    return (Function<T, Object>) entry.getValue();
                }
            }

            return (Function<T, Object>) Function.identity();
        }
    }
}
