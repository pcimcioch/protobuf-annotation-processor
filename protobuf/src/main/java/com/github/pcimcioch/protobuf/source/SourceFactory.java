package com.github.pcimcioch.protobuf.source;

import com.github.pcimcioch.protobuf.code.ClassSource;
import com.github.pcimcioch.protobuf.code.EnumSource;
import com.github.pcimcioch.protobuf.code.RecordSource;
import com.github.pcimcioch.protobuf.code.Source;
import com.github.pcimcioch.protobuf.model.ProtoDefinitions;
import com.github.pcimcioch.protobuf.model.ProtoDefinitionsWrapper;
import com.github.pcimcioch.protobuf.model.message.EnumerationDefinition;
import com.github.pcimcioch.protobuf.model.message.MessageDefinition;

import java.util.ArrayList;
import java.util.List;

import static com.github.pcimcioch.protobuf.code.StaticSource.staticModifier;

/**
 * Creates java source code for the protobuf transfer objects
 */
public class SourceFactory {
    private final MessageFactory messageFactory = new MessageFactory();
    private final EnumerationFactory enumerationFactory = new EnumerationFactory();
    private final WrapperFactory wrapperFactory = new WrapperFactory();

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
        model.wrappers().stream()
                .map(this::buildWrapper)
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

    private ClassSource buildWrapper(ProtoDefinitionsWrapper wrapper) {
        ClassSource source = wrapperFactory.buildWrapperClass(wrapper);
        wrapper.definitions().messages().stream()
                .map(this::buildMessage)
                .forEach(source::add);
        wrapper.definitions().enumerations().stream()
                .map(this::buildEnum)
                .forEach(source::add);
        wrapper.definitions().wrappers().stream()
                .map(this::buildWrapper)
                .map(w -> w.set(staticModifier()))
                .forEach(source::add);

        return source;
    }
}
