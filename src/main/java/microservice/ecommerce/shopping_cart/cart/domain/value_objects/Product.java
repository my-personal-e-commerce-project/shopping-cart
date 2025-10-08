package microservice.ecommerce.shopping_cart.cart.domain.value_objects;

public class Product {

    private String id;
    private String slug;
    private String name;
    private String image;
    private Quantity quantity;
    private Price price;

    public Product(
        String id,
        String slug,
        String name,
        String image,
        Quantity quantity,
        Price price
    ) {
        this.id = id;
        this.slug = slug;
        this.name = name;
        this.image = image;
        this.quantity = quantity;
        this.price = price;
    }

    public String id() { return id; }
    public String slug() { return slug; }
    public String name() { return name; }
    public String image() { return image; }
    public int quantity() { return quantity.getValue(); }
    public Double price() { return price.getValue(); }
}
