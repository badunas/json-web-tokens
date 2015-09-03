package org.badun.jwtdemo.service.security.jwt;

import org.badun.jwtdemo.service.security.ExtendedUserDetails;
import org.badun.jwtdemo.util.KeyUtil;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Collections;

/**
 * Created by Artsiom Badun.
 */
public class Jose4JService implements JwtService {
    private final Key secret;

    public Jose4JService(String secretString) {
        secret = KeyUtil.decodeHmacKey(secretString);
    }

    @Override
    public String generateToken(UserDetails userDetails, int ttlMinutes) {
        ExtendedUserDetails extendedUserDetails = (ExtendedUserDetails) userDetails;
        try {
            JwtClaims claims = buildJwtClaims(extendedUserDetails, ttlMinutes);
            JsonWebSignature jws = buildJws(claims);
            return buildJwt(jws);
        } catch (JoseException e) {
            throw new JwtException("Failed to generate token for user ID: " + extendedUserDetails.getUserId(), e);
        }
    }

    private JwtClaims buildJwtClaims(ExtendedUserDetails userDetails, int ttlMinutes) {
        JwtClaims claims = getDefaultClaims();
        claims.setExpirationTimeMinutesInTheFuture(ttlMinutes);
        claims.setClaim(Claim.USER_ID.val(), userDetails.getUserId());
        claims.setClaim(Claim.USER_NAME.val(), userDetails.getUsername());
        claims.setClaim(Claim.ROLE.val(), userDetails.getAuthorities().iterator().next());
        return claims;
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
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA256);
        return jws;
    }

    private String buildJwt(JsonWebSignature jws) throws JoseException {
        return jws.getCompactSerialization();
    }

    @Override
    public ExtendedUserDetails parseToken(String token) {
        try {
            JwtConsumer jwtConsumer = buildJwtConsumer();
            JwtClaims claims = fetchJwtClaims(jwtConsumer, token);
            return buildUserDetails(claims);
        } catch (InvalidJwtException e) {
            throw new JwtException("Failed to parse token: " + token, e);
        }
    }

    private JwtClaims fetchJwtClaims(JwtConsumer jwtConsumer, String token) throws InvalidJwtException {
        return jwtConsumer.processToClaims(token);
    }

    private ExtendedUserDetails buildUserDetails(JwtClaims claims) {
        return new ExtendedUserDetails(
                claims.getClaimValue(Claim.USER_NAME.val()).toString(),
                "[PROTECTED]",
                claims.getClaimValue(Claim.USER_ID.val()).toString(),
                Collections.singletonList(new SimpleGrantedAuthority(claims.getClaimValue(Claim.ROLE.val()).toString()))
        );
    }

    private JwtConsumer buildJwtConsumer() {
        return new JwtConsumerBuilder()
                .setRequireExpirationTime()
                .setVerificationKey(secret)
                .build();
    }
}
