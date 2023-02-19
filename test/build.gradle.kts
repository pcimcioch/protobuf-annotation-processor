plugins {
    id("com.google.protobuf") version "0.9.2"
    id("me.champeau.jmh") version "0.6.8"
}

dependencies {
    implementation("com.google.protobuf:protobuf-java:3.22.0")

    implementation(project(":protobuf-api"))
    annotationProcessor(project(":protobuf"))

    jmhAnnotationProcessor("org.openjdk.jmh:jmh-generator-annprocess:1.36")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.22.0"
    }
}

jmh {
    resultFormat.set("json")
}