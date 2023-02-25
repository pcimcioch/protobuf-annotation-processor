package com.github.pcimcioch.protobuf.io.exception;

import java.io.Serial;

/**
 * Indicates that input data has ended prematurely
 */
public class InputEndedException extends ProtobufException {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     */
    public InputEndedException() {
        super("Unexpected end of the input data");
    }
}
