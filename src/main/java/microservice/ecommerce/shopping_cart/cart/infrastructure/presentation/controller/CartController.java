package microservice.ecommerce.shopping_cart.cart.infrastructure.presentation.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import microservice.ecommerce.shopping_cart.cart.application.port.in.GetCartByUserIdUseCase;
import microservice.ecommerce.shopping_cart.cart.application.port.in.UpdateOrCreateCartUseCase;
import microservice.ecommerce.shopping_cart.cart.domain.agregate.Cart;
import microservice.ecommerce.shopping_cart.cart.infrastructure.dtos.CartDto;
import microservice.ecommerce.shopping_cart.cart.infrastructure.dtos.CartItemDto;
import microservice.ecommerce.shopping_cart.cart.infrastructure.dtos.PayloadResponse;
import microservice.ecommerce.shopping_cart.cart.infrastructure.validate.UpdateCartRequest;
import microservice.ecommerce.shopping_cart.shared.application.exception.DataNotFound;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final GetCartByUserIdUseCase getCartByUserIdUseCase;
    private final UpdateOrCreateCartUseCase updateOrCreateCartUseCase;

    @GetMapping
    public ResponseEntity<PayloadResponse> getCart(
        Authentication authentication
    ) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        
        String userId = jwt.getSubject();
       
        Cart cart = getCartByUserIdUseCase.execute(userId);
        
        return ResponseEntity.ok(
            PayloadResponse.builder().data(toMap(cart)).build()
        );
    }

    @PutMapping
    public ResponseEntity<PayloadResponse> updateCart(
        Authentication authentication,
        @RequestBody UpdateCartRequest body 
    ) {
        try{
            Jwt jwt = (Jwt) authentication.getPrincipal();
        
            String userId = jwt.getSubject();

            Cart cart = updateOrCreateCartUseCase
                .execute(userId, body.getProductId(), body.getQuantity());

            return ResponseEntity.ok(
                PayloadResponse.builder().data(toMap(cart)).build()
            );

        } catch(IllegalArgumentException e) {
            Map<String, Object> errors = new HashMap<String, Object>();
            errors.put("quantity", e.getMessage());

            return new ResponseEntity<PayloadResponse>(
                PayloadResponse.builder().errors(
                    errors
                ).build(),
                HttpStatus.BAD_REQUEST
            );
        }
    }

    @ExceptionHandler(DataNotFound.class)
    public ResponseEntity<PayloadResponse> handleDataNotFound(DataNotFound e) {
        return new ResponseEntity<PayloadResponse>(
            PayloadResponse.builder().message(
                e.getMessage()
            ).build(),
            HttpStatus.NOT_FOUND
        );
    }

    private CartDto toMap(Cart cart) {
        return CartDto.builder()
            .id(cart.id())
            .userId(cart.userId())
            .totalQuantity(cart.totalQuantity().value())
            .totalPrice(cart.totalPrice().value())
            .items(cart.items().stream().map(item -> {
                return CartItemDto.builder()
                    .id(item.id())
                    .product(item.product())
                    .quantity(item.quantity().value())
                    .price(item.price().format())
                    .quantity_in_stock(item.in_stock())
                    .build();
            }).toList())
            .build();
    }
}
