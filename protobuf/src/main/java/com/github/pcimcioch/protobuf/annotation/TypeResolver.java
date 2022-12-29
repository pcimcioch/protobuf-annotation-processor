package com.github.pcimcioch.protobuf.annotation;

import com.github.pcimcioch.protobuf.model.field.FieldDefinition;
import com.github.pcimcioch.protobuf.model.type.TypeName;

import java.util.IdentityHashMap;
import java.util.Map;

import static com.github.pcimcioch.protobuf.model.type.TypeName.canonicalName;

class TypeResolver {
    private final Map<Message, TypeName> messageTypes = new IdentityHashMap<>();
    private final Map<Enumeration, TypeName> enumerationTypes = new IdentityHashMap<>();
    private final Map<Field, FieldState> fieldTypes = new IdentityHashMap<>();

    private record FieldState(FieldKind kind, TypeName type) {
    }

    enum FieldKind {SCALAR, ENUM, MESSAGE, UNKNOWN}

    TypeResolver(ProtoFiles files) {
        initMessages(files);
        initEnumerations(files);
        initFields(files);
    }

    TypeName typeOf(Message message) {
        return messageTypes.get(message);
    }

    TypeName typeOf(Enumeration enumeration) {
        return enumerationTypes.get(enumeration);
    }

    FieldKind kindOf(Field field) {
        return fieldTypes.get(field).kind();
    }

    TypeName typeOf(Field field) {
        return fieldTypes.get(field).type();
    }

    private void initMessages(ProtoFiles files) {
        files.files().forEach(this::initMessages);
    }

    private void initMessages(ProtoFile file) {
        for (Message message : file.messages()) {
            messageTypes.put(message, typeOf(file, message.name()));
        }
    }

    private void initEnumerations(ProtoFiles files) {
        files.files().forEach(this::initEnumerations);
    }

    private void initEnumerations(ProtoFile file) {
        for (Enumeration enumeration : file.enumerations()) {
            enumerationTypes.put(enumeration, typeOf(file, enumeration.name()));
        }
    }

    private void initFields(ProtoFiles files) {
        files.files().forEach(this::initFields);
    }

    private void initFields(ProtoFile file) {
        file.messages().forEach(message -> initFields(file, message));
    }

    private void initFields(ProtoFile file, Message message) {
        for (Field field : message.fields()) {
            if (FieldDefinition.isScalar(field.type())) {
                fieldTypes.put(field, new FieldState(FieldKind.SCALAR, null));
                continue;
            }

            TypeName fieldType = typeOf(file, field.type());
            if (containsMessageType(fieldType)) {
                fieldTypes.put(field, new FieldState(FieldKind.MESSAGE, fieldType));
            } else if (containsEnumerationType(fieldType)) {
                fieldTypes.put(field, new FieldState(FieldKind.ENUM, fieldType));
            } else {
                fieldTypes.put(field, new FieldState(FieldKind.UNKNOWN, fieldType));
            }
        }
    }

    private boolean containsMessageType(TypeName type) {
        return messageTypes.values().stream().anyMatch(type::equals);
    }

    private boolean containsEnumerationType(TypeName type) {
        return enumerationTypes.values().stream().anyMatch(type::equals);
    }

    private TypeName typeOf(ProtoFile file, String name) {
        if (name == null) {
            return canonicalName("");
        }
        if (name.startsWith(".")) {
            return canonicalName(file.javaPackage() + name);
        }
        if (name.contains(".")) {
            return canonicalName(name);
        }
        return canonicalName(file.javaPackage() + "." + name);
    }
}
