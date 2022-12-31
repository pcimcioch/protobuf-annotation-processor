package com.github.pcimcioch.protobuf.annotation;

import com.github.pcimcioch.protobuf.annotation.ProtoFiles.ProtoFile;
import com.github.pcimcioch.protobuf.annotation.TypeResolver.FieldKind;
import com.github.pcimcioch.protobuf.model.ProtoDefinitions;
import com.github.pcimcioch.protobuf.model.field.FieldDefinition;
import com.github.pcimcioch.protobuf.model.message.EnumerationDefinition;
import com.github.pcimcioch.protobuf.model.message.EnumerationElementDefinition;
import com.github.pcimcioch.protobuf.model.message.MessageDefinition;
import com.github.pcimcioch.protobuf.model.message.ReservedDefinition;
import com.github.pcimcioch.protobuf.model.message.ReservedDefinition.Range;
import com.github.pcimcioch.protobuf.model.type.TypeName;

import java.util.Arrays;
import java.util.List;
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
        TypeResolver typeResolver = new TypeResolver(protoFiles);
        List<MessageDefinition> messages = protoFiles.files().stream()
                .flatMap(protoFile -> buildMessages(typeResolver, protoFile))
                .toList();
        List<EnumerationDefinition> enumerations = protoFiles.files().stream()
                .flatMap(protoFile -> buildEnumerations(typeResolver, protoFile))
                .toList();

        return new ProtoDefinitions(messages, enumerations);
    }

    private Stream<MessageDefinition> buildMessages(TypeResolver typeResolver, ProtoFile protoFile) {
        return protoFile.messages().stream()
                .map(message -> new MessageDefinition(
                        typeResolver.typeOf(message),
                        buildFields(typeResolver, message),
                        buildReserved(message.reserved()),
                        List.of(),
                        List.of()
                ));
    }

    private List<FieldDefinition> buildFields(TypeResolver typeResolver, Message message) {
        return Arrays.stream(message.fields())
                .map(field -> this.buildField(typeResolver, field))
                .toList();
    }

    private FieldDefinition buildField(TypeResolver typeResolver, Field field) {
        FieldKind fieldKind = typeResolver.kindOf(field);
        TypeName fieldType = typeResolver.typeOf(field);

        return switch (fieldKind) {
            case SCALAR -> FieldDefinition.scalar(field.name(), field.number(), field.type(), field.deprecated());
            case MESSAGE -> FieldDefinition.message(field.name(), field.number(), fieldType, field.deprecated());
            case ENUM -> FieldDefinition.enumeration(field.name(), field.number(), fieldType, field.deprecated());
            case UNKNOWN -> throw new IllegalArgumentException("Cannot find field type for " + fieldType);
        };
    }

    private Stream<EnumerationDefinition> buildEnumerations(TypeResolver typeResolver, ProtoFile protoFile) {
        return protoFile.enumerations().stream()
                .map(enumeration -> new EnumerationDefinition(
                        typeResolver.typeOf(enumeration),
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
