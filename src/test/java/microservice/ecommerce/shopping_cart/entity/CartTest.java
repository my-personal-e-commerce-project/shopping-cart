package microservice.ecommerce.shopping_cart.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import microservice.ecommerce.shopping_cart.cart.domain.agregate.Cart;
import microservice.ecommerce.shopping_cart.cart.domain.entity.Product;
import microservice.ecommerce.shopping_cart.cart.domain.value_objects.Price;
import microservice.ecommerce.shopping_cart.cart.domain.value_objects.Quantity;

public class CartTest {

    private Cart cart;

    @BeforeEach
    private void setUp() {
        cart = new Cart(
            "1",
            "1",
            null
        );
    }

    @Test
    public void testCartCartIdIsAStringAndIs1() {
        assertEquals(cart.id(), "1");
    }

    @Test
    public void testCartUserIdIsAStringAndIs1() {
        assertEquals(cart.userId(), "1");
    }

    @Test
    public void testCartTotalQuantityIsZero() {
        assertEquals(cart.totalQuantity().value(), 0);
    }

    @Test
    public void testCartTotalPriceIsZero() {
        assertEquals(cart.totalPrice().value(), 0);
    }

    @Test
    public void testCartItemsReturnsEmptyList() {
        assertEquals(cart.items(), List.of());
    }

    @Test
    public void testSyncItemsMethodWhenQuantityIsTen() {
        cart.syncItems(new Product(
            "1", 
            "slug", 
            "name", 
            "image", 
            new Quantity(10), 
            new Price(10.0)
        ), 10);

        assertEquals(cart.items().size(), 1);
        assertEquals(cart.totalQuantity().value(), 10);
        assertEquals(cart.totalPrice().value(), new Price(100.0).value());
    }
}
