package org.badun.jwtdemo.service.security.token;

/**
 * Created by Artsiom Badun.
 */
public class ClaimException extends RuntimeException {

    public ClaimException(String message) {
        super(message);
    }
}
