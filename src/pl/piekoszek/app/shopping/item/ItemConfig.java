package pl.piekoszek.app.shopping.item;

import pl.piekoszek.backend.http.server.BasicAuthMessageHandler;
import pl.piekoszek.backend.http.server.EndpointsProvider;
import pl.piekoszek.mongo.Mongo;

import java.util.prefs.BackingStoreException;

public class ItemConfig {

    public static EndpointsProvider controller(Mongo mongo, BasicAuthMessageHandler basicAuthMessageHandler) {
        return new ItemController(mongo, basicAuthMessageHandler);
    }

}
