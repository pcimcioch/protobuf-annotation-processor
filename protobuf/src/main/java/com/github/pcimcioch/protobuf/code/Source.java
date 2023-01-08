package com.github.pcimcioch.protobuf.code;

import static com.github.pcimcioch.protobuf.code.CodeBody.body;
import static com.github.pcimcioch.protobuf.code.CodeBody.param;

public abstract class Source {
    private final String packageName;
    private final String simpleName;

    protected Source(String packageName, String simpleName) {
        this.packageName = packageName;
        this.simpleName = simpleName;
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

    protected abstract String typeOnlyCode();

    protected String packageName() {
        return packageName;
    }

    protected String simpleName() {
        return simpleName;
    }
}
