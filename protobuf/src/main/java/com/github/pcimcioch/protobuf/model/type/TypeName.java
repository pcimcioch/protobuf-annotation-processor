package com.github.pcimcioch.protobuf.model.type;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents java type name
 */
public final class TypeName {
    private static final Pattern simpleTypePattern = Pattern.compile("^[a-zA-Z]+$");
    private static final Pattern canonicalNamePattern = Pattern.compile("^(?<package>[a-z][a-z0-9_]*(\\.[a-z0-9_]+)*)(?<classes>(\\.[A-Z][A-Za-z0-9_]*)+)$");

    private final String packageName;
    private final LinkedList<String> classNames;

    private TypeName(String packageName, String classNames) {
        this.packageName = packageName == null ? "" : packageName;
        this.classNames = new LinkedList<>(Arrays.asList(classNames.split("\\.")));
    }

    /**
     * Returns package name.
     * <p>
     * For {@code com.example.ClassA.ClassB.ClassC} it will be {@code com.example}
     *
     * @return package name
     */
    public String packageName() {
        return packageName;
    }

    /**
     * Returns simple name
     * <p>
     * For {@code com.example.ClassA.ClassB.ClassC} it will be {@code ClassC}
     *
     * @return simple name
     */
    public String simpleName() {
        return classNames.getLast();
    }

    /**
     * Returns base name
     * <p>
     * For {@code com.example.ClassA.ClassB.ClassC} it will be {@code com.example.ClassA}
     *
     * @return base name
     */
    public String baseName() {
        String prefix = "".equals(packageName)
                ? ""
                : (packageName + ".");
        return prefix + classNames.getFirst();
    }

    /**
     * Returns nested class name
     * <p>
     * For {@code com.example.ClassA.ClassB.ClassC} it will be {@code [ClassB, ClassC]}
     *
     * @return nested class name
     */
    public List<String> nestedClassNames() {
        LinkedList<String> nested = new LinkedList<>(classNames);
        nested.removeFirst();
        return nested;
    }

    /**
     * Returns fully qualified canonical name
     * <p>
     * For {@code com.example.ClassA.ClassB.ClassC} it will be {@code com.example.ClassA.ClassB.ClassC}
     *
     * @return canonical name
     */
    public String canonicalName() {
        String prefix = "".equals(packageName)
                ? ""
                : (packageName + ".");
        return prefix + String.join(".", classNames);
    }

    /**
     * Returns new type with given class name appended to the end
     *
     * @param className class name to append
     * @return new type name
     */
    public TypeName with(String className) {
        return canonicalName(canonicalName() + "." + className);
    }

    /**
     * Checks whether this name is direct child of given name
     *
     * @param other parent name
     * @return whether this name is direct child of given name
     */
    public boolean isDirectChildOf(TypeName other) {
        return equals(other.with(simpleName()));
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
        return packageName.equals(typeName.packageName) && classNames.equals(typeName.classNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(packageName, classNames);
    }

    /**
     * Create type from simple class name, without package
     *
     * @param name name
     * @return type name
     */
    public static TypeName simpleName(String name) {
        mustMatch(simpleTypePattern, name, "Incorrect simple type name");
        return new TypeName(null, name);
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
                trim(matcher.group("classes"))
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
