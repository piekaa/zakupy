package pl.piekoszek.app.shopping.category;

import pl.piekoszek.backend.http.server.*;
import pl.piekoszek.mongo.Mongo;

import java.util.UUID;

import static pl.piekoszek.app.shopping.category.CategoryService.COLLECTION;

class CategoryController implements EndpointsProvider {



    private Mongo mongo;

    CategoryController(Mongo mongo) {
        this.mongo = mongo;
    }

    private MessageHandler<Object> getAll = (info, body)
            -> mongo.queryAll(COLLECTION, "{}", Category.class);

    private MessageHandler<Category> add = (info, body) -> {
        body._id = UUID.randomUUID().toString();
        mongo.insert(COLLECTION, body);
        return new ResponseInfo(body, ResponseStatus.CREATED);
    };

    private MessageHandler<Object> delete = (info, body) -> {
        //todo fire event and remove category from item
        mongo.deleteById(COLLECTION, info.getPathParams().get("id"));
        return new ResponseInfo(ResponseStatus.OK);
    };

    @Override
    public EndpointInfo[] endpoints() {
        return new EndpointInfo[]{
                new EndpointInfo("GET", "/api/category", getAll, Object.class),
                new EndpointInfo("POST", "/api/category", add, Category.class),
                new EndpointInfo("DELETE", "/api/category/:id", delete, Object.class)
        };
    }
}
