package com.github.pcimcioch.protobuf.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Message field
 */
@Retention(RetentionPolicy.SOURCE)
public @interface Field {
    /**
     * Type of the field. For predefined scalar values, you can use static String constants defined in this annotation
     *
     * @return type of the field
     */
    String type();

    /**
     * Name of the field
     *
     * @return name of the field
     */
    String name();

    /**
     * Number of the field
     *
     * @return number of the field
     */
    int number();

    /**
     * Whether field is deprecated
     *
     * @return whether field is deprecated
     */
    boolean deprecated() default false;

    /**
     * double
     */
    String _double = "double";

    /**
     * float
     */
    String _float = "float";

    /**
     * int32
     */
    String int32 = "int32";

    /**
     * int64
     */
    String int64 = "int64";

    /**
     * uint32
     */
    String uint32 = "uint32";

    /**
     * uint64
     */
    String uint64 = "uint64";

    /**
     * sint32
     */
    String sint32 = "sint32";

    /**
     * sint64
     */
    String sint64 = "sint64";

    /**
     * fixed32
     */
    String fixed32 = "fixed32";

    /**
     * fixed64
     */
    String fixed64 = "fixed64";

    /**
     * sfixed32
     */
    String sfixed32 = "sfixed32";

    /**
     * sfixed64
     */
    String sfixed64 = "sfixed64";

    /**
     * bool
     */
    String bool = "bool";

    /**
     * string
     */
    String string = "string";

    /**
     * bytes
     */
    String bytes = "bytes";
}
