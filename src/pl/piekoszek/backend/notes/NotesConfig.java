package pl.piekoszek.backend.notes;

import pl.piekoszek.backend.http.server.EndpointsProvider;
import pl.piekoszek.mongo.Mongo;

public class NotesConfig {

    public static EndpointsProvider controller(Mongo mongo) {
        return new NotesController(mongo);
    }
}
