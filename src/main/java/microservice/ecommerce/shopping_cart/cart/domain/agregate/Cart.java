package microservice.ecommerce.shopping_cart.cart.domain.agregate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import microservice.ecommerce.shopping_cart.cart.domain.entity.CartItem;
import microservice.ecommerce.shopping_cart.cart.domain.exception.InvalidQuantity;
import microservice.ecommerce.shopping_cart.cart.domain.value_objects.Price;
import microservice.ecommerce.shopping_cart.cart.domain.value_objects.Product;
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
        Quantity quantity, 
        Price totalPrice,
        List<CartItem> items
    ) {
        this.id = id;
        this.userId = userId;
        this.totalQuantity = new Quantity(
            items.stream()
                 .mapToInt(i -> i.quantity().getValue())
                 .sum()
        );

        this.totalPrice = new Price(
            items.stream()
                 .mapToDouble(i -> i.price().getValue())
                 .sum()
        );;
        
        this.items = items.stream()
            .collect(
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
        return items.values().stream().toList();
    }

    private void recalculateTotal(Price price) {
        totalQuantity=null;
        items().forEach(item -> {
            int quantity = item.quantity().getValue();

            if(totalQuantity == null) {
                totalQuantity = new Quantity(quantity);
                return;
            }
            totalQuantity = new Quantity(totalQuantity.getValue() + quantity);
        });

        totalPrice = new Price(price.getValue() * totalQuantity.getValue());
    }

    public void syncItems(String product_id, Product product, int quantity) {
        if(items.containsKey(product_id)) {
            if(quantity == 0) {
                items.remove(product_id);
                return;
            }
            CartItem item = items.get(product_id);
            item.changeQuantity(quantity, product);
        } else {
            
            if(quantity == 0) {
                throw new InvalidQuantity("The quantity cannot be zero");
            }
            
            CartItem item = new CartItem(
                UUID.randomUUID().toString(),
                new Quantity(quantity),
                new Price(product.price() * quantity),
                product_id
            );
            item.calculateValidQuantity(product);
            items.put(product_id, item);
        }
        recalculateTotal(new Price(product.price()));
    }
}

