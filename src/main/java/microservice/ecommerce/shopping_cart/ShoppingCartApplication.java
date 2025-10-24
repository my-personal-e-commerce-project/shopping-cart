package microservice.ecommerce.shopping_cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ShoppingCartApplication {

    public String keycloak_host;

    public static void main(String[] args) {
		    SpringApplication.run(ShoppingCartApplication.class, args);
	  }
}
