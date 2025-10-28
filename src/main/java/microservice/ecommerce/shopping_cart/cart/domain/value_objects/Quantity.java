package microservice.ecommerce.shopping_cart.cart.domain.value_objects;

public class Quantity {

    private final int value;

    public Quantity(int value) {
        if (value < 0) throw new IllegalArgumentException("Quantity cannot be negative");
        this.value = value;
    }

    public int value() {
        return value;
    }
}
