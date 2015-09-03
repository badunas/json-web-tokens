package org.badun.jwtdemo.service.security.jwt;

/**
 * Created by Artsiom Badun.
 */
public enum Claim {
    USER_ID("uid"),
    USER_NAME("uname"),
    ROLE("role");

    Claim(String val) {
        this.val = val;
    }

    private String val;

    public String val() {
        return val;
    }
}
