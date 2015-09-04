package org.badun.jwtdemo.service.security.extention;

import org.badun.jwtdemo.service.security.TokenManager;
import org.badun.jwtdemo.service.security.token.ClaimName;
import org.badun.jwtdemo.service.security.token.Claims;
import org.badun.jwtdemo.service.security.token.TokenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * Created by Artsiom Badun.
 */
@Component
public class TokenAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private TokenManager tokenManager;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = authentication.getCredentials().toString();
        Claims claims;
        try {
            claims = tokenManager.verifyToken(token);
        } catch (TokenException e) {
            throw new InvalidTokenAuthenticationException("Authentication failed for token: " + token, e);
        }
        UserDetails userDetails = buildUserDetails(claims);
        return new TokenAuthentication(token, userDetails);
    }

    private UserDetails buildUserDetails(Claims claims) {
        String username = claims.getValue(ClaimName.USER_NAME);
        String role = claims.getValue(ClaimName.ROLE);
        return new User(
                username,
                "[PROTECTED]",
                Collections.singletonList(new SimpleGrantedAuthority(role))
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (TokenAuthentication.class.isAssignableFrom(authentication));
    }
}
