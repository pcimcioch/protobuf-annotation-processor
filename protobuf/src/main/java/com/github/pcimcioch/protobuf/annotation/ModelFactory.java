package com.github.pcimcioch.protobuf.annotation;

import com.github.pcimcioch.protobuf.annotation.Enumeration.Element;
import com.github.pcimcioch.protobuf.annotation.HierarchyResolver.Clazz;
import com.github.pcimcioch.protobuf.annotation.HierarchyResolver.FieldState;
import com.github.pcimcioch.protobuf.model.ProtoDefinitions;
import com.github.pcimcioch.protobuf.model.ProtoDefinitionsWrapper;
import com.github.pcimcioch.protobuf.model.field.FieldDefinition;
import com.github.pcimcioch.protobuf.model.field.FieldRules;
import com.github.pcimcioch.protobuf.model.message.EnumerationDefinition;
import com.github.pcimcioch.protobuf.model.message.EnumerationElementDefinition;
import com.github.pcimcioch.protobuf.model.message.MessageDefinition;
import com.github.pcimcioch.protobuf.model.message.ReservedDefinition;
import com.github.pcimcioch.protobuf.model.message.ReservedDefinition.Range;

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
        HierarchyResolver hierarchyResolver = new HierarchyResolver(protoFiles);

        List<MessageDefinition> messages = buildMessages(hierarchyResolver, hierarchyResolver.messages());
        List<EnumerationDefinition> enumerations = buildEnumerations(hierarchyResolver.enumerations());
        List<ProtoDefinitionsWrapper> wrappers = buildWrappers(hierarchyResolver, hierarchyResolver.wrappers());

        return new ProtoDefinitions(messages, enumerations, wrappers);
    }

    private List<MessageDefinition> buildMessages(HierarchyResolver hierarchyResolver, Stream<Clazz> messages) {
        return messages
                .map(clazz -> buildMessage(hierarchyResolver, clazz))
                .toList();
    }

    private MessageDefinition buildMessage(HierarchyResolver hierarchyResolver, Clazz clazz) {
        Message message = clazz.asMessage();

        return new MessageDefinition(
                clazz.type(),
                buildFields(hierarchyResolver, message),
                buildReserved(message.reserved()),
                buildMessages(hierarchyResolver, clazz.messages()),
                buildEnumerations(clazz.enumerations()),
                message.supportUnknownFields()
        );
    }

    private List<FieldDefinition> buildFields(HierarchyResolver hierarchyResolver, Message message) {
        return Arrays.stream(message.fields())
                .map(field -> this.buildField(hierarchyResolver, field))
                .toList();
    }

    private FieldDefinition buildField(HierarchyResolver hierarchyResolver, Field field) {
        FieldState fieldState = hierarchyResolver.fieldStateOf(field);

        return switch (fieldState.kind()) {
            case SCALAR -> FieldDefinition.scalar(field.name(), field.number(), field.type(), buildFieldRules(field));
            case MESSAGE ->
                    FieldDefinition.message(field.name(), field.number(), fieldState.type(), buildFieldRules(field));
            case ENUM ->
                    FieldDefinition.enumeration(field.name(), field.number(), fieldState.type(), buildFieldRules(field));
            case UNKNOWN -> throw new IllegalArgumentException("Cannot find field type for " + fieldState.type());
        };
    }

    private FieldRules buildFieldRules(Field field) {
        return new FieldRules(field.deprecated(), field.repeated(), field.packed());
    }

    private List<EnumerationDefinition> buildEnumerations(Stream<Clazz> enumerations) {
        return enumerations
                .map(this::buildEnumeration)
                .toList();
    }

    private EnumerationDefinition buildEnumeration(Clazz clazz) {
        Enumeration enumeration = clazz.asEnumeration();

        return new EnumerationDefinition(
                clazz.type(),
                buildEnumerationElements(enumeration),
                enumeration.allowAlias(),
                buildReserved(enumeration.reserved())
        );
    }

    private List<EnumerationElementDefinition> buildEnumerationElements(Enumeration enumeration) {
        return Arrays.stream(enumeration.elements())
                .map(this::buildEnumerationElement)
                .toList();
    }

    private EnumerationElementDefinition buildEnumerationElement(Element element) {
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

    private List<ProtoDefinitionsWrapper> buildWrappers(HierarchyResolver hierarchyResolver, Stream<Clazz> wrappers) {
        return wrappers
                .map(clazz -> buildWrapper(hierarchyResolver, clazz))
                .toList();
    }

    private ProtoDefinitionsWrapper buildWrapper(HierarchyResolver hierarchyResolver, Clazz wrapper) {
        return new ProtoDefinitionsWrapper(
                wrapper.type(),
                buildProtoDefinitions(hierarchyResolver, wrapper)
        );
    }

    private ProtoDefinitions buildProtoDefinitions(HierarchyResolver hierarchyResolver, Clazz wrapper) {
        return new ProtoDefinitions(
                buildMessages(hierarchyResolver, wrapper.messages()),
                buildEnumerations(wrapper.enumerations()),
                buildWrappers(hierarchyResolver, wrapper.wrappers())
        );
    }
}
