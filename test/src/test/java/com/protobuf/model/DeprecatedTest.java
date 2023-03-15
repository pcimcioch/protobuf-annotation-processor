package com.protobuf.model;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DeprecatedTest {

    @Test
    void builderFieldsNonDeprecated() throws NoSuchFieldException {
        // when then
        assertThat(fieldDeprecated(DeprecatedRecord.Builder.class, "someField")).isFalse();
        assertThat(fieldDeprecated(DeprecatedRecord.Builder.class, "someEnumValue")).isFalse();
        assertThat(fieldDeprecated(DeprecatedRecord.Builder.class, "someOther")).isFalse();

        assertThat(fieldDeprecated(DeprecatedRecord.Builder.class, "deprecatedField")).isFalse();
        assertThat(fieldDeprecated(DeprecatedRecord.Builder.class, "deprecatedEnumValue")).isFalse();
        assertThat(fieldDeprecated(DeprecatedRecord.Builder.class, "deprecatedOther")).isFalse();

        assertThat(fieldDeprecated(DeprecatedRecord.Builder.class, "someRepeatableField")).isFalse();
        assertThat(fieldDeprecated(DeprecatedRecord.Builder.class, "someRepeatableEnumValue")).isFalse();
        assertThat(fieldDeprecated(DeprecatedRecord.Builder.class, "someRepeatableOther")).isFalse();

        assertThat(fieldDeprecated(DeprecatedRecord.Builder.class, "deprecatedRepeatableField")).isFalse();
        assertThat(fieldDeprecated(DeprecatedRecord.Builder.class, "deprecatedRepeatableEnumValue")).isFalse();
        assertThat(fieldDeprecated(DeprecatedRecord.Builder.class, "deprecatedRepeatableOther")).isFalse();
    }

    @Test
    void builderSettersNonDeprecated() throws NoSuchMethodException {
        // when then
        assertThat(methodDeprecated(DeprecatedRecord.Builder.class, "someField", int.class)).isFalse();
        assertThat(methodDeprecated(DeprecatedRecord.Builder.class, "someEnum", DeprecatedEnum.class)).isFalse();
        assertThat(methodDeprecated(DeprecatedRecord.Builder.class, "someEnumValue", int.class)).isFalse();
        assertThat(methodDeprecated(DeprecatedRecord.Builder.class, "someOther", DeprecatedSubRecord.class)).isFalse();

        assertThat(methodDeprecated(DeprecatedRecord.Builder.class, "someRepeatableField", Collection.class)).isFalse();
        assertThat(methodDeprecated(DeprecatedRecord.Builder.class, "addSomeRepeatableField", int.class)).isFalse();
        assertThat(methodDeprecated(DeprecatedRecord.Builder.class, "addAllSomeRepeatableField", Collection.class)).isFalse();

        assertThat(methodDeprecated(DeprecatedRecord.Builder.class, "someRepeatableEnum", Collection.class)).isFalse();
        assertThat(methodDeprecated(DeprecatedRecord.Builder.class, "addSomeRepeatableEnum", DeprecatedEnum.class)).isFalse();
        assertThat(methodDeprecated(DeprecatedRecord.Builder.class, "addAllSomeRepeatableEnum", Collection.class)).isFalse();

        assertThat(methodDeprecated(DeprecatedRecord.Builder.class, "someRepeatableEnumValue", Collection.class)).isFalse();
        assertThat(methodDeprecated(DeprecatedRecord.Builder.class, "addSomeRepeatableEnumValue", int.class)).isFalse();
        assertThat(methodDeprecated(DeprecatedRecord.Builder.class, "addAllSomeRepeatableEnumValue", Collection.class)).isFalse();

        assertThat(methodDeprecated(DeprecatedRecord.Builder.class, "someRepeatableOther", Collection.class)).isFalse();
        assertThat(methodDeprecated(DeprecatedRecord.Builder.class, "addSomeRepeatableOther", DeprecatedSubRecord.class)).isFalse();
        assertThat(methodDeprecated(DeprecatedRecord.Builder.class, "addAllSomeRepeatableOther", Collection.class)).isFalse();
    }

    @Test
    void builderMethodsDeprecated() throws NoSuchMethodException {
        // when then
        assertThat(methodDeprecated(DeprecatedRecord.Builder.class, "deprecatedField", int.class)).isTrue();
        assertThat(methodDeprecated(DeprecatedRecord.Builder.class, "deprecatedEnum", DeprecatedEnum.class)).isTrue();
        assertThat(methodDeprecated(DeprecatedRecord.Builder.class, "deprecatedEnumValue", int.class)).isTrue();
        assertThat(methodDeprecated(DeprecatedRecord.Builder.class, "deprecatedOther", DeprecatedSubRecord.class)).isTrue();

        assertThat(methodDeprecated(DeprecatedRecord.Builder.class, "deprecatedRepeatableField", Collection.class)).isTrue();
        assertThat(methodDeprecated(DeprecatedRecord.Builder.class, "addDeprecatedRepeatableField", int.class)).isTrue();
        assertThat(methodDeprecated(DeprecatedRecord.Builder.class, "addAllDeprecatedRepeatableField", Collection.class)).isTrue();

        assertThat(methodDeprecated(DeprecatedRecord.Builder.class, "deprecatedRepeatableEnum", Collection.class)).isTrue();
        assertThat(methodDeprecated(DeprecatedRecord.Builder.class, "addDeprecatedRepeatableEnum", DeprecatedEnum.class)).isTrue();
        assertThat(methodDeprecated(DeprecatedRecord.Builder.class, "addAllDeprecatedRepeatableEnum", Collection.class)).isTrue();

        assertThat(methodDeprecated(DeprecatedRecord.Builder.class, "deprecatedRepeatableEnumValue", Collection.class)).isTrue();
        assertThat(methodDeprecated(DeprecatedRecord.Builder.class, "addDeprecatedRepeatableEnumValue", int.class)).isTrue();
        assertThat(methodDeprecated(DeprecatedRecord.Builder.class, "addAllDeprecatedRepeatableEnumValue", Collection.class)).isTrue();

        assertThat(methodDeprecated(DeprecatedRecord.Builder.class, "deprecatedRepeatableOther", Collection.class)).isTrue();
        assertThat(methodDeprecated(DeprecatedRecord.Builder.class, "addDeprecatedRepeatableOther", DeprecatedSubRecord.class)).isTrue();
        assertThat(methodDeprecated(DeprecatedRecord.Builder.class, "addAllDeprecatedRepeatableOther", Collection.class)).isTrue();
    }

    @Test
    void builderMergeDeprecated() throws NoSuchMethodException {
        // when then
        assertThat(methodDeprecated(DeprecatedRecord.Builder.class, "mergeDeprecatedOther", DeprecatedSubRecord.class)).isTrue();
    }

    @Test
    void builderMergeNonDeprecated() throws NoSuchMethodException {
        // when then
        assertThat(methodDeprecated(DeprecatedRecord.Builder.class, "mergeSomeOther", DeprecatedSubRecord.class)).isFalse();
    }

    @Test
    void messageFieldNonDeprecated() throws NoSuchFieldException {
        // when then
        assertThat(fieldDeprecated(DeprecatedRecord.class, "someField")).isFalse();
        assertThat(fieldDeprecated(DeprecatedRecord.class, "someEnumValue")).isFalse();
        assertThat(fieldDeprecated(DeprecatedRecord.class, "someOther")).isFalse();

        assertThat(fieldDeprecated(DeprecatedRecord.class, "someRepeatableField")).isFalse();
        assertThat(fieldDeprecated(DeprecatedRecord.class, "someRepeatableEnumValue")).isFalse();
        assertThat(fieldDeprecated(DeprecatedRecord.class, "someRepeatableOther")).isFalse();
    }

    @Test
    void messageGetterNonDeprecated() throws NoSuchMethodException {
        // when then
        assertThat(methodDeprecated(DeprecatedRecord.class, "someField")).isFalse();
        assertThat(methodDeprecated(DeprecatedRecord.class, "someEnumValue")).isFalse();
        assertThat(methodDeprecated(DeprecatedRecord.class, "someEnum")).isFalse();
        assertThat(methodDeprecated(DeprecatedRecord.class, "someOther")).isFalse();

        assertThat(methodDeprecated(DeprecatedRecord.class, "someRepeatableField")).isFalse();
        assertThat(methodDeprecated(DeprecatedRecord.class, "someRepeatableEnumValue")).isFalse();
        assertThat(methodDeprecated(DeprecatedRecord.class, "someRepeatableEnum")).isFalse();
        assertThat(methodDeprecated(DeprecatedRecord.class, "someRepeatableOther")).isFalse();
    }

    @Test
    void messageFieldDeprecated() throws NoSuchFieldException {
        // when then
        assertThat(fieldDeprecated(DeprecatedRecord.class, "deprecatedField")).isTrue();
        assertThat(fieldDeprecated(DeprecatedRecord.class, "deprecatedEnumValue")).isTrue();
        assertThat(fieldDeprecated(DeprecatedRecord.class, "deprecatedOther")).isTrue();

        assertThat(fieldDeprecated(DeprecatedRecord.class, "deprecatedRepeatableField")).isTrue();
        assertThat(fieldDeprecated(DeprecatedRecord.class, "deprecatedRepeatableEnumValue")).isTrue();
        assertThat(fieldDeprecated(DeprecatedRecord.class, "deprecatedRepeatableOther")).isTrue();
    }

    @Test
    void messageGetterDeprecated() throws NoSuchMethodException {
        // when then
        assertThat(methodDeprecated(DeprecatedRecord.class, "deprecatedField")).isTrue();
        assertThat(methodDeprecated(DeprecatedRecord.class, "deprecatedEnumValue")).isTrue();
        assertThat(methodDeprecated(DeprecatedRecord.class, "deprecatedEnum")).isTrue();
        assertThat(methodDeprecated(DeprecatedRecord.class, "deprecatedOther")).isTrue();

        assertThat(methodDeprecated(DeprecatedRecord.class, "deprecatedRepeatableField")).isTrue();
        assertThat(methodDeprecated(DeprecatedRecord.class, "deprecatedRepeatableEnumValue")).isTrue();
        assertThat(methodDeprecated(DeprecatedRecord.class, "deprecatedRepeatableEnum")).isTrue();
        assertThat(methodDeprecated(DeprecatedRecord.class, "deprecatedRepeatableOther")).isTrue();
    }

    private boolean methodDeprecated(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
        return clazz.getDeclaredMethod(methodName, parameterTypes).getAnnotation(Deprecated.class) != null;
    }

    private boolean fieldDeprecated(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        return clazz.getDeclaredField(fieldName).getAnnotation(Deprecated.class) != null;
    }
}
