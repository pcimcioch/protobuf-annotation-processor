package com.github.pcimcioch.protobuf.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enables java_multiple_files option
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface JavaMultipleFiles {

    /**
     * Set option java_outer_classname
     *
     * @return outer classname
     */
    String javaOuterClassName();
}
