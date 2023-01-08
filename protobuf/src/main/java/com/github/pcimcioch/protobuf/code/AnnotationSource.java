package com.github.pcimcioch.protobuf.code;

import java.lang.annotation.Annotation;

/**
 * Annotation source
 */
public final class AnnotationSource {
    private final String name;

    private AnnotationSource(String name) {
        this.name = name;
    }

    /**
     * Create new annotation source
     *
     * @param annotation annotation class
     * @return annotation source
     */
    public static AnnotationSource annotation(Class<? extends Annotation> annotation) {
        return new AnnotationSource(annotation.getSimpleName());
    }

    @Override
    public String toString() {
        return "@" + name;
    }
}
