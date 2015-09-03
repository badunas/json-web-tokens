package org.badun.jwtdemo.service.security.jwt;

import org.badun.jwtdemo.service.security.ExtendedUserDetails;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

/**
 * Created by Artsiom Badun.
 */
public interface JwtService {

    String generateToken(UserDetails userDetails, int ttlMinutes);

    UserDetails parseToken(String token);
}
