package pl.piekoszek.collections;


public class ByteBuffer {

    private byte[] bytes;
    private int nextPos;

    public ByteBuffer() {
        bytes = new byte[30];
    }

    public ByteBuffer add(byte b) {
        if (nextPos == bytes.length) {
            realloc();
        }
        bytes[nextPos++] = b;
        return this;
    }

    public ByteBuffer add(byte[] newBytes) {
        for (int i = 0; i < newBytes.length; i++) {
            add(newBytes[i]);
        }
        return this;
    }

    public ByteBuffer addLittleEndian(int value) {
        return add(new byte[]{
                (byte) (value & 0xff),
                (byte) ((value >> 8) & 0xff),
                (byte) ((value >> 16) & 0xff),
                (byte) ((value >> 24) & 0xff),
        });
    }

    public ByteBuffer addLittleEndian(long value) {
        return add(new byte[]{
                (byte) (value & 0xff),
                (byte) ((value >> 8) & 0xff),
                (byte) ((value >> 16) & 0xff),
                (byte) ((value >> 24) & 0xff),
                (byte) ((value >> 32) & 0xff),
                (byte) ((value >> 40) & 0xff),
                (byte) ((value >> 48) & 0xff),
                (byte) ((value >> 56) & 0xff),
        });
    }
    public ByteBuffer addLittleEndian(double value) {
        long l = Double.doubleToRawLongBits(value);
        return addLittleEndian(l);
    }


    public ByteBuffer addCString(String string) {
        return add(string.getBytes()).add((byte) 0);
    }

    public ByteBuffer add(String string) {
        byte[] strBytes = string.getBytes();
        return addLittleEndian(strBytes.length+1).add(string.getBytes()).add((byte) 0);
    }

    public boolean endsWith1310() {
        if (nextPos < 2) {
            return false;
        }
        return bytes[nextPos - 2] == 13 && bytes[nextPos - 1] == 10;
    }

    public boolean endsWith13101310() {
        if (nextPos < 4) {
            return false;
        }
        return bytes[nextPos - 4] == 13 && bytes[nextPos - 3] == 10 && bytes[nextPos - 2] == 13 && bytes[nextPos - 1] == 10;
    }

    public byte[] getAllBytes() {
        byte[] result = new byte[nextPos];
        System.arraycopy(bytes, 0, result, 0, nextPos);
        return result;
    }

    private void realloc() {
        byte[] newBytes = new byte[bytes.length * 2];
        System.arraycopy(bytes, 0, newBytes, 0, bytes.length);
        bytes = newBytes;
    }
}
