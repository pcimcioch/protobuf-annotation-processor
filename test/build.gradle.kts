plugins {
    id("com.google.protobuf") version "0.9.1"
}

dependencies {
    implementation("com.google.protobuf:protobuf-java:3.21.12")

    implementation(project(":protobuf-api"))
    annotationProcessor(project(":protobuf"))
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.21.12"
    }
}

// TODO can it be removed?
//sourceSets {
//    named("main") {
//        java.srcDir("build/generated/source/proto/main/java")
//    }
//}