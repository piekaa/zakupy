package pl.piekoszek.backend.gs;

import pl.piekoszek.backend.http.server.EndpointsProvider;
import pl.piekoszek.backend.payu.PayuService;
import pl.piekoszek.mongo.Mongo;

public class ShopConfig {

    public static EndpointsProvider controller(Mongo mongo, PayuService payuService) {
        return new ShopController(payuService, new ProductRepository(mongo), new OrderRepository(mongo));
    }

}
