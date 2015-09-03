package org.badun.jwtdemo.service.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Created by Artsiom Badun.
 */
public class ExtendedUserDetails extends User {
    private String userId;

    public ExtendedUserDetails(String username,
                               String password,
                               String userId,
                               Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
