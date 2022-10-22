package org.github.pcimcioch.protobuf.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
public @interface Field {
    String type();
    String name();
    int number();

    String _double = "double";
    String _float = "float";
    String int32 = "int32";
    String int64 = "int64";
    String uint32 = "uint32";
    String uint64 = "uint64";
    String sint32 = "sint32";
    String sint64 = "sint64";
    String fixed32 = "fixed32";
    String fixed64 = "fixed64";
    String sfixed32 = "sfixed32";
    String sfixed64 = "sfixed64";
    String bool = "bool";
    String string = "string";
    String bytes = "bytes";
}
