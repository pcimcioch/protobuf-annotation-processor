package com.github.pcimcioch.protobuf.io;

import com.github.pcimcioch.protobuf.dto.ByteArray;
import com.github.pcimcioch.protobuf.dto.ProtobufMessage;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import static com.github.pcimcioch.protobuf.dto.ProtoDto.isDefault;
import static com.github.pcimcioch.protobuf.io.WireType.I32;
import static com.github.pcimcioch.protobuf.io.WireType.I64;
import static com.github.pcimcioch.protobuf.io.WireType.LEN;
import static com.github.pcimcioch.protobuf.io.WireType.VARINT;

/**
 * Writes protobuf data
 */
public class ProtobufWriter {

    private final ProtobufOutput output;

    /**
     * Constructor. Given output stream will not be closed by this class in any way
     *
     * @param outputStream output stream to save data to
     */
    public ProtobufWriter(OutputStream outputStream) {
        this.output = new ProtobufOutput(outputStream);
    }

    /**
     * Writes double
     *
     * @param number field number
     * @param value  value to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter double_(int number, double value) throws IOException {
        if (!isDefault(value)) {
            output.writeVarint32(I64.tagFrom(number));
            output.writeDouble(value);
        }

        return this;
    }

    /**
     * Writes list of double
     *
     * @param number field number
     * @param values values to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter double_(int number, List<Double> values) throws IOException {
        if (!isDefault(values)) {
            for (double value : values) {
                output.writeVarint32(I64.tagFrom(number));
                output.writeDouble(value);
            }
        }

        return this;
    }

    /**
     * Writes float
     *
     * @param number field number
     * @param value  value to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter float_(int number, float value) throws IOException {
        if (!isDefault(value)) {
            output.writeVarint32(I32.tagFrom(number));
            output.writeFloat(value);
        }

        return this;
    }

    /**
     * Writes list of float
     *
     * @param number field number
     * @param values values to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter float_(int number, List<Float> values) throws IOException {
        if (!isDefault(values)) {
            for (float value : values) {
                output.writeVarint32(I32.tagFrom(number));
                output.writeFloat(value);
            }
        }

        return this;
    }

    /**
     * Writes int32
     *
     * @param number field number
     * @param value  value to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter int32(int number, int value) throws IOException {
        if (!isDefault(value)) {
            output.writeVarint32(VARINT.tagFrom(number));
            output.writeVarint32(value);
        }

        return this;
    }

    /**
     * Writes list of int32
     *
     * @param number field number
     * @param values values to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter int32(int number, List<Integer> values) throws IOException {
        if (!isDefault(values)) {
            for (int value : values) {
                output.writeVarint32(VARINT.tagFrom(number));
                output.writeVarint32(value);
            }
        }

        return this;
    }

    /**
     * Writes int64
     *
     * @param number field number
     * @param value  value to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter int64(int number, long value) throws IOException {
        if (!isDefault(value)) {
            output.writeVarint32(VARINT.tagFrom(number));
            output.writeVarint64(value);
        }

        return this;
    }

    /**
     * Writes list of int64
     *
     * @param number field number
     * @param values values to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter int64(int number, List<Long> values) throws IOException {
        if (!isDefault(values)) {
            for (long value : values) {
                output.writeVarint32(VARINT.tagFrom(number));
                output.writeVarint64(value);
            }
        }

        return this;
    }

    /**
     * Writes uint32
     *
     * @param number field number
     * @param value  value to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter uint32(int number, int value) throws IOException {
        if (!isDefault(value)) {
            output.writeVarint32(VARINT.tagFrom(number));
            output.writeVarint32(value);
        }

        return this;
    }

    /**
     * Writes list of uint32
     *
     * @param number field number
     * @param values values to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter uint32(int number, List<Integer> values) throws IOException {
        if (!isDefault(values)) {
            for (int value : values) {
                output.writeVarint32(VARINT.tagFrom(number));
                output.writeVarint32(value);
            }
        }

        return this;
    }

    /**
     * Writes uint64
     *
     * @param number field number
     * @param value  value to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter uint64(int number, long value) throws IOException {
        if (!isDefault(value)) {
            output.writeVarint32(VARINT.tagFrom(number));
            output.writeVarint64(value);
        }

        return this;
    }

    /**
     * Writes uint64
     *
     * @param number field number
     * @param values values to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter uint64(int number, List<Long> values) throws IOException {
        if (!isDefault(values)) {
            for (long value : values) {
                output.writeVarint32(VARINT.tagFrom(number));
                output.writeVarint64(value);
            }
        }

        return this;
    }

    /**
     * Writes sint32
     *
     * @param number field number
     * @param value  value to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter sint32(int number, int value) throws IOException {
        if (!isDefault(value)) {
            output.writeVarint32(VARINT.tagFrom(number));
            output.writeZigZag32(value);
        }

        return this;
    }

    /**
     * Writes list of sint32
     *
     * @param number field number
     * @param values values to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter sint32(int number, List<Integer> values) throws IOException {
        if (!isDefault(values)) {
            for (int value : values) {
                output.writeVarint32(VARINT.tagFrom(number));
                output.writeZigZag32(value);
            }
        }

        return this;
    }

    /**
     * Writes sint64
     *
     * @param number field number
     * @param value  value to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter sint64(int number, long value) throws IOException {
        if (!isDefault(value)) {
            output.writeVarint32(VARINT.tagFrom(number));
            output.writeZigZag64(value);
        }

        return this;
    }

    /**
     * Writes list of sint64
     *
     * @param number field number
     * @param values values to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter sint64(int number, List<Long> values) throws IOException {
        if (!isDefault(values)) {
            for (long value : values) {
                output.writeVarint32(VARINT.tagFrom(number));
                output.writeZigZag64(value);
            }
        }

        return this;
    }

    /**
     * Writes fixed32
     *
     * @param number field number
     * @param value  value to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter fixed32(int number, int value) throws IOException {
        if (!isDefault(value)) {
            output.writeVarint32(I32.tagFrom(number));
            output.writeFixedInt(value);
        }

        return this;
    }

    /**
     * Writes list of fixed32
     *
     * @param number field number
     * @param values values to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter fixed32(int number, List<Integer> values) throws IOException {
        if (!isDefault(values)) {
            for (int value : values) {
                output.writeVarint32(I32.tagFrom(number));
                output.writeFixedInt(value);
            }
        }

        return this;
    }

    /**
     * Writes fixed64
     *
     * @param number field number
     * @param value  value to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter fixed64(int number, long value) throws IOException {
        if (!isDefault(value)) {
            output.writeVarint32(I64.tagFrom(number));
            output.writeFixedLong(value);
        }

        return this;
    }

    /**
     * Writes list of fixed64
     *
     * @param number field number
     * @param values values to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter fixed64(int number, List<Long> values) throws IOException {
        if (!isDefault(values)) {
            for (long value : values) {
                output.writeVarint32(I64.tagFrom(number));
                output.writeFixedLong(value);
            }
        }

        return this;
    }

    /**
     * Writes sfixed32
     *
     * @param number field number
     * @param value  value to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter sfixed32(int number, int value) throws IOException {
        if (!isDefault(value)) {
            output.writeVarint32(I32.tagFrom(number));
            output.writeFixedInt(value);
        }

        return this;
    }

    /**
     * Writes list of sfixed32
     *
     * @param number field number
     * @param values values to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter sfixed32(int number, List<Integer> values) throws IOException {
        if (!isDefault(values)) {
            for (int value : values) {
                output.writeVarint32(I32.tagFrom(number));
                output.writeFixedInt(value);
            }
        }

        return this;
    }

    /**
     * Writes sfixed64
     *
     * @param number field number
     * @param value  value to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter sfixed64(int number, long value) throws IOException {
        if (!isDefault(value)) {
            output.writeVarint32(I64.tagFrom(number));
            output.writeFixedLong(value);
        }

        return this;
    }

    /**
     * Writes list of sfixed64
     *
     * @param number field number
     * @param values values to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter sfixed64(int number, List<Long> values) throws IOException {
        if (!isDefault(values)) {
            for (long value : values) {
                output.writeVarint32(I64.tagFrom(number));
                output.writeFixedLong(value);
            }
        }

        return this;
    }

    /**
     * Writes bool
     *
     * @param number field number
     * @param value  value to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter bool(int number, boolean value) throws IOException {
        if (!isDefault(value)) {
            output.writeVarint32(VARINT.tagFrom(number));
            output.writeBoolean(true);
        }

        return this;
    }

    /**
     * Writes list of bool
     *
     * @param number field number
     * @param values values to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter bool(int number, List<Boolean> values) throws IOException {
        if (!isDefault(values)) {
            for (boolean value : values) {
                output.writeVarint32(VARINT.tagFrom(number));
                output.writeBoolean(value);
            }
        }

        return this;
    }

    /**
     * Writes string
     *
     * @param number field number
     * @param value  value to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter string(int number, String value) throws IOException {
        if (!isDefault(value)) {
            output.writeVarint32(LEN.tagFrom(number));
            output.writeString(value);
        }

        return this;
    }

    /**
     * Writes list of string
     *
     * @param number field number
     * @param values values to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter string(int number, List<String> values) throws IOException {
        if (!isDefault(values)) {
            for (String value : values) {
                output.writeVarint32(LEN.tagFrom(number));
                output.writeString(value);
            }
        }

        return this;
    }

    /**
     * Writes bytes
     *
     * @param number field number
     * @param value  value to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter bytes(int number, ByteArray value) throws IOException {
        if (!isDefault(value)) {
            output.writeVarint32(LEN.tagFrom(number));
            output.writeByteArray(value);
        }

        return this;
    }

    /**
     * Writes list of bytes
     *
     * @param number field number
     * @param values values to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter bytes(int number, List<ByteArray> values) throws IOException {
        if (!isDefault(values)) {
            for (ByteArray value : values) {
                output.writeVarint32(LEN.tagFrom(number));
                output.writeByteArray(value);
            }
        }

        return this;
    }

    /**
     * Write message
     *
     * @param number field number
     * @param value  message to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter message(int number, ProtobufMessage<?> value) throws IOException {
        if (!isDefault(value)) {
            output.writeVarint32(LEN.tagFrom(number));
            output.writeBytes(value.toByteArray());
        }

        return this;
    }

    /**
     * Write list of messages
     *
     * @param number field number
     * @param values messages to write
     * @return this
     * @throws IOException in case of any data write error
     */
    public ProtobufWriter message(int number, List<? extends ProtobufMessage<?>> values) throws IOException {
        if (!isDefault(values)) {
            for (ProtobufMessage<?> value : values) {
                output.writeVarint32(LEN.tagFrom(number));
                output.writeBytes(value.toByteArray());
            }
        }

        return this;
    }
}
