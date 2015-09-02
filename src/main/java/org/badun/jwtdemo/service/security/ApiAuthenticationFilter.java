package org.badun.jwtdemo.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private static final String AUTH_HEADER_NAME = "X-Auth-Token";

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ApiAuthenticationEntryPoint entryPoint;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;

        try {
            Optional<String> token = getTokenFromRequest(request);
            if (token.isPresent()) {
                onTokenProvided(token.get());
            } else {
                onTokenNotProvided(request);
            }
        } catch (AuthenticationException e) {
            SecurityContextHolder.clearContext();
            entryPoint.commence(request, response, e);
            return;
        }

        chain.doFilter(request, response);
    }

    private Optional<String> getTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader(AUTH_HEADER_NAME);
        if (StringUtils.isEmpty(header)) {
            return Optional.empty();
        }
        return Optional.of(header);
    }

    private void onTokenProvided(String token) {

    }

    private void onTokenNotProvided(HttpServletRequest request) {

    }

    @Override
    public void destroy() {
    }
}
