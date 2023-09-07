package com.gracerun.security.authentication.core;

import com.gracerun.security.authentication.constant.AuthCode;
import com.gracerun.security.authentication.util.ResponseUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.session.Session;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
public class WebSecurityConfig<S extends Session> extends WebSecurityConfigurerAdapter {


    private static final AntPathRequestMatcher WEBSOCKET_PATH_MATCHER = new AntPathRequestMatcher("/websocket/**");

    @Resource
    private LoginAuthenticationProvider loginAuthenticationProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().disable()
                .headers().disable()
                .authenticationProvider(loginAuthenticationProvider)
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> {
                    if (WEBSOCKET_PATH_MATCHER.matches(request)) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    }
                    ResponseUtil.writeJson(response, AuthCode.NOT_LOGIN, AuthCode.NOT_LOGIN_CN, null);
                })
                .and()
                .sessionManagement()
                .sessionFixation()
                .newSession()
                .sessionCreationPolicy(SessionCreationPolicy.NEVER)
                .maximumSessions(1)
                .expiredSessionStrategy((event) ->
                        ResponseUtil.writeJson(event.getResponse(), AuthCode.EXPIRED_LOGIN, AuthCode.EXPIRED_LOGIN_CN)
                )
                .and()
                .and()
                .logout().logoutSuccessHandler((request, response, authentication) ->
                        ResponseUtil.writeJson(response, AuthCode.SUCCESS, AuthCode.SUCCESS_CN)
                )
                .and()
                .addFilterAt(loginAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public LoginAuthenticationFilter loginAuthenticationFilter() throws Exception {
        LoginAuthenticationFilter filter = new LoginAuthenticationFilter();
        filter.setAuthenticationSuccessHandler((request, response, authentication) -> {
                    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
                    if (csrfToken != null) {
                        response.setHeader(csrfToken.getHeaderName(), csrfToken.getToken());
                    }
                    ResponseUtil.writeJson(response, AuthCode.SUCCESS, AuthCode.SUCCESS_CN, null);
                    HttpSession session = request.getSession(false);
                    if (session == null) {
                        return;
                    }
                    session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
                }
        );
        filter.setAuthenticationFailureHandler((request, response, exception) ->
                ResponseUtil.writeJson(response, AuthCode.BIZ_ERR, exception.getMessage(), null)
        );
        return filter;
    }

}
