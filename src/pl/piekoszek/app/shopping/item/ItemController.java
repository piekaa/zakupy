package pl.piekoszek.app.shopping.item;

import pl.piekoszek.app.shopping.auth.CollectionUtil;
import pl.piekoszek.backend.http.server.*;
import pl.piekoszek.mongo.Mongo;

import java.util.UUID;

class ItemController implements EndpointsProvider {

    private static final String COLLECTION = "item";

    private Mongo mongo;
    private BasicAuthMessageHandler basicAuthMessageHandler;

    public ItemController(Mongo mongo, BasicAuthMessageHandler basicAuthMessageHandler) {
        this.mongo = mongo;
        this.basicAuthMessageHandler = basicAuthMessageHandler;
    }

    private MessageHandler<Object> getAll = (info, body)
            -> mongo.queryAll(CollectionUtil.collectionByUser(COLLECTION, info), "{}", Item.class);

    private MessageHandler<Item> add = (info, body) -> {
        body._id = UUID.randomUUID().toString();
        mongo.insert(CollectionUtil.collectionByUser(COLLECTION, info), body);
        return new ResponseInfo(body, ResponseStatus.CREATED);
    };

    private MessageHandler<Item> editCategories = (info, body) -> {
        var item = mongo.getById(body._id, CollectionUtil.collectionByUser(COLLECTION, info), Item.class);
        item.categories = body.categories;
        mongo.update(CollectionUtil.collectionByUser(COLLECTION, info), item);
        return body;
    };

    private MessageHandler<Object> delete = (info, body) -> {
        mongo.deleteById(CollectionUtil.collectionByUser(COLLECTION, info), info.getPathParams().get("id"));
        return new ResponseInfo(ResponseStatus.OK);
    };

    @Override
    public EndpointInfo[] endpoints() {
        return new EndpointInfo[]{
                new EndpointInfo("GET", "/api/item", getAll, basicAuthMessageHandler, Object.class),
                new EndpointInfo("POST", "/api/item", add, basicAuthMessageHandler, Item.class),
                new EndpointInfo("PUT", "/api/item/category", editCategories, basicAuthMessageHandler, Item.class),
                new EndpointInfo("DELETE", "/api/item/:id", delete, basicAuthMessageHandler, Object.class)
        };
    }
}
