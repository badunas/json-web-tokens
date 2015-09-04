package org.badun.jwtdemo.service.security.filter;

import org.badun.jwtdemo.service.security.extention.TokenAuthentication;
import org.badun.jwtdemo.service.security.token.Claim;
import org.badun.jwtdemo.service.security.token.ClaimName;
import org.badun.jwtdemo.service.security.token.Claims;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Artsiom Badun.
 */
public abstract class BaseTokenFilter implements Filter {
    public static final String DEFAULT_AUTH_TOKEN_HEADER = "X-Auth-Token";
    public static final String AUTH_TOKEN_GENERATED_HEADER = "auth-token-generated";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;
        doFilterInternal(request, response, filterChain);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    protected abstract void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException;

    protected Optional<String> getTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader(DEFAULT_AUTH_TOKEN_HEADER);
        if (StringUtils.isEmpty(header)) {
            return Optional.empty();
        }
        return Optional.of(header);
    }

    protected Authentication getExistingAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    protected boolean isUserAlreadyAuthenticated() {
        Authentication existingAuth = getExistingAuthentication();
        return existingAuth != null && existingAuth.isAuthenticated();
    }

    protected Claims collectClaims() {
        UserDetails userDetails = (UserDetails) getExistingAuthentication().getPrincipal();
        List<Claim> claimList = new ArrayList<>();
        claimList.add(new Claim(ClaimName.USER_NAME, userDetails.getUsername()));
        claimList.add(new Claim(ClaimName.ROLE, userDetails.getAuthorities().iterator().next().getAuthority()));
        return new Claims(claimList);
    }
}
