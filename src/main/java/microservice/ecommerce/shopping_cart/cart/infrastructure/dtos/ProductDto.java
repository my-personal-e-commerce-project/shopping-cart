package microservice.ecommerce.shopping_cart.cart.infrastructure.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductDto {
    
    private String id;
    private String slug;
    private String name;
    private String image;
    private int quantity;
    private Double price;
}
