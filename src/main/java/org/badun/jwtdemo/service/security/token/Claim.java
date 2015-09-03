package org.badun.jwtdemo.service.security.token;

/**
 * Created by Artsiom Badun.
 */
public class Claim {
    private final Claims name;
    private final String value;

    public Claim(Claims name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name.val();
    }

    public Claims name() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
