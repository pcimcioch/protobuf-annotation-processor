package com.github.pcimcioch.protobuf.source;

import com.github.pcimcioch.protobuf.model.ProtoDefinitions;
import com.github.pcimcioch.protobuf.model.message.EnumerationDefinition;
import com.github.pcimcioch.protobuf.model.message.MessageDefinition;
import org.jboss.forge.roaster.model.source.Import;
import org.jboss.forge.roaster.model.source.JavaEnumSource;
import org.jboss.forge.roaster.model.source.JavaRecordSource;
import org.jboss.forge.roaster.model.source.JavaSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates java source code for the protobuf transfer objects
 */
public class SourceFactory {

    private final MessageFactory messageFactory = new MessageFactory();
    private final EnumerationFactory enumerationFactory = new EnumerationFactory();

    /**
     * Builds java source files from the protobuf model
     *
     * @param model model
     * @return java source files
     */
    public List<JavaSource<?>> buildSource(ProtoDefinitions model) {
        List<JavaSource<?>> sources = new ArrayList<>();
        model.messages().stream()
                .map(this::buildMessage)
                .forEach(sources::add);
        model.enumerations().stream()
                .map(this::buildEnum)
                .forEach(sources::add);

        return sources;
    }

    private JavaRecordSource buildMessage(MessageDefinition message) {
        JavaRecordSource source = messageFactory.buildMessageRecord(message);
        message.messages().stream()
                .map(this::buildMessage)
                .forEach(r -> addNested(source, r));
        message.enumerations().stream()
                .map(this::buildEnum)
                .forEach(e -> addNested(source, e));

        return source;
    }

    private JavaEnumSource buildEnum(EnumerationDefinition enumeration) {
        return enumerationFactory.buildEnumerationEnum(enumeration);
    }

    private void addNested(JavaRecordSource source, JavaSource<?> nested) {
        source.addNestedType(nested);
        for (Import anImport : nested.getImports()) {
            if (!source.hasImport(anImport) && !anImport.getQualifiedName().startsWith(source.getCanonicalName())) {
                source.addImport(anImport);
            }
        }
    }
}

