package org.badun.jwtdemo.service.security.token;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Artsiom Badun.
 */
public enum Claims {
    USER_NAME("uname"),
    ROLE("role");

    Claims(String val) {
        this.val = val;
    }

    private String val;

    public String val() {
        return val;
    }

    private static Map<String, Claims> vals = Arrays.stream(values())
            .collect(Collectors.toMap(Claims::val, value -> value));

    public static Claims get(String name) {
        return vals.get(name);
    }
}
