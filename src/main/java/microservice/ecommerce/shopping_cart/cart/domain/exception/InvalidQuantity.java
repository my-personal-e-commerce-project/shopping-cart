package microservice.ecommerce.shopping_cart.cart.domain.exception;

public class InvalidQuantity extends IllegalArgumentException {
    
    public InvalidQuantity(String msg) {
        super(msg);
    }
}
