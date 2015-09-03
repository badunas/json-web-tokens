package org.badun.jwtdemo.service.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Created by Artsiom Badun.
 */
public class TokenAuthentication extends AbstractAuthenticationToken {
    private String token;
    private UserDetails userDetails;

    public TokenAuthentication(String token) {
        this(token, null);
    }

    public TokenAuthentication(String token, UserDetails userDetails) {
        super(null);
        this.token = token;
        this.userDetails = userDetails;
    }

    public String getToken() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return userDetails;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return ((User) userDetails).getAuthorities();
    }
}
