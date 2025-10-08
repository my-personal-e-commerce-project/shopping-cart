package microservice.ecommerce.shopping_cart.cart.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import microservice.ecommerce.shopping_cart.cart.infrastructure.dtos.ProductDto;

@FeignClient(
    name = "product",
    url = "${client.product-service.url}"
)
public interface ProductClient {
    
    @GetMapping("/product/{id}")
    public ProductDto find(@PathVariable String id);
}
