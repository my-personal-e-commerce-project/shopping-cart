package microservice.ecommerce.shopping_cart.cart.infrastructure.dtos;

import java.util.Map;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PayloadResponse {

    private Object data;

    @Builder.Default
    private String message = "success";
    
    private Map<String, Object> errors;
}
