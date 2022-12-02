package com.github.pcimcioch.protobuf.source;

import com.github.pcimcioch.protobuf.model.FieldDefinition;
import com.github.pcimcioch.protobuf.model.MessageDefinition;
import com.github.pcimcioch.protobuf.model.ProtoDefinitions;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaRecordSource;
import org.jboss.forge.roaster.model.source.JavaSource;

import java.util.ArrayList;
import java.util.List;

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
     * @return java souce files
     */
    public List<SourceFile> buildSource(ProtoDefinitions model) {
        List<SourceFile> sources = new ArrayList<>();

        for (MessageDefinition message : model.messages()) {
            sources.add(new SourceFile(buildMessageRecord(message)));
            sources.add(new SourceFile(builderFactory.buildBuilder(message)));
        }

        return sources;
    }

    private JavaRecordSource buildMessageRecord(MessageDefinition message) {
        JavaRecordSource messageRecord = buildSourceFile(message);
        addRecordComponents(messageRecord, message);
        addEncodingMethods(messageRecord, message);
        addDecodingMethods(messageRecord, message);
        addBuilderMethod(messageRecord, message);

        return messageRecord;
    }

    private JavaRecordSource buildSourceFile(MessageDefinition message) {
        return Roaster.create(JavaRecordSource.class)
                .setPackage(message.name().packageName())
                .setName(message.name().simpleName());
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
                .setName("builder")
                .setReturnType(message.builderName().canonicalName())
                .setBody(body.toString());
    }
}