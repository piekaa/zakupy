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
        byte[] buffer = new byte[10000];
        for (; ; ) {
            int value;
            try {
                value = fileInputStream.read(buffer);
            } catch (IOException e) {
                throw new FileReadException(e);
            }
            if (value == -1) {
                break;
            }

            byteBuffer.add(buffer, value);
        }
        return byteBuffer.getAllBytes();
    }
}
