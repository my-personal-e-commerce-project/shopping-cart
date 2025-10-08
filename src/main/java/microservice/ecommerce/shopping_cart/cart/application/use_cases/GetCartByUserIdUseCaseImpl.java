package microservice.ecommerce.shopping_cart.cart.application.use_cases;

import microservice.ecommerce.shopping_cart.cart.application.port.in.GetCartByUserIdUseCase;
import microservice.ecommerce.shopping_cart.cart.application.port.out.ExtractProductPort;
import microservice.ecommerce.shopping_cart.cart.domain.agregate.Cart;
import microservice.ecommerce.shopping_cart.cart.domain.entity.CartItem;
import microservice.ecommerce.shopping_cart.cart.domain.repository.CartRepository;
import microservice.ecommerce.shopping_cart.cart.domain.value_objects.Product;

public class GetCartByUserIdUseCaseImpl implements GetCartByUserIdUseCase {
  
    private CartRepository cartRepository;
    private ExtractProductPort extractProductPort; 

    public GetCartByUserIdUseCaseImpl(
        CartRepository cartRepository,
        ExtractProductPort extractProductPort
    ) {
        this.cartRepository = cartRepository;
        this.extractProductPort = extractProductPort;
    }

    @Override
    public Cart execute(String userId) {
        Cart cart = cartRepository
            .findCartByUserIdOrCreateAndReturn(userId);

        cart.items().forEach(this::reCalculateQuantityItemCart);

        return cart;
    }

    private void reCalculateQuantityItemCart(CartItem cartItem) {
        Product product = extractProductPort.execute(cartItem.product_id());
        cartItem.calculateValidQuantity(product); 
    }
}
