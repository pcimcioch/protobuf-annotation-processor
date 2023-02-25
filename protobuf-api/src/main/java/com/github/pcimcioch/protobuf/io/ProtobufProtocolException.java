package com.github.pcimcioch.protobuf.io;

import java.io.IOException;
import java.io.Serial;

/**
 * Represents problems with proto binary format
 */
public final class ProtobufProtocolException extends IOException {

    @Serial
    private static final long serialVersionUID = 1L;

    private ProtobufProtocolException(String message) {
        super(message);
    }

    static ProtobufProtocolException inputEnded() {
        return new ProtobufProtocolException("Tried to read bytes over the input");
    }

    static ProtobufProtocolException limitExceeded() {
        return new ProtobufProtocolException("Tried to read bytes over the limit");
    }

    static ProtobufProtocolException malformedVarint() {
        return new ProtobufProtocolException("Malformed varint");
    }

    static ProtobufProtocolException sgroupNotSupported() {
        return new ProtobufProtocolException("Wire Type SGROUP is not supported");
    }

    static ProtobufProtocolException egroupNotSupported() {
        return new ProtobufProtocolException("Wire Type EGROUP is not supported");
    }

    static ProtobufProtocolException unknownWireType() {
        return new ProtobufProtocolException("Unknown wire type");
    }
}
