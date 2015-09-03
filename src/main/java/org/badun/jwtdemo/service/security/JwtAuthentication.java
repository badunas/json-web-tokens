package org.badun.jwtdemo.service.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Created by Artsiom Badun.
 */
public class JwtAuthentication extends AbstractAuthenticationToken {
    private String jwtToken;
    private UserDetails userDetails;

    public JwtAuthentication(String jwtToken) {
        this(jwtToken, null);
    }

    public JwtAuthentication(String jwtToken, UserDetails userDetails) {
        super(null);
        this.jwtToken = jwtToken;
        this.userDetails = userDetails;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    @Override
    public Object getCredentials() {
        return jwtToken;
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
