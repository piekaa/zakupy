package pl.piekoszek.collections;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class WholeFileReader {

    private FileInputStream fileInputStream;
    private ByteBuffer byteBuffer;

    public WholeFileReader(File file) {
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new IllegalStateException();
        }
        byteBuffer = new ByteBuffer();
    }

    public byte[] read() {
        for (; ; ) {
            int value = 0;
            try {
                value = fileInputStream.read();
            } catch (IOException e) {
                throw new FileReadException(e);
            }
            if (value == -1) {
                break;
            }
            byteBuffer.add((byte) value);
        }
        return byteBuffer.getAllBytes();
    }
}
