package microservice.ecommerce.shopping_cart.shared.application.exception;

public class DataNotFound extends RuntimeException {

    public DataNotFound(String message) {
        super(message);
    }
}
