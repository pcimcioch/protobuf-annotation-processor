package com.protobuf.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DeprecatedTest {

    @Test
    void builderDeprecated() throws NoSuchFieldException, NoSuchMethodException {
        // when then
        assertThat(fieldDeprecated(DeprecatedRecord.Builder.class, "deprecatedField")).isTrue();
        assertThat(fieldDeprecated(DeprecatedRecord.Builder.class, "deprecatedEnumValue")).isTrue();
        assertThat(methodDeprecated(DeprecatedRecord.Builder.class, "deprecatedField", int.class)).isTrue();
        assertThat(methodDeprecated(DeprecatedRecord.Builder.class, "deprecatedEnum", DeprecatedEnum.class)).isTrue();
        assertThat(methodDeprecated(DeprecatedRecord.Builder.class, "deprecatedEnumValue", int.class)).isTrue();

        assertThat(fieldDeprecated(DeprecatedRecord.Builder.class, "someField")).isFalse();
        assertThat(fieldDeprecated(DeprecatedRecord.Builder.class, "someEnumValue")).isFalse();
        assertThat(methodDeprecated(DeprecatedRecord.Builder.class, "someField", int.class)).isFalse();
        assertThat(methodDeprecated(DeprecatedRecord.Builder.class, "someEnum", DeprecatedEnum.class)).isFalse();
        assertThat(methodDeprecated(DeprecatedRecord.Builder.class, "someEnumValue", int.class)).isFalse();
    }

    @Test
    void messageDeprecated() throws NoSuchFieldException, NoSuchMethodException {
        // when then
        assertThat(fieldDeprecated(DeprecatedRecord.class, "deprecatedField")).isTrue();
        assertThat(fieldDeprecated(DeprecatedRecord.class, "deprecatedEnumValue")).isTrue();
        assertThat(methodDeprecated(DeprecatedRecord.class, "deprecatedField")).isTrue();
        assertThat(methodDeprecated(DeprecatedRecord.class, "deprecatedEnumValue")).isTrue();
        assertThat(methodDeprecated(DeprecatedRecord.class, "deprecatedEnum")).isTrue();

        assertThat(fieldDeprecated(DeprecatedRecord.class, "someField")).isFalse();
        assertThat(fieldDeprecated(DeprecatedRecord.class, "someEnumValue")).isFalse();
        assertThat(methodDeprecated(DeprecatedRecord.class, "someField")).isFalse();
        assertThat(methodDeprecated(DeprecatedRecord.class, "someEnumValue")).isFalse();
        assertThat(methodDeprecated(DeprecatedRecord.class, "someEnum")).isFalse();
    }

    private boolean methodDeprecated(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
        return clazz.getDeclaredMethod(methodName, parameterTypes).getAnnotation(Deprecated.class) != null;
    }

    private boolean fieldDeprecated(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        return clazz.getDeclaredField(fieldName).getAnnotation(Deprecated.class) != null;
    }
}
