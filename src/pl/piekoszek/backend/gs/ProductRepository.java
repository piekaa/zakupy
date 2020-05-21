package pl.piekoszek.backend.gs;

import pl.piekoszek.mongo.Mongo;

import java.util.List;
import java.util.Optional;

class ProductRepository {

    private Mongo mongo;

    ProductRepository(Mongo mongo) {
        this.mongo = mongo;
    }

    List<Product> getAll() {
        return mongo.queryAll("{}", "gs", "products", Product.class);
    }

    Optional<Product> getOne(String id) {

        String query = "{\"_id\":  \"" + id + "\"}";
        List<Product> products = mongo.queryAll(query, "gs", "products", Product.class);
        if (products.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(products.get(0));
        }
    }

    void insert(Product product) {
        mongo.insert("gs", "products", product);
    }
}
