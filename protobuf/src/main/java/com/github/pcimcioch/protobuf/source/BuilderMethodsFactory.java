package com.github.pcimcioch.protobuf.source;

import com.github.pcimcioch.protobuf.code.CodeBody;
import com.github.pcimcioch.protobuf.code.RecordSource;
import com.github.pcimcioch.protobuf.model.message.MessageDefinition;

import static com.github.pcimcioch.protobuf.code.AnnotationSource.annotation;
import static com.github.pcimcioch.protobuf.code.CodeBody.body;
import static com.github.pcimcioch.protobuf.code.CodeBody.param;
import static com.github.pcimcioch.protobuf.code.FieldSource.field;
import static com.github.pcimcioch.protobuf.code.FinalSource.finalModifier;
import static com.github.pcimcioch.protobuf.code.InitializerSource.initializer;
import static com.github.pcimcioch.protobuf.code.MethodSource.method;
import static com.github.pcimcioch.protobuf.code.ParameterSource.parameter;
import static com.github.pcimcioch.protobuf.code.ReturnSource.returns;
import static com.github.pcimcioch.protobuf.code.StaticSource.staticModifier;
import static com.github.pcimcioch.protobuf.code.VisibilitySource.privateVisibility;
import static com.github.pcimcioch.protobuf.code.VisibilitySource.publicVisibility;

class BuilderMethodsFactory {

    void addBuilderMethods(RecordSource source, MessageDefinition message) {
        addEmptyMethods(source, message);
        addMergeMethod(source, message);
        addBuilderFactoryMethods(source, message);
    }

    private void addEmptyMethods(RecordSource source, MessageDefinition message) {
        source.add(field(message.name(), "EMPTY")
                .set(privateVisibility())
                .set(staticModifier())
                .set(finalModifier())
                .set(initializer("new Builder().build()"))
        );

        CodeBody emptyBody = body("return EMPTY;");
        source.add(method("empty")
                .set(publicVisibility())
                .set(staticModifier())
                .set(returns(message.name()))
                .set(emptyBody)
        );

        CodeBody isEmptyBody = body("return EMPTY.equals(this);");
        source.add(method("isEmpty")
                .set(publicVisibility())
                .set(returns(boolean.class))
                .set(isEmptyBody)
                .add(annotation(Override.class))
        );
    }

    private void addMergeMethod(RecordSource source, MessageDefinition message) {
        CodeBody body = body("return toBuilder().merge(toMerge).build();");

        source.add(method("merge")
                .set(publicVisibility())
                .set(returns(message.name()))
                .set(body)
                .add(annotation(Override.class))
                .add(parameter(message.name(), "toMerge"))
        );
    }

    private void addBuilderFactoryMethods(RecordSource source, MessageDefinition message) {
        CodeBody toBuilderBody = body("return builder().merge(this);");
        source.add(method("toBuilder")
                .set(publicVisibility())
                .set(returns(message.builderName()))
                .set(toBuilderBody)
        );

        CodeBody builderBody = body("return new $BuilderType();",
                param("BuilderType", message.builderName())
        );
        source.add(method("builder")
                .set(publicVisibility())
                .set(staticModifier())
                .set(returns(message.builderName()))
                .set(builderBody)
        );
    }
}
