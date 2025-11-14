package microservice.ecommerce.shopping_cart.cart.infrastructure.presentation.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import microservice.ecommerce.shopping_cart.cart.application.port.in.GetCartByUserIdUseCase;
import microservice.ecommerce.shopping_cart.cart.application.port.in.UpdateOrCreateCartUseCase;
import microservice.ecommerce.shopping_cart.cart.domain.agregate.Cart;
import microservice.ecommerce.shopping_cart.cart.domain.exception.ProductNotFound;
import microservice.ecommerce.shopping_cart.cart.infrastructure.dtos.CartDto;
import microservice.ecommerce.shopping_cart.cart.infrastructure.dtos.CartItemDto;
import microservice.ecommerce.shopping_cart.cart.infrastructure.dtos.PayloadResponse;
import microservice.ecommerce.shopping_cart.cart.infrastructure.dtos.ProductDto;
import microservice.ecommerce.shopping_cart.cart.infrastructure.validate.UpdateCartRequest;
import microservice.ecommerce.shopping_cart.shared.application.exception.DataNotFound;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final GetCartByUserIdUseCase getCartByUserIdUseCase;
    private final UpdateOrCreateCartUseCase updateOrCreateCartUseCase;

    @GetMapping
    public ResponseEntity<PayloadResponse<CartDto>> getCart(
        Authentication authentication
    ) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        
        String userId = jwt.getSubject();
       
        Cart cart = getCartByUserIdUseCase.execute(userId);
        
        PayloadResponse<CartDto> response = new PayloadResponse<CartDto>();

        response.setData(toMap(cart));

        return ResponseEntity.ok(
            response
        );
    }

    @PutMapping
    public ResponseEntity<PayloadResponse<CartDto>> updateCart(
        Authentication authentication,
        @RequestBody UpdateCartRequest body 
    ) {
        try{
            Jwt jwt = (Jwt) authentication.getPrincipal();
        
            String userId = jwt.getSubject();

            Cart cart = updateOrCreateCartUseCase
                .execute(userId, body.getProductId(), body.getQuantity());

            PayloadResponse<CartDto> response = new PayloadResponse<CartDto>();

            response.setData(toMap(cart));

            return ResponseEntity.ok(
                response
            );

        } catch(IllegalArgumentException e) {
            Map<String, Object> errors = new HashMap<String, Object>();
            errors.put("quantity", e.getMessage());

            PayloadResponse<CartDto> response = new PayloadResponse<CartDto>();

            response.setErrors(errors);
            return new ResponseEntity<PayloadResponse<CartDto>>(
                response,
                HttpStatus.BAD_REQUEST
            );
        }
    }

    @ExceptionHandler(DataNotFound.class)
    public ResponseEntity<PayloadResponse<?>> handleDataNotFound(DataNotFound e) {
        return new ResponseEntity<PayloadResponse<?>>(
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
                    .product(ProductDto.builder()
                        .id(item.product().id())
                        .slug(item.product().slug())
                        .name(item.product().name())
                        .images(List.of(item.product().image()))
                        .category_id(item.product().category_id())
                        .price(item.product().price().value())
                        .stock(item.product().quantity().value())
                        .build())
                    .quantity(item.quantity().value())
                    .price(item.price().format())
                    .quantity_in_stock(item.in_stock())
                    .build();
            }).toList())
            .build();
    }
}
