package com.github.pcimcioch.protobuf.code;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class StringSubstitutor {
    private static final Pattern tokenPattern = Pattern.compile("\\$([a-zA-Z0-9]+)");

    private StringSubstitutor() {
    }

    static String replace(String template, Map<String, String> parameters) {
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
