package com.github.pcimcioch.protobuf.code;

import java.util.ArrayList;
import java.util.List;

import static com.github.pcimcioch.protobuf.code.CodeBody.body;
import static com.github.pcimcioch.protobuf.code.CodeBody.param;

/**
 * Enum source
 */
public final class EnumSource extends Source {
    private String visibility = "";
    private final List<String> elements = new ArrayList<>();
    private final List<String> implementsList = new ArrayList<>();
    private final List<String> fields = new ArrayList<>();
    private final List<String> constructors = new ArrayList<>();
    private final List<String> methods = new ArrayList<>();

    private EnumSource(TypeName type) {
        super(type);
    }

    /**
     * Create new enum source
     *
     * @param type type
     * @return enum source
     */
    public static EnumSource enumeration(TypeName type) {
        return new EnumSource(type);
    }

    /**
     * Add enum element
     *
     * @param enumElementSource enum element
     * @return source
     */
    public EnumSource add(EnumElementSource enumElementSource) {
        elements.add(enumElementSource.toString());
        return this;
    }

    /**
     * Set visibility
     *
     * @param visibilitySource visibility
     * @return source
     */
    public EnumSource set(VisibilitySource visibilitySource) {
        this.visibility = visibilitySource.toString();
        return this;
    }

    /**
     * Add implements
     *
     * @param implementsSource implements
     * @return source
     */
    public EnumSource add(ImplementsSource implementsSource) {
        implementsList.add(implementsSource.toString());
        return this;
    }

    /**
     * Adds field
     *
     * @param fieldSource field
     * @return source
     */
    public EnumSource add(FieldSource fieldSource) {
        fields.add(fieldSource.toString());
        return this;
    }

    /**
     * Add constructor
     *
     * @param constructorSource constructor
     * @return source
     */
    public EnumSource add(ConstructorSource constructorSource) {
        constructors.add(constructorSource.toString(simpleName()));
        return this;
    }

    /**
     * Add method
     *
     * @param methodSource method
     * @return source
     */
    public EnumSource add(MethodSource methodSource) {
        methods.add(methodSource.toString());
        return this;
    }

    @Override
    protected String typeOnlyCode() {
        return body("""             
                        $visibility enum $name $implements {
                            $elements
                            
                            $fields
                            
                            $constructors
                            
                            $methods
                        }
                        """,
                param("visibility", visibility),
                param("name", simpleName()),
                param("implements", implementsList, ", ", "implements ", "", ""),
                param("elements", elements, ",\n", "", ";"),
                param("fields", fields, "\n"),
                param("constructors", constructors, "\n"),
                param("methods", methods, "\n")
        ).toString();
    }
}
