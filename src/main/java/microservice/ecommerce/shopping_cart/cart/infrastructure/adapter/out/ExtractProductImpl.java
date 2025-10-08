package microservice.ecommerce.shopping_cart.cart.infrastructure.adapter.out;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import microservice.ecommerce.shopping_cart.cart.application.port.out.ExtractProductPort;
import microservice.ecommerce.shopping_cart.cart.domain.value_objects.Price;
import microservice.ecommerce.shopping_cart.cart.domain.value_objects.Product;
import microservice.ecommerce.shopping_cart.cart.domain.value_objects.Quantity;
import microservice.ecommerce.shopping_cart.cart.infrastructure.client.ProductClient;
import microservice.ecommerce.shopping_cart.cart.infrastructure.dtos.ProductDto;

@Component
@RequiredArgsConstructor
public class ExtractProductImpl implements ExtractProductPort {

    private final ProductClient productClient;

    @Override
    public Product execute(String product_id) {
        ProductDto product = productClient.find(product_id);

        return new Product(
            product.getId(), 
            product.getSlug(), 
            product.getName(), 
            product.getImage(),
            new Quantity(product.getQuantity()), 
            new Price(product.getPrice())
        );
    } 
}
