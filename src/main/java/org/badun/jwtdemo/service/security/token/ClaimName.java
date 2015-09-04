package org.badun.jwtdemo.service.security.token;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Artsiom Badun.
 */
public enum ClaimName {
    USER_NAME("uname"),
    ROLE("role"),
    TOKEN_ID("jti"),
    EXPIRATION_DATE("exp");

    ClaimName(String val) {
        this.val = val;
    }

    private String val;

    public String val() {
        return val;
    }

    private static Map<String, ClaimName> vals = Arrays.stream(values())
            .collect(Collectors.toMap(ClaimName::val, value -> value));

    public static ClaimName get(String name) {
        return vals.get(name);
    }
}
