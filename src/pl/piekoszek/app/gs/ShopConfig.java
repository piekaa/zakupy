package pl.piekoszek.app.gs;

import pl.piekoszek.backend.http.server.EndpointsProvider;
import pl.piekoszek.app.payu.PayuService;
import pl.piekoszek.mongo.Mongo;

public class ShopConfig {

    public static EndpointsProvider controller(Mongo mongo, PayuService payuService) {
        return new ShopController(payuService, new ProductRepository(mongo), new OrderRepository(mongo));
    }

}
