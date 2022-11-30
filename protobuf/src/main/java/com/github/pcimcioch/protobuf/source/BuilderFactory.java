package com.github.pcimcioch.protobuf.source;

import com.github.pcimcioch.protobuf.model.FieldDefinition;
import com.github.pcimcioch.protobuf.model.MessageDefinition;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import static com.github.pcimcioch.protobuf.source.MethodBody.body;
import static com.github.pcimcioch.protobuf.source.MethodBody.param;

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
                .setPackage(message.builderName().packageName())
                .setName(message.builderName().simpleName());
    }

    private void addFields(JavaClassSource builderClass, MessageDefinition message) {
        for (FieldDefinition field : message.fields()) {
            builderClass.addField()
                    .setName(field.name())
                    .setPrivate()
                    .setType(field.typeName().canonicalName())
                    .setLiteralInitializer(field.defaultValue());
        }
    }

    private void addBuildMethod(JavaClassSource builderClass, MessageDefinition message) {
        MethodBody body = body("return new ${MessageType}(${ConstructorParameters});",
                param("MessageType", message.name().canonicalName()),
                param("ConstructorParameters", message.fields()));

        builderClass.addMethod()
                .setPublic()
                .setName("build")
                .setReturnType(message.name().canonicalName())
                .setBody(body.toString());
    }

    private void addSetterMethods(JavaClassSource builderClass, MessageDefinition message) {
        for (FieldDefinition field : message.fields()) {
            MethodBody body = body("""
                            this.${field} = ${field};
                            return this;""",
                    param("field", field));

            builderClass.addMethod()
                    .setPublic()
                    .setName(field.name())
                    .setReturnType(message.builderName().canonicalName())
                    .setBody(body.toString())
                    .addParameter(field.typeName().canonicalName(), field.name());
        }
    }
}