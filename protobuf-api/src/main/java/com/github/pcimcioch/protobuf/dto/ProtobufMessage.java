package com.github.pcimcioch.protobuf.dto;

import com.github.pcimcioch.protobuf.io.ProtobufWriter;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Protobuf message type
 */
public interface ProtobufMessage<T extends ProtobufMessage<T>> {

    /**
     * Writes this message as binary to the given writer
     *
     * @param writer protobuf writer
     * @throws IOException in case of any write error
     */
    void writeTo(ProtobufWriter writer) throws IOException;

    /**
     * Returns whether this message is empty. Meaning all the fields have default values
     *
     * @return whether this message is empty
     */
    boolean isEmpty();

    /**
     * Returns new message that was created by merging given message into this. Merging is done by replacing
     * non-default fields from toMerge
     *
     * @param toMerge message to merge
     * @return new message
     */
    T merge(T toMerge);

    /**
     * Returns message size in protobuf format
     *
     * @return message size
     */
    int protobufSize();

    /**
     * Writes this message as binary to the given output stream
     *
     * @param output output stream
     * @throws IOException in case of any write error
     */
    default void writeTo(OutputStream output) throws IOException {
        try (ProtobufWriter writer = new ProtobufWriter(output)) {
            writeTo(writer);
        }
    }

    /**
     * Returns this message as binary representation
     *
     * @return binary representation
     * @throws IOException in case of any write error
     */
    default byte[] toByteArray() throws IOException {
        byte[] data = new byte[protobufSize()];

        try (ProtobufWriter writer = new ProtobufWriter(data)) {
            writeTo(writer);
        }

        return data;
    }
}
