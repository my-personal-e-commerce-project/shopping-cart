package microservice.ecommerce.shopping_cart.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import microservice.ecommerce.shopping_cart.cart.application.port.out.ExtractProductPort;
import microservice.ecommerce.shopping_cart.cart.application.use_cases.UpdateOrCreateCartUseCaseImpl;
import microservice.ecommerce.shopping_cart.cart.domain.agregate.Cart;
import microservice.ecommerce.shopping_cart.cart.domain.entity.Product;
import microservice.ecommerce.shopping_cart.cart.domain.repository.CartRepository;

@ExtendWith(MockitoExtension.class)
public class UpdateOrCreateCartUseCaseImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ExtractProductPort extractProductPort;

    @InjectMocks
    private UpdateOrCreateCartUseCaseImpl updateOrCreateCartUseCase;

    private Product product;
    private Cart cart;

    @BeforeEach
    private void setUp() {
        cart = new Cart(
            "1",
            "1",
            null
        );
        product = new Product(
            "1",
            "slug",
            "name",
            "image",
            new microservice.ecommerce.shopping_cart.cart.domain.value_objects.Quantity(10),
            new microservice.ecommerce.shopping_cart.cart.domain.value_objects.Price(10.0)
        );
    }

    @Test
    public void testExecute() {
        int quantity = 2;

        cart.syncItems(product, quantity);

        when(cartRepository.findCartByUserIdOrCreateAndReturn("1")).thenReturn(cart);
        when(extractProductPort.execute("1")).thenReturn(product);

        updateOrCreateCartUseCase.execute(cart.userId(), product.id(), quantity);

        assertEquals(cart.items().size(), 1);
        assertEquals(cart.totalQuantity().value(), quantity);
        assertEquals(cart.totalPrice().value(), new microservice.ecommerce.shopping_cart.cart.domain.value_objects.Price(20.0).value());

        assertEquals(cart.items().get(0).quantity().value(), quantity);
        assertEquals(cart.items().get(0).product_id(), product.id());
    }
}
