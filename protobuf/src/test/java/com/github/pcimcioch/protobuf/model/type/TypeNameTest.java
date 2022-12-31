package com.github.pcimcioch.protobuf.model.type;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TypeNameTest {

    @ParameterizedTest
    @MethodSource("correctSimpleNames")
    void createFromCorrectSimpleNames(String name) {
        // when
        TypeName result = TypeName.simpleName(name);

        // then
        assertThat(result.packageName()).isEmpty();
        assertThat(result.parentClassesName()).isEmpty();
        assertThat(result.simpleName()).isEqualTo(name);
        assertThat(result.canonicalName()).isEqualTo(name);
    }

    static Stream<String> correctSimpleNames() {
        return Stream.of(
                "name",
                "int",
                "Integer"
        );
    }

    @ParameterizedTest
    @MethodSource("incorrectSimpleNames")
    void failFromIncorrectSimpleNames(String name) {
        // when then
        assertThatThrownBy(() -> TypeName.simpleName(name))
                .isInstanceOf(IllegalArgumentException.class);
    }

    static Stream<String> incorrectSimpleNames() {
        return Stream.of(
                null,
                "",
                " ",
                "numbers123",
                "special!"
        );
    }

    @ParameterizedTest
    @MethodSource("correctCanonicalNames")
    void createFromCorrectCanonicalNames(String name, String expectedPackage, String expectedParent, String expectedSimple) {
        // when
        TypeName result = TypeName.canonicalName(name);

        // then
        assertThat(result.packageName()).isEqualTo(expectedPackage);
        assertThat(result.parentClassesName()).isEqualTo(expectedParent);
        assertThat(result.simpleName()).isEqualTo(expectedSimple);
        assertThat(result.canonicalName()).isEqualTo(name);
    }

    static Stream<Arguments> correctCanonicalNames() {
        return Stream.of(
                Arguments.of("example.MyClass", "example", "", "MyClass"),
                Arguments.of("com.example.MyClass", "com.example", "", "MyClass"),
                Arguments.of("com.example.MyClass.Nested", "com.example", "MyClass", "Nested"),
                Arguments.of("com.example.MyClass.Nested.DoubleNested", "com.example", "MyClass.Nested", "DoubleNested"),
                Arguments.of("example1_.123.MyClass2_", "example1_.123", "", "MyClass2_")
        );
    }

    @ParameterizedTest
    @MethodSource("incorrectCanonicalNames")
    void failFromIncorrectCanonicalNames(String name) {
        // when then
        assertThatThrownBy(() -> TypeName.canonicalName(name))
                .isInstanceOf(IllegalArgumentException.class);
    }

    static Stream<String> incorrectCanonicalNames() {
        return Stream.of(
                "example",
                "1example.MyClass",
                "com.example.MyClass.test",
                "example!.MyClass",
                "example.MyClass!"
        );
    }

    @Test
    void isDirectChildOf() {
        // given
        TypeName parent = TypeName.canonicalName("com.test.Parent.Test");
        TypeName child = TypeName.canonicalName("com.test.Parent.Test.Child");

        // when then
        assertThat(child.isDirectChildOf(parent)).isTrue();
    }

    @ParameterizedTest
    @MethodSource("notChildCanonicalNames")
    void isNotDirectChildOf(TypeName type) {
        // given
        TypeName parent = TypeName.canonicalName("com.test.Parent.Test");

        // when then
        assertThat(type.isDirectChildOf(parent)).isFalse();
    }

    static Stream<TypeName> notChildCanonicalNames() {
        return Stream.of(
                TypeName.canonicalName("com.test.Parent.Test.Child1.Child2"),
                TypeName.canonicalName("com.test.Parent.Test2"),
                TypeName.canonicalName("com.test2.Parent.Test.Child"),
                TypeName.canonicalName("com.test.Parent2.Test.Child"),
                TypeName.canonicalName("com.test.Parent")
        );
    }
}