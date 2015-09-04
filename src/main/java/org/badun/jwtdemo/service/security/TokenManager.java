package org.badun.jwtdemo.service.security;

import org.badun.jwtdemo.service.data.RedisService;
import org.badun.jwtdemo.service.security.token.ClaimName;
import org.badun.jwtdemo.service.security.token.Claims;
import org.badun.jwtdemo.service.security.token.TokenException;
import org.badun.jwtdemo.service.security.token.TokenProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

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

    public String getNewToken(Claims claims) {
        return tokenProcessor.generateToken(claims, DEFAULT_TOKEN_TTL_MINUTES);
    }

    public String buildBlackListKey(Claims claims) {
        String username = claims.getValue(ClaimName.USER_NAME);
        String tokenId = claims.getValue(ClaimName.TOKEN_ID);
        return DEFAULT_BL_PREFIX + username + "_" + tokenId;
    }

    public Claims verifyToken(String token) throws TokenException {
        Claims claims = tokenProcessor.parseToken(token);
        checkIfTokenExpired(claims);
        checkIfTokenInBlackList(claims);
        return claims;
    }

    private void checkIfTokenExpired(Claims claims) {
        long expirationDate = Long.parseLong(claims.getValue(ClaimName.EXPIRATION_DATE)) * 1000;
        if (expirationDate - System.currentTimeMillis() < 0) {
            throw new TokenException("Token expired.");
        }
    }

    private void checkIfTokenInBlackList(Claims claims) {
        if (redisService.isExists(buildBlackListKey(claims))) {
            throw new TokenException("Token in blacklist.");
        }
    }

    public String refreshToken(String oldToken) {
        Claims claims = tokenProcessor.parseToken(oldToken);
        sendTokenToBlackList(claims);
        return getNewToken(claims);
    }

    private void sendTokenToBlackList(Claims claims) {
        redisService.setValue(
                buildBlackListKey(claims),
                String.valueOf(System.currentTimeMillis()),
                DEFAULT_TOKEN_TTL_MINUTES, TimeUnit.MINUTES);
    }
}
