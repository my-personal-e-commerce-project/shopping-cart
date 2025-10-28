package microservice.ecommerce.shopping_cart.cart.domain.value_objects;

public class Price {
    private final double value;

    public Price(double value) {
        if (value < 0) {
            throw new IllegalArgumentException("Price cannot be null or negative");
        }
        this.value = value;
    }

    public double value() {
        return value;
    }

    public String format () {
        return "$" + String.format("%.2f", value);
    }
}
