package com.github.pcimcioch.protobuf.io.exception;

import java.io.IOException;
import java.io.Serial;

/**
 * Represents problems with protobuf format encoding
 */
public abstract class ProtobufException extends IOException {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     *
     * @param message exception message
     */
    protected ProtobufException(String message) {
        super(message);
    }
}
