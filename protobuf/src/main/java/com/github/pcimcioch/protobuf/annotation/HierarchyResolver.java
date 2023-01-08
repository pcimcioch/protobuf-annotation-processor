package com.github.pcimcioch.protobuf.annotation;

import com.github.pcimcioch.protobuf.annotation.ProtoFiles.ProtoFile;
import com.github.pcimcioch.protobuf.model.field.FieldDefinition;
import com.github.pcimcioch.protobuf.code.TypeName;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.github.pcimcioch.protobuf.code.TypeName.canonicalName;
import static java.lang.Character.isLowerCase;

class HierarchyResolver {

    private final Map<String, Clazz> classes = new LinkedHashMap<>();
    private final Map<Field, FieldState> fieldStates = new HashMap<>();

    enum FieldKind {SCALAR, ENUM, MESSAGE, UNKNOWN}

    record FieldState(FieldKind kind, TypeName type) {
    }

    HierarchyResolver(ProtoFiles files) {
        init(files);
        validate();
        initFields(files);
    }

    Stream<Clazz> messages() {
        return classes.values().stream()
                .filter(Clazz::isMessage);
    }

    Stream<Clazz> enumerations() {
        return classes.values().stream()
                .filter(Clazz::isEnumeration);
    }

    FieldState fieldStateOf(Field field) {
        return fieldStates.get(field);
    }

    private void init(ProtoFiles files) {
        for (ProtoFile file : files.files()) {
            for (Message message : file.messages()) {
                createClazz(typeOf(file, message.name())).setAnnotation(message);
            }
            for (Enumeration enumeration : file.enumerations()) {
                createClazz(typeOf(file, enumeration.name())).setAnnotation(enumeration);
            }
        }
    }

    private void validate() {
        classes.values().forEach(Clazz::validate);
    }

    private void initFields(ProtoFiles files) {
        for (ProtoFile file : files.files()) {
            for (Message message : file.messages()) {
                for (Field field : message.fields()) {
                    fieldStates.put(field, fieldStateOf(file, field));
                }
            }
        }
    }

    private FieldState fieldStateOf(ProtoFile file, Field field) {
        if (FieldDefinition.isScalar(field.type())) {
            return new FieldState(FieldKind.SCALAR, null);
        }

        TypeName fieldType = typeOf(file, field.type());
        Clazz clazz = getClazz(fieldType);
        if (clazz != null && clazz.isEnumeration()) {
            return new FieldState(FieldKind.ENUM, fieldType);
        }
        if (clazz != null && clazz.isMessage()) {
            return new FieldState(FieldKind.MESSAGE, fieldType);
        }

        return new FieldState(FieldKind.UNKNOWN, fieldType);
    }

    private Clazz createClazz(TypeName type) {
        String baseName = type.baseName();
        List<String> nestedClassNames = type.nestedClassNames();

        TypeName clazzType = canonicalName(baseName);
        Clazz clazz = createNested(baseName, clazzType);
        for (String nestedClassName : nestedClassNames) {
            clazzType = clazzType.with(nestedClassName);
            clazz = clazz.createNested(nestedClassName, clazzType);
        }

        return clazz;
    }

    private Clazz createNested(String clazz, TypeName type) {
        return classes.computeIfAbsent(clazz, key -> new Clazz(type));
    }

    private Clazz getClazz(TypeName type) {
        String baseName = type.baseName();
        List<String> nestedClassNames = type.nestedClassNames();

        Clazz clazz = getNested(baseName);
        for (String nestedClassName : nestedClassNames) {
            clazz = clazz == null ? null : clazz.getNested(nestedClassName);
        }

        return clazz;
    }

    private Clazz getNested(String clazz) {
        return classes.get(clazz);
    }

    private TypeName typeOf(ProtoFile file, String name) {
        if (name == null) {
            return canonicalName("");
        }
        if (name.startsWith(".")) {
            return canonicalName(file.javaPackage() + name);
        }
        if (name.contains(".") && isLowerCase(name.charAt(0))) {
            return canonicalName(name);
        }
        return canonicalName(file.javaPackage() + "." + name);
    }

    static final class Clazz {
        private Annotation annotation;
        private final TypeName type;
        private final Map<String, Clazz> nested = new LinkedHashMap<>();

        private Clazz(TypeName type) {
            this.type = type;
        }

        TypeName type() {
            return type;
        }

        Message asMessage() {
            return (Message) annotation;
        }

        Enumeration asEnumeration() {
            return (Enumeration) annotation;
        }

        Stream<Clazz> messages() {
            return nested.values().stream()
                    .filter(Clazz::isMessage);
        }

        Stream<Clazz> enumerations() {
            return nested.values().stream()
                    .filter(Clazz::isEnumeration);
        }

        private void validate() {
            if (isEmpty()) {
                throw new IllegalArgumentException("Type is not defined: " + type);
            }
            if (isEnumeration() && !nested.isEmpty()) {
                throw new IllegalArgumentException("Enumeration type can not contain any nested types");
            }

            nested.values().forEach(Clazz::validate);
        }

        private Clazz createNested(String clazz, TypeName type) {
            return nested.computeIfAbsent(clazz, key -> new Clazz(type));
        }

        private Clazz getNested(String clazz) {
            return nested.get(clazz);
        }

        private void setAnnotation(Annotation annotation) {
            if (!isEmpty()) {
                throw new IllegalArgumentException("Duplicated type name " + type);
            }
            this.annotation = annotation;
        }

        private boolean isMessage() {
            return annotation instanceof Message;
        }

        private boolean isEnumeration() {
            return annotation instanceof Enumeration;
        }

        private boolean isEmpty() {
            return annotation == null;
        }
    }
}
