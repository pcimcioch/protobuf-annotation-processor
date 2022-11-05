package com.github.pcimcioch.protobuf.exception;

import java.io.Serial;

/**
 * Indicates that there was a problem while parsing input bytes to the java object
 */
public class ProtobufParseException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1234567L;

    /**
     * Constructor
     *
     * @param message error message
     */
    public ProtobufParseException(String message) {
        super(message);
    }
}
