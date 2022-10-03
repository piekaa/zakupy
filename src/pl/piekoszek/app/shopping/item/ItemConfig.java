package pl.piekoszek.app.shopping.item;

import pl.piekoszek.backend.http.server.EndpointsProvider;
import pl.piekoszek.mongo.Mongo;

public class ItemConfig {

    public static EndpointsProvider controller(Mongo mongo) {
        return new ItemController(mongo);
    }

}
