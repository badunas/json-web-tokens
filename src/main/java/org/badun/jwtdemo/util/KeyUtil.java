package org.badun.jwtdemo.util;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

/**
 * Created by Artsiom Badun.
 */
public class KeyUtil {
    public static final String HMAC_SHA256 = "HmacSHA256";

    public static Key generateKey(String secretString, String alg) {
        return new SecretKeySpec(secretString.getBytes(), alg);
    }

    public static Key generateHmacKey(String secretString) {
        return generateKey(secretString, HMAC_SHA256);
    }

    public static Key decodeKey(String secretString, String alg) {
        byte[] encodedSecret = Base64.getDecoder().decode(secretString);
        return new SecretKeySpec(
                encodedSecret,
                0,
                encodedSecret.length,
                alg);
    }

    public static Key decodeHmacKey(String secretString) {
        return decodeKey(secretString, HMAC_SHA256);
    }

    public static String encodeKey(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
}
