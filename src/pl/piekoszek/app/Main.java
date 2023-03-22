package pl.piekoszek.app;

import pl.piekoszek.app.shopping.auth.ShoppingBasicAuthHandler;
import pl.piekoszek.app.shopping.cart.CartConfig;
import pl.piekoszek.app.shopping.category.CategoryConfig;
import pl.piekoszek.app.shopping.item.ItemConfig;
import pl.piekoszek.app.shopping.stats.PurchaseService;
import pl.piekoszek.backend.http.server.BasicAuthMessageHandler;
import pl.piekoszek.backend.http.server.HttpServer;
import pl.piekoszek.backend.tcp.client.TcpClient;
import pl.piekoszek.backend.tcp.server.TcpServer;
import pl.piekoszek.mongo.Mongo;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        HttpServer httpServer = new HttpServer("static");

        Mongo mongo = new Mongo(new TcpClient("localhost", 27017).connection(), "zakupy");
        var basicAuthHandler = new ShoppingBasicAuthHandler(mongo);

        httpServer.register(CategoryConfig.controller(mongo, basicAuthHandler).endpoints());
        httpServer.register(ItemConfig.controller(mongo, basicAuthHandler).endpoints());
        httpServer.register(CartConfig.controller(mongo, new PurchaseService(mongo), basicAuthHandler).endpoints());


        new TcpServer(httpServer, 2106);
    }

}
