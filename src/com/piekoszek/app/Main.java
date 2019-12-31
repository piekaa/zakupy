package com.piekoszek.app;

import com.piekoszek.app.server.http.HttpServer;
import com.piekoszek.app.server.http.Response;
import com.piekoszek.app.server.tcp.TcpServer;

import java.io.IOException;

import static com.piekoszek.app.server.http.ResponseStatus.OK;

public class Main {

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = new HttpServer("static");
        httpServer.registerPost("/test", r -> {
            System.out.println(r.bodyText());
            return new Response(OK, "Post response!");
        });
        new TcpServer(httpServer);
    }

}
