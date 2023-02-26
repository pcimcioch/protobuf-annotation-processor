package com.github.pcimcioch.protobuf.io.exception;

import java.io.Serial;

/**
 * Indicates that unsupported wire type was encountered
 */
public class UnsupportedWireTypeException extends ProtobufException {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     *
     * @param wireTypeName name of unsupported wire type
     */
    public UnsupportedWireTypeException(String wireTypeName) {
        super("Unsupported wire type " + wireTypeName);
    }
}