package com.github.pcimcioch.protobuf.code;

import java.util.ArrayList;
import java.util.List;

import static com.github.pcimcioch.protobuf.code.CodeBody.body;
import static com.github.pcimcioch.protobuf.code.CodeBody.param;

public final class FieldSource {
    private final String type;
    private final String name;
    private String visibility = "";
    private String finalModifier = "";
    private String staticModifier = "";
    private String initializer = "";
    private final List<String> annotations = new ArrayList<>();

    private FieldSource(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public static FieldSource field(TypeName type, String name) {
        return new FieldSource(type.canonicalName(), name);
    }

    public static FieldSource field(Class<?> clazz, String name) {
        return new FieldSource(clazz.getCanonicalName(), name);
    }

    public FieldSource add(AnnotationSource annotationSource) {
        annotations.add(annotationSource.toString());
        return this;
    }

    public FieldSource addIf(AnnotationSource annotationSource, boolean check) {
        if (check) {
            return add(annotationSource);
        }
        return this;
    }

    public FieldSource set(InitializerSource initializerSource) {
        this.initializer = initializerSource.toString();
        return this;
    }

    public FieldSource set(VisibilitySource visibilitySource) {
        this.visibility = visibilitySource.toString();
        return this;
    }

    public FieldSource set(StaticSource staticSource) {
        this.staticModifier = staticSource.toString();
        return this;
    }

    public FieldSource set(FinalSource finalSource) {
        this.finalModifier = finalSource.toString();
        return this;
    }

    @Override
    public String toString() {
        String init = initializer.isEmpty() ? "" : (" = " + initializer);

        return body("$annotations $visibility $static $final $type $name$initializer;",
                param("annotations", annotations, " "),
                param("visibility", visibility),
                param("static", staticModifier),
                param("final", finalModifier),
                param("type", type),
                param("name", name),
                param("initializer", init)
        ).toString();
    }
}
