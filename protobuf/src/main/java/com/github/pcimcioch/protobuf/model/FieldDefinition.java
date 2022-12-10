package com.github.pcimcioch.protobuf.model;

import com.github.pcimcioch.protobuf.code.MethodBody;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaRecordSource;
import org.jboss.forge.roaster.model.source.MethodSource;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Message field
 */
public interface FieldDefinition {

    /**
     * Returns number used by this field
     *
     * @return number
     */
    int number();

    /**
     * Returns field names reserved by this field
     *
     * @return field names
     */
    List<String> fieldNames();

    /**
     * Adds builder code. Normally it is field and setter method[s]
     *
     * @param builderClass builder class
     */
    void addBuilderCode(JavaClassSource builderClass);

    /**
     * Returns field name that has to be passed to the message record constructor when building
     *
     * @return field name
     */
    String builderField();

    /**
     * Returns code for decoding this field
     *
     * @return decoding code
     */
    MethodBody decodingCode();

    /**
     * Returns code for encoding this field
     *
     * @return encoding code
     */
    MethodBody encodingCode();

    /**
     * Adds message code. Normally it is field and sometimes getter methods
     *
     * @param messageRecord message record
     */
    void addMessageCode(JavaRecordSource messageRecord);

    /**
     * Adds constructor code. Normally parameter and field assignment with optional validation
     *
     * @param constructor message constructor
     */
    void addMessageConstructorCode(MethodSource<JavaRecordSource> constructor);

    /**
     * Validations for basic field components
     */
    final class Valid {
        private static final Pattern namePattern = Pattern.compile("^[a-zA-z_][a-zA-Z0-9_]*$");

        static void number(int number) {
            if (number <= 0) {
                throw new IllegalArgumentException("Number must be positive, but was: " + number);
            }
        }

        static void name(String name) {
            if (name == null) {
                throw new IllegalArgumentException("Incorrect field name: <null>");
            }
            if (!namePattern.matcher(name).matches()) {
                throw new IllegalArgumentException("Incorrect field name: " + name);
            }
        }

        static void type(TypeName type) {
            if (type == null) {
                throw new IllegalArgumentException("Must provide enum type");
            }
        }
    }
}
