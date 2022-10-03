package pl.piekoszek.app.shopping.cart;

import pl.piekoszek.backend.http.server.EndpointsProvider;
import pl.piekoszek.mongo.Mongo;

public class CartConfig {

    public static EndpointsProvider controller(Mongo mongo) {
        return new CartController(mongo);
    }
}
