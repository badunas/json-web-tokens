package org.badun.jwtdemo.service.security.extention;


import org.springframework.security.core.AuthenticationException;

/**
 * Created by Artsiom Badun.
 */
public class InvalidTokenAuthenticationException extends AuthenticationException {

    public InvalidTokenAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }
}
