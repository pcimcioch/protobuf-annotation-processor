package org.github.pcimcioch.protobuf.exception;

public class ProtobufException extends RuntimeException {
    static final long serialVersionUID = 1L;

    public ProtobufException(String message) {
        super(message);
    }
}
