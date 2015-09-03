package org.badun.jwtdemo.service.security.token;

import java.util.List;

/**
 * Created by Artsiom Badun.
 */
public interface TokenProcessor {

    String generateToken(List<Claim> claims, int ttlMinutes);

    List<Claim> parseToken(String token);
}
