package pl.piekoszek.app.shopping.category;

import pl.piekoszek.backend.http.server.EndpointsProvider;
import pl.piekoszek.mongo.Mongo;

public class CategoryConfig {

    public static EndpointsProvider controller(Mongo mongo) {
        return new CategoryController(mongo);
    }

}
