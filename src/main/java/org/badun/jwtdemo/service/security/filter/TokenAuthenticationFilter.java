package org.badun.jwtdemo.service.security.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.badun.jwtdemo.service.security.extention.TokenAuthenticationFailedEntryPoint;
import org.badun.jwtdemo.service.security.extention.TokenAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * Created by Artsiom Badun.
 */
@Component
public class TokenAuthenticationFilter extends BaseTokenFilter {
    private static final Log log = LogFactory.getLog(TokenAuthenticationFilter.class);

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenAuthenticationFailedEntryPoint entryPoint;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        Optional<String> token = getTokenFromRequest(request);
        if (isAuthenticationRequired(token)) {
            try {
                Authentication authentication = authenticationManager.authenticate(new TokenAuthentication(token.get()));
                log.debug("API user was authenticated: " + authentication);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (AuthenticationException e) {
                SecurityContextHolder.clearContext();
                entryPoint.commence(request, response, e);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private boolean isAuthenticationRequired(Optional<String> token) {
        Authentication existingAuth = getExistingAuthentication();
        if (!token.isPresent()) return false;
        if (!isUserAlreadyAuthenticated()) return true;
        if (existingAuth instanceof TokenAuthentication
                && !((TokenAuthentication) existingAuth).getToken().equals(token.get())) {
            return true;
        }
        if (existingAuth instanceof AnonymousAuthenticationToken) {
            return true;
        }
        return false;
    }
}
