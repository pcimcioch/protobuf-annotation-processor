# Protobuf Annotation Processor
This library was created to generate java data classes for protobuf binary format using java annotation processor mechanism.
Currently, the library is in the early stage of development and support only most basic functionalities.

There are few [existing solutions](#similar-solutions), but I wanted to achieve the following:
- Use standard java toolchain. No need for external compilers and additional gradle/maven plugins (in contrary to official protoc)
- Generate code in the pre-compile stage, not in the runtime (in contrary to Protostuff)
- Generate whole java classes, not enhance existing POJOs (in contrary to Protostuff).

I believe that first two points are the big advantage of this library. The last one may be considered a disadvantage in 
some use cases. But well, if there is a demand for such feature, it can always be added. The current architecture do not
forbid adding such functionality.

Generated data classes are java records, which I think fits perfectly in this case. Those are basically data transfer objects with
only serialization logic. By definition, they are immutable and provide good equals / hashcode and toString implementations.

# Quick Start
You can define your protobuf schema using only java annotations
```java
@Message(
        packageName = "com.example",
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
        SimpleRecord r2 = SimpleRecord.builder()
                .amount(10)
                .latitude(20.0)
                .longitude(30.0)
                .build();

        // Serialize record
        Path file = Paths.get("test.data");

        byte[] data = r1.toByteArray();
        try (OutputStream output = Files.newOutputStream(file)) {
            r1.writeTo(output);
        }

        // Deserialize record
        SimpleRecord r3 = SimpleRecord.parse(data);
        try (InputStream input = Files.newInputStream(file)) {
            SimpleRecord r4 = SimpleRecord.parse(input);
        }

        // read record fields
        System.out.printf("Record [amount=%d, latitude=%f, longitude=%f]%n", r3.amount(), r3.latitude(), r3.longitude());
    }
}
```

There is also a plan to support `*.proto` files as a source of protobuf schema, but it is not implemented yet
```java
// model schema defined in file model.proto
@Protobuf("model.proto")
class Main {
}
```

# Maven Repository
Library is available in Central Maven Repository

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

```kotlin
implementation("com.github.pcimcioch:protobuf-api:1.0.0")
annotationProcessor("com.github.pcimcioch:protobuf:1.0.0")
```

Note that `protobuf-api` is `implementation` dependency, not `compileOnly`. I may change it in the future, but for now it's just easier
to have it like that

# Similar Solutions

## Official Protoc Compiler
The best solution in terms of the support and feature-completeness is official 
[protobuf library](https://developers.google.com/protocol-buffers/docs/javatutorial) from the Google.

The biggest disadvantage of this solution is that you have to use separate `protoc` compiler to create your java classes.
Moreover, I don't really like the model that is generated by this compiler (although it is highly personal, subjective opinion).

To easily incorporate it in your build pipeline you can use some 3rd party plugins, like for example 
[protobuf-gradle-plugin](https://github.com/google/protobuf-gradle-plugin)

## Protostuff
[Protostuff](https://protostuff.github.io/docs/protostuff-runtime/) is an annotation-based solution to turn existing POJOs
into the protobuf serializers / deserializers.

It integrates seamlessly with your java application and allows you to add protobuf serialization logic to the existing POJOs.
The biggest disadvantage is that it uses reflection and generates code in the runtime. Also, it requires you to create POJOs
to annotate, it does not create whole classes for you (which may be advantage or disadvantage, depending on what you need)

# Features
For general Protobuf documentation see [official documentation](https://developers.google.com/protocol-buffers/docs/proto3)
This library will support only `proto3` specification

Current feature support:

| Feature                   | Link                                                                                | Support                                    |
|---------------------------|-------------------------------------------------------------------------------------|--------------------------------------------|
| Proto2                    | <https://developers.google.com/protocol-buffers/docs/proto>                         | Not planned to be supported                |
| Proto3                    | <https://developers.google.com/protocol-buffers/docs/proto3>                        | Partial support (see details below)        |
| Scalar Fields             | <https://developers.google.com/protocol-buffers/docs/proto3#scalar>                 | Supported                                  |
| Default Values            | <https://developers.google.com/protocol-buffers/docs/proto3#default>                | Supported                                  |
| Packages                  | <https://developers.google.com/protocol-buffers/docs/proto3#packages>               | Supported                                  |
| Repeated Fields           | <https://developers.google.com/protocol-buffers/docs/proto3#specifying_field_rules> | Not yet supported                          |
| Optional Fields           | <https://developers.google.com/protocol-buffers/docs/proto3#specifying_field_rules> | Not yet supported                          |
| Enumerations              | <https://developers.google.com/protocol-buffers/docs/proto3#enum>                   | Not yet supported                          |
| Reserved Fields           | <https://developers.google.com/protocol-buffers/docs/proto3#reserved>               | Not yet supported                          |
| Using Other Message Types | <https://developers.google.com/protocol-buffers/docs/proto3#other>                  | Not yet supported                          |
| Nested Types              | <https://developers.google.com/protocol-buffers/docs/proto3#nested>                 | Not yet supported                          |
| Unknown Fields            | <https://developers.google.com/protocol-buffers/docs/proto3#unknowns>               | Ignored, not included in serialized object |
| Maps                      | <https://developers.google.com/protocol-buffers/docs/proto3#maps>                   | Not yet supported                          |
| Options                   | <https://developers.google.com/protocol-buffers/docs/proto3#options>                | Not yet supported                          |
| Any                       | <https://developers.google.com/protocol-buffers/docs/proto3#any>                    | Not planned to be supported                |
| Oneof                     | <https://developers.google.com/protocol-buffers/docs/proto3#oneof>                  | Not planned to be supported                |
| Services                  | <https://developers.google.com/protocol-buffers/docs/proto3#services>               | Not planned to be supported                |
| JSON Mapping              | <https://developers.google.com/protocol-buffers/docs/proto3#json>                   | Not planned to be supported                |
| Options                   | <https://developers.google.com/protocol-buffers/docs/proto3#options>                | Not planned to be supported                |