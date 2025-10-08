package microservice.ecommerce.shopping_cart.cart.domain.repository;

import microservice.ecommerce.shopping_cart.cart.domain.agregate.Cart;

public interface CartRepository {

    public Cart findCartByUserIdOrCreateAndReturn(String userId);
    public Cart update(Cart cart);
}
