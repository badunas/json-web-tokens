package org.badun.jwtdemo.service.security;

import org.badun.jwtdemo.service.security.jwt.JwtException;
import org.badun.jwtdemo.service.security.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Created by Artsiom Badun.
 */
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private JwtService jwtService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = authentication.getCredentials().toString();
        UserDetails userDetails;
        try {
            userDetails = (UserDetails) jwtService.parseToken(token);
        } catch (JwtException e) {
            throw new InvalidTokenAuthenticationException("Authentication failed for token: " + token, e);
        }
        return new JwtAuthentication(token, userDetails);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthentication.class.isAssignableFrom(authentication));
    }
}
