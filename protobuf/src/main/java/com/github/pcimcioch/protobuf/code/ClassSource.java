package com.github.pcimcioch.protobuf.code;

import java.util.ArrayList;
import java.util.List;

import static com.github.pcimcioch.protobuf.code.CodeBody.body;
import static com.github.pcimcioch.protobuf.code.CodeBody.param;

public final class ClassSource extends Source {
    private String visibility = "";
    private String staticModifier = "";
    private String finalModifier = "";
    private final List<String> implementsList = new ArrayList<>();
    private final List<String> fields = new ArrayList<>();
    private final List<String> constructors = new ArrayList<>();
    private final List<String> methods = new ArrayList<>();

    private ClassSource(String packageName, String simpleName) {
        super(packageName, simpleName);
    }

    public static ClassSource clazz(TypeName name) {
        return new ClassSource(name.packageName(), name.simpleName());
    }

    public ClassSource set(VisibilitySource visibilitySource) {
        this.visibility = visibilitySource.toString();
        return this;
    }

    public ClassSource set(StaticSource staticSource) {
        this.staticModifier = staticSource.toString();
        return this;
    }

    public ClassSource set(FinalSource finalSource) {
        this.finalModifier = finalSource.toString();
        return this;
    }

    public ClassSource add(ImplementsSource implementsSource) {
        implementsList.add(implementsSource.toString());
        return this;
    }

    public ClassSource add(FieldSource fieldSource) {
        fields.add(fieldSource.toString());
        return this;
    }

    public ClassSource add(ConstructorSource constructorSource) {
        constructors.add(constructorSource.toString(simpleName()));
        return this;
    }

    public ClassSource add(MethodSource methodSource) {
        methods.add(methodSource.toString());
        return this;
    }

    @Override
    protected String typeOnlyCode() {
        return body("""             
                        $visibility $static $final class $name $implements {
                            $fields
                            
                            $constructors
                            
                            $methods
                        }
                        """,
                param("visibility", visibility),
                param("static", staticModifier),
                param("final", finalModifier),
                param("name", simpleName()),
                param("implements", implementsList, ", ", "implements ", "", ""),
                param("fields", fields, ";\n"),
                param("constructors", constructors, "\n\n"),
                param("methods", methods, "\n\n")
        ).toString();
    }
}
