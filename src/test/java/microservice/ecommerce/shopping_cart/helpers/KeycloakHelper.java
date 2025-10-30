package microservice.ecommerce.shopping_cart.helpers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import jakarta.ws.rs.core.Response;

public class KeycloakHelper {

    @Value("${KEYCLOAK_HOST}")
    private String KEYCLOAK_HOST;

    @Value("${KEYCLOAK_REALM}")
    private String KEYCLOAK_REALM;

    @Value("${KEYCLOAK_USERNAME}")
    private String KEYCLOAK_USERNAME;

    @Value("${KEYCLOAK_PASSWORD}")
    private String KEYCLOAK_PASSWORD;

    private Keycloak keycloak;

    public KeycloakHelper() {
        keycloak = KeycloakBuilder.builder()
            .serverUrl(KEYCLOAK_HOST)
            .realm(KEYCLOAK_REALM)
            .clientId("admin-cli")
            .username(KEYCLOAK_USERNAME)
            .password(KEYCLOAK_PASSWORD)
            .build();

    }

    public void createUser() {
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

        UsersResource usersResource = keycloak.realm("mi-realm").users();
        Response response = usersResource.create(user);

        String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

        usersResource.get(userId).resetPassword(cred);   
    }

    public void deleteUser(String username) {
        UsersResource usersResource = keycloak.realm(KEYCLOAK_REALM).users();

        List<UserRepresentation> users = usersResource.search(username, true);
        if (users.isEmpty()) {
            return;
        }

        String userId = users.get(0).getId();

        usersResource.delete(userId);
    }

    public String login() {
        String username = "test";
        String password = "test";
        String TOKEN_URL = KEYCLOAK_HOST + "/realms/mi-realm/protocol/openid-connect/token";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        Map<String, String> params = new HashMap<>();
        params.put("client_id", "test");
        params.put("grant_type", "password");
        params.put("username", username);
        params.put("password", password);

        HttpEntity<Map<String, String>> request = new HttpEntity(params, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(TOKEN_URL, request, Map.class);

        return (String) response.getBody().get("access_token");
    }
}
