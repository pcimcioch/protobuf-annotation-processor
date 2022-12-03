package com.github.pcimcioch.protobuf.source;

import com.github.pcimcioch.protobuf.dto.ProtobufMessage;
import com.github.pcimcioch.protobuf.model.FieldDefinition;
import com.github.pcimcioch.protobuf.model.MessageDefinition;
import com.github.pcimcioch.protobuf.model.ProtoDefinitions;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaRecordSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.source.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.github.pcimcioch.protobuf.source.MethodBody.body;
import static com.github.pcimcioch.protobuf.source.MethodBody.param;

/**
 * Creates java source code for the protobuf transfer objects
 */
public class SourceFactory {

    private final EncodingFactory encodingFactory = new EncodingFactory();
    private final DecodingFactory decodingFactory = new DecodingFactory();
    private final BuilderFactory builderFactory = new BuilderFactory();

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
            sources.add(new SourceFile(buildMessageRecord(message)));
        }
        // TODO generate enums

        return sources;
    }

    private JavaRecordSource buildMessageRecord(MessageDefinition message) {
        JavaRecordSource messageRecord = buildSourceFile(message);
        addConstructor(messageRecord, message);
        addRecordComponents(messageRecord, message);
        addEncodingMethods(messageRecord, message);
        addDecodingMethods(messageRecord, message);
        addBuilderMethod(messageRecord, message);
        addBuilderClass(messageRecord, message);

        return messageRecord;
    }

    private JavaRecordSource buildSourceFile(MessageDefinition message) {
        return Roaster.create(JavaRecordSource.class)
                .setPackage(message.name().packageName())
                .setName(message.name().simpleName())
                .addInterface(ProtobufMessage.class);
    }

    // TODO it would be better to use compact constructor here. Waiting for https://github.com/forge/roaster/issues/275
    private void addConstructor(JavaRecordSource messageRecord, MessageDefinition message) {
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

        MethodSource<JavaRecordSource> constructor = messageRecord.addMethod()
                .setPublic()
                .setConstructor(true)
                .setBody(body.toString());

        for (FieldDefinition field : message.fields()) {
            constructor.addParameter(field.typeName().canonicalName(), field.name());
        }
    }

    private void addRecordComponents(JavaRecordSource record, MessageDefinition message) {
        for (FieldDefinition field : message.fields()) {
            record.addRecordComponent(field.typeName().canonicalName(), field.name());
        }
    }

    private void addEncodingMethods(JavaRecordSource messageRecord, MessageDefinition message) {
        encodingFactory.addEncodingMethods(messageRecord, message);
    }

    private void addDecodingMethods(JavaRecordSource messageRecord, MessageDefinition message) {
        decodingFactory.addDecodingMethods(messageRecord, message);
    }

    private void addBuilderMethod(JavaRecordSource messageRecord, MessageDefinition message) {
        MethodBody body = body(
                "return new $BuilderType();",
                param("BuilderType", message.builderName()));

        messageRecord.addMethod()
                .setPublic()
                .setStatic(true)
                .setReturnType(message.builderName().canonicalName())
                .setName("builder")
                .setBody(body.toString());
    }

    private void addBuilderClass(JavaRecordSource messageRecord, MessageDefinition message) {
        messageRecord.addNestedType(builderFactory.buildBuilder(message));
    }
}