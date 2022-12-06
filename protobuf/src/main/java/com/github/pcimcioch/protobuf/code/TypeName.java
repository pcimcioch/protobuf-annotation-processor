package com.github.pcimcioch.protobuf.code;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents java type name
 */
public class TypeName {
    private static final Pattern simpleTypePattern = Pattern.compile("^[a-zA-Z]+$");
    private static final Pattern canonicalNamePattern = Pattern.compile("^(?<package>[a-z][a-z0-9_]*(\\.[a-z0-9_]+)*)(?<parent>(\\.[A-Z][A-Za-z0-9_]*)*)(?<simple>\\.[A-Z][A-Za-z0-9_]*)$");

    private final String packageName;
    private final String parentClassesName;
    private final String simpleName;

    private TypeName(String packageName, String parentClassesName, String simpleName) {
        this.packageName = packageName == null ? "" : packageName;
        this.parentClassesName = parentClassesName == null ? "" : parentClassesName;
        this.simpleName = simpleName == null ? "" : simpleName;
    }

    /**
     * Returns package name.
     * <p>
     * For {@code com.example.MyClass} it will be {@code com.example}
     *
     * @return package name
     */
    public String packageName() {
        return packageName;
    }

    /**
     * Returns parent classes names
     * <p>
     * For {@code com.example.MyClass.Nested.DoubleNested} it will be {@code MyClass.Nested}
     *
     * @return parent classes names
     */
    public String parentClassesName() {
        return parentClassesName;
    }

    /**
     * Returns simple name
     * <p>
     * For {@code com.example.MyClass} it will be {@code MyClass}
     *
     * @return simple name
     */
    public String simpleName() {
        return simpleName;
    }

    /**
     * Returns fully qualified canonical name
     *
     * @return canonical name
     */
    public String canonicalName() {
        String canonicalName = "";
        if (!"".equals(packageName)) {
            canonicalName += packageName + ".";
        }
        if (!"".equals(parentClassesName)) {
            canonicalName += parentClassesName + ".";
        }

        return canonicalName + simpleName;
    }

    @Override
    public String toString() {
        return canonicalName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypeName typeName = (TypeName) o;
        return packageName.equals(typeName.packageName) && parentClassesName.equals(typeName.parentClassesName) && simpleName.equals(typeName.simpleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(packageName, parentClassesName, simpleName);
    }

    /**
     * Create type from simple class name, without package
     *
     * @param name name
     * @return type name
     */
    public static TypeName simpleName(String name) {
        mustMatch(simpleTypePattern, name, "Incorrect simple type name");
        return new TypeName(null, null, name);
    }

    /**
     * Create type from full canonical class name
     *
     * @param name name
     * @return type name
     */
    public static TypeName canonicalName(String name) {
        Matcher matcher = mustMatch(canonicalNamePattern, name, "Incorrect canonical name");
        return new TypeName(
                trim(matcher.group("package")),
                trim(matcher.group("parent")),
                trim(matcher.group("simple"))
        );
    }

    private static Matcher mustMatch(Pattern pattern, String name, String message) {
        if (name == null) {
            throw new IllegalArgumentException(message + ": <null>");
        }

        Matcher matcher = pattern.matcher(name);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(message + ": " + name);
        }

        return matcher;
    }

    private static String trim(String value) {
        return value != null && value.startsWith(".")
                ? value.substring(1)
                : value;
    }
}
