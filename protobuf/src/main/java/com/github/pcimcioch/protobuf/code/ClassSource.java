package com.github.pcimcioch.protobuf.code;

import java.util.ArrayList;
import java.util.List;

import static com.github.pcimcioch.protobuf.code.CodeBody.body;
import static com.github.pcimcioch.protobuf.code.CodeBody.param;

/**
 * Class source
 */
public final class ClassSource extends Source {
    private String visibility = "";
    private String staticModifier = "";
    private String finalModifier = "";
    private final List<String> fields = new ArrayList<>();
    private final List<String> constructors = new ArrayList<>();
    private final List<String> methods = new ArrayList<>();
    private final List<String> nested = new ArrayList<>();

    private ClassSource(TypeName type) {
        super(type);
    }

    /**
     * Create new class source
     *
     * @param type class type
     * @return class source
     */
    public static ClassSource clazz(TypeName type) {
        return new ClassSource(type);
    }

    /**
     * Set visibility
     *
     * @param visibilitySource visibility
     * @return source
     */
    public ClassSource set(VisibilitySource visibilitySource) {
        this.visibility = visibilitySource.toString();
        return this;
    }

    /**
     * Set static modifier
     *
     * @param staticSource static modifier
     * @return source
     */
    public ClassSource set(StaticSource staticSource) {
        this.staticModifier = staticSource.toString();
        return this;
    }

    /**
     * Set final modifier
     *
     * @param finalSource final modifier
     * @return source
     */
    public ClassSource set(FinalSource finalSource) {
        this.finalModifier = finalSource.toString();
        return this;
    }

    /**
     * Add constructor
     *
     * @param constructorSource constructor
     * @return source
     */
    public ClassSource add(ConstructorSource constructorSource) {
        constructors.add(constructorSource.toString(simpleName()));
        return this;
    }

    /**
     * Adds field
     *
     * @param fieldSource field
     * @return source
     */
    public ClassSource add(FieldSource fieldSource) {
        fields.add(fieldSource.toString());
        return this;
    }

    /**
     * Add method
     *
     * @param methodSource method
     * @return source
     */
    public ClassSource add(MethodSource methodSource) {
        methods.add(methodSource.toString());
        return this;
    }

    /**
     * Add nested source
     * @param nestedSource nested source
     * @return source
     */
    public ClassSource add(Source nestedSource) {
        nested.add(nestedSource.typeOnlyCode());
        return this;
    }

    @Override
    protected String typeOnlyCode() {
        return body("""             
                        $visibility $static $final class $name {
                            $fields
                            
                            $constructors
                            
                            $methods
                            
                            $nested
                        }
                        """,
                param("visibility", visibility),
                param("static", staticModifier),
                param("final", finalModifier),
                param("name", simpleName()),
                param("fields", fields, "\n"),
                param("constructors", constructors, "\n"),
                param("methods", methods, "\n"),
                param("nested", nested, "\n")
        ).toString();
    }
}
