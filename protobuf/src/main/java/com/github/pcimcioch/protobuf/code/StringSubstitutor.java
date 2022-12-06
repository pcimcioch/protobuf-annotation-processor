package com.github.pcimcioch.protobuf.code;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class performing string substitution.
 */
public final class StringSubstitutor {
    private static final Pattern tokenPattern = Pattern.compile("\\$([a-zA-Z0-9]+)");

    private StringSubstitutor() {
    }

    /**
     * Replace tokens in given string template. Token starts with dollar sign '$' followed by alphanumeric key
     *
     * @param template   string template containing tokens to replace
     * @param parameters map of tokenKey to tokenValue. Each $tokenKey in the template will be replaced with tokenValue
     * @return substituted string
     */
    public static String replace(String template, Map<String, String> parameters) {
        StringBuilder sb = new StringBuilder();
        Matcher myMatcher = tokenPattern.matcher(template);

        while (myMatcher.find()) {
            String field = myMatcher.group(1);
            myMatcher.appendReplacement(sb, "");
            String value = Optional.ofNullable(parameters.get(field))
                    .orElseThrow(() -> new IllegalStateException("Missing string substitution argument " + field));
            sb.append(value);
        }

        myMatcher.appendTail(sb);
        return sb.toString();
    }
}
