plugins {
    `java-library`
    signing
    `maven-publish`
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
                name.set("Protobuf Annotation Processor API")
                description.set("API for library generating protobuf data classes")
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
                username = project.findProperty("ossrhUsername")?.toString() ?: ""
                password = project.findProperty("ossrhPassword")?.toString() ?: ""
            }
        }
    }
}

signing {
    useInMemoryPgpKeys(project.findProperty("signingKey").toString(), project.findProperty("signingPassword").toString())
    sign(publishing.publications["maven"])
}