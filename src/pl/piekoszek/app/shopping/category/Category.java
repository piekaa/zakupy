package pl.piekoszek.app.shopping.category;

//todo remove public
public class Category {
    public String _id;
    public String name;
    public String color;

    public Category(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Category() {
    }
}

