package com.github.pcimcioch.protobuf.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Reserved names and values
 */
@Retention(RetentionPolicy.SOURCE)
public @interface Reserved {

    /**
     * Reserved names
     *
     * @return names
     */
    String[] names() default {};

    /**
     * Reserved numbers
     *
     * @return numbers
     */
    int[] numbers() default {};

    /**
     * Reserved number ranges
     *
     * @return ranges
     */
    Range[] ranges() default {};

    /**
     * Number range
     */
    @interface Range {
        /**
         * Lower bound of a range, inclusive)=
         *
         * @return lower bound
         */
        int from();

        /**
         * Upper bound of a range, inclusive
         *
         * @return upper bound
         */
        int to();
    }
}
