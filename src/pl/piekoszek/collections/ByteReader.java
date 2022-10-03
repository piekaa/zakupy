package pl.piekoszek.collections;


/**
 * Little endian
 */
public class ByteReader {

    private byte[] bytes;
    private int cursor;

    public ByteReader(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte readByte() {
        return bytes[cursor++];
    }

    public int readInt() {
        int result;
        result = bytes[cursor + 3] & 0xff;
        result <<= 8;
        result |= bytes[cursor + 2] & 0xff;
        result <<= 8;
        result |= bytes[cursor + 1] & 0xff;
        result <<= 8;
        result |= bytes[cursor] & 0xff;
        cursor += 4;
        return result;
    }

    public long readLong() {
        long result;
        result = bytes[cursor + 7] & 0xff;
        result <<= 8;
        result |= bytes[cursor + 6] & 0xff;
        result <<= 8;
        result |= bytes[cursor + 5] & 0xff;
        result <<= 8;
        result |= bytes[cursor + 4] & 0xff;
        result <<= 8;
        result |= bytes[cursor + 3] & 0xff;
        result <<= 8;
        result |= bytes[cursor + 2] & 0xff;
        result <<= 8;
        result |= bytes[cursor + 1] & 0xff;
        result <<= 8;
        result |= bytes[cursor] & 0xff;
        cursor += 8;
        return result;
    }

    public double readDouble() {
        return Double.longBitsToDouble(readLong());
    }

    public boolean readBoolean() {
        return readByte() == 1;
    }

    public String readCString() {
        ByteBuffer byteBuffer = new ByteBuffer();
        for (; ; ) {
            byte b = bytes[cursor++];
            if (b == 0) {
                break;
            }
            byteBuffer.add(b);
        }
        return new String(byteBuffer.getAllBytes());
    }

    public String readString() {
        ByteBuffer byteBuffer = new ByteBuffer();
        int size = readInt();
        for (int i = 0; i < size - 1; i++) {
            byte b = bytes[cursor++];
            byteBuffer.add(b);
        }
        cursor++;
        return new String(byteBuffer.getAllBytes());
    }

    public boolean end() {
        return cursor == bytes.length;
    }
}
