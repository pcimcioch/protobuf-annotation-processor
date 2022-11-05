package com.github.pcimcioch.protobuf.model;

/**
 * Represents java type name
 */
public class TypeName {
    private final String packageName;
    private final String simpleName;

    /**
     * Constructor
     *
     * @param canonicalName fully qualified canonical name
     */
    public TypeName(String canonicalName) {
        int i = canonicalName.lastIndexOf(".");
        this.packageName = i == -1 ? "" : canonicalName.substring(0, i);
        this.simpleName = i == -1 ? canonicalName : canonicalName.substring(i + 1);
    }

    /**
     * Returns package name
     *
     * @return package name
     */
    public String packageName() {
        return packageName;
    }

    /**
     * Returns simple name
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
        return "".equals(packageName) ? simpleName : packageName + "." + simpleName;
    }

    @Override
    public String toString() {
        return canonicalName();
    }
}
