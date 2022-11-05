plugins {
    `java-library`
    signing
    `maven-publish`
}

dependencies {
    api(project(":protobuf-api"))

    implementation("org.jboss.forge.roaster:roaster-api:2.26.0.Final")
    runtimeOnly("org.jboss.forge.roaster:roaster-jdt:2.26.0.Final")
}

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

            pom {
                name.set("Protobuf Annotation Processor")
                description.set("Library generating protobuf data classes")
                url.set("https://github.com/pcimcioch/protobuf-annotation-processor")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/pcimcioch/protobuf-annotation-processor.git")
                    developerConnection.set("scm:git:git@github.com:pcimcioch/protobuf-annotation-processor.git")
                    url.set("https://github.com/pcimcioch/protobuf-annotation-processor")
                }
                developers {
                    developer {
                        id.set("pcimcioch")
                        name.set("Przemysław Cimcioch")
                        email.set("cimcioch.przemyslaw@gmail.com")
                    }
                }
            }
        }
    }

    repositories {
        val url = if (project.version.toString().contains("SNAPSHOT")) "https://oss.sonatype.org/content/repositories/snapshots" else "https://oss.sonatype.org/service/local/staging/deploy/maven2"
        maven(url) {
            credentials {
                username = project.findProperty("ossrh.username")?.toString() ?: ""
                password = project.findProperty("ossrh.password")?.toString() ?: ""
            }
        }
    }
}

signing {
    sign(publishing.publications["maven"])
}