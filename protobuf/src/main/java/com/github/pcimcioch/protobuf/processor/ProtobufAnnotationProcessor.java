package com.github.pcimcioch.protobuf.processor;

import com.github.pcimcioch.protobuf.annotation.Enumeration;
import com.github.pcimcioch.protobuf.annotation.Enumerations;
import com.github.pcimcioch.protobuf.annotation.JavaPackage;
import com.github.pcimcioch.protobuf.annotation.Message;
import com.github.pcimcioch.protobuf.annotation.Messages;
import com.github.pcimcioch.protobuf.annotation.ModelFactory;
import com.github.pcimcioch.protobuf.annotation.ProtoFile;
import com.github.pcimcioch.protobuf.model.ProtoDefinitions;
import com.github.pcimcioch.protobuf.source.SourceFactory;
import com.github.pcimcioch.protobuf.source.SourceFactory.SourceFile;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static javax.tools.Diagnostic.Kind.ERROR;

/**
 * Annotation Processor that creates java source files for protobuf transfer objects from annotations
 */
@SupportedAnnotationTypes({
        "com.github.pcimcioch.protobuf.annotation.Message",
        "com.github.pcimcioch.protobuf.annotation.Messages",
        "com.github.pcimcioch.protobuf.annotation.Enumeration",
        "com.github.pcimcioch.protobuf.annotation.Enumerations",
        "com.github.pcimcioch.protobuf.annotation.Option"
})
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class ProtobufAnnotationProcessor extends AbstractProcessor {

    private final ModelFactory modelFactory = new ModelFactory();
    private final SourceFactory sourceFactory = new SourceFactory();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            List<ProtoFile> protoFiles = getAnnotatedElements(roundEnv);
            ProtoDefinitions model = buildModel(protoFiles);
            generateSources(model);
        } catch (Exception ex) {
            processingEnv.getMessager().printMessage(ERROR, "Unable to build source: " + ex.getMessage());
        }

        return true;
    }

    private List<ProtoFile> getAnnotatedElements(RoundEnvironment roundEnv) {
        return roundEnv.getElementsAnnotatedWithAny(Set.of(Message.class, Messages.class, Enumeration.class, Enumerations.class)).stream()
                .map(this::toProtoFile)
                .toList();
    }

    private ProtoFile toProtoFile(Element element) {
        Message[] messages = element.getAnnotationsByType(Message.class);
        Enumeration[] enumerations = element.getAnnotationsByType(Enumeration.class);
        String javaPackage = Optional.ofNullable(element.getAnnotation(JavaPackage.class))
                .map(JavaPackage::value)
                .orElseGet(() -> packageOf(element).getQualifiedName().toString());

        return new ProtoFile(javaPackage, List.of(messages), List.of(enumerations));
    }

    private ProtoDefinitions buildModel(List<ProtoFile> protoFiles) {
        return modelFactory.buildProtoDefinitions(protoFiles);
    }

    private void generateSources(ProtoDefinitions model) throws IOException {
        for (SourceFile sourceFile : sourceFactory.buildSource(model)) {
            try (Writer writer = processingEnv.getFiler().createSourceFile(sourceFile.canonicalName()).openWriter()) {
                writer.write(sourceFile.source());
            }
        }
    }

    private static PackageElement packageOf(Element element) {
        Element enclosing = element;
        while (enclosing.getKind() != ElementKind.PACKAGE) {
            enclosing = enclosing.getEnclosingElement();
        }

        return (PackageElement) enclosing;
    }
}
