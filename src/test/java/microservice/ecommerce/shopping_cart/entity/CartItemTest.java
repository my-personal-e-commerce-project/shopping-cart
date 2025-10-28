package microservice.ecommerce.shopping_cart.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import microservice.ecommerce.shopping_cart.cart.domain.entity.CartItem;
import microservice.ecommerce.shopping_cart.cart.domain.entity.Product;
import microservice.ecommerce.shopping_cart.cart.domain.value_objects.Price;
import microservice.ecommerce.shopping_cart.cart.domain.value_objects.Quantity;

public class CartItemTest {
    private CartItem cartItem;

    @BeforeEach
    private void setUp() {
        cartItem = new CartItem(
            "1",
            new Quantity(10),
            new Price(10.0),
            "1"
        );
    }

    @Test
    public void testCartItemIdIsAStringAndIs1() {
        assertEquals(cartItem.id(), "1");
    }

    @Test
    public void testProductQuantityInstanceOfQuantityValueObjects() {
        assertEquals(cartItem.price() instanceof Price, true);
    }


    @Test
    public void quantityMethodReturnedQuantityValueObjectsWithValue10() {
        assertEquals(cartItem.quantity().value(), new Quantity(10).value());
    }

    @Test
    void testProductPriceInstanceOfPriceValueObjects() {
        assertEquals(cartItem.price() instanceof Price, true);
    }

    @Test
    void priceMethodReturnedQuantityValueObjectsWithValue10() {
        assertEquals(cartItem.price().value(), new Price(10.0).value());
    }

    @Test
    void testProductIdIsAStringAndIs1() {
        assertEquals(cartItem.product_id(), "1");
    }

    @Test
    void calculateValidQuantityMethodReturnedTrueIfTheStockProductIsTen() {
        cartItem.calculateValidQuantity(
            new Product(
                "1", 
                "name", 
                "slug", 
                "image", 
                new Quantity(10),
                new Price(10.0)
            ) 
        );

        assertEquals(cartItem.in_stock(), true);
    }

    @Test
    void testChangeQuantityMethodTheQuantityIs1() {
        cartItem.changeQuantity(1, 
            new Product(
                "1", 
                "name", 
                "slug", 
                "image", 
                new Quantity(10), 
                new Price(10.0)
            )
        );

        assertEquals(cartItem.quantity().value(), new Quantity(1).value());
    }
}
