package com.gracerun.security.authentication.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gracerun.security.authentication.bean.LoginRequest;
import com.gracerun.util.ValidationResult;
import com.gracerun.util.ValidationUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * 用户名密码登录
 *
 * @author Tom
 * @version 1.0.0
 * @date 2023/9/6
 */
@Slf4j
@Setter
public class LoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private boolean postOnly = true;
    private ObjectMapper mapper = new ObjectMapper();
    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/login",
            HttpMethod.POST.name());

    public LoginAuthenticationFilter() {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
    }

    public LoginAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        try (InputStream is = request.getInputStream()) {
            LoginRequest loginRequest = mapper.readValue(is, LoginRequest.class);
            validateParam(loginRequest);
            return this.getAuthenticationManager().authenticate(loginRequest);
        } catch (IllegalArgumentException e) {
            throw new AuthenticationServiceException(e.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new AuthenticationServiceException("登录参数错误");
        }
    }

    private <S> void validateParam(S request) {
        ValidationResult result = ValidationUtil.validateEntity(request);
        if (result.isHasErrors()) {
            StringBuilder sbd = new StringBuilder();
            result.getErrorMsg().forEach((k, v) -> sbd.append(k).append(v).append("&"));
            throw new AuthenticationServiceException(sbd.deleteCharAt(sbd.length() - 1).toString());
        }
    }

}
