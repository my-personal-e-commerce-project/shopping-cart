package microservice.ecommerce.shopping_cart.cart.infrastructure.validate;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateCartRequest {

    private int quantity;
    private String productId;
}
