package com.github.pcimcioch.protobuf.source;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class StringSubstitutor {
    private static final Pattern tokenPattern = Pattern.compile("\\$\\{([^}]*)}");

    private StringSubstitutor() {
    }

    public static String replace(String template, Map<String, String> parameters) {
        StringBuilder sb = new StringBuilder();
        Matcher myMatcher = tokenPattern.matcher(template);

        while (myMatcher.find()) {
            String field = myMatcher.group(1);
            myMatcher.appendReplacement(sb, "");
            sb.append(parameters.get(field));
        }

        myMatcher.appendTail(sb);
        return sb.toString();
    }
}