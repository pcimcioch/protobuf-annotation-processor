package com.github.pcimcioch.protobuf.source;

import com.github.pcimcioch.protobuf.code.CodeBody;
import com.github.pcimcioch.protobuf.code.EnumElementSource;
import com.github.pcimcioch.protobuf.code.EnumSource;
import com.github.pcimcioch.protobuf.dto.ProtobufEnumeration;
import com.github.pcimcioch.protobuf.model.message.EnumerationDefinition;
import com.github.pcimcioch.protobuf.model.message.EnumerationElementDefinition;

import java.util.HashSet;
import java.util.Set;

import static com.github.pcimcioch.protobuf.code.AnnotationSource.annotation;
import static com.github.pcimcioch.protobuf.code.CodeBody.body;
import static com.github.pcimcioch.protobuf.code.CodeBody.param;
import static com.github.pcimcioch.protobuf.code.ConstructorSource.constructor;
import static com.github.pcimcioch.protobuf.code.EnumElementSource.element;
import static com.github.pcimcioch.protobuf.code.EnumSource.enumeration;
import static com.github.pcimcioch.protobuf.code.FieldSource.field;
import static com.github.pcimcioch.protobuf.code.FinalSource.finalModifier;
import static com.github.pcimcioch.protobuf.code.ImplementsSource.implementz;
import static com.github.pcimcioch.protobuf.code.MethodSource.method;
import static com.github.pcimcioch.protobuf.code.ParameterSource.parameter;
import static com.github.pcimcioch.protobuf.code.ReturnSource.returns;
import static com.github.pcimcioch.protobuf.code.StaticSource.staticModifier;
import static com.github.pcimcioch.protobuf.code.VisibilitySource.privateVisibility;
import static com.github.pcimcioch.protobuf.code.VisibilitySource.publicVisibility;
import static com.github.pcimcioch.protobuf.model.message.EnumerationElementDefinition.UNRECOGNIZED_ELEMENT_NAME;

class EnumerationFactory {

    private static final EnumElementSource UNRECOGNIZED_ELEMENT = element(UNRECOGNIZED_ELEMENT_NAME, -1);

    EnumSource buildEnumerationEnum(EnumerationDefinition enumeration) {
        EnumSource source = buildSourceFile(enumeration);
        addElements(source, enumeration);
        addNumberField(source);
        addConstructor(source);
        addNumberMethod(source);
        addFactoryMethod(source, enumeration);
        addDefaultValueMethod(source, enumeration);

        return source;
    }

    private EnumSource buildSourceFile(EnumerationDefinition enumeration) {
        return enumeration(enumeration.name())
                .set(publicVisibility())
                .add(implementz(ProtobufEnumeration.class));
    }

    private void addElements(EnumSource source, EnumerationDefinition enumeration) {
        for (EnumerationElementDefinition element : enumeration.elements()) {
            source.add(element(element.name(), element.number()));
        }

        source.add(UNRECOGNIZED_ELEMENT);
    }

    private void addNumberField(EnumSource source) {
        source.add(field(int.class, "number")
                .set(privateVisibility())
                .set(finalModifier())
        );
    }

    private void addConstructor(EnumSource source) {
        source.add(constructor()
                .set(body("this.number = number;"))
                .add(parameter(int.class, "number"))
        );
    }

    private void addNumberMethod(EnumSource source) {
        CodeBody body = body("""
                        if (this == $unrecognized) {
                          throw new IllegalArgumentException("Unrecognized enum does not have a number");
                        }
                        return number;""",
                param("unrecognized", UNRECOGNIZED_ELEMENT_NAME)
        );

        source.add(method("number")
                .set(publicVisibility())
                .set(returns(int.class))
                .set(body)
                .add(annotation(Override.class))
        );
    }

    private void addFactoryMethod(EnumSource source, EnumerationDefinition enumeration) {
        CodeBody body = body();

        body.appendln("return switch(number) {");

        Set<Integer> numbers = new HashSet<>();
        for (EnumerationElementDefinition element : enumeration.elements()) {
            if (numbers.add(element.number())) {
                body.appendln("case $number -> $name;",
                        param("number", element.number()),
                        param("name", element.name())
                );
            }
        }
        body
                .appendln("default -> $unrecognized;",
                        param("unrecognized", UNRECOGNIZED_ELEMENT_NAME)
                )
                .append("};");

        source.add(method("forNumber")
                .set(publicVisibility())
                .set(staticModifier())
                .set(returns(enumeration.name()))
                .set(body)
                .add(parameter(int.class, "number"))
        );
    }

    private void addDefaultValueMethod(EnumSource source, EnumerationDefinition enumeration) {
        CodeBody body = body("return $ELEMENT;",
                param("ELEMENT", enumeration.defaultElement().name())
        );

        source.add(method("defaultValue")
                .set(publicVisibility())
                .set(staticModifier())
                .set(returns(enumeration.name()))
                .set(body)
        );
    }
}
