package microservice.ecommerce.shopping_cart.cart.application.port.in;

import microservice.ecommerce.shopping_cart.cart.domain.agregate.Cart;

public interface UpdateOrCreateCartUseCase {

    public Cart execute(String userId, String product_id, int quantity);
}
