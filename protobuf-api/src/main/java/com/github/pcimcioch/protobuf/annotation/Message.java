package com.github.pcimcioch.protobuf.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Message
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Message {

    /**
     * Package of the Message. Will be translated to java package
     *
     * @return package of the message
     */
    String packageName();

    /**
     * Message name
     *
     * @return message name
     */
    String name();

    /**
     * Fields of this message
     *
     * @return fields
     */
    Field[] fields();
}
