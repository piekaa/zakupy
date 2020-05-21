package pl.piekoszek.backend.gs;

public class Product {

    public Product() {
    }

    public Product(String _id, String imagePath, String name, int price) {
        this._id = _id;
        this.imagePath = imagePath;
        this.name = name;
        this.price = price;
    }

    public String _id;
    public String imagePath;
    public String name;
    public int price;
}
