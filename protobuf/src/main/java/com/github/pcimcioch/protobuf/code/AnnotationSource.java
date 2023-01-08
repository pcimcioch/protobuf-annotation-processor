package com.github.pcimcioch.protobuf.code;

import java.lang.annotation.Annotation;

public final class AnnotationSource {
    private final String name;

    private AnnotationSource(String name) {
        this.name = name;
    }

    public static AnnotationSource annotation(Class<? extends Annotation> annotation) {
        return new AnnotationSource(annotation.getSimpleName());
    }

    @Override
    public String toString() {
        return "@" + name;
    }
}
