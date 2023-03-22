package pl.piekoszek.app.shopping.stats;

import pl.piekoszek.app.shopping.item.Item;
import pl.piekoszek.mongo.Mongo;

import java.util.List;

public class PurchaseService {

    private final Mongo mongo;

    public PurchaseService(Mongo mongo) {
        this.mongo = mongo;
    }

    public void savePurchase(List<Item> items, int price) {
        mongo.insert("purchase", new Purchase(items, price));
    }
}
