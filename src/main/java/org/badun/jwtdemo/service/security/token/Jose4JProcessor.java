package org.badun.jwtdemo.service.security.token;

import org.badun.jwtdemo.util.KeyUtil;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Artsiom Badun.
 */
public class Jose4JProcessor implements TokenProcessor {
    private final String DEFAULT_SIGN_ALG = AlgorithmIdentifiers.HMAC_SHA256;
    private final Key secret;

    public Jose4JProcessor(String secretString) {
        secret = KeyUtil.decodeHmacKey(secretString);
    }

    @Override
    public String generateToken(Claims claims, int ttlMinutes) {
        try {
            JwtClaims jwtClaims = buildJwtClaims(claims, ttlMinutes);
            JsonWebSignature jws = buildJws(jwtClaims);
            return buildJwt(jws);
        } catch (JoseException e) {
            throw new TokenException("Failed to generate token for: " + claims.get(0).getValue(), e);
        }
    }

    private JwtClaims buildJwtClaims(Claims claims, int ttlMinutes) {
        JwtClaims jwtClaims = getDefaultClaims();
        jwtClaims.setExpirationTimeMinutesInTheFuture(ttlMinutes);
        for (Claim claim : claims.getClaimsExcept(ClaimName.TOKEN_ID)) {
            jwtClaims.setClaim(claim.getName(), claim.getValue());
        }
        return jwtClaims;
    }

    private JwtClaims getDefaultClaims() {
        JwtClaims claims = new JwtClaims();
        claims.setGeneratedJwtId();
        return claims;
    }

    private JsonWebSignature buildJws(JwtClaims claims) {
        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setKey(secret);
        jws.setAlgorithmHeaderValue(DEFAULT_SIGN_ALG);
        return jws;
    }

    private String buildJwt(JsonWebSignature jws) throws JoseException {
        return jws.getCompactSerialization();
    }

    @Override
    public Claims parseToken(String token) {
        try {
            JwtConsumer jwtConsumer = buildJwtConsumer();
            JwtClaims jwtClaims = fetchJwtClaims(jwtConsumer, token);
            return collectClaims(jwtClaims);
        } catch (InvalidJwtException e) {
            throw new TokenException("Failed to parse token: " + token, e);
        }
    }

    private JwtClaims fetchJwtClaims(JwtConsumer jwtConsumer, String token) throws InvalidJwtException {
        return jwtConsumer.processToClaims(token);
    }

    private Claims collectClaims(JwtClaims jwtClaims) {
        List<Claim> climeList = new ArrayList<>();
        jwtClaims.getClaimsMap().forEach((name, value) -> {
            ClaimName claimName = ClaimName.get(name);
            if (claimName != null) {
                climeList.add(new Claim(claimName, value.toString()));
            }
        });
        return new Claims(climeList);
    }

    private JwtConsumer buildJwtConsumer() {
        return new JwtConsumerBuilder()
                .setRequireExpirationTime()
                .setVerificationKey(secret)
                .build();
    }
}
