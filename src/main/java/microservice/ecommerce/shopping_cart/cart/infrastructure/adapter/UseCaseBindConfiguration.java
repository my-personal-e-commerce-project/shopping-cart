package microservice.ecommerce.shopping_cart.cart.infrastructure.adapter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import microservice.ecommerce.shopping_cart.cart.application.port.in.GetCartByUserIdUseCase;
import microservice.ecommerce.shopping_cart.cart.application.port.in.UpdateOrCreateCartUseCase;
import microservice.ecommerce.shopping_cart.cart.application.port.out.ExtractProductPort;
import microservice.ecommerce.shopping_cart.cart.application.use_cases.GetCartByUserIdUseCaseImpl;
import microservice.ecommerce.shopping_cart.cart.application.use_cases.UpdateOrCreateCartUseCaseImpl;
import microservice.ecommerce.shopping_cart.cart.domain.repository.CartRepository;

@Configuration
public class UseCaseBindConfiguration {

    @Bean
    public GetCartByUserIdUseCase getCartByUserIdUseCase(
        CartRepository cartRepository,
        ExtractProductPort extractProductPort 
    ) {

        return new GetCartByUserIdUseCaseImpl(
            cartRepository,
            extractProductPort 
        );
    };

    @Bean
    public UpdateOrCreateCartUseCase updateOrCreateCartUseCase(
        CartRepository cartRepository,
        ExtractProductPort extractProductPort
    ) {

        return new UpdateOrCreateCartUseCaseImpl(
            cartRepository,
            extractProductPort 
        );
    }
}
