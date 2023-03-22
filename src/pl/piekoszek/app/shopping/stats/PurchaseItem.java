package pl.piekoszek.app.shopping.stats;

import pl.piekoszek.app.shopping.item.Item;

import java.util.List;

public class PurchaseItem {
    public String name;
    public List<String> categories;

    public PurchaseItem(Item item) {
        this.name = item.name;
        this.categories = item.categories.stream().map(c -> c.name).toList();
    }

    public PurchaseItem() {

    }

}
