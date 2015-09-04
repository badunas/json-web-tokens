package org.badun.jwtdemo.service.security.filter;

import org.badun.jwtdemo.service.security.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Artsiom Badun.
 */
@Component
public class TokenGeneratorFilter extends BaseTokenFilter {

    @Autowired
    private TokenManager tokenManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (isTokenGenerationNeeded(request)) {
            String token = tokenManager.getNewToken(collectClaims());
            setTokenToResponse(request, response, token);
        }
        chain.doFilter(request, response);
    }

    private boolean isTokenGenerationNeeded(HttpServletRequest request) {
        return !getTokenFromRequest(request).isPresent()
                && isUserAlreadyAuthenticated()
                && request.getAttribute(AUTH_TOKEN_GENERATED_HEADER) == null;
    }

    private void setTokenToResponse(HttpServletRequest request, HttpServletResponse response, String token) {
        response.setHeader(DEFAULT_AUTH_TOKEN_HEADER, token);
        request.setAttribute(AUTH_TOKEN_GENERATED_HEADER, "");
    }
}
