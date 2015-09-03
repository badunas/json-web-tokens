package org.badun.jwtdemo.service.security;

import org.badun.jwtdemo.service.data.RedisService;
import org.badun.jwtdemo.service.security.token.Claim;
import org.badun.jwtdemo.service.security.token.Claims;
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
    public static final int DEFAULT_TOKEN_TTL_MINUTES = 60*24*30;
    public static final String DEFAULT_BL_PREFIX = "bl_";

    @Autowired
    private TokenProcessor tokenProcessor;
    @Autowired
    private RedisService redisService;

    public List<Claim> verifyToken(String token) throws TokenException {
        List<Claim> claims = tokenProcessor.parseToken(token);
        checkIfTokenExpired(claims);
        checkIfTokenInBlackList(claims);
        return claims;
    }

    private String getClaimValue(List<Claim> claims, Claims claimName) {
        return claims.stream()
                .filter(claim -> claim.name() == claimName)
                .findFirst()
                .get()
                .getValue();
    }

    private void checkIfTokenExpired(List<Claim> claims) {
        long expirationDate = Long.parseLong(getClaimValue(claims, Claims.EXPIRATION_DATE)) * 1000;
        if (expirationDate - System.currentTimeMillis() < 0) {
            throw new TokenException("Token expired.");
        }
    }

    private void checkIfTokenInBlackList(List<Claim> claims) {
        String username = getClaimValue(claims, Claims.USER_NAME);
        String tokenId = getClaimValue(claims, Claims.TOKEN_ID);
        if (redisService.isExists(DEFAULT_BL_PREFIX + username + "_" + tokenId)) {
            throw new TokenException("Token in blacklist.");
        }
    }
}
