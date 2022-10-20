package pl.piekoszek.app.shopping.category;

import pl.piekoszek.app.shopping.auth.CollectionUtil;
import pl.piekoszek.backend.http.server.*;
import pl.piekoszek.mongo.Mongo;

import java.util.UUID;

class CategoryController implements EndpointsProvider {

    static final String COLLECTION = "category";

    private Mongo mongo;
    private BasicAuthMessageHandler basicAuthMessageHandler;

    CategoryController(Mongo mongo, BasicAuthMessageHandler basicAuthMessageHandler) {
        this.mongo = mongo;
        this.basicAuthMessageHandler = basicAuthMessageHandler;
    }

    private MessageHandler<Object> getAll = (info, body)
            -> mongo.queryAll(CollectionUtil.collectionByUser(COLLECTION, info), "{}", Category.class);

    private MessageHandler<Category> add = (info, body) -> {
        body._id = UUID.randomUUID().toString();
        mongo.insert(CollectionUtil.collectionByUser(COLLECTION, info), body);
        return new ResponseInfo(body, ResponseStatus.CREATED);
    };

    private MessageHandler<Object> delete = (info, body) -> {
        mongo.deleteById(CollectionUtil.collectionByUser(COLLECTION, info), info.getPathParams().get("id"));
        return new ResponseInfo(ResponseStatus.OK);
    };

    public EndpointInfo[] endpoints() {
        return new EndpointInfo[]{
                new EndpointInfo("GET", "/api/category", getAll, basicAuthMessageHandler, Object.class),
                new EndpointInfo("POST", "/api/category", add, basicAuthMessageHandler, Category.class),
                new EndpointInfo("DELETE", "/api/category/:id", delete, basicAuthMessageHandler, Object.class)
        };
    }
}
