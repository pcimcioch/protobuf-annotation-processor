package com.github.pcimcioch.protobuf.code;

import java.util.ArrayList;
import java.util.List;

import static com.github.pcimcioch.protobuf.code.CodeBody.param;

public final class MethodSource {
    private final String name;
    private String visibility = "";
    private String staticModifier = "";
    private String finalModifier = "";
    private String body = "";
    private String returnType = "void";
    private final List<String> parameters = new ArrayList<>();
    private final List<String> annotations = new ArrayList<>();
    private final List<String> throwsList = new ArrayList<>();

    private MethodSource(String name) {
        this.name = name;
    }

    public static MethodSource method(String name) {
        return new MethodSource(name);
    }

    public MethodSource add(ParameterSource parameterSource) {
        parameters.add(parameterSource.toString());
        return this;
    }

    public MethodSource add(ThrowsSource throwsSource) {
        this.throwsList.add(throwsSource.toString());
        return this;
    }

    public MethodSource add(AnnotationSource annotationSource) {
        annotations.add(annotationSource.toString());
        return this;
    }

    public MethodSource addIf(AnnotationSource annotationSource, boolean check) {
        if (check) {
            return add(annotationSource);
        }
        return this;
    }

    public MethodSource set(CodeBody body) {
        this.body = body.toString();
        return this;
    }

    public MethodSource set(ReturnSource returnSource) {
        this.returnType = returnSource.toString();
        return this;
    }

    public MethodSource set(VisibilitySource visibilitySource) {
        this.visibility = visibilitySource.toString();
        return this;
    }

    public MethodSource set(StaticSource staticSource) {
        this.staticModifier = staticSource.toString();
        return this;
    }

    public MethodSource set(FinalSource finalSource) {
        this.finalModifier = finalSource.toString();
        return this;
    }

    @Override
    public String toString() {
        return CodeBody.body("""
                        $annotations
                        $visibility $static $final $ReturnType $name($parameters) $throws {
                            $body
                        }
                        """,
                param("annotations", annotations, "\n"),
                param("visibility", visibility),
                param("static", staticModifier),
                param("final", finalModifier),
                param("ReturnType", returnType),
                param("name", name),
                param("parameters", parameters),
                param("throws", throwsList, ", ", "throws ", "", ""),
                param("body", body)
        ).toString();
    }
}
