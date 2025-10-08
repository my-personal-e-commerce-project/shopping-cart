package microservice.ecommerce.shopping_cart.cart.infrastructure.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "carts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartJpaEntity {

    @Id
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    private String id;

    @Column(name = "user_id", unique = true, nullable = false, updatable = false)
    private String userId;

    @Builder.Default
    @Column(name = "total_quantity", nullable = false)
    private int totalQuantity = 0;

    @Builder.Default
    @Column(name = "total_price", nullable = false)
    private double totalPrice = 0;

    @Builder.Default
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItemJpaEntity> items = new ArrayList<>();

    public int getTotalQuantity() {
        return items.stream().mapToInt(CartItemJpaEntity::getQuantity).sum();
    }

    public double getTotalPrice() {
        return items.stream().mapToDouble(CartItemJpaEntity::getPrice).sum();
    }
}
