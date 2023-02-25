package com.github.pcimcioch.protobuf.io.exception;

import java.io.Serial;

/**
 * Indicates that varint is encoded incorrectly
 */
public class MalformedVarintException extends ProtobufException {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     */
    public MalformedVarintException() {
        super("Malformed varint");
    }
}
