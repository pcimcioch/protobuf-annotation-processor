package com.github.pcimcioch.protobuf.source;

import com.github.pcimcioch.protobuf.code.MethodBody;
import com.github.pcimcioch.protobuf.dto.ProtobufEnumeration;
import com.github.pcimcioch.protobuf.dto.ProtobufMessage;
import com.github.pcimcioch.protobuf.model.EnumerationDefinition;
import com.github.pcimcioch.protobuf.model.EnumerationElementDefinition;
import com.github.pcimcioch.protobuf.model.FieldDefinition;
import com.github.pcimcioch.protobuf.model.MessageDefinition;
import com.github.pcimcioch.protobuf.model.ProtoDefinitions;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaEnumSource;
import org.jboss.forge.roaster.model.source.JavaRecordSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.source.MethodSource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.github.pcimcioch.protobuf.model.EnumerationElementDefinition.UNRECOGNIZED_ELEMENT_NAME;
import static com.github.pcimcioch.protobuf.code.MethodBody.body;
import static com.github.pcimcioch.protobuf.code.MethodBody.param;

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

