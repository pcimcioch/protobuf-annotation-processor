package com.github.pcimcioch.protobuf.dto;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Protobuf message type
 */
public interface ProtobufMessage {

    /**
     * Writes this message as binary to the given output stream
     *
     * @param output output stream
     * @throws IOException in case of any write error
     */
    void writeTo(OutputStream output) throws IOException;

    /**
     * Returns this message as binary representation
     *
     * @return binary representation
     * @throws IOException in case of any write error
     */
    byte[] toByteArray() throws IOException;

    /**
     * Returns whether this message is empty. Meaning all the fields have default values
     *
     * @return whether this message is empty
     */
    boolean isEmpty();
}
