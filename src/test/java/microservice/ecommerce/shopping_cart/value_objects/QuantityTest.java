package microservice.ecommerce.shopping_cart.value_objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import microservice.ecommerce.shopping_cart.cart.domain.value_objects.Quantity;

public class QuantityTest {

    @Test
    public void testGetValueQuantity() {
        int quantity = 10;
        Quantity q = new Quantity(quantity);
        assertEquals(q.value(), quantity, 0);
    }

    @Test
    public void testExceptionQuantity () {

         IllegalArgumentException e = 
            assertThrows(
                IllegalArgumentException.class, 
                () -> new Quantity(-10)
            );

         assertEquals(e.getMessage(), "Quantity cannot be negative");
    }
}
