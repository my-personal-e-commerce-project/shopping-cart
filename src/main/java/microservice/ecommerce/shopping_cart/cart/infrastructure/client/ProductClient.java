package microservice.ecommerce.shopping_cart.cart.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import microservice.ecommerce.shopping_cart.cart.infrastructure.dtos.PayloadResponse;
import microservice.ecommerce.shopping_cart.cart.infrastructure.dtos.ProductDto;

@FeignClient(
    name = "products"
)
public interface ProductClient {
    
    @GetMapping("/api/v1/products/id/{id}")
    public ResponseEntity<PayloadResponse<ProductDto>> find(@PathVariable String id);
}
