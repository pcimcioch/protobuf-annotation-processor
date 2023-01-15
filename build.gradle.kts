plugins {
    java
}

subprojects {
    apply(plugin = "java")

    group = "com.github.pcimcioch"

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }

    repositories {
        mavenCentral()
    }

    dependencies {
        testImplementation("org.assertj:assertj-core:3.23.1")
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
        testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.2")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
    }

    tasks.getByName<Test>("test") {
        useJUnitPlatform()
    }
}