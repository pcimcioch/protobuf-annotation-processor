package com.protobuf.model;

import com.protobuf.model.test.PackagesPartial;
import com.test.PackagesFull;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PackagesTest {

    @Test
    void differentPackageDefinitions() {
        // when then
        assertThat(PackagesFull.class.getPackageName()).isEqualTo("com.test");
        assertThat(PackagesPartial.class.getPackageName()).isEqualTo("com.protobuf.model.test");
        assertThat(PackagesSimple.class.getPackageName()).isEqualTo("com.protobuf.model");
    }
}
