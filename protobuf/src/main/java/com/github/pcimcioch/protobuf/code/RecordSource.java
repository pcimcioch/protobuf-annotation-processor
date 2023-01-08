package com.github.pcimcioch.protobuf.code;

import java.util.ArrayList;
import java.util.List;

import static com.github.pcimcioch.protobuf.code.CodeBody.body;
import static com.github.pcimcioch.protobuf.code.CodeBody.param;

/**
 * Record source
 */
public final class RecordSource extends Source {
    private String visibility = "";
    private final List<String> components = new ArrayList<>();
    private final List<String> compactConstructors = new ArrayList<>();
    private final List<String> implementsList = new ArrayList<>();
    private final List<String> fields = new ArrayList<>();
    private final List<String> methods = new ArrayList<>();
    private final List<String> nested = new ArrayList<>();

    private RecordSource(TypeName type) {
        super(type);
    }

    /**
     * Create new record source
     *
     * @param type type
     * @return record source
     */
    public static RecordSource record(TypeName type) {
        return new RecordSource(type);
    }

    /**
     * Add parameter
     *
     * @param parameterSource parameter
     * @return source
     */
    public RecordSource add(ParameterSource parameterSource) {
        components.add(parameterSource.toString());
        return this;
    }

    /**
     * Add compact constructor
     *
     * @param compactConstructorSource compact constructor
     * @return source
     */
    public RecordSource add(CompactConstructorSource compactConstructorSource) {
        compactConstructors.add(compactConstructorSource.toString(simpleName()));
        return this;
    }

    /**
     * Add implements
     *
     * @param implementsSource implements
     * @return source
     */
    public RecordSource add(ImplementsSource implementsSource) {
        implementsList.add(implementsSource.toString());
        return this;
    }

    /**
     * Adds field
     *
     * @param fieldSource field
     * @return source
     */
    public RecordSource add(FieldSource fieldSource) {
        fields.add(fieldSource.toString());
        return this;
    }

    /**
     * Add method
     *
     * @param methodSource method
     * @return source
     */
    public RecordSource add(MethodSource methodSource) {
        methods.add(methodSource.toString());
        return this;
    }

    /**
     * Add nested source
     * @param nestedSource nested source
     * @return source
     */
    public RecordSource add(Source nestedSource) {
        nested.add(nestedSource.typeOnlyCode());
        return this;
    }

    /**
     * Set visibility
     *
     * @param visibilitySource visibility
     * @return source
     */
    public RecordSource set(VisibilitySource visibilitySource) {
        this.visibility = visibilitySource.toString();
        return this;
    }

    @Override
    protected String typeOnlyCode() {
        return body("""             
                        $visibility record $name($components) $implements {
                            $fields
                            
                            $compactConstructors
                            
                            $methods
                            
                            $nested
                        }
                        """,
                param("visibility", visibility),
                param("components", components),
                param("name", simpleName()),
                param("implements", implementsList, ", ", "implements ", "", ""),
                param("fields", fields, "\n"),
                param("compactConstructors", compactConstructors, "\n"),
                param("methods", methods, "\n"),
                param("nested", nested, "\n")
        ).toString();
    }
}
