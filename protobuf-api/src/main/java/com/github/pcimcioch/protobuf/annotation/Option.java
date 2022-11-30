package com.github.pcimcioch.protobuf.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Option
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Option {
    /**
     * Option name
     *
     * @return name
     */
    String name();

    /**
     * Option value
     *
     * @return value
     */
    String value();

    /**
     * Set default java package for all structures in this file
     */
    String javaPackage = "java_package";
}
