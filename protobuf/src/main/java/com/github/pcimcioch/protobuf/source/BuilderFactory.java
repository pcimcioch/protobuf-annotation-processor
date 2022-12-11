package com.github.pcimcioch.protobuf.source;

import com.github.pcimcioch.protobuf.code.MethodBody;
import com.github.pcimcioch.protobuf.model.field.FieldDefinition;
import com.github.pcimcioch.protobuf.model.message.MessageDefinition;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.util.List;

import static com.github.pcimcioch.protobuf.code.MethodBody.body;
import static com.github.pcimcioch.protobuf.code.MethodBody.param;

class BuilderFactory {

    JavaClassSource buildBuilder(MessageDefinition message) {
        JavaClassSource builderClass = buildSourceFile(message);

        addFieldCode(builderClass, message);
        addBuildMethod(builderClass, message);

        return builderClass;
    }

    private JavaClassSource buildSourceFile(MessageDefinition message) {
        return Roaster.create(JavaClassSource.class)
                .setPublic()
                .setStatic(true)
                .setFinal(true)
                .setPackage(message.builderName().packageName())
                .setName(message.builderName().simpleName());
    }

    private void addFieldCode(JavaClassSource builderClass, MessageDefinition message) {
        for (FieldDefinition field : message.fields()) {
            field.addBuilderCode(builderClass);
        }
    }

    private void addBuildMethod(JavaClassSource builderClass, MessageDefinition message) {
        List<String> constructorParameters = message.fields().stream()
                .map(FieldDefinition::builderField)
                .toList();

        MethodBody body = body("return new $MessageType($constructorParameters);",
                param("MessageType", message.name()),
                param("constructorParameters", constructorParameters));

        builderClass.addMethod()
                .setPublic()
                .setReturnType(message.name().canonicalName())
                .setName("build")
                .setBody(body.toString());
    }
}
