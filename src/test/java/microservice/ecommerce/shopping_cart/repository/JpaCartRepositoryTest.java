package microservice.ecommerce.shopping_cart.repository;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import microservice.ecommerce.shopping_cart.cart.domain.agregate.Cart;
import microservice.ecommerce.shopping_cart.cart.domain.entity.Product;
import microservice.ecommerce.shopping_cart.cart.domain.value_objects.Price;
import microservice.ecommerce.shopping_cart.cart.domain.value_objects.Quantity;
import microservice.ecommerce.shopping_cart.cart.infrastructure.adapter.repository.JpaCartRepository;
import microservice.ecommerce.shopping_cart.cart.infrastructure.model.CartJpaEntity;

import static org.mockito.Mockito.*;

import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class JpaCartRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private JpaCartRepository jpaCartRepository;

    @Test
    public void testFindCartByUserIdOrCreateAndReturnMethodWhenCartNotFound() {
        @SuppressWarnings("unchecked")
        TypedQuery<CartJpaEntity> mockQuery = mock(TypedQuery.class);

        when(entityManager.createQuery(anyString(), eq(CartJpaEntity.class)))
                .thenReturn(mockQuery);

        when(mockQuery.setParameter(anyString(), any())).thenReturn(mockQuery);

        when(mockQuery.getResultStream()).thenReturn(Stream.empty());

        Cart cart = jpaCartRepository.findCartByUserIdOrCreateAndReturn("userId");

        assertEquals(0, cart.totalQuantity().value());
        assertEquals(0, cart.totalPrice().value());

        verify(entityManager).createQuery(anyString(), eq(CartJpaEntity.class));
        verify(mockQuery).setParameter("userId", "userId");
        verify(mockQuery).getResultStream();
    }

    @Test
    public void testFindCartByUserIdOrCreateAndReturnMethodWhenCartFound() {
        CartJpaEntity fakeCart = new CartJpaEntity(
            "1",
            "userId",
            0,
            0,
            new ArrayList<>()
        );

        @SuppressWarnings("unchecked")
        TypedQuery<CartJpaEntity> mockQuery = mock(TypedQuery.class);

        when(entityManager.createQuery(anyString(), eq(CartJpaEntity.class)))
                .thenReturn(mockQuery);

        when(mockQuery.setParameter(anyString(), any())).thenReturn(mockQuery);

        when(mockQuery.getResultStream()).thenReturn(Stream.of(fakeCart));

        Cart cart = jpaCartRepository.findCartByUserIdOrCreateAndReturn("userId");

        assertEquals(0, cart.totalQuantity().value());
        assertEquals(0, cart.totalPrice().value());

        verify(entityManager).createQuery(anyString(), eq(CartJpaEntity.class));
        verify(mockQuery).setParameter("userId", "userId");
        verify(mockQuery).getResultStream();
    }

    @Test
    public void testUpdateMethodButMyCartIsNull() {
        
        String cart_id = "id1";

        Cart cart = new Cart(cart_id, "userId", new ArrayList<>());

        Product product = new Product(
            "1",
            "slug",
            "name",
            "image",
            new Quantity(10),
            new Price(10.0)
        );

        cart.syncItems(product, 10);

        when(entityManager.find(CartJpaEntity.class, cart_id))
            .thenReturn(null);

        Cart updatedCart = jpaCartRepository.update(cart);

        assertEquals(updatedCart, null);
    }

    @Test
    public void testUpdateMethodButMyCartExists() {
        String cart_id = "id1";

        Cart cart = new Cart(cart_id, "userId", new ArrayList<>());

        Product product = new Product(
            "1",
            "slug",
            "name",
            "image",
            new Quantity(10),
            new Price(10.0)
        );

        cart.syncItems(product, 10);

        when(entityManager.find(CartJpaEntity.class, cart_id))
        .thenReturn(
            CartJpaEntity
                .builder()
                .id(cart_id)
                .userId("userId")
                .totalQuantity(10)
                .totalPrice(100.0)
                .build()
        );

        Cart updatedCart = jpaCartRepository.update(cart);

        assertEquals(updatedCart.id(), cart_id);
        assertEquals(updatedCart.userId(), "userId");
        assertEquals(updatedCart.totalQuantity().value(), 10);
        assertEquals(updatedCart.totalPrice().value(), 100.0);
    }
}
