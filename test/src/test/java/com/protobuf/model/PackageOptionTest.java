package com.protobuf.model;

import com.protobuf.test.PackageOptionSimple;
import com.protobuf.test.test.PackageOptionPartial;
import com.test.PackageOptionFull;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PackageOptionTest {

    @Test
    void differentPackageDefinitions() {
        // when then
        assertThat(PackageOptionFull.class.getPackageName()).isEqualTo("com.test");
        assertThat(PackageOptionPartial.class.getPackageName()).isEqualTo("com.protobuf.test.test");
        assertThat(PackageOptionSimple.class.getPackageName()).isEqualTo("com.protobuf.test");
    }
}
