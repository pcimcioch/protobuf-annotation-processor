package org.github.pcimcioch.protobuf.source;

import org.github.pcimcioch.protobuf.model.FieldDefinition;
import org.github.pcimcioch.protobuf.model.MessageDefinition;
import org.github.pcimcioch.protobuf.model.ProtoDefinitions;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaRecordSource;
import org.jboss.forge.roaster.model.source.JavaSource;

import java.util.ArrayList;
import java.util.List;

import static org.github.pcimcioch.protobuf.source.MethodBody.body;
import static org.github.pcimcioch.protobuf.source.MethodBody.param;

// TODO add tests for serialization and deserialization compatibility (object serialized with encodingMethod should be deserializable with decodingMethod)
public class SourceFactory {

    private final EncodingFactory encodingFactory = new EncodingFactory();
    private final DecodingFactory decodingFactory = new DecodingFactory();
    private final BuilderFactory builderFactory = new BuilderFactory();

    public static final class SourceFile {
        private final JavaSource<?> source;

        private SourceFile(JavaSource<?> source) {
            this.source = source;
        }

        public String canonicalName() {
            return source.getCanonicalName();
        }

        public String source() {
            return source.toString();
        }
    }

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
                .setPackage(message.messageTypePackage())
                .setName(message.messageTypeSimpleName());
    }

    private void addRecordComponents(JavaRecordSource record, MessageDefinition message) {
        for (FieldDefinition field : message.fields()) {
            record.addRecordComponent(field.typeName(), field.name());
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
                "return new ${BuilderType}();",
                param("BuilderType", message.builderSimpleName()));

        messageRecord.addMethod()
                .setPublic()
                .setName("builder")
                .setReturnType(message.builderSimpleName())
                .setBody(body.toString());
    }
}