package microservice.ecommerce.shopping_cart.cart.infrastructure.dtos;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PayloadResponse <T> {

    private T data;

    @Builder.Default
    private String message = "success";
    
    private Map<String, Object> errors;
}
