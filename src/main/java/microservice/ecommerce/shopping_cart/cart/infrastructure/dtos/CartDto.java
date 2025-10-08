package microservice.ecommerce.shopping_cart.cart.infrastructure.dtos;

import java.util.List;

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
public class CartDto {

    private String id;
    private String userId;
    private int totalQuantity;
    private double totalPrice;
    private List<CartItemDto> items;
}
