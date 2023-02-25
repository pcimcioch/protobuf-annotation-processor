package com.github.pcimcioch.protobuf.io;

import com.github.pcimcioch.protobuf.io.exception.MalformedVarintException;

import java.io.IOException;

import static java.lang.Double.longBitsToDouble;
import static java.lang.Float.intBitsToFloat;

// TODO [performance] With project Panama there may be some more efficient ways to read little endians directly from the buffer's byte[]
// TODO [performance] Reading varints can be faster if 9 bytes (max varint) are already in the buffer. No loops are needed and no calls to ensureAvailable()
@SuppressWarnings("PointlessBitwiseExpression")
class ProtoInput {
    private final ReadBuffer in;

    ProtoInput(ReadBuffer buffer) {
        this.in = buffer;
    }

    int readFixedInt() throws IOException {
        in.ensureAvailable(4);

        byte ch4 = in.read();
        byte ch3 = in.read();
        byte ch2 = in.read();
        byte ch1 = in.read();

        return (((ch1 & 0xff) << 24) |
                ((ch2 & 0xff) << 16) |
                ((ch3 & 0xff) << 8) |
                ((ch4 & 0xff) << 0));
    }

    long readFixedLong() throws IOException {
        in.ensureAvailable(8);

        byte ch8 = in.read();
        byte ch7 = in.read();
        byte ch6 = in.read();
        byte ch5 = in.read();
        byte ch4 = in.read();
        byte ch3 = in.read();
        byte ch2 = in.read();
        byte ch1 = in.read();

        return (((ch1 & 0xffL) << 56) |
                ((ch2 & 0xffL) << 48) |
                ((ch3 & 0xffL) << 40) |
                ((ch4 & 0xffL) << 32) |
                ((ch5 & 0xffL) << 24) |
                ((ch6 & 0xffL) << 16) |
                ((ch7 & 0xffL) << 8) |
                ((ch8 & 0xffL) << 0));
    }

    double readDouble() throws IOException {
        return longBitsToDouble(readFixedLong());
    }

    float readFloat() throws IOException {
        return intBitsToFloat(readFixedInt());
    }

    boolean readBoolean() throws IOException {
        return readVarint32() != 0;
    }

    int readVarint32() throws IOException {
        return (int) readVarint64();
    }

    long readVarint64() throws IOException {
        long result = 0L;

        for (int shift = 0; shift < 64; shift += 7) {
            in.ensureAvailable(1);
            byte b = in.read();

            result |= (b & 0b01111111L) << shift;

            if ((b & 0b10000000) == 0) {
                return result;
            }
        }

        throw new MalformedVarintException();
    }

    int readZigZag32() throws IOException {
        int encoded = readVarint32();
        return (encoded >>> 1) ^ -(encoded & 1);
    }

    long readZigZag64() throws IOException {
        long encoded = readVarint64();
        return (encoded >>> 1) ^ -(encoded & 1);
    }

    String readString() throws IOException {
        int size = readVarint32();
        return in.readString(size);
    }

    byte[] readBytes() throws IOException {
        int size = readVarint32();
        return in.read(size);
    }

    void skip(int size) throws IOException {
        in.skip(size);
    }
}
