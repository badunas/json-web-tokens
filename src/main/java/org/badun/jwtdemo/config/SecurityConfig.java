package org.badun.jwtdemo.config;

import org.badun.jwtdemo.service.security.ApiAuthenticationFilter;
import org.badun.jwtdemo.service.security.TokenAuthenticationProvider;
import org.badun.jwtdemo.service.security.token.Jose4JProcessor;
import org.badun.jwtdemo.service.security.token.TokenProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * Created by Artsiom Badun.
 */
@Configuration
@EnableWebMvcSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private Environment environment;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public TokenProcessor jwtService() {
        return new Jose4JProcessor(environment.getProperty(Env.JWT_SECRET.name()));
    }

    @Autowired
    private ApiAuthenticationFilter apiAuthenticationFilter;
    @Autowired
    private TokenAuthenticationProvider apiAuthenticationProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/api/**")
                    .authorizeRequests()
                    .anyRequest().authenticated()
                .and()
                    .anonymous().disable()
                    .httpBasic()
                .and()
                    .addFilterAfter(apiAuthenticationFilter, BasicAuthenticationFilter.class)
                    .authenticationProvider(apiAuthenticationProvider)
                    .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .csrf().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("user").password("111").roles("USER").and()
                .withUser("admin").password("111").roles("USER", "ADMIN");
    }
}
