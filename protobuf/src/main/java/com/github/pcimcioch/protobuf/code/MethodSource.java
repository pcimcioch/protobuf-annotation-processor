package com.github.pcimcioch.protobuf.code;

import java.util.ArrayList;
import java.util.List;

import static com.github.pcimcioch.protobuf.code.CodeBody.param;

/**
 * Method source
 */
public final class MethodSource {
    private final String name;
    private String visibility = "";
    private String staticModifier = "";
    private String body = "";
    private String returnType = "void";
    private final List<String> parameters = new ArrayList<>();
    private final List<String> annotations = new ArrayList<>();
    private final List<String> throwsList = new ArrayList<>();

    private MethodSource(String name) {
        this.name = name;
    }

    /**
     * Create new method source
     *
     * @param name name
     * @return method source
     */
    public static MethodSource method(String name) {
        return new MethodSource(name);
    }

    /**
     * Add parameter
     *
     * @param parameterSource parameter
     * @return source
     */
    public MethodSource add(ParameterSource parameterSource) {
        parameters.add(parameterSource.toString());
        return this;
    }

    /**
     * Add throws
     *
     * @param throwsSource throws
     * @return source
     */
    public MethodSource add(ThrowsSource throwsSource) {
        this.throwsList.add(throwsSource.toString());
        return this;
    }

    /**
     * Add annotation
     *
     * @param annotationSource annotation
     * @return source
     */
    public MethodSource add(AnnotationSource annotationSource) {
        annotations.add(annotationSource.toString());
        return this;
    }

    /**
     * Add annotation if condition is fulfilled
     *
     * @param annotationSource annotation
     * @param condition        condition
     * @return source
     */
    public MethodSource addIf(AnnotationSource annotationSource, boolean condition) {
        if (condition) {
            return add(annotationSource);
        }
        return this;
    }

    /**
     * Set body
     *
     * @param body body
     * @return source
     */
    public MethodSource set(CodeBody body) {
        this.body = body.toString();
        return this;
    }

    /**
     * Set returns
     *
     * @param returnSource returns
     * @return source
     */
    public MethodSource set(ReturnSource returnSource) {
        this.returnType = returnSource.toString();
        return this;
    }

    /**
     * Set visibility
     *
     * @param visibilitySource visibility
     * @return source
     */
    public MethodSource set(VisibilitySource visibilitySource) {
        this.visibility = visibilitySource.toString();
        return this;
    }

    /**
     * Set static modifier
     *
     * @param staticSource static modifier
     * @return source
     */
    public MethodSource set(StaticSource staticSource) {
        this.staticModifier = staticSource.toString();
        return this;
    }

    @Override
    public String toString() {
        return CodeBody.body("""
                        $annotations
                        $visibility $static $ReturnType $name($parameters) $throws {
                            $body
                        }
                        """,
                param("annotations", annotations, "\n"),
                param("visibility", visibility),
                param("static", staticModifier),
                param("ReturnType", returnType),
                param("name", name),
                param("parameters", parameters),
                param("throws", throwsList, ", ", "throws ", "", ""),
                param("body", body)
        ).toString();
    }
}
