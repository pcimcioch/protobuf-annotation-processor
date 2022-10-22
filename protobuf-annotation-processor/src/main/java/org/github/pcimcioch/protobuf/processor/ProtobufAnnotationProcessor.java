package org.github.pcimcioch.protobuf.processor;

import org.github.pcimcioch.protobuf.annotation.Message;
import org.github.pcimcioch.protobuf.annotation.ModelFactory;
import org.github.pcimcioch.protobuf.model.ProtoDefinitions;
import org.github.pcimcioch.protobuf.source.SourceFactory;
import org.github.pcimcioch.protobuf.source.SourceFactory.SourceFile;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.io.Writer;
import java.util.List;
import java.util.Set;

import static javax.tools.Diagnostic.Kind.ERROR;

@SupportedAnnotationTypes("org.github.pcimcioch.protobuf.annotation.Message")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class ProtobufAnnotationProcessor extends AbstractProcessor {

    private final ModelFactory modelFactory = new ModelFactory();
    private final SourceFactory sourceFactory = new SourceFactory();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        List<Message> messageAnnotations = getAllAnnotations(roundEnv);
        ProtoDefinitions model = buildModel(messageAnnotations);

        generateSources(model);

        return true;
    }

    private List<Message> getAllAnnotations(RoundEnvironment roundEnv) {
        return roundEnv.getElementsAnnotatedWith(Message.class).stream()
                .map(e -> e.getAnnotation(Message.class))
                .toList();
    }

    private ProtoDefinitions buildModel(List<Message> annotations) {
        return modelFactory.buildProtoDefinitions(annotations);
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
