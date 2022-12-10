package com.github.pcimcioch.protobuf.source;

import com.github.pcimcioch.protobuf.code.MethodBody;
import com.github.pcimcioch.protobuf.dto.ProtobufEnumeration;
import com.github.pcimcioch.protobuf.model.EnumerationDefinition;
import com.github.pcimcioch.protobuf.model.EnumerationElementDefinition;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaEnumSource;
import org.jboss.forge.roaster.model.source.MethodSource;

import java.util.HashSet;
import java.util.Set;

import static com.github.pcimcioch.protobuf.code.MethodBody.body;
import static com.github.pcimcioch.protobuf.code.MethodBody.param;
import static com.github.pcimcioch.protobuf.model.EnumerationElementDefinition.UNRECOGNIZED_ELEMENT_NAME;

// TODO add tests for enums
final class EnumerationFactory {

    JavaEnumSource buildEnumerationEnum(EnumerationDefinition enumeration) {
        JavaEnumSource source = buildSourceFile(enumeration);
        addElements(source, enumeration);
        addFields(source);
        addConstructor(source);
        addNumberMethod(source);
        addFactoryMethod(source, enumeration);

        return source;
    }

    private JavaEnumSource buildSourceFile(EnumerationDefinition enumeration) {
        return Roaster.create(JavaEnumSource.class)
                .setPackage(enumeration.name().packageName())
                .setName(enumeration.name().simpleName())
                .addInterface(ProtobufEnumeration.class);
    }

    private void addElements(JavaEnumSource source, EnumerationDefinition enumeration) {
        for (EnumerationElementDefinition element : enumeration.elements()) {
            source.addEnumConstant()
                    .setName(element.name())
                    .setConstructorArguments(String.valueOf(element.number()));
        }

        source.addEnumConstant()
                .setName(UNRECOGNIZED_ELEMENT_NAME)
                .setConstructorArguments("-1");
    }

    private void addFields(JavaEnumSource source) {
        source.addField()
                .setPrivate()
                .setFinal(true)
                .setName("number");
    }

    private void addConstructor(JavaEnumSource source) {
        MethodBody body = body("this.number = number;");

        MethodSource<JavaEnumSource> constructor = source.addMethod()
                .setConstructor(true)
                .setBody(body.toString());
        constructor.addParameter(int.class, "number");
    }

    private void addNumberMethod(JavaEnumSource source) {
        MethodBody body = body("""
                        if (this == $unrecognized) {
                          throw new IllegalArgumentException("Unrecognized enum does not have a number");
                        }
                        return number;
                        """,
                param("unrecognized", UNRECOGNIZED_ELEMENT_NAME)
        );

        MethodSource<JavaEnumSource> method = source.addMethod()
                .setPublic()
                .setReturnType(int.class)
                .setName("number")
                .setBody(body.toString());
        method.addAnnotation(Override.class);
    }

    private void addFactoryMethod(JavaEnumSource source, EnumerationDefinition enumeration) {
        MethodBody body = body();

        body.append("return switch(number) {");
        Set<Integer> numbers = new HashSet<>();
        for (EnumerationElementDefinition element : enumeration.elements()) {
            if (numbers.add(element.number())) {
                body.append("case $number -> $name;",
                        param("number", element.number()),
                        param("name", element.name())
                );
            }
        }
        body.append("default -> $unrecognized;",
                param("unrecognized", UNRECOGNIZED_ELEMENT_NAME)
        );
        body.append("};");

        MethodSource<JavaEnumSource> method = source.addMethod()
                .setPublic()
                .setStatic(true)
                .setReturnType(enumeration.name().canonicalName())
                .setName("forNumber")
                .setBody(body.toString());
        method.addParameter(int.class, "number");
    }
}
