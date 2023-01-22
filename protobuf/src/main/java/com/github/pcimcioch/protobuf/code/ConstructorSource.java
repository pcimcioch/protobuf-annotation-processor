package com.github.pcimcioch.protobuf.code;

import java.util.ArrayList;
import java.util.List;

import static com.github.pcimcioch.protobuf.code.CodeBody.param;

/**
 * Constructor source
 */
public final class ConstructorSource {
    private String visibility = "";
    private String body = "";
    private final List<String> parameters = new ArrayList<>();

    private ConstructorSource() {
    }

    /**
     * Create constructor source
     *
     * @return constructor source
     */
    public static ConstructorSource constructor() {
        return new ConstructorSource();
    }

    /**
     * Add parameter
     *
     * @param parameterSource parameter
     * @return source
     */
    public ConstructorSource add(ParameterSource parameterSource) {
        parameters.add(parameterSource.toString());
        return this;
    }

    /**
     * Set visibility
     *
     * @param visibilitySource visibility
     * @return source
     */
    public ConstructorSource set(VisibilitySource visibilitySource) {
        this.visibility = visibilitySource.toString();
        return this;
    }

    /**
     * Set body
     *
     * @param body body
     * @return source
     */
    public ConstructorSource set(CodeBody body) {
        this.body = body.toString();
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
                        $visibility $ClassName($parameters) {
                            $body
                        }
                        """,
                param("ClassName", className),
                param("visibility", visibility),
                param("parameters", parameters),
                param("body", body)
        ).toString();
    }
}
