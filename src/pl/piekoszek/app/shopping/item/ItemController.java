package pl.piekoszek.app.shopping.item;

import pl.piekoszek.app.shopping.auth.CollectionUtil;
import pl.piekoszek.app.shopping.category.Category;
import pl.piekoszek.backend.http.server.*;
import pl.piekoszek.mongo.Mongo;

import java.util.List;
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

    private MessageHandler<AddItemsRequest> add = (info, itemsToAdd) -> {

        if(itemsToAdd.delimiter.isBlank()) {
            addItem(itemsToAdd.text, itemsToAdd.defaultCategory, info);
        } else {
            for (String itemName : itemsToAdd.text.split(" " + itemsToAdd.delimiter + " ")) {
                addItem(itemName, itemsToAdd.defaultCategory, info);
            }
        }
        return new ResponseInfo(itemsToAdd, ResponseStatus.CREATED);
    };

    private void addItem(String name, Category defaultCategory, RequestInfo userInfo) {
        var item = new Item(name.trim());
        item._id = UUID.randomUUID().toString();
        if( defaultCategory != null) {
            item.categories = List.of(defaultCategory);
        }
        mongo.insert(CollectionUtil.collectionByUser(COLLECTION, userInfo), item);
    }

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
                new EndpointInfo("POST", "/api/item", add, basicAuthMessageHandler, AddItemsRequest.class),
                new EndpointInfo("PUT", "/api/item/category", editCategories, basicAuthMessageHandler, Item.class),
                new EndpointInfo("DELETE", "/api/item/:id", delete, basicAuthMessageHandler, Object.class)
        };
    }
}
