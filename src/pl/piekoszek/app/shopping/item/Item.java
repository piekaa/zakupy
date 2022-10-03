package pl.piekoszek.app.shopping.item;

import pl.piekoszek.app.shopping.category.Category;

import java.util.ArrayList;
import java.util.List;

public class Item {
    public String _id;
    public String name;
    public boolean inCart;
    public List<Category> categories = new ArrayList<>();

    public Item(String name) {
        this.name = name;
    }

    public Item() {
    }
}
