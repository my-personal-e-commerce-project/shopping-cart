package microservice.ecommerce.shopping_cart.cart.infrastructure.adapter.out;

import org.springframework.stereotype.Component;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import microservice.ecommerce.shopping_cart.cart.application.port.out.ExtractProductPort;
import microservice.ecommerce.shopping_cart.cart.domain.value_objects.Price;
import microservice.ecommerce.shopping_cart.cart.domain.entity.Product;
import microservice.ecommerce.shopping_cart.cart.domain.value_objects.Quantity;
import microservice.ecommerce.shopping_cart.cart.infrastructure.client.ProductClient;
import microservice.ecommerce.shopping_cart.cart.infrastructure.dtos.PayloadResponse;
import microservice.ecommerce.shopping_cart.cart.infrastructure.dtos.ProductDto;
import microservice.ecommerce.shopping_cart.shared.application.exception.DataNotFound;

@Component
@RequiredArgsConstructor
public class ExtractProductImpl implements ExtractProductPort {

    private final ProductClient productClient;

    @Override
    public Product execute(String product_id) {
       
        try {
            PayloadResponse<ProductDto> res = productClient.find(product_id).getBody();

            ProductDto product = res.getData(); 

            return new Product(
                product.getId(), 
                product.getSlug(), 
                product.getName(), 
                product.getImages().get(0),
                new Quantity(product.getStock()), 
                new Price(product.getPrice())
            );
        } catch (FeignException.NotFound e) {
            throw new DataNotFound("Product not found");
        }
    } 
}
