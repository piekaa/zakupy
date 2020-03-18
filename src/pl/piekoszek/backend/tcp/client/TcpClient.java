package pl.piekoszek.backend.tcp.client;

import pl.piekoszek.backend.tcp.Connection;

import java.io.IOException;
import java.net.Socket;

public class TcpClient {

    private Socket socket;

    public TcpClient(String host, int port) throws IOException {
        socket = new Socket(host, port);
    }

    public Connection connection() throws IOException {
        return new Connection(socket.getInputStream(), socket.getOutputStream());
    }
}
