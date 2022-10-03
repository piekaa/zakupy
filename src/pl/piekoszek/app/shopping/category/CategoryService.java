package pl.piekoszek.app.shopping.category;

import pl.piekoszek.mongo.Mongo;

public class CategoryService {

    static final String COLLECTION = "category";

    private Mongo mongo;

    public CategoryService(Mongo mongo) {
        this.mongo = mongo;
    }

    public Category getById(String id) {
        return mongo.getById(id, COLLECTION, Category.class);
    }
}
