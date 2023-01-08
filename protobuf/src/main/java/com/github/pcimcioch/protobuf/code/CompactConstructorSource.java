package com.github.pcimcioch.protobuf.code;

import static com.github.pcimcioch.protobuf.code.CodeBody.param;

/**
 * Compact constructor source
 */
public final class CompactConstructorSource {
    private String visibility = "";
    private String body = "";

    private CompactConstructorSource() {
    }

    /**
     * Create new compact constructor
     *
     * @return compact constructor
     */
    public static CompactConstructorSource compactConstructor() {
        return new CompactConstructorSource();
    }

    /**
     * Set body
     *
     * @param body body
     * @return source
     */
    public CompactConstructorSource set(CodeBody body) {
        this.body = body.toString();
        return this;
    }

    /**
     * Set visibility
     *
     * @param visibilitySource visibility
     * @return source
     */
    public CompactConstructorSource set(VisibilitySource visibilitySource) {
        this.visibility = visibilitySource.toString();
        return this;
    }

    @Override
    public String toString() {
        throw new IllegalStateException("Call toString(String)");
    }

    /**
     * To source code
     *
     * @param className class name
     * @return code
     */
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
