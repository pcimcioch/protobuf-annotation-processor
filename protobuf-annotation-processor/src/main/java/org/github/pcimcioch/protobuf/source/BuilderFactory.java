package org.github.pcimcioch.protobuf.source;

import org.github.pcimcioch.protobuf.model.FieldDefinition;
import org.github.pcimcioch.protobuf.model.MessageDefinition;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import static org.github.pcimcioch.protobuf.source.MethodBody.body;
import static org.github.pcimcioch.protobuf.source.MethodBody.param;

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
        MethodBody body = body("return new ${MessageType}(${ConstructorParameters});",
                param("MessageType", message.messageTypeSimpleName()),
                param("ConstructorParameters", message.fields()));

        builderClass.addMethod()
                .setPublic()
                .setName("build")
                .setReturnType(message.messageTypeSimpleName())
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
                    .setReturnType(message.builderSimpleName())
                    .setBody(body.toString())
                    .addParameter(field.typeName(), field.name());
        }
    }
}
