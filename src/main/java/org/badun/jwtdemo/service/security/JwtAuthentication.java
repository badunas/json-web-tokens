package org.badun.jwtdemo.service.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Created by Artsiom Badun.
 */
public class JwtAuthentication extends AbstractAuthenticationToken {
    private String jwtToken;
    private ExtendedUserDetails userDetails;

    public JwtAuthentication(String jwtToken) {
        this(jwtToken, null);
    }

    public JwtAuthentication(String jwtToken, ExtendedUserDetails userDetails) {
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
        return userDetails.getAuthorities();
    }
}
