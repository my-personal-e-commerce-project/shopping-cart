package microservice.ecommerce.shopping_cart;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import jakarta.annotation.PostConstruct;

@EnableFeignClients
@SpringBootApplication
public class ShoppingCartApplication {

    public static void main(String[] args) {
		    SpringApplication.run(ShoppingCartApplication.class, args);
	  }
}
