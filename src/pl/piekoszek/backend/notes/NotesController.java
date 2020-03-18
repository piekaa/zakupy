package pl.piekoszek.backend.notes;

import pl.piekoszek.backend.server.http.*;
import pl.piekoszek.mongo.Mongo;

import java.util.UUID;

class NotesController implements EndpointsProvider {

    private Mongo mongo;

    private MessageHandler<Note> insert = (info, body) -> {
        body._id = UUID.randomUUID().toString();
        mongo.insert("piekoszek", "notes", body);
        return new ResponseInfo(body, ResponseStatus.CREATED);
    };

    private MessageHandler<Object> getAll = (info, body) ->
            mongo.queryAll("{}", "piekoszek", "notes", Note.class);

    private MessageHandler<Object> getByType = (info, body) ->
            mongo.queryAll("{\"type\": \"" + info.getPathParams().get("type") + "\"}", "piekoszek", "notes", Note.class);

    NotesController(Mongo mongo) {
        this.mongo = mongo;
    }

    @Override
    public EndpointInfo[] endpoints() {
        return new EndpointInfo[]{
                new EndpointInfo("POST", "/note", insert, Note.class),
                new EndpointInfo("GET", "/note", getAll, Object.class),
                new EndpointInfo("GET", "/note/:type", getByType, Object.class),
        };
    }
}
