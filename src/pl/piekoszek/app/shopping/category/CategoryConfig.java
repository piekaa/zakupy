package pl.piekoszek.app.shopping.category;

import pl.piekoszek.backend.http.server.BasicAuthMessageHandler;
import pl.piekoszek.backend.http.server.EndpointsProvider;
import pl.piekoszek.mongo.Mongo;

public class CategoryConfig {

    public static EndpointsProvider controller(Mongo mongo, BasicAuthMessageHandler basicAuthMessageHandler) {
        return new CategoryController(mongo, basicAuthMessageHandler);
    }

}
