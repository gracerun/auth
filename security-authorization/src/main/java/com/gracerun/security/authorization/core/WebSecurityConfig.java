package com.gracerun.security.authorization.core;

import com.gracerun.security.authentication.constant.AuthCode;
import com.gracerun.security.authentication.util.ResponseUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class WebSecurityConfig<S extends Session> extends WebSecurityConfigurerAdapter {

    @Resource
    private DynamicSecurityMetadataSource customSecurityMetadataSource;

    @Resource
    private FindByIndexNameSessionRepository<S> sessionRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().disable()
                .headers().disable()
                .exceptionHandling()
                .accessDeniedHandler((request, response, accessDeniedException) ->
                        ResponseUtil.writeJson(response, AuthCode.UN_AUTHORIZED, AuthCode.UN_AUTHORIZED_CN)
                )
                .and()
                .authorizeRequests()
                .antMatchers("/favicon.ico", "/error", "/doc.html", "/webjars/**", "/swagger-resources", "/v3/**").permitAll()
                .and()
                .authorizeRequests((authorizeRequests) -> authorizeRequests
                        .anyRequest()
                        .authenticated()
                        .accessDecisionManager(accessDecisionManager())
                        .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                            @Override
                            public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                                customSecurityMetadataSource.setSuperMetadataSource(o.getSecurityMetadataSource());
                                o.setSecurityMetadataSource(customSecurityMetadataSource);
                                return o;
                            }
                        }));

    }

    private AccessDecisionManager accessDecisionManager() {
        List<AccessDecisionVoter<?>> decisionVoters = new ArrayList<>();
        decisionVoters.add(new WebExpressionVoter());
        RoleVoter roleVoter = new RoleVoter();
        roleVoter.setRolePrefix(grantedAuthorityDefaults().getRolePrefix());
        decisionVoters.add(roleVoter);
        return new AffirmativeBased(decisionVoters);
    }

    @Bean
    public SpringSessionBackedSessionRegistry<S> sessionRegistry() {
        return new SpringSessionBackedSessionRegistry<>(this.sessionRepository);
    }

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }

    @Bean
    public RequestCache requestCache() {
        HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
        requestCache.setCreateSessionAllowed(false);
        return requestCache;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


}
