package pl.piekoszek.backend.tcp.server;

import pl.piekoszek.backend.tcp.Connection;
import pl.piekoszek.backend.tcp.ConnectionHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer {

    ServerSocket serverSocket;
    ConnectionHandler connectionHandler;

    public TcpServer(ConnectionHandler connectionHandler, int port) throws IOException {
        this.connectionHandler = connectionHandler;
        serverSocket = new ServerSocket(port);
        System.out.println("Server is listening on port: " + port);
        for (; ; ) {
            Socket socket = serverSocket.accept();
            Connection connection = new Connection(socket.getInputStream(), socket.getOutputStream());
            new Thread(() -> {
                connectionHandler.handle(connection);
                try {
                    System.out.println("Connection closed");
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
