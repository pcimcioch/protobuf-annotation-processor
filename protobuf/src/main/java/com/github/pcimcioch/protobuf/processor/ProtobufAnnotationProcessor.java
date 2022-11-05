package com.github.pcimcioch.protobuf.processor;

import com.github.pcimcioch.protobuf.annotation.Message;
import com.github.pcimcioch.protobuf.annotation.Messages;
import com.github.pcimcioch.protobuf.annotation.ModelFactory;
import com.github.pcimcioch.protobuf.model.ProtoDefinitions;
import com.github.pcimcioch.protobuf.source.SourceFactory;
import com.github.pcimcioch.protobuf.source.SourceFactory.SourceFile;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.io.Writer;
import java.util.Set;

import static javax.tools.Diagnostic.Kind.ERROR;

/**
 * Annotation Processor that creates java source files for protobuf transfer objects from annotations
 */
@SupportedAnnotationTypes({"com.github.pcimcioch.protobuf.annotation.Message", "com.github.pcimcioch.protobuf.annotation.Messages"})
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class ProtobufAnnotationProcessor extends AbstractProcessor {

    private final ModelFactory modelFactory = new ModelFactory();
    private final SourceFactory sourceFactory = new SourceFactory();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        ProtoDefinitions model = buildModel(getAnnotatedElements(roundEnv));
        generateSources(model);

        return true;
    }

    private static Set<? extends Element> getAnnotatedElements(RoundEnvironment roundEnv) {
        return roundEnv.getElementsAnnotatedWithAny(Set.of(Message.class, Messages.class));
    }

    private ProtoDefinitions buildModel(Set<? extends Element> elements) {
        return modelFactory.buildProtoDefinitions(elements);
    }

    private void generateSources(ProtoDefinitions model) {
        try {
            for (SourceFile sourceFile : sourceFactory.buildSource(model)) {
                try (Writer writer = processingEnv.getFiler().createSourceFile(sourceFile.canonicalName()).openWriter()) {
                    writer.write(sourceFile.source());
                }
            }
        } catch (Exception ex) {
            processingEnv.getMessager().printMessage(ERROR, "Unable to build source: " + ex.getMessage());
            // TODO rethrow ex?
        }
    }
}
