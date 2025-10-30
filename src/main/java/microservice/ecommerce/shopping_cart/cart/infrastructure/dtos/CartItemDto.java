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
    private String price;
    private Object product;
    private boolean quantity_in_stock;
}
