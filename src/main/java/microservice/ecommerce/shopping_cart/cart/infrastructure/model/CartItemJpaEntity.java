package microservice.ecommerce.shopping_cart.cart.infrastructure.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Table(name = "cart_items")
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemJpaEntity {

    @Id
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private CartJpaEntity cart;

    @Column(name = "product_id", nullable = false, updatable = false, unique = true)
    private String product;

    @Builder.Default
    @Column(name = "quantity", nullable = false)
    private int quantity = 1;

    @Builder.Default
    @Column(name = "price", nullable = false)
    private double price = 0;
}
