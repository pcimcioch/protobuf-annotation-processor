package com.github.pcimcioch.protobuf.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Enumeration element
 */
@Retention(RetentionPolicy.SOURCE)
public @interface EnumerationElement {

    /**
     * Element name
     *
     * @return name
     */
    String name();

    /**
     * Enumeration number
     *
     * @return number
     */
    int number();
}
