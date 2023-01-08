package com.github.pcimcioch.protobuf.code;

import com.github.pcimcioch.protobuf.code.TypeName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static com.github.pcimcioch.protobuf.code.TypeName.canonicalName;
import static com.github.pcimcioch.protobuf.code.TypeName.simpleName;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TypeNameTest {

    @ParameterizedTest
    @MethodSource("correctSimpleNames")
    void createFromCorrectSimpleNames(String name) {
        // when
        TypeName result = simpleName(name);

        // then
        assertThat(result.packageName()).isEmpty();
        assertThat(result.simpleName()).isEqualTo(name);
        assertThat(result.baseName()).isEqualTo(name);
        assertThat(result.nestedClassNames()).isEmpty();
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
        assertThatThrownBy(() -> simpleName(name))
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
    void createFromCorrectCanonicalNames(String name, String expectedPackage, String expectedBase, List<String> expectedNested, String expectedSimple) {
        // when
        TypeName result = canonicalName(name);

        // then
        assertThat(result.packageName()).isEqualTo(expectedPackage);
        assertThat(result.baseName()).isEqualTo(expectedBase);
        assertThat(result.nestedClassNames()).isEqualTo(expectedNested);
        assertThat(result.simpleName()).isEqualTo(expectedSimple);
        assertThat(result.canonicalName()).isEqualTo(name);
    }

    static Stream<Arguments> correctCanonicalNames() {
        return Stream.of(
                Arguments.of("example.MyClass", "example", "example.MyClass", emptyList(), "MyClass"),
                Arguments.of("com.example.MyClass", "com.example", "com.example.MyClass", emptyList(), "MyClass"),
                Arguments.of("com.example.MyClass.Nested", "com.example", "com.example.MyClass", List.of("Nested"), "Nested"),
                Arguments.of("com.example.MyClass.Nested.DoubleNested", "com.example", "com.example.MyClass", List.of("Nested", "DoubleNested"), "DoubleNested"),
                Arguments.of("example1_.123.MyClass2_", "example1_.123", "example1_.123.MyClass2_", emptyList(), "MyClass2_")
        );
    }

    @ParameterizedTest
    @MethodSource("incorrectCanonicalNames")
    void failFromIncorrectCanonicalNames(String name) {
        // when then
        assertThatThrownBy(() -> canonicalName(name))
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
    void with() {
        // given
        TypeName type = canonicalName("com.example.Test");

        // when then
        assertThat(type.with("Nested").canonicalName()).isEqualTo("com.example.Test.Nested");
    }

    @Test
    void isDirectChildOf() {
        // given
        TypeName parent = canonicalName("com.test.Parent.Test");
        TypeName child = canonicalName("com.test.Parent.Test.Child");

        // when then
        assertThat(child.isDirectChildOf(parent)).isTrue();
    }

    @ParameterizedTest
    @MethodSource("notChildCanonicalNames")
    void isNotDirectChildOf(TypeName type) {
        // given
        TypeName parent = canonicalName("com.test.Parent.Test");

        // when then
        assertThat(type.isDirectChildOf(parent)).isFalse();
    }

    static Stream<TypeName> notChildCanonicalNames() {
        return Stream.of(
                canonicalName("com.test.Parent.Test.Child1.Child2"),
                canonicalName("com.test.Parent.Test2"),
                canonicalName("com.test2.Parent.Test.Child"),
                canonicalName("com.test.Parent2.Test.Child"),
                canonicalName("com.test.Parent")
        );
    }
}