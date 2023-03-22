package pl.piekoszek.app.shopping.cart;

import pl.piekoszek.app.shopping.stats.PurchaseService;
import pl.piekoszek.backend.http.server.BasicAuthMessageHandler;
import pl.piekoszek.backend.http.server.EndpointsProvider;
import pl.piekoszek.mongo.Mongo;

public class CartConfig {

    public static EndpointsProvider controller(Mongo mongo, PurchaseService purchaseService, BasicAuthMessageHandler basicAuthMessageHandler) {
        return new CartController(mongo, purchaseService, basicAuthMessageHandler);
    }
}
