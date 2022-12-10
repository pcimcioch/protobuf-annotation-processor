package com.github.pcimcioch.protobuf.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enumeration
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Repeatable(Enumerations.class)
public @interface Enumeration {

    /**
     * Enumeration name
     *
     * @return name
     */
    String name();

    /**
     * Option allow_alias
     *
     * @return allow_alias option
     */
    boolean allowAlias() default false;

    /**
     * Enumeration elements
     *
     * @return elements
     */
    EnumerationElement[] elements();

    /**
     * Reserved elements and numbers
     *
     * @return reserved
     */
    Reserved reserved() default @Reserved;
}
