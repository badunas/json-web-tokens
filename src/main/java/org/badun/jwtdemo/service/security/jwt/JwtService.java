package org.badun.jwtdemo.service.security.jwt;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Created by Artsiom Badun.
 */
public interface JwtService {

    String generateToken(UserDetails userDetails, int ttlMinutes);

    UserDetails parseToken(String token);
}
