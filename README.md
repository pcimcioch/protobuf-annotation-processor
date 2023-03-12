# Protobuf Annotation Processor

This library was created to generate java data classes for protobuf binary format using java annotation processor
mechanism.
Currently, the library is in the early stage of development and support only most basic functionalities.

There are few [existing solutions](#similar-solutions), but I wanted to achieve the following:

- Use standard java toolchain. No need for external compilers and additional gradle/maven plugins (in contrary to
  official protoc)
- Generate code in the pre-compile stage, not in the runtime (in contrary to Protostuff)
- Generate whole java classes, not enhance existing POJOs (in contrary to Protostuff).

I believe that first two points are the big advantage of this library. The last one may be considered a disadvantage in
some use cases. But well, if there is a demand for such feature, it can always be added. The current architecture do not
forbid adding such functionality.

Generated data classes are java records, which I think fits perfectly in this case. Those are basically data transfer
objects with
only serialization logic. By definition, they are immutable and provide good equals, hashcode and toString
implementations.

The biggest disadvantage of this library compared to the others is the performance. Official protoc generates code that
is very efficient.
It doesn't mean that this implementation is slow. If you don't use Protobuf for lightning fast serialization, this
implementation might be a viable
pick for you. If you do care about each microsecond though, just check its performance for your case.
There are few [JMH performance tests](test/src/jmh/java/com/protobuf/performance/PerformanceTest.java) that compare this
solution with
protoc.

# Quick Start

You can define your protobuf schema using only java annotations

```java

@Message(
        name = "SimpleRecord",
        fields = {
                @Field(type = "int32", name = "amount", number = 0),
                @Field(type = "double", name = "latitude", number = 1),
                @Field(type = "double", name = "longitude", number = 2)
        }
)
class Main {
    public static void main(String[] args) {
        // Create record
        SimpleRecord r1 = new SimpleRecord(10, 20.0, 30.0);
        // You can use builder as well
        SimpleRecord r2 = SimpleRecord.builder()
                .amount(10)
                .latitude(20.0)
                .longitude(30.0)
                .build();

        // Serialize to bytes array
        byte[] data = r1.toByteArray();
        // Or to output stream
        Path file = Paths.get("test.data");
        try (OutputStream output = Files.newOutputStream(file)) {
            r1.writeTo(output);
        }

        // Deserialize from bytes array
        SimpleRecord r3 = SimpleRecord.parse(data);
        // Or from input stream
        try (InputStream input = Files.newInputStream(file)) {
            SimpleRecord r4 = SimpleRecord.parse(input);
        }

        // read record fields
        System.out.printf("Record [amount=%d, latitude=%f, longitude=%f]%n", r3.amount(), r3.latitude(), r3.longitude());
    }
}
```

There is also a plan to support `*.proto` files as a source of protobuf schema, but **it is not implemented yet**

```java
// model schema defined in file model.proto
@Protobuf("model.proto")
class Main {
}
```

# Maven Repository

Library is available in Central Maven Repository

```kotlin
implementation("com.github.pcimcioch:protobuf-api:1.0.0")
annotationProcessor("com.github.pcimcioch:protobuf:1.0.0")
```

Note that `protobuf-api` is `implementation` dependency, not `compileOnly`.

TODO make sure this is correct maven configuration
```xml

<dependencies>
    <dependency>
        <groupId>com.github.pcimcioch</groupId>
        <artifactId>protobuf-api</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>

<pluginManagement>
<plugins>
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.6.1</version>
        <configuration>
            <annotationProcessorPaths>
                <annotationProcessorPath>
                    <groupId>com.github.pcimcioch</groupId>
                    <artifactId>protobuf</artifactId>
                    <version>1.0.0</version>
                </annotationProcessorPath>
            </annotationProcessorPaths>
        </configuration>
    </plugin>
</plugins>
</pluginManagement>
```

# Examples

For full documentation just see [examples in the test module](test/src/main/java/com/protobuf/model)

# Similar Solutions

## Official Protoc Compiler

The best solution in terms of the support and feature-completeness is official
[protobuf library](https://developers.google.com/protocol-buffers/docs/javatutorial) from the Google.

The biggest disadvantage of this solution is that you have to use separate `protoc` compiler to create your java
classes.
Moreover, I don't really like the model that is generated by this compiler (although it is highly personal, subjective
opinion).

To easily incorporate it in your build pipeline you can use some 3rd party plugins, like for example
[protobuf-gradle-plugin](https://github.com/google/protobuf-gradle-plugin)

## Protostuff

[Protostuff](https://protostuff.github.io/docs/protostuff-runtime/) is an annotation-based solution to turn existing
POJOs
into the protobuf serializers / deserializers.

It integrates seamlessly with your java application and allows you to add protobuf serialization logic to the existing
POJOs.
The biggest disadvantage is that it uses reflection and generates code in the runtime. Also, it requires you to create
POJOs
to annotate, it does not create whole classes for you (which may be advantage or disadvantage, depending on what you
need)

# Features

For general Protobuf documentation
see [official documentation](https://developers.google.com/protocol-buffers/docs/proto3)
This library will support only `proto3` specification

Current feature support:

| Feature                     | Link                                                                                | Support                              |
|-----------------------------|-------------------------------------------------------------------------------------|--------------------------------------|
| Proto2                      | <https://developers.google.com/protocol-buffers/docs/proto>                         | ⭕ Not planned to be supported        |
| Proto3                      | <https://developers.google.com/protocol-buffers/docs/proto3>                        | ✔️ Supported (see the details below) |
| Scalar Fields               | <https://developers.google.com/protocol-buffers/docs/proto3#scalar>                 | ✔️ Supported                         |
| Default Values              | <https://developers.google.com/protocol-buffers/docs/proto3#default>                | ✔️ Supported                         |
| Packages                    | <https://developers.google.com/protocol-buffers/docs/proto3#packages>               | ✔️ Supported                         |
| Repeated Fields             | <https://developers.google.com/protocol-buffers/docs/proto3#specifying_field_rules> | ✔️ Supported                         |
| Optional Fields             | <https://developers.google.com/protocol-buffers/docs/proto3#specifying_field_rules> | ⭕ Not planned to be supported        |
| Required Fields             | <https://developers.google.com/protocol-buffers/docs/proto3#specifying_field_rules> | ⭕ Not planned to be supported        |
| Enumerations                | <https://developers.google.com/protocol-buffers/docs/proto3#enum>                   | ✔️ Supported                         |
| Reserved Fields             | <https://developers.google.com/protocol-buffers/docs/proto3#reserved>               | ✔️ Supported                         |
| Using Other Message Types   | <https://developers.google.com/protocol-buffers/docs/proto3#other>                  | ✔️ Supported                         |
| Nested Types                | <https://developers.google.com/protocol-buffers/docs/proto3#nested>                 | ✔️ Supported                         |
| Unknown Fields              | <https://developers.google.com/protocol-buffers/docs/proto3#unknowns>               | ⏳ Not yet supported                  |
| Maps                        | <https://developers.google.com/protocol-buffers/docs/proto3#maps>                   | ⏳ Not yet supported                  |
| Option java_package         | <https://developers.google.com/protocol-buffers/docs/proto3#options>                | ✔️ Supported                         |
| Option java_multiple_files  | <https://developers.google.com/protocol-buffers/docs/proto3#options>                | ✔️ Supported, `false` by default     |
| Option java_outer_classname | <https://developers.google.com/protocol-buffers/docs/proto3#options>                | ✔️ Supported                         |
| Option optimize_for         | <https://developers.google.com/protocol-buffers/docs/proto3#options>                | ✔️ Always `LITE_RUNTIME`             |
| Option deprecated           | <https://developers.google.com/protocol-buffers/docs/proto3#options>                | ✔️ Supported                         |
| Any                         | <https://developers.google.com/protocol-buffers/docs/proto3#any>                    | ⭕ Not planned to be supported        |
| Oneof                       | <https://developers.google.com/protocol-buffers/docs/proto3#oneof>                  | ⭕ Not planned to be supported        |
| Services                    | <https://developers.google.com/protocol-buffers/docs/proto3#services>               | ⭕ Not planned to be supported        |
| JSON Mapping                | <https://developers.google.com/protocol-buffers/docs/proto3#json>                   | ⭕ Not planned to be supported        |