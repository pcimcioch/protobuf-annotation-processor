package com.github.pcimcioch.protobuf.annotation;

import com.github.pcimcioch.protobuf.annotation.ProtoFiles.ProtoFile;
import com.github.pcimcioch.protobuf.annotation.Reserved.Range;
import com.github.pcimcioch.protobuf.model.ProtoDefinitions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ModelFactoryTest {

    private static final Reserved NO_RESERVED = reserved();

    private final ModelFactory testee = new ModelFactory();

    @Test
    @Disabled
    void createMessage() {
        // given
        ProtoFiles files = files(
                file(
                        "com.example",
                        message("MyMessage", field("int32", "field", 1))
                )
        );

        // when
        ProtoDefinitions definitions = testee.buildProtoDefinitions(files);

        // then
        ProtoDefinitions expected = null;

        assertThat(definitions).isEqualTo(expected);
    }

    private static ProtoFiles files(ProtoFile... files) {
        return new ProtoFiles(List.of(files));
    }

    private static ProtoFile file(String javaPackage, Annotation... annotations) {
        List<Message> messages = Arrays.stream(annotations)
                .filter(Message.class::isInstance)
                .map(Message.class::cast)
                .toList();
        List<Enumeration> enumerations = Arrays.stream(annotations)
                .filter(Enumeration.class::isInstance)
                .map(Enumeration.class::cast)
                .toList();

        return new ProtoFile(javaPackage, messages, enumerations);
    }

    private static Message message(String name, Field... fields) {
        return message(name, NO_RESERVED, fields);
    }

    private static Message message(String name, Reserved reserved, Field... fields) {
        return new Message() {
            @Override
            public String name() {
                return name;
            }

            @Override
            public Field[] fields() {
                return fields;
            }

            @Override
            public Reserved reserved() {
                return reserved;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return Message.class;
            }
        };
    }

    private static Field field(String type, String name, int number) {
        return field(type, name, number, false);
    }

    private static Field field(String type, String name, int number, boolean deprecated) {
        return new Field() {
            @Override
            public String type() {
                return type;
            }

            @Override
            public String name() {
                return name;
            }

            @Override
            public int number() {
                return number;
            }

            @Override
            public boolean deprecated() {
                return deprecated;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return Field.class;
            }
        };
    }

    private static Enumeration enumeration(String name, EnumerationElement... elements) {
        return enumeration(name, false, NO_RESERVED, elements);
    }

    private static Enumeration enumeration(String name, boolean allowAlias, Reserved reserved, EnumerationElement... elements) {
        return new Enumeration() {
            @Override
            public String name() {
                return name;
            }

            @Override
            public boolean allowAlias() {
                return allowAlias;
            }

            @Override
            public EnumerationElement[] elements() {
                return elements;
            }

            @Override
            public Reserved reserved() {
                return reserved;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return Enumeration.class;
            }
        };
    }

    private static EnumerationElement element(String name, int number) {
        return new EnumerationElement() {
            @Override
            public String name() {
                return name;
            }

            @Override
            public int number() {
                return number;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return EnumerationElement.class;
            }
        };
    }

    private static Reserved reserved(Object... elements) {
        return new Reserved() {
            @Override
            public String[] names() {
                return Arrays.stream(elements)
                        .filter(String.class::isInstance)
                        .map(String.class::cast)
                        .toArray(String[]::new);
            }

            @Override
            public int[] numbers() {
                return Arrays.stream(elements)
                        .filter(Integer.class::isInstance)
                        .mapToInt(Integer.class::cast)
                        .toArray();
            }

            @Override
            public Range[] ranges() {
                return Arrays.stream(elements)
                        .filter(Range.class::isInstance)
                        .map(Range.class::cast)
                        .toArray(Range[]::new);
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return Reserved.class;
            }
        };
    }

    private static Range range(int from, int to) {
        return new Range() {
            @Override
            public int from() {
                return from;
            }

            @Override
            public int to() {
                return to;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return Range.class;
            }
        };
    }
}