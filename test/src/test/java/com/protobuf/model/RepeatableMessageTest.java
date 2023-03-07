package com.protobuf.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RepeatableMessageTest {

    private static final RepeatableOtherAddress address1 = new RepeatableOtherAddress("Test", 10);
    private static final RepeatableOtherAddress address2 = new RepeatableOtherAddress("Foobar", 12);
    private static final RepeatableOtherAddress address3 = new RepeatableOtherAddress("Testers", 54);
    private static final RepeatableOtherAddress address4 = new RepeatableOtherAddress("Protobuf", 123);
    private static final RepeatableOtherAddress address5 = new RepeatableOtherAddress("Annotation", 1);
    private static final RepeatableOtherAddress address6 = new RepeatableOtherAddress("Bug", 111);
    private static final RepeatableOtherAddress address7 = new RepeatableOtherAddress("Last", 1000);

    @Test
    void defaultValues() {
        // when
        RepeatableOtherWork model = RepeatableOtherWork.empty();

        // then
        assertThat(model.addresses()).isEmpty();
        assertThat(model.protobufSize()).isEqualTo(0);
    }

    @Test
    void singleElements() {
        // when
        RepeatableOtherWork model = new RepeatableOtherWork(List.of(address1));

        // then
        assertThat(model.addresses()).containsExactly(address1);
        assertThat(model.protobufSize()).isEqualTo(10);
    }

    @Test
    void multipleElements() {
        // when
        RepeatableOtherWork model = new RepeatableOtherWork(List.of(address1, address2));

        // then
        assertThat(model.addresses()).containsExactly(address1, address2);
        assertThat(model.protobufSize()).isEqualTo(22);
    }

    @Test
    void modifyInputList() {
        // given
        List<RepeatableOtherAddress> input = new ArrayList<>();
        input.add(address1);
        RepeatableOtherWork model = new RepeatableOtherWork(input);

        // when
        input.add(address2);

        // then
        assertThat(model.addresses()).containsExactly(address1);
        assertThat(model.protobufSize()).isEqualTo(10);
    }

    @Test
    void modifyOutputList() {
        // given
        List<RepeatableOtherAddress> input = new ArrayList<>();
        input.add(address1);
        RepeatableOtherWork model = new RepeatableOtherWork(input);

        // when then
        assertThatThrownBy(() -> model.addresses().add(address2))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void modifyBuilderParameters() {
        // given
        List<RepeatableOtherAddress> input1 = new ArrayList<>();
        input1.add(address1);
        input1.add(address2);
        List<RepeatableOtherAddress> input2 = new ArrayList<>();
        input2.add(address3);
        input2.add(address4);
        RepeatableOtherWork.Builder modelBuilder = RepeatableOtherWork.builder()
                .addresses(input1)
                .addAllAddresses(input2)
                .addAddresses(address5);

        // when
        input1.add(address6);
        input2.add(address7);
        RepeatableOtherWork model = modelBuilder.build();

        // then
        assertThat(model.addresses()).containsExactly(address1, address2, address3, address4, address5);
        assertThat(model.protobufSize()).isEqualTo(65);
    }

    @Test
    void builderSetterOverrides() {
        // when
        RepeatableOtherWork model = RepeatableOtherWork.builder()
                .addAllAddresses(Set.of(address1, address2))
                .addresses(List.of(address3, address4))
                .build();

        // then
        assertThat(model.addresses()).containsExactly(address3, address4);
        assertThat(model.protobufSize()).isEqualTo(27);
    }
}
