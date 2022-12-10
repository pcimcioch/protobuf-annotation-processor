package com.github.pcimcioch.protobuf.model;

import com.github.pcimcioch.protobuf.code.MethodBody;
import org.jboss.forge.roaster.model.source.AnnotationTargetSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaRecordSource;
import org.jboss.forge.roaster.model.source.MethodSource;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Message field
 */
public abstract class FieldDefinition {

    private final String name;
    private final int number;
    private final TypeName type;
    private final boolean deprecated;

    FieldDefinition(String name, int number, TypeName type, boolean deprecated) {
        this.name = Valid.name(name);
        this.number = Valid.number(number);
        this.type = Valid.type(type);
        this.deprecated = deprecated;
    }

    /**
     * Returns number used by this field
     *
     * @return number
     */
    public int number() {
        return number;
    }

    /**
     * Returns field names reserved by this field
     *
     * @return field names
     */
    abstract public List<String> fieldNames();

    /**
     * Adds builder code. Normally it is field and setter method[s]
     *
     * @param builderClass builder class
     */
    abstract public void addBuilderCode(JavaClassSource builderClass);

    /**
     * Returns field name that has to be passed to the message record constructor when building
     *
     * @return field name
     */
    abstract public String builderField();

    /**
     * Returns code for decoding this field
     *
     * @return decoding code
     */
    abstract public MethodBody decodingCode();

    /**
     * Returns code for encoding this field
     *
     * @return encoding code
     */
    abstract public MethodBody encodingCode();

    /**
     * Adds message code. Normally it is field and sometimes getter methods
     *
     * @param messageRecord message record
     */
    abstract public void addMessageCode(JavaRecordSource messageRecord);

    /**
     * Adds constructor code. Normally parameter and field assignment with optional validation
     *
     * @param constructor message constructor
     */
    abstract public void addMessageConstructorCode(MethodSource<JavaRecordSource> constructor);

    String name() {
        return name;
    }

    TypeName type() {
        return type;
    }

    void applyDeprecated(AnnotationTargetSource<?, ?> source) {
        if (deprecated) {
            source.addAnnotation(Deprecated.class);
        }
    }

    /**
     * Validations for basic field components
     */
    private static final class Valid {
        private static final Pattern namePattern = Pattern.compile("^[a-zA-z_][a-zA-Z0-9_]*$");

        private static int number(int number) {
            if (number <= 0) {
                throw new IllegalArgumentException("Number must be positive, but was: " + number);
            }

            return number;
        }

        private static String name(String name) {
            if (name == null) {
                throw new IllegalArgumentException("Incorrect field name: <null>");
            }
            if (!namePattern.matcher(name).matches()) {
                throw new IllegalArgumentException("Incorrect field name: " + name);
            }

            return name;
        }

        private static TypeName type(TypeName type) {
            if (type == null) {
                throw new IllegalArgumentException("Must provide enum type");
            }

            return type;
        }
    }
}
