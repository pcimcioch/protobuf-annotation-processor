package com.github.pcimcioch.protobuf.code;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;

/**
 * Represents java type name
 */
public final class TypeName {
    private static final Pattern simpleTypePattern = Pattern.compile("^[a-zA-Z]+$");
    private static final Pattern canonicalNamePattern = Pattern.compile("^(?<package>[a-z][a-z0-9_]*(\\.[a-z0-9_]+)*)(?<classes>(\\.[A-Z][A-Za-z0-9_]*)+)$");

    private static final TypeName list = canonicalName(List.class);
    private static final TypeName collection = canonicalName(Collection.class);

    private final String packageName;
    private final LinkedList<String> classNames;
    private final TypeName generic;

    private TypeName(String packageName, String classNames, TypeName generic) {
        this(
                packageName == null ? "" : packageName,
                new LinkedList<>(asList(classNames.split("\\."))),
                generic
        );
    }

    private TypeName(String packageName, LinkedList<String> classNames, TypeName generic) {
        this.packageName = packageName;
        this.classNames = classNames;
        this.generic = generic;
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
     * Returns generic name
     * <p>
     * For {@code com.example.ClassA<com.example.ClassB>} it will be {@code com.example.ClassB}
     *
     * @return generic name
     */
    public TypeName generic() {
        return generic;
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
        String classes = String.join(".", classNames);
        String suffix = generic == null
                ? ""
                : "<" + generic.canonicalName() + ">";

        return prefix + classes + suffix;
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
     * Returns new type which is a copy of this type without generic type
     *
     * @return new type name
     */
    public TypeName withoutGeneric() {
        return new TypeName(packageName, classNames, null);
    }

    /**
     * Returns new type with given generic type appended
     *
     * @param typeName generic type
     * @return new type name
     */
    public TypeName of(TypeName typeName) {
        return new TypeName(packageName, classNames, typeName);
    }

    /**
     * Returns this type wrapped in {@link java.util.List}
     *
     * @return this type wrapped in the list
     */
    public TypeName inList() {
        return list.of(this);
    }

    /**
     * Returns this type wrapped in {@link java.util.Collection}
     *
     * @return this type wrapped in the collection
     */
    public TypeName inCollection() {
        return collection.of(this);
    }

    /**
     * Checks whether this name is direct child of given name
     *
     * @param other parent name
     * @return whether this name is direct child of given name
     */
    public boolean isDirectChildOf(TypeName other) {
        return withoutGeneric().equals(other.withoutGeneric().with(simpleName()));
    }

    /**
     * Checks whether type is primitive
     *
     * @return whether type is primitive
     */
    public boolean isPrimitive() {
        return "".equals(packageName) && classNames.size() == 1 && Character.isLowerCase(simpleName().charAt(0));
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
        return packageName.equals(typeName.packageName) && classNames.equals(typeName.classNames) && Objects.equals(generic, typeName.generic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(packageName, classNames, generic);
    }

    /**
     * Create type from simple class name, without package
     *
     * @param name name
     * @return type name
     */
    public static TypeName simpleName(String name) {
        mustMatch(simpleTypePattern, name, "Incorrect simple type name");
        return new TypeName(null, name, null);
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
                trim(matcher.group("classes")),
                null
        );
    }

    /**
     * Create type from full canonical class name
     *
     * @param clazz class type
     * @return type name
     */
    public static TypeName canonicalName(Class<?> clazz) {
        return canonicalName(clazz.getCanonicalName());
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
