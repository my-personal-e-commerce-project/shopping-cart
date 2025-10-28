package microservice.ecommerce.shopping_cart.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import microservice.ecommerce.shopping_cart.cart.domain.entity.Product;
import microservice.ecommerce.shopping_cart.cart.domain.value_objects.Price;
import microservice.ecommerce.shopping_cart.cart.domain.value_objects.Quantity;

public class ProductTest {

    private Product product;

    @BeforeEach
    private void setUp() {
        product = new Product(
            "1",
            "slug",
            "name",
            "image",
            new Quantity(10),
            new Price(10.0)
        );
    }

    @Test
    void testGetProductId() {
        assertEquals(product.id(), "1");
    }

    @Test
    void testGetProductSlug() {
        assertEquals(product.slug(), "slug");
    }

    @Test
    void testGetProductName() {
        assertEquals(product.name(), "name");
    }

    @Test
    void testGetProductImage() {
        assertEquals(product.image(), "image");
    }

    @Test
    void testProductQuantityInstanceOfQuantityValueObjects() {
        assertEquals(product.price() instanceof Price, true);
    }
    
    @Test
    void testGetProductQuantity() {
        assertEquals(product.quantity().value(), 10);
    }

    @Test
    void testProductPriceInstanceOfPriceValueObjects() {
        assertEquals(product.price() instanceof Price, true);
    }

    @Test
    void testGetProductPrice() {
        assertEquals(product.price().value(), 10.0);
    }
}
