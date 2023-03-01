package com.github.pcimcioch.protobuf.io.exception;

import java.io.Serial;

/**
 * Indicates that nested object in protobuf encoding is truncated and data stream ended prematurely by hitting the read limit
 */
public class LimitExceededException extends ProtobufException {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     */
    public LimitExceededException() {
        super("Reading over limit");
    }
}
