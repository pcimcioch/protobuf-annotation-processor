package com.github.pcimcioch.protobuf.source;

import com.github.pcimcioch.protobuf.dto.ProtobufMessage;
import com.github.pcimcioch.protobuf.model.EnumerationDefinition;
import com.github.pcimcioch.protobuf.model.EnumerationElementDefinition;
import com.github.pcimcioch.protobuf.model.FieldDefinition;
import com.github.pcimcioch.protobuf.model.MessageDefinition;
import com.github.pcimcioch.protobuf.model.ProtoDefinitions;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaEnumSource;
import org.jboss.forge.roaster.model.source.JavaRecordSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.source.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.github.pcimcioch.protobuf.model.EnumerationElementDefinition.UNRECOGNIZED_ELEMENT_NAME;
import static com.github.pcimcioch.protobuf.source.MethodBody.body;
import static com.github.pcimcioch.protobuf.source.MethodBody.param;

/**
 * Creates java source code for the protobuf transfer objects
 */
public class SourceFactory {

    private final MessageFactory messageFactory = new MessageFactory();
    private final EnumerationFactory enumerationFactory = new EnumerationFactory();

    /**
     * Represents java source file
     */
    public static final class SourceFile {
        private final JavaSource<?> source;

        private SourceFile(JavaSource<?> source) {
            this.source = source;
        }

        /**
         * Returns canonical java file name
         *
         * @return canonical name
         */
        public String canonicalName() {
            return source.getCanonicalName();
        }

        /**
         * Returns java source code of the file
         *
         * @return source code
         */
        public String source() {
            return source.toString();
        }
    }

    /**
     * Builds java source files from the protobuf model
     *
     * @param model model
     * @return java source files
     */
    public List<SourceFile> buildSource(ProtoDefinitions model) {
        List<SourceFile> sources = new ArrayList<>();

        for (MessageDefinition message : model.messages()) {
            sources.add(new SourceFile(messageFactory.buildMessageRecord(message)));
        }
        for (EnumerationDefinition enumeration : model.enumerations()) {
            sources.add(new SourceFile(enumerationFactory.buildEnumerationEnum(enumeration)));
        }

        return sources;
    }
}

final class MessageFactory {
    private final EncodingFactory encodingFactory = new EncodingFactory();
    private final DecodingFactory decodingFactory = new DecodingFactory();
    private final BuilderFactory builderFactory = new BuilderFactory();

    JavaRecordSource buildMessageRecord(MessageDefinition message) {
        JavaRecordSource source = buildSourceFile(message);
        addConstructor(source, message);
        addRecordComponents(source, message);
        addEncodingMethods(source, message);
        addDecodingMethods(source, message);
        addBuilderMethod(source, message);
        addBuilderClass(source, message);

        return source;
    }

    private JavaRecordSource buildSourceFile(MessageDefinition message) {
        return Roaster.create(JavaRecordSource.class)
                .setPackage(message.name().packageName())
                .setName(message.name().simpleName())
                .addInterface(ProtobufMessage.class);
    }

    // TODO it would be better to use compact constructor here. Waiting for https://github.com/forge/roaster/issues/275
    private void addConstructor(JavaRecordSource source, MessageDefinition message) {
        MethodBody body = body();
        for (FieldDefinition field : message.fields()) {
            if (field.requireNonNull()) {
                body.append("this.$fieldName = $Objects.requireNonNull($fieldName, \"Field $fieldName cannot be null\");",
                        param("fieldName", field),
                        param("Objects", Objects.class));
            } else {
                body.append("this.$fieldName = $fieldName;",
                        param("fieldName", field));
            }
        }

        MethodSource<JavaRecordSource> constructor = source.addMethod()
                .setPublic()
                .setConstructor(true)
                .setBody(body.toString());

        for (FieldDefinition field : message.fields()) {
            constructor.addParameter(field.typeName().canonicalName(), field.name());
        }
    }

    private void addRecordComponents(JavaRecordSource source, MessageDefinition message) {
        for (FieldDefinition field : message.fields()) {
            source.addRecordComponent(field.typeName().canonicalName(), field.name());
        }
    }

    private void addEncodingMethods(JavaRecordSource source, MessageDefinition message) {
        encodingFactory.addEncodingMethods(source, message);
    }

    private void addDecodingMethods(JavaRecordSource source, MessageDefinition message) {
        decodingFactory.addDecodingMethods(source, message);
    }

    private void addBuilderMethod(JavaRecordSource source, MessageDefinition message) {
        MethodBody body = body(
                "return new $BuilderType();",
                param("BuilderType", message.builderName()));

        source.addMethod()
                .setPublic()
                .setStatic(true)
                .setReturnType(message.builderName().canonicalName())
                .setName("builder")
                .setBody(body.toString());
    }

    private void addBuilderClass(JavaRecordSource source, MessageDefinition message) {
        source.addNestedType(builderFactory.buildBuilder(message));
    }
}

final class EnumerationFactory {

    JavaEnumSource buildEnumerationEnum(EnumerationDefinition enumeration) {
        JavaEnumSource source = buildSourceFile(enumeration);
        addElements(source, enumeration);
        addFields(source);
        addConstructor(source);
        addNumberMethod(source, enumeration);
        addFactoryMethod(source, enumeration);

        return source;
    }

    private JavaEnumSource buildSourceFile(EnumerationDefinition enumeration) {
        return Roaster.create(JavaEnumSource.class)
                .setPackage(enumeration.name().packageName())
                .setName(enumeration.name().simpleName());
        // TODO add common enum interface
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

    private void addNumberMethod(JavaEnumSource source, EnumerationDefinition enumeration) {
        MethodBody body = body("""
                        if (this == $unrecognized) {
                          throw new IllegalArgumentException("Unrecognized enum does not have a number");
                        }
                        return number;""",
                param("unrecognized", UNRECOGNIZED_ELEMENT_NAME)
        );

        source.addMethod()
                .setReturnType(int.class)
                .setName("number")
                .setBody(body.toString());
    }

    private void addFactoryMethod(JavaEnumSource source, EnumerationDefinition enumeration) {
        MethodBody body = body();

        body.append("return switch(number) {");
        for (EnumerationElementDefinition element : enumeration.elements()) {
            body.append("case $number -> $name;",
                    param("number", element.number()),
                    param("name", element.name())
            );
        }
        body.append("default -> $unrecognized;",
                param("unrecognized", UNRECOGNIZED_ELEMENT_NAME)
        );
        body.append("};");

        MethodSource<JavaEnumSource> method = source.addMethod()
                .setStatic(true)
                .setReturnType(enumeration.name().canonicalName())
                .setName("forNumber")
                .setBody(body.toString());
        method.addParameter(int.class, "number");
    }
}