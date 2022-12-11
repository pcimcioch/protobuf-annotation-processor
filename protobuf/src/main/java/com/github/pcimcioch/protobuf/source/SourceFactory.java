package com.github.pcimcioch.protobuf.source;

import com.github.pcimcioch.protobuf.model.message.EnumerationDefinition;
import com.github.pcimcioch.protobuf.model.message.MessageDefinition;
import com.github.pcimcioch.protobuf.model.ProtoDefinitions;
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

