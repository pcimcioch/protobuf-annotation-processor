package com.github.pcimcioch.protobuf.code;

import static com.github.pcimcioch.protobuf.code.CodeBody.param;

public final class CanonicalConstructorSource {
    private String visibility = "";
    private String body = "";

    private CanonicalConstructorSource() {
    }

    public static CanonicalConstructorSource canonicalConstructor() {
        return new CanonicalConstructorSource();
    }

    public CanonicalConstructorSource set(CodeBody body) {
        this.body = body.toString();
        return this;
    }

    public CanonicalConstructorSource set(VisibilitySource visibilitySource) {
        this.visibility = visibilitySource.toString();
        return this;
    }

    @Override
    public String toString() {
        throw new IllegalStateException("Call toString(String)");
    }

    public String toString(String className) {
        return CodeBody.body("""
                        $visibility $ClassName {
                            $body
                        }
                        """,
                param("visibility", visibility),
                param("ClassName", className),
                param("body", body)
        ).toString();
    }
}
