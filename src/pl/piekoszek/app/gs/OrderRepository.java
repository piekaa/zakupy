package pl.piekoszek.app.gs;

import pl.piekoszek.mongo.Mongo;

public class OrderRepository {

    private Mongo mongo;

    OrderRepository(Mongo mongo) {
        this.mongo = mongo;
    }

    public OrderStatus get(String gsId) {
        return mongo.queryAll("{\"_id\":\"" + gsId + "\"}", "gs", "orders", OrderStatus.class).get(0);
    }

    public void insert(OrderStatus orderStatus) {
        mongo.insert("gs", "orders", orderStatus);
    }

    public void updateStatus(String id, String status) {
        mongo.update("gs", "orders",
                "{\"_id\":\"" + id + "\"}",
                "{ \"$set\": { \"status\" : \""+status+"\"}}");
    }
}
