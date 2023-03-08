package com.github.pcimcioch.protobuf.dto;

import com.github.pcimcioch.protobuf.io.ProtobufWriter;
import com.github.pcimcioch.protobuf.io.Size;

import java.io.ByteArrayOutputStream;
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
     * @param size size computer
     * @return message size
     */
    int protobufSize(Size size);

    /**
     * Returns message size in protobuf format. This method will always recompute whole message and all its nested messages sizes.
     * This may be very inefficient in case of big and nested messages. For better performance use {@link #protobufSize(Size)}
     * passing {@link Size#cached()} implementation
     *
     * @return message size
     */
    default int protobufSize() {
        return protobufSize(Size.inPlace());
    }

    /**
     * Writes this message as binary to the given output stream
     *
     * @param output output stream
     * @throws IOException in case of any write error
     */
    default void writeTo(OutputStream output) throws IOException {
        try (ProtobufWriter writer = new ProtobufWriter(output, Size.cached())) {
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
        Size size = Size.cached();
        byte[] data = new byte[protobufSize(size)];

        try (ProtobufWriter writer = new ProtobufWriter(data, size)) {
            writeTo(writer);
        }

        return data;
    }
}
