package com.github.pcimcioch.protobuf.code;

import java.util.ArrayList;
import java.util.List;

import static com.github.pcimcioch.protobuf.code.CodeBody.param;

public final class ConstructorSource {
    private String visibility = "";
    private String body = "";
    private final List<String> parameters = new ArrayList<>();

    private ConstructorSource() {
    }

    public static ConstructorSource constructor() {
        return new ConstructorSource();
    }

    public ConstructorSource add(ParameterSource parameterSource) {
        parameters.add(parameterSource.toString());
        return this;
    }

    public ConstructorSource set(CodeBody body) {
        this.body = body.toString();
        return this;
    }

    public ConstructorSource set(VisibilitySource visibilitySource) {
        this.visibility = visibilitySource.toString();
        return this;
    }

    @Override
    public String toString() {
        throw new IllegalStateException("Call toString(String)");
    }

    public String toString(String className) {
        return CodeBody.body("""
                        $visibility $ClassName($parameters) {
                            $body
                        }
                        """,
                param("visibility", visibility),
                param("ClassName", className),
                param("parameters", parameters),
                param("body", body)
        ).toString();
    }
}
