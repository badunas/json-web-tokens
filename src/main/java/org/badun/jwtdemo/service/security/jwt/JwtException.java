package org.badun.jwtdemo.service.security.jwt;

/**
 * Created by Artsiom Badun.
 */
public class JwtException extends RuntimeException {

    public JwtException(String message) {
        super(message);
    }

    public JwtException(String message, Throwable cause) {
        super(message, cause);
    }
}
