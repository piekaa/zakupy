package pl.piekoszek.app.shopping.stats;

import pl.piekoszek.app.shopping.item.Item;

import java.util.List;

public class Purchase {
    public List<PurchaseItem> items;
    public int price;

    public Purchase(List<Item> items, int price) {
        this.items = items.stream().map(PurchaseItem::new).toList();
        this.price = price;
    }

    public Purchase() {
    }
}