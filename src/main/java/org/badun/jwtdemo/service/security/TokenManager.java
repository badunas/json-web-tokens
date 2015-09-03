package org.badun.jwtdemo.service.security;

import org.badun.jwtdemo.service.security.token.Claim;
import org.badun.jwtdemo.service.security.token.TokenException;
import org.badun.jwtdemo.service.security.token.TokenProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Artsiom Badun.
 */
@Service
public class TokenManager {

    @Autowired
    private TokenProcessor tokenProcessor;

    public List<Claim> verifyToken(String token) throws TokenException {
        List<Claim> claims = tokenProcessor.parseToken(token);
        return claims;
    }
}
