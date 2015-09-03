package org.badun.jwtdemo.service.security.token;

/**
 * Created by Artsiom Badun.
 */
public class TokenException extends RuntimeException {

    public TokenException(String message) {
        super(message);
    }

    public TokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
