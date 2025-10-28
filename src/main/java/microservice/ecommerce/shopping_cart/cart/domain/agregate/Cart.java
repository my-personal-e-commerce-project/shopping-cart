package microservice.ecommerce.shopping_cart.cart.domain.agregate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import microservice.ecommerce.shopping_cart.cart.domain.entity.CartItem;
import microservice.ecommerce.shopping_cart.cart.domain.value_objects.Price;
import microservice.ecommerce.shopping_cart.cart.domain.entity.Product;
import microservice.ecommerce.shopping_cart.cart.domain.value_objects.Quantity;

public class Cart {

    private String id;
    private String userId;
    private Quantity totalQuantity;
    private Price totalPrice;
    private Map<String, CartItem> items;

    public Cart(
        String id, 
        String userId, 
        List<CartItem> items
    ) {
        this.id = id;
        this.userId = userId;

        if(items == null) {
            items = List.of();
        }

        this.totalQuantity = new Quantity(
            items.stream()
                 .mapToInt(i -> i.quantity().value())
                 .sum()
        );

        this.totalPrice = new Price(
            items.stream()
                 .mapToDouble(i -> i.price().value())
                 .sum()
        );

        this.items = items.stream().collect(
            HashMap::new, 
            (map, item) -> map.put(item.product_id(), item), HashMap::putAll
        );

    }

    public String id() { return id; }

    public String userId() { return userId; }
    
    public Quantity totalQuantity() {
        return totalQuantity; 
    }

    public Price totalPrice() {
        return totalPrice;
    }    

    public List<CartItem> items() { 
        if(items == null) {
            return null;
        }

        return items.values().stream().toList();
    }

    private void recalculateTotal(Price price) {
        totalQuantity=null;
        items().forEach(item -> {
            int quantity = item.quantity().value();

            if(totalQuantity == null) {
                totalQuantity = new Quantity(quantity);
                return;
            }
            totalQuantity = new Quantity(totalQuantity.value() + quantity);
        });

        totalPrice = new Price(price.value() * totalQuantity.value());
    }

    public void syncItems(Product product, int quantity) {
        if(items.containsKey(product.id())) {
            if(quantity == 0) {
                items.remove(product.id());
                return;
            }
            CartItem item = items.get(product.id());
            item.changeQuantity(quantity, product);
        } else {
            if(quantity == 0) {
                throw new IllegalArgumentException("The quantity cannot be zero");
            }
            
            CartItem item = new CartItem(
                UUID.randomUUID().toString(),
                new Quantity(quantity),
                new Price(product.price().value() * quantity),
                product.id() 
            );
            item.calculateValidQuantity(product);
            items.put(product.id(), item);
        }

        recalculateTotal(new Price(product.price().value()));
    }
}

