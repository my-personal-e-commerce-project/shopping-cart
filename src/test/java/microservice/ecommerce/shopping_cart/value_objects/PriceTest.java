package microservice.ecommerce.shopping_cart.value_objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import microservice.ecommerce.shopping_cart.cart.domain.value_objects.Price;

public class PriceTest {

    @Test
    public void testGetValue() {
        double price = 10.00;

        Price p = new Price(price);
        assertEquals(p.value(), price, 0);
    }

    @Test
    public void testExceptionPrice () {

         IllegalArgumentException e = 
            assertThrows(
                IllegalArgumentException.class, 
                () -> new Price(-10.00)
            );

         assertEquals(e.getMessage(), "Price cannot be null or negative");
    }

    @Test
    public void testToString () {
        double price = 10.00;
        assertEquals(new Price(price).format(), "$10,00");
    }
}
