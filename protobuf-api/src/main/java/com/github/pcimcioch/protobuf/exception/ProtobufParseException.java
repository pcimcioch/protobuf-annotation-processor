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
     * @param messageFormat error message format
     * @param params        format parameters
     */
    public ProtobufParseException(String messageFormat, Object... params) {
        super(String.format(messageFormat, params));
    }
}
