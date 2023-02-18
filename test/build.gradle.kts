plugins {
    id("com.google.protobuf") version "0.9.2"
    id("me.champeau.jmh") version "0.6.8"
}

dependencies {
    implementation("com.google.protobuf:protobuf-java:3.22.0")

    implementation(project(":protobuf-api"))
    annotationProcessor(project(":protobuf"))
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.22.0"
    }
}