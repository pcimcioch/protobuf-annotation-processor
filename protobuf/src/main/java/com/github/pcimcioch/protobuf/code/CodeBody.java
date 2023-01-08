package com.github.pcimcioch.protobuf.code;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

/**
 * Code body builder
 */
public final class CodeBody {

    private final StringBuilder builder = new StringBuilder();
    private boolean isFirst = true;

    private CodeBody() {
    }

    /**
     * Creates parameter used in body template substitution
     *
     * @param key   substitution key
     * @param value substitution value
     * @return parameter
     */
    public static Parameter param(String key, Object value) {
        return new Parameter(key, value.toString());
    }

    /**
     * Creates parameter used in body template substitution
     *
     * @param key   substitution key
     * @param value substitution value
     * @return parameter
     */
    public static Parameter param(String key, Class<?> value) {
        return new Parameter(key, value.getCanonicalName());
    }

    /**
     * Creates parameter used in body template substitution. Values will be delimited with ", "
     *
     * @param key    substitution key
     * @param values substitution values
     * @return parameter
     */
    public static Parameter param(String key, List<?> values) {
        String value = values.stream()
                .map(Object::toString)
                .collect(joining(", "));
        return new Parameter(key, value);
    }

    /**
     * Creates parameter used in body template substitution. Values will be delimited with given delimiter
     *
     * @param key       substitution key
     * @param values    substitution values
     * @param delimiter delimiter
     * @return parameter
     */
    public static Parameter param(String key, List<?> values, String delimiter) {
        String value = values.stream()
                .map(Object::toString)
                .collect(joining(delimiter));
        return new Parameter(key, value);
    }

    /**
     * Creates parameter used in body template substitution. Values will be delimited with given delimiter and
     * prefixed and suffixed with given values
     *
     * @param key       substitution key
     * @param values    substitution values
     * @param delimiter delimiter
     * @param prefix    prefix
     * @param suffix    suffix
     * @return parameter
     */
    public static Parameter param(String key, List<?> values, String delimiter, String prefix, String suffix) {
        String value = values.stream()
                .map(Object::toString)
                .collect(joining(delimiter, prefix, suffix));
        return new Parameter(key, value);
    }

    /**
     * Creates parameter used in body template substitution. Values will be delimited with given delimiter and
     * prefixed and suffixed with given values.
     * If values are empty then empty will be used
     *
     * @param key       substitution key
     * @param values    substitution values
     * @param delimiter delimiter
     * @param prefix    prefix
     * @param suffix    suffix
     * @param empty     empty
     * @return parameter
     */
    public static Parameter param(String key, List<?> values, String delimiter, String prefix, String suffix, String empty) {
        StringJoiner joiner = new StringJoiner(delimiter, prefix, suffix);
        joiner.setEmptyValue(empty);
        values.stream()
                .map(Object::toString)
                .forEach(joiner::add);

        return new Parameter(key, joiner.toString());
    }

    /**
     * Creates body from the template
     *
     * @param sourceTemplate source code template
     * @param parameters     parameters to fill in source code template
     * @return filled method body
     */
    public static CodeBody body(String sourceTemplate, Parameter... parameters) {
        return body().append(sourceTemplate, parameters);
    }

    /**
     * Returns empty body
     *
     * @return body
     */
    public static CodeBody body() {
        return new CodeBody();
    }

    /**
     * Appends source code body template to the body
     *
     * @param sourceTemplate source code template
     * @param parameters     parameters to fill in source code template
     * @return method body with new code appended
     */
    public CodeBody append(String sourceTemplate, Parameter... parameters) {
        Map<String, String> formattedParameters = stream(parameters)
                .collect(toMap(Parameter::key, Parameter::value));
        builder.append(StringSubstitutor.replace(sourceTemplate, formattedParameters));

        return this;
    }

    /**
     * Appends source code body template to the body and the new line at the end
     *
     * @param sourceTemplate source code template
     * @param parameters     parameters to fill in source code template
     * @return method body with new code appended
     */
    public CodeBody appendln(String sourceTemplate, Parameter... parameters) {
        append(sourceTemplate, parameters);
        builder.append("\n");

        return this;
    }

    /**
     * Appends source code body
     *
     * @param body source code to append
     * @return method body with new code appended
     */
    public CodeBody append(CodeBody body) {
        builder.append(body.toString());

        return this;
    }

    /**
     * Appends source code body and the new line at the end
     *
     * @param body source code to append
     * @return method body with new code appended
     */
    public CodeBody appendln(CodeBody body) {
        append(body);
        builder.append("\n");

        return this;
    }

    /**
     * Appends source code except first time of this call
     *
     * @param sourceTemplate source code template
     * @param parameters     parameters to fill in source code template
     * @return method body with new code appended if it is not the first call
     */
    public CodeBody appendExceptFirst(String sourceTemplate, Parameter... parameters) {
        if (isFirst) {
            isFirst = false;
            return this;
        }

        return append(sourceTemplate, parameters);

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
    public record Parameter(String key, String value) {
    }
}
