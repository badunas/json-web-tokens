package org.badun.jwtdemo.service.security.token;

import java.util.List;

/**
 * Created by Artsiom Badun.
 */
public interface TokenProcessor {

    String generateToken(Claims claims, int ttlMinutes);

    Claims parseToken(String token);
}
