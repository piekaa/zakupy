package com.piekoszek.app;

import com.piekoszek.app.server.http.HttpServer;
import com.piekoszek.app.server.tcp.TcpServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        new TcpServer(new HttpServer());
    }

}