package org.badun.jwtdemo.service.security;

import org.badun.jwtdemo.service.security.token.Claim;
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
import java.util.List;

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
        List<Claim> claims;
        try {
            claims = tokenManager.verifyToken(token);
        } catch (TokenException e) {
            throw new InvalidTokenAuthenticationException("Authentication failed for token: " + token, e);
        }
        UserDetails userDetails = buildUserDetails(claims);
        return new TokenAuthentication(token, userDetails);
    }

    private UserDetails buildUserDetails(List<Claim> claims) {
        String username = null;
        String role = null;
        for (Claim claim : claims) {
            if (claim.name() == Claims.USER_NAME) {
                username = claim.getValue();
            } else if (claim.name() == Claims.ROLE) {
                role = claim.getValue();
            }
        }
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
