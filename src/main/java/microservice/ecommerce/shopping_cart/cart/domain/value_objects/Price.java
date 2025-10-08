package microservice.ecommerce.shopping_cart.cart.domain.value_objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Price {
  private final Double value;

  public Price(Double value) {
    if (value == null || value < 0) {
      throw new IllegalArgumentException("Price cannot be null or negative");
    }
    this.value = value;
  }
}
