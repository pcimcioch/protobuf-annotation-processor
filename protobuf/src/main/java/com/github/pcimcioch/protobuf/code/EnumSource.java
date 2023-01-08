package com.github.pcimcioch.protobuf.code;

import java.util.ArrayList;
import java.util.List;

import static com.github.pcimcioch.protobuf.code.CodeBody.body;
import static com.github.pcimcioch.protobuf.code.CodeBody.param;

public final class EnumSource extends Source {
    private String visibility = "";
    private final List<String> elements = new ArrayList<>();
    private final List<String> implementsList = new ArrayList<>();
    private final List<String> fields = new ArrayList<>();
    private final List<String> constructors = new ArrayList<>();
    private final List<String> methods = new ArrayList<>();

    private EnumSource(String packageName, String simpleName) {
        super(packageName, simpleName);
    }

    public static EnumSource enumeration(TypeName name) {
        return new EnumSource(name.packageName(), name.simpleName());
    }

    public EnumSource add(EnumElementSource enumElementSource) {
        elements.add(enumElementSource.toString());
        return this;
    }

    public EnumSource set(VisibilitySource visibilitySource) {
        this.visibility = visibilitySource.toString();
        return this;
    }

    public EnumSource add(ImplementsSource implementsSource) {
        implementsList.add(implementsSource.toString());
        return this;
    }

    public EnumSource add(FieldSource fieldSource) {
        fields.add(fieldSource.toString());
        return this;
    }

    public EnumSource add(ConstructorSource constructorSource) {
        constructors.add(constructorSource.toString(simpleName()));
        return this;
    }

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
                param("fields", fields, ";\n"),
                param("constructors", constructors, "\n\n"),
                param("methods", methods, "\n\n")
        ).toString();
    }
}
