package microservice.ecommerce.shopping_cart.cart.infrastructure.adapter.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import microservice.ecommerce.shopping_cart.cart.domain.agregate.Cart;
import microservice.ecommerce.shopping_cart.cart.domain.entity.CartItem;
import microservice.ecommerce.shopping_cart.cart.domain.repository.CartRepository;
import microservice.ecommerce.shopping_cart.cart.domain.value_objects.Price;
import microservice.ecommerce.shopping_cart.cart.domain.value_objects.Quantity;
import microservice.ecommerce.shopping_cart.cart.infrastructure.model.CartItemJpaEntity;
import microservice.ecommerce.shopping_cart.cart.infrastructure.model.CartJpaEntity;

@Repository
public class JpaCartRepository implements CartRepository {
   
    @PersistenceContext
    private EntityManager entityManager;

    public JpaCartRepository(
        EntityManager entityManager
    ) {
        this.entityManager = entityManager;
    }

    @Transactional
    @Override
    public Cart findCartByUserIdOrCreateAndReturn(String userId) {
        CartJpaEntity cart = entityManager.createQuery(
            "SELECT c FROM CartJpaEntity c WHERE c.userId = :userId",
            CartJpaEntity.class
        )
            .setParameter("userId", userId)
            .getResultStream()
            .findFirst()
            .orElse(null);
       
        if(cart == null) {
            cart = CartJpaEntity.builder()
                .id(UUID.randomUUID().toString())
                .userId(userId)
                .build();

            entityManager.persist(cart);
        }

        return toCart(cart);
    }

    @Transactional
    @Override
    public Cart update(Cart cart) {
        CartJpaEntity cartJpaEntity = entityManager.find(CartJpaEntity.class, cart.id());

        if (cartJpaEntity == null) {
            return null;
        }

        cartJpaEntity.setTotalQuantity(cart.totalQuantity().value());
        cartJpaEntity.setTotalPrice(cart.totalPrice().value());

        cartJpaEntity.getItems().clear();

        for (var item : cart.items()) {
            CartItemJpaEntity newItem = CartItemJpaEntity.builder()
                .id(item.id())
                .product(item.product_id())
                .quantity(item.quantity().value())
                .price(item.price().value())
                .cart(cartJpaEntity)
                .build();

            cartJpaEntity.getItems().add(newItem);
        }

        entityManager.merge(cartJpaEntity);

        return cart;
    }

   
    private Cart toCart(CartJpaEntity cart) {
        return new Cart(
            cart.getId(),
            cart.getUserId(),
            cart.getItems().stream().map(this::toCartItem).toList()
        );
    }

    private CartItem toCartItem(CartItemJpaEntity cartItem) {
        return new CartItem(
            cartItem.getId(),
            new Quantity(cartItem.getQuantity()),
            new Price(cartItem.getPrice()),
            cartItem.getProduct()
        );
    }
}
