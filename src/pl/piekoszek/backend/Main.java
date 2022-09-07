package pl.piekoszek.backend;

import pl.piekoszek.app.calculation.CalculatorConfig;
import pl.piekoszek.backend.http.server.HttpServer;
import pl.piekoszek.backend.tcp.server.TcpServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = new HttpServer("static");

//        Mongo mongo = new Mongo(new TcpClient("localhost", 27017).connection());

        httpServer.register(CalculatorConfig.controller().endpoints());
//        httpServer.register(NotesConfig.controller(mongo).endpoints());
//        httpServer.register(EchoConfig.controller().endpoints());

//        PayuService payuService = PayuConfig.payuService();

//        httpServer.register(ShopConfig.controller(mongo, payuService).endpoints());

//        httpServer.register(PayuConfig.controller().endpoints());

//        httpServer.register(new EndpointInfo("POST", "/priviet", ((requestInfo, body) -> "siemaneczko " + requestInfo.getRequest().bodyText()), Object.class));
//        httpServer.register(new EndpointInfo("POST", "/priviet2", ((requestInfo, body) -> "siemaneczko " + body), String.class));

//        httpServer.register(new EndpointInfo("GET", "/path/:variables/test/:Eine/kleine/:Nachtmusik", (info, body) ->
//                info.getPathParams().get("variables") + " - " + info.getPathParams().get("Eine") + " - " + info.getPathParams().get("Nachtmusik"), Object.class));

        new TcpServer(httpServer, 2106);
    }

}
