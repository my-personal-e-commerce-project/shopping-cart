package microservice.ecommerce.shopping_cart.cart.infrastructure.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Builder
@Setter
@NoArgsConstructor
public class ProductDto {
    
    private String id;
    private String slug;
    private String name;
    private List<String> images;
    private int stock;
    private String category_id;
    private Double price;
}
