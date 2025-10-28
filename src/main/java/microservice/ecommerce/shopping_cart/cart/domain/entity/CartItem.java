package microservice.ecommerce.shopping_cart.cart.domain.entity;

import microservice.ecommerce.shopping_cart.cart.domain.value_objects.Price;
import microservice.ecommerce.shopping_cart.cart.domain.value_objects.Quantity;

public class CartItem {
    
    private String id;
    private Quantity quantity;
    private Price price;
    private String product_id;
    private boolean in_stock = false;

    public CartItem(
        String id,
        Quantity quantity,
        Price price,
        String product_id
    ) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
        this.product_id = product_id;
    }

    public String id() { return id; }
    public Quantity quantity() { return quantity; }
    public Price price() { return price; }
    public String product_id() { return product_id; }
    public boolean in_stock() { return in_stock; }

    public void calculateValidQuantity(Product product) {
        this.in_stock = product.quantity().value() >= quantity.value();
    }

    public void changeQuantity(int quantityDelta, Product product) {
        quantity = new Quantity(quantityDelta);
        price = new Price(product.price().value() * quantityDelta);
        
        calculateValidQuantity(product);
    }
}
