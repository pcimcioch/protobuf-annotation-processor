package org.github.pcimcioch.protobuf.source;

import org.github.pcimcioch.protobuf.model.FieldDefinition;
import org.github.pcimcioch.protobuf.model.MessageDefinition;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.util.stream.Collectors;

// TODO should be record's inner class
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
                .setPackage(message.messageTypePackage())
                .setName(message.builderSimpleName());
    }

    private void addFields(JavaClassSource builderClass, MessageDefinition message) {
        for (FieldDefinition field : message.fields()) {
            builderClass.addField()
                    .setName(field.name())
                    .setPrivate()
                    .setType(field.typeName())
                    .setLiteralInitializer(field.defaultValue());
        }
    }

    private void addBuildMethod(JavaClassSource builderClass, MessageDefinition message) {
        String parameters = message.fields().stream()
                .map(FieldDefinition::name)
                .collect(Collectors.joining(", "));
        String body = "return new " + message.messageTypeSimpleName() + "(" + parameters + ");";

        builderClass.addMethod()
                .setPublic()
                .setName("build")
                .setReturnType(message.messageTypeSimpleName())
                .setBody(body);
    }

    private void addSetterMethods(JavaClassSource builderClass, MessageDefinition message) {
        for (FieldDefinition field : message.fields()) {
            String body = "this." + field.name() + "=" + field.name() + ";\n" +
                    "return this;";

            builderClass.addMethod()
                    .setPublic()
                    .setName(field.name())
                    .setReturnType(message.builderSimpleName())
                    .setBody(body)
                    .addParameter(field.typeName(), field.name());
        }
    }
}
