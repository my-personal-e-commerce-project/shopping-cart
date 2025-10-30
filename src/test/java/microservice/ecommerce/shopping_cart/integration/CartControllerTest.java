package microservice.ecommerce.shopping_cart.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import microservice.ecommerce.shopping_cart.helpers.KeycloakHelper;

@SpringBootTest
@AutoConfigureMockMvc
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void retrieveCartUsingTheGetCartMethodButThereNoIsShoppingCartAtDB() throws Exception{
        KeycloakHelper keycloakHelper = new KeycloakHelper();
        keycloakHelper.createUser();
        String token = keycloakHelper.login();

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/api/v1/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
        )
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json("{\"message\": \"success\", \"errors\": {}, \"data\": }"))
            ;

        keycloakHelper.deleteUser("test");
    }
}
