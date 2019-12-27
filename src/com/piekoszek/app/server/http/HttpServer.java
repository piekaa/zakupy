package com.piekoszek.app.server.http;

import com.piekoszek.app.server.Connection;
import com.piekoszek.app.server.ConnectionHandler;

import java.io.IOException;

public class HttpServer implements ConnectionHandler {

    @Override
    public void handle(Connection connection) {
        for (; ; ) {
            Request request = RequestReader.read(connection.inputStream);
            if (request == null) {
                break;
            }
            try {
                ResponseWriter.write(connection.outputStream, new Response(200));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
