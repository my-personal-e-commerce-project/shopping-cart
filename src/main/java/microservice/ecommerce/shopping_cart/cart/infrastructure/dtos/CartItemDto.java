package microservice.ecommerce.shopping_cart.cart.infrastructure.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDto {

    private String id;
    private int quantity;
    private double price;
    private String product_id;
    private boolean quantity_in_stock;
}
