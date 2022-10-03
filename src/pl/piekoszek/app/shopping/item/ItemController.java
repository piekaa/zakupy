package pl.piekoszek.app.shopping.item;

import pl.piekoszek.backend.http.server.*;
import pl.piekoszek.mongo.Mongo;

import java.util.UUID;

class ItemController implements EndpointsProvider {

    private static final String COLLECTION = "item";

    private Mongo mongo;

    public ItemController(Mongo mongo) {
        this.mongo = mongo;
    }

    private MessageHandler<Object> getAll = (info, body)
            -> mongo.queryAll(COLLECTION, "{}", Item.class);

    private MessageHandler<Item> add = (info, body) -> {
        body._id = UUID.randomUUID().toString();
        mongo.insert(COLLECTION, body);
        return new ResponseInfo(body, ResponseStatus.CREATED);
    };

    private MessageHandler<Item> editCategories = (info, body) -> {
        var item = mongo.getById(body._id, COLLECTION, Item.class);
        item.categories = body.categories;
        mongo.update(COLLECTION, item);
        return body;
    };

    private MessageHandler<Object> delete = (info, body) -> {
        mongo.deleteById(COLLECTION, info.getPathParams().get("id"));
        return new ResponseInfo(ResponseStatus.OK);
    };

    @Override
    public EndpointInfo[] endpoints() {
        return new EndpointInfo[]{
                new EndpointInfo("GET", "/api/item", getAll, Object.class),
                new EndpointInfo("POST", "/api/item", add, Item.class),
                new EndpointInfo("PUT", "/api/item/category", editCategories, Item.class),
                new EndpointInfo("DELETE", "/api/item/:id", delete, Object.class)
        };
    }
}
