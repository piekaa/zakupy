package pl.piekoszek.app;

import pl.piekoszek.app.shopping.cart.CartConfig;
import pl.piekoszek.app.shopping.category.CategoryConfig;
import pl.piekoszek.app.shopping.item.ItemConfig;
import pl.piekoszek.backend.http.server.HttpServer;
import pl.piekoszek.backend.tcp.client.TcpClient;
import pl.piekoszek.backend.tcp.server.TcpServer;
import pl.piekoszek.mongo.Mongo;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        HttpServer httpServer = new HttpServer("static");

        Mongo mongo = new Mongo(new TcpClient("localhost", 27017).connection(), "zakupy");
        httpServer.register(CategoryConfig.controller(mongo).endpoints());
        httpServer.register(ItemConfig.controller(mongo).endpoints());
        httpServer.register(CartConfig.controller(mongo).endpoints());


        new TcpServer(httpServer, 2106);
    }

}
