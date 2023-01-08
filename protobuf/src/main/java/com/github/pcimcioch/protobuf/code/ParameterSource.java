package com.github.pcimcioch.protobuf.code;

import java.util.ArrayList;
import java.util.List;

import static com.github.pcimcioch.protobuf.code.CodeBody.body;
import static com.github.pcimcioch.protobuf.code.CodeBody.param;

/**
 * Parameter source
 */
public final class ParameterSource {
    private final String type;
    private final String name;
    private final List<String> annotations = new ArrayList<>();

    private ParameterSource(String type, String name) {
        this.type = type;
        this.name = name;
    }

    /**
     * Create new parameter source
     *
     * @param type type
     * @param name name
     * @return parameter source
     */
    public static ParameterSource parameter(TypeName type, String name) {
        return new ParameterSource(type.canonicalName(), name);
    }

    /**
     * Create new parameter source
     *
     * @param clazz type
     * @param name  name
     * @return parameter source
     */
    public static ParameterSource parameter(Class<?> clazz, String name) {
        return new ParameterSource(clazz.getCanonicalName(), name);
    }

    /**
     * Add annotation
     *
     * @param annotationSource annotation
     * @return source
     */
    public ParameterSource add(AnnotationSource annotationSource) {
        this.annotations.add(annotationSource.toString());
        return this;
    }

    /**
     * Add annotation if condition is fulfilled
     *
     * @param annotationSource annotation
     * @param condition        condition
     * @return source
     */
    public ParameterSource addIf(AnnotationSource annotationSource, boolean condition) {
        if (condition) {
            return add(annotationSource);
        }
        return this;
    }

    @Override
    public String toString() {
        return body("$annotations $type $name",
                param("type", type),
                param("name", name),
                param("annotations", annotations, " ")
        ).toString();
    }
}
