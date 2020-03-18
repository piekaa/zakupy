package pl.piekoszek.backend.tcp;

import java.io.InputStream;
import java.io.OutputStream;

public class Connection {

    public InputStream inputStream;
    public OutputStream outputStream;

    public Connection(InputStream inputStream, OutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }
}
