package com.piekoszek.app.collections;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class WholeFileReader {

    private FileInputStream fileInputStream;
    private ByteBuffer byteBuffer;

    public WholeFileReader(File file) throws FileNotFoundException {
        fileInputStream = new FileInputStream(file);
        byteBuffer = new ByteBuffer();
    }

    public byte[] read() throws IOException {
        for (; ; ) {
            int value = fileInputStream.read();
            if (value == -1) {
                break;
            }
            byteBuffer.add((byte) value);
        }
        return byteBuffer.getAllBytes();
    }
}
