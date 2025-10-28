package microservice.ecommerce.shopping_cart.cart.application.use_cases;

import microservice.ecommerce.shopping_cart.cart.application.port.in.UpdateOrCreateCartUseCase;
import microservice.ecommerce.shopping_cart.cart.application.port.out.ExtractProductPort;
import microservice.ecommerce.shopping_cart.cart.domain.agregate.Cart;
import microservice.ecommerce.shopping_cart.cart.domain.repository.CartRepository;
import microservice.ecommerce.shopping_cart.cart.domain.entity.Product;
import microservice.ecommerce.shopping_cart.shared.application.exception.DataNotFound;

public class UpdateOrCreateCartUseCaseImpl implements UpdateOrCreateCartUseCase {
    
    private CartRepository cartRepository;
    private ExtractProductPort extractProductPort;

    public UpdateOrCreateCartUseCaseImpl(
        CartRepository cartRepository,
        ExtractProductPort extractProductPort
    ) {
        this.cartRepository = cartRepository;
        this.extractProductPort = extractProductPort;
    }

    @Override
    public Cart execute(String userId, String product_id, int quantity) {
        Cart cart = cartRepository.findCartByUserIdOrCreateAndReturn(userId);

        Product product = extractProductPort.execute(product_id);

        if(product == null) {
            throw new DataNotFound("Product not found");
        }
        
        cart.syncItems(product, quantity);

        return cartRepository.update(cart);
    }
}
