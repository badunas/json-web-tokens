package org.badun.jwtdemo.service.security.token;

/**
 * Created by Artsiom Badun.
 */
public class Claim {
    private final ClaimName name;
    private final String value;

    public Claim(ClaimName name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name.val();
    }

    public ClaimName name() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
