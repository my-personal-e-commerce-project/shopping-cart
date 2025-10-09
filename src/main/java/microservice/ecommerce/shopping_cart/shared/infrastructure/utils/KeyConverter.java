package microservice.ecommerce.shopping_cart.shared.infrastructure.utils;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KeyConverter {

    public static RSAPublicKey getPublicKeyFromBase64(String base64Key) throws Exception {
        byte[] encoded = Base64.getDecoder().decode(base64Key);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(encoded));
    }
}
