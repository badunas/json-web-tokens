package org.badun.jwtdemo.service.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.badun.jwtdemo.service.security.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * Created by Artsiom Badun.
 */
@Component
public class ApiAuthenticationFilter implements Filter {
    private static final Log log = LogFactory.getLog(ApiAuthenticationFilter.class);
    private static final String DEFAULT_AUTH_TOKEN_HEADER = "X-Auth-Token";
    private static final int DEFAULT_TOKEN_TTL_MINUTES = 60*24*30;
    private static final String AUTH_TOKEN_GENERATED_HEADER = "auth-token-Generated";

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ApiAuthenticationEntryPoint entryPoint;
    @Autowired
    private JwtService jwtService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;
        Optional<String> token = getTokenFromRequest(request);
        if (token.isPresent()) {
            try {
                onTokenProvided(token.get());
            } catch (AuthenticationException e) {
                SecurityContextHolder.clearContext();
                entryPoint.commence(request, response, e);
                return;
            }
        } else {
            onTokenNotProvided(request, response);
        }
        chain.doFilter(request, response);
    }

    private Optional<String> getTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader(DEFAULT_AUTH_TOKEN_HEADER);
        if (StringUtils.isEmpty(header)) {
            return Optional.empty();
        }
        return Optional.of(header);
    }

    private void onTokenProvided(String token) {
        if (authenticationIsNotRequired(token)) {
            return;
        }
        Authentication authentication = authenticationManager.authenticate(new JwtAuthentication(token));
        log.debug("API user was authenticated: " + authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private boolean authenticationIsNotRequired(String token) {
        Authentication existingAuth = getExistingAuthentication();
        if (isUserAlreadyAuthenticated(existingAuth)) return false;
        if (existingAuth instanceof JwtAuthentication && !((JwtAuthentication) existingAuth).getJwtToken().equals(token)) {
            return false;
        }
        if (existingAuth instanceof AnonymousAuthenticationToken) {
            return false;
        }
        return true;
    }

    private Authentication getExistingAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private boolean isUserAlreadyAuthenticated(Authentication existingAuth) {
        return existingAuth != null && existingAuth.isAuthenticated();
    }

    private void onTokenNotProvided(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = getExistingAuthentication();
        if (isUserAlreadyAuthenticated(authentication)) {
            return;
        }
        if (isNotTokenGenerationNeeded(request)) {
            return;
        }
        String token = jwtService.generateToken((UserDetails) authentication.getPrincipal(), DEFAULT_TOKEN_TTL_MINUTES);
        response.setHeader(DEFAULT_AUTH_TOKEN_HEADER, token);
    }

    private boolean isNotTokenGenerationNeeded(HttpServletRequest request) {
        return request.getAttribute(AUTH_TOKEN_GENERATED_HEADER) != null;
    }

    @Override
    public void destroy() {
    }
}
