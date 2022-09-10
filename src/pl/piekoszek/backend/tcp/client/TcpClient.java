package pl.piekoszek.backend.tcp.client;

import pl.piekoszek.backend.tcp.Connection;

import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.security.Security;

public class TcpClient {

    private Socket socket;

    public TcpClient(String host, int port) throws IOException {
        if (host.startsWith("https://")) {
            URL url = new URL(host);
            SSLSocketFactory factory =
                    (SSLSocketFactory) SSLSocketFactory.getDefault();
            socket = factory.createSocket(url.getHost(), port);

        } else {
            socket = new Socket(host, port);
        }
    }

    public Connection connection() throws IOException {
        return new Connection(socket.getInputStream(), socket.getOutputStream());
    }
}
