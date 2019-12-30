package com.piekoszek.app.collections;

public class ByteBuffer {

    private byte[] bytes;
    private int nextPos;

    public ByteBuffer() {
        bytes = new byte[30];
    }

    public void add(byte b) {
        if (nextPos == bytes.length) {
            realloc();
        }
        bytes[nextPos++] = b;
    }

    public void add(byte[] newBytes) {
        for (int i = 0; i < newBytes.length; i++) {
            add(newBytes[i]);
        }
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
