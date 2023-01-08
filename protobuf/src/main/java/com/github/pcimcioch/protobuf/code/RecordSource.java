package com.github.pcimcioch.protobuf.code;

import java.util.ArrayList;
import java.util.List;

import static com.github.pcimcioch.protobuf.code.CodeBody.body;
import static com.github.pcimcioch.protobuf.code.CodeBody.param;

public final class RecordSource extends Source {
    private String visibility = "";
    private String staticModifier = "";
    private String finalModifier = "";
    private final List<String> components = new ArrayList<>();
    private final List<String> canonicalConstructors = new ArrayList<>();
    private final List<String> implementsList = new ArrayList<>();
    private final List<String> fields = new ArrayList<>();
    private final List<String> constructors = new ArrayList<>();
    private final List<String> methods = new ArrayList<>();
    private final List<String> nested = new ArrayList<>();

    private RecordSource(String packageName, String simpleName) {
        super(packageName, simpleName);
    }

    public static RecordSource record(TypeName name) {
        return new RecordSource(name.packageName(), name.simpleName());
    }

    public RecordSource add(ParameterSource parameterSource) {
        components.add(parameterSource.toString());
        return this;
    }

    public RecordSource add(CanonicalConstructorSource canonicalConstructorSource) {
        canonicalConstructors.add(canonicalConstructorSource.toString(simpleName()));
        return this;
    }

    public RecordSource add(ImplementsSource implementsSource) {
        implementsList.add(implementsSource.toString());
        return this;
    }

    public RecordSource add(FieldSource fieldSource) {
        fields.add(fieldSource.toString());
        return this;
    }

    public RecordSource add(ConstructorSource constructorSource) {
        constructors.add(constructorSource.toString(simpleName()));
        return this;
    }

    public RecordSource add(MethodSource methodSource) {
        methods.add(methodSource.toString());
        return this;
    }

    public RecordSource add(Source nestedSource) {
        nested.add(nestedSource.typeOnlyCode());
        return this;
    }

    public RecordSource set(VisibilitySource visibilitySource) {
        this.visibility = visibilitySource.toString();
        return this;
    }

    public RecordSource set(FinalSource finalSource) {
        this.finalModifier = finalSource.toString();
        return this;
    }

    public RecordSource set(StaticSource staticSource) {
        this.staticModifier = staticSource.toString();
        return this;
    }

    @Override
    protected String typeOnlyCode() {
        return body("""             
                        $visibility $static $final record($components) $name $implements {
                            $fields
                            
                            $canonicalConstructors
                            
                            $constructors
                            
                            $methods
                            
                            $nested
                        }
                        """,
                param("visibility", visibility),
                param("static", staticModifier),
                param("final", finalModifier),
                param("components", components),
                param("name", simpleName()),
                param("implements", implementsList, ", ", "implements ", "", ""),
                param("fields", fields, ";\n"),
                param("canonicalConstructors", canonicalConstructors, "\n\n"),
                param("constructors", constructors, "\n\n"),
                param("methods", methods, "\n\n"),
                param("nested", nested, "\n\n")
        ).toString();
    }
}
