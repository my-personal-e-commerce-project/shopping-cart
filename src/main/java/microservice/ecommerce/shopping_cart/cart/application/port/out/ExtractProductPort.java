package microservice.ecommerce.shopping_cart.cart.application.port.out;

import microservice.ecommerce.shopping_cart.cart.domain.entity.Product;

public interface ExtractProductPort {

    Product execute(String product_id);
}
