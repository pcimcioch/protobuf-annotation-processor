package com.github.pcimcioch.protobuf.annotation;

import com.github.pcimcioch.protobuf.model.EnumerationDefinition;
import com.github.pcimcioch.protobuf.model.EnumerationElementDefinition;
import com.github.pcimcioch.protobuf.model.EnumerationFieldDefinition;
import com.github.pcimcioch.protobuf.model.FieldDefinition;
import com.github.pcimcioch.protobuf.model.MessageDefinition;
import com.github.pcimcioch.protobuf.model.ProtoDefinitions;
import com.github.pcimcioch.protobuf.model.ReservedDefinition;
import com.github.pcimcioch.protobuf.model.ReservedDefinition.Range;
import com.github.pcimcioch.protobuf.model.ScalarFieldDefinition;
import com.github.pcimcioch.protobuf.model.TypeName;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

/**
 * Create model from annotations
 */
public class ModelFactory {

    /**
     * Create model from protobuf files
     *
     * @param protoFiles protobuf files
     * @return model
     */
    public ProtoDefinitions buildProtoDefinitions(ProtoFiles protoFiles) {
        List<MessageDefinition> messages = protoFiles.files().stream()
                .flatMap(protoFile -> buildMessages(protoFiles, protoFile))
                .toList();
        List<EnumerationDefinition> enumerations = protoFiles.files().stream()
                .flatMap(this::buildEnumerations)
                .toList();

        return new ProtoDefinitions(messages, enumerations);
    }

    private Stream<MessageDefinition> buildMessages(ProtoFiles protoFiles, ProtoFile protoFile) {
        return protoFile.messages().stream()
                .map(message -> new MessageDefinition(
                        protoFile.nameOf(message.name()),
                        buildFields(protoFiles, protoFile, message),
                        buildReserved(message.reserved())
                ));
    }

    private List<FieldDefinition> buildFields(ProtoFiles protoFiles, ProtoFile protoFile, Message message) {
        return Arrays.stream(message.fields())
                .map(field -> this.buildField(protoFiles, protoFile, field))
                .toList();
    }

    private FieldDefinition buildField(ProtoFiles protoFiles, ProtoFile protoFile, Field field) {
        Optional<ScalarFieldDefinition> scalar = ScalarFieldDefinition.create(field.name(), field.number(), field.type(), field.deprecated());
        if (scalar.isPresent()) {
            return scalar.get();
        }

        TypeName fieldType = protoFile.nameOf(field.type());
        if (protoFiles.containsEnumeration(fieldType)) {
            return EnumerationFieldDefinition.create(field.name(), field.number(), fieldType, field.deprecated());
        }

        return null;
    }

    private Stream<EnumerationDefinition> buildEnumerations(ProtoFile protoFile) {
        return protoFile.enumerations().stream()
                .map(enumeration -> new EnumerationDefinition(
                        protoFile.nameOf(enumeration.name()),
                        buildEnumerationElements(enumeration), enumeration.allowAlias(),
                        buildReserved(enumeration.reserved())
                ));
    }

    private List<EnumerationElementDefinition> buildEnumerationElements(Enumeration enumeration) {
        return Arrays.stream(enumeration.elements())
                .map(this::buildEnumerationElement)
                .toList();
    }

    private EnumerationElementDefinition buildEnumerationElement(EnumerationElement element) {
        return new EnumerationElementDefinition(element.name(), element.number());
    }

    private ReservedDefinition buildReserved(Reserved reserved) {
        Set<String> names = Set.of(reserved.names());
        Set<Integer> numbers = Arrays.stream(reserved.numbers())
                .boxed()
                .collect(toSet());
        Set<Range> ranges = Arrays.stream(reserved.ranges())
                .map(range -> new Range(range.from(), range.to()))
                .collect(toSet());

        return new ReservedDefinition(names, numbers, ranges);
    }
}
