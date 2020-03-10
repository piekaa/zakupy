package pl.piekoszek.backend;

import pl.piekoszek.backend.calculation.CalculatorConfig;
import pl.piekoszek.backend.server.http.EndpointInfo;
import pl.piekoszek.backend.server.http.HttpServer;
import pl.piekoszek.backend.server.tcp.TcpServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = new HttpServer("static");

        httpServer.register(CalculatorConfig.controller().endpoints());

        httpServer.register(new EndpointInfo("POST", "/priviet", ((requestInfo, body) -> "siemaneczko " + requestInfo.getRequest().bodyText()), Object.class));
        httpServer.register(new EndpointInfo("POST", "/priviet2", ((requestInfo, body) -> "siemaneczko " + body), String.class));

        new TcpServer(httpServer);
    }

}
