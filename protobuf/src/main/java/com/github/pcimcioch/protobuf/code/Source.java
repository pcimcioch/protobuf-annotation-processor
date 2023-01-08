package com.github.pcimcioch.protobuf.code;

import static com.github.pcimcioch.protobuf.code.CodeBody.body;
import static com.github.pcimcioch.protobuf.code.CodeBody.param;

/**
 * File source
 */
public abstract class Source {
    private final String packageName;
    private final String simpleName;

    /**
     * Constructor
     *
     * @param type type name
     */
    protected Source(TypeName type) {
        this.packageName = type.packageName();
        this.simpleName = type.simpleName();
    }

    @Override
    public String toString() {
        return body("""
                        package $package;
                                        
                        $type
                        """,
                param("package", packageName),
                param("type", typeOnlyCode())
        ).toString();
    }

    /**
     * Returns canonical name
     *
     * @return canonical name
     */
    public String canonicalName() {
        return packageName + "." + simpleName;
    }

    /**
     * Returns sourcecode without package declaration
     *
     * @return source code
     */
    protected abstract String typeOnlyCode();

    /**
     * Returns simple name
     *
     * @return simple name
     */
    protected String simpleName() {
        return simpleName;
    }
}
