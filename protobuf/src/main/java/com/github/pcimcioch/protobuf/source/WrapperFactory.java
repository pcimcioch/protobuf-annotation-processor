package com.github.pcimcioch.protobuf.source;

import com.github.pcimcioch.protobuf.code.ClassSource;
import com.github.pcimcioch.protobuf.model.ProtoDefinitionsWrapper;

import static com.github.pcimcioch.protobuf.code.ClassSource.clazz;
import static com.github.pcimcioch.protobuf.code.ConstructorSource.constructor;
import static com.github.pcimcioch.protobuf.code.FinalSource.finalModifier;
import static com.github.pcimcioch.protobuf.code.VisibilitySource.privateVisibility;

class WrapperFactory {

    ClassSource buildWrapperClass(ProtoDefinitionsWrapper wrapper) {
        ClassSource source = buildSourceFile(wrapper);
        addConstructor(source);

        return source;
    }

    private ClassSource buildSourceFile(ProtoDefinitionsWrapper wrapper) {
        return clazz(wrapper.name())
                .set(finalModifier());
    }

    private void addConstructor(ClassSource source) {
        source.add(constructor()
                .set(privateVisibility()));
    }
}
