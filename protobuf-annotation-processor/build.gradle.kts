plugins {
    id("java-library")
}

dependencies {
    api(project(":protobuf-api"))

    implementation("org.jboss.forge.roaster:roaster-api:2.26.0.Final")
    runtimeOnly("org.jboss.forge.roaster:roaster-jdt:2.26.0.Final")
}