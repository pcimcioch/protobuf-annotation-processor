package com.github.pcimcioch.protobuf.source;

import com.github.pcimcioch.protobuf.code.EnumSource;
import com.github.pcimcioch.protobuf.code.RecordSource;
import com.github.pcimcioch.protobuf.code.Source;
import com.github.pcimcioch.protobuf.model.ProtoDefinitions;
import com.github.pcimcioch.protobuf.model.message.EnumerationDefinition;
import com.github.pcimcioch.protobuf.model.message.MessageDefinition;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates java source code for the protobuf transfer objects
 */
// TODO add tests where there are multiple classes with the same simple name "Builder". In different files and nested
public class SourceFactory {
    private final MessageFactory messageFactory = new MessageFactory();
    private final EnumerationFactory enumerationFactory = new EnumerationFactory();

    /**
     * Builds java source files from the protobuf model
     *
     * @param model model
     * @return java source files
     */
    public List<Source> buildSource(ProtoDefinitions model) {
        List<Source> sources = new ArrayList<>();
        model.messages().stream()
                .map(this::buildMessage)
                .forEach(sources::add);
        model.enumerations().stream()
                .map(this::buildEnum)
                .forEach(sources::add);

        return sources;
    }

    private RecordSource buildMessage(MessageDefinition message) {
        RecordSource source = messageFactory.buildMessageRecord(message);
        message.messages().stream()
                .map(this::buildMessage)
                .forEach(source::add);
        message.enumerations().stream()
                .map(this::buildEnum)
                .forEach(source::add);

        return source;
    }

    private EnumSource buildEnum(EnumerationDefinition enumeration) {
        return enumerationFactory.buildEnumerationEnum(enumeration);
    }
}
