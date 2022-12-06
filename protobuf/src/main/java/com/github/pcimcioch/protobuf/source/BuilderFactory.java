package com.github.pcimcioch.protobuf.source;

import com.github.pcimcioch.protobuf.code.MethodBody;
import com.github.pcimcioch.protobuf.model.FieldDefinition;
import com.github.pcimcioch.protobuf.model.MessageDefinition;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import static com.github.pcimcioch.protobuf.code.MethodBody.body;
import static com.github.pcimcioch.protobuf.code.MethodBody.param;

class BuilderFactory {

    JavaClassSource buildBuilder(MessageDefinition message) {
        JavaClassSource builderClass = buildSourceFile(message);

        addFields(builderClass, message);
        addBuildMethod(builderClass, message);
        addSetterMethods(builderClass, message);

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

    private void addFields(JavaClassSource builderClass, MessageDefinition message) {
        for (FieldDefinition field : message.fields()) {
            builderClass.addField()
                    .setPrivate()
                    .setType(field.typeName().canonicalName())
                    .setName(field.name())
                    .setLiteralInitializer(field.defaultValue());
        }
    }

    private void addBuildMethod(JavaClassSource builderClass, MessageDefinition message) {
        MethodBody body = body("return new $MessageType($ConstructorParameters);",
                param("MessageType", message.name()),
                param("ConstructorParameters", message.fields()));

        builderClass.addMethod()
                .setPublic()
                .setReturnType(message.name().canonicalName())
                .setName("build")
                .setBody(body.toString());
    }

    private void addSetterMethods(JavaClassSource builderClass, MessageDefinition message) {
        for (FieldDefinition field : message.fields()) {
            field.addBuilderMethods(builderClass);
        }
    }
}
