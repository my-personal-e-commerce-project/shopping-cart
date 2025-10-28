package microservice.ecommerce.shopping_cart.usecases;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import microservice.ecommerce.shopping_cart.cart.application.port.out.ExtractProductPort;
import microservice.ecommerce.shopping_cart.cart.application.use_cases.GetCartByUserIdUseCaseImpl;
import microservice.ecommerce.shopping_cart.cart.domain.agregate.Cart;
import microservice.ecommerce.shopping_cart.cart.domain.entity.Product;
import microservice.ecommerce.shopping_cart.cart.domain.repository.CartRepository;
import microservice.ecommerce.shopping_cart.cart.domain.value_objects.Price;
import microservice.ecommerce.shopping_cart.cart.domain.value_objects.Quantity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class GetCartByUserIdUseCaseImplTest {

    @Mock
    private CartRepository cartRepository;
    
    @Mock
    private ExtractProductPort extractProductPort;

    @InjectMocks
    private GetCartByUserIdUseCaseImpl getCartByIdUseCase;

    private Cart cart;
    private Product product;

    @BeforeEach
    private void setUp() {
        cart = new Cart(
            "1xid",
            "2eid",
            null
        );
        
        product = new Product(
            "3rid",
            "slug",
            "name",
            "image",
            new Quantity(10),
            new Price(10.0)
        );
    }

    @Test
    public void retrieveCartWithoutCartItemUseingTheExecuteMethod() {
        when(cartRepository.findCartByUserIdOrCreateAndReturn("2eid"))
            .thenReturn(cart);

        Cart cart = getCartByIdUseCase.execute("2eid");

        assertEquals(cart.id(), "1xid");
        assertEquals(cart.userId(), "2eid");
        assertEquals(cart.totalPrice().value(), 0);
        assertEquals(cart.totalQuantity().value(), 0);
    }

    @Test
    public void retrieveCartWithCartItemUseingTheExecuteMethod() {
        cart.syncItems(product, 19);

        when(cartRepository.findCartByUserIdOrCreateAndReturn("2eid"))
            .thenReturn(cart);

        when(extractProductPort.execute("3rid"))
            .thenReturn(product);

        Cart cart = getCartByIdUseCase.execute("2eid");

        assertEquals(cart.id(), "1xid");
        assertEquals(cart.userId(), "2eid");
        assertEquals(cart.totalPrice().value(), 190);
        assertEquals(cart.totalQuantity().value(), 19);
    }
}
