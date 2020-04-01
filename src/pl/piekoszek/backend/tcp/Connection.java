package pl.piekoszek.backend.tcp;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Connection {

    public InputStream inputStream;
    public OutputStream outputStream;
    private Closeable closeable;

    public Connection(InputStream inputStream, OutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    public Connection(InputStream inputStream, OutputStream outputStream, Closeable closeable) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.closeable = closeable;
    }

    public void close() {
        try {
            closeable.close();
        } catch (IOException e) {
            // I don't care, I love it
        }
    }
}
