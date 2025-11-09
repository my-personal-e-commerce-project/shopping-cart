package microservice.ecommerce.shopping_cart.integration;

import java.util.List;
import java.util.Map;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import jakarta.ws.rs.core.Response;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;
 
    @Value("${KEYCLOAK.HOST}")
    private String KEYCLOAK_HOST;

    @Value("${KEYCLOAK.REALM}")
    private String KEYCLOAK_REALM;

    @Value("${KEYCLOAK.CLIENT_ID}")
    private String KEYCLOAK_CLIENT_ID;

    @Value("${KEYCLOAK.CLIENT_SECRET}")
    private String KEYCLOAK_CLIENT_SECRET;

    private Keycloak keycloak;
    
    @BeforeEach
    private void setup() {
        keycloak = KeycloakBuilder.builder()
            .serverUrl(KEYCLOAK_HOST)
            .realm(KEYCLOAK_REALM)
            .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
            .clientId(KEYCLOAK_CLIENT_ID)
            .clientSecret(KEYCLOAK_CLIENT_SECRET)
            .build();
    }

    private void createUser() {
        UserRepresentation user = new UserRepresentation();
        user.setUsername("test");
        user.setFirstName("test");
        user.setLastName("test");
        user.setEmail("test@gmail.com");
        user.setEnabled(true);

        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setTemporary(false);
        cred.setType(CredentialRepresentation.PASSWORD);
        cred.setValue("test");

        UsersResource usersResource = keycloak.realm(KEYCLOAK_REALM).users();
        Response response = usersResource.create(user);

        if(response.getStatus() != 201) {
            return;
        }

        String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
        
        usersResource.get(userId).resetPassword(cred);   
    }

    private void deleteUser(String username) {
        UsersResource usersResource = keycloak.realm(KEYCLOAK_REALM).users();

        List<UserRepresentation> users = usersResource.search(username, true);
        if (users.isEmpty()) {
            return;
        }

        String userId = users.get(0).getId();

        usersResource.delete(userId);
    }

    private String login() {
        String username = "test";
        String password = "test";

        String TOKEN_URL = KEYCLOAK_HOST + "/realms/" + KEYCLOAK_REALM + "/protocol/openid-connect/token";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", KEYCLOAK_CLIENT_ID);
        params.add("client_secret", KEYCLOAK_CLIENT_SECRET);
        params.add("grant_type", "password");
        params.add("username", username);
        params.add("password", password);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(TOKEN_URL, request, Map.class);

        return (String) response.getBody().get("access_token");
    }

    @Test
    public void retrieveCartUsingTheGetCartMethodButThereNoIsShoppingCartAtDB() throws Exception{
        createUser();

        String token = login();
       
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/api/v1/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
        )
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("message").isString());

        deleteUser("test");
    }
}
