package com.github.pcimcioch.protobuf.io.exception;

import java.io.Serial;

/**
 * Indicates that unknown wire type was encountered
 */
public class UnknownWireTypeException extends ProtobufException {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     */
    public UnknownWireTypeException() {
        super("Reading over limit");
    }
}
