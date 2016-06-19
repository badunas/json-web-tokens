package org.badun.jwtdemo.web;

import org.badun.jwtdemo.service.security.TokenManager;
import org.badun.jwtdemo.service.security.filter.BaseTokenFilter;
import org.badun.jwtdemo.web.model.HeartBeatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Artsiom Badun.
 */
@RestController
@RequestMapping(value = "/api")
public class ApiController {

    @Autowired
    private TokenManager tokenManager;

    @RequestMapping(value = "/token/refresh")
    public String refreshToken(@RequestHeader(BaseTokenFilter.DEFAULT_AUTH_TOKEN_HEADER) String token,
                               HttpServletResponse response) {
        String newToken = tokenManager.refreshToken(token);
        response.setHeader(BaseTokenFilter.DEFAULT_AUTH_TOKEN_HEADER, newToken);
        return "Token was refreshed.";
    }
}
