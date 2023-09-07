package com.gracerun.security.authentication.core;

import com.gracerun.security.authentication.constant.AuthCode;
import com.gracerun.security.authentication.bean.UserDetail;
import com.gracerun.security.authentication.util.ResponseUtil;
import com.gracerun.security.authentication.util.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 密码过期拦截
 *
 * @author adc
 * @version 1.0.0
 * @date 2020-09-16
 */
@Slf4j
public class ExpirePasswordFilter extends GenericFilterBean {

    private List<RequestMatcher> excludeRequestMatcher = new ArrayList<>();

    public ExpirePasswordFilter antMatchers(String... antPatterns) {
        for (String pattern : antPatterns) {
            excludeRequestMatcher.add(new AntPathRequestMatcher(pattern));
        }
        return this;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if (!CollectionUtils.isEmpty(excludeRequestMatcher)) {
            for (RequestMatcher antMapping : excludeRequestMatcher) {
                if (antMapping.matches(request)) {
                    chain.doFilter(request, response);
                    return;
                }
            }
        }

        UserDetail userContext = UserHolder.getCurrentUser();
        if (userContext != null && userContext.isAuthenticated() && userContext.isExpireStatus()) {
            ResponseUtil.writeJson(response, AuthCode.AUTH_EXPIRED, AuthCode.AUTH_EXPIRED_CN, null);
            return;
        }
        chain.doFilter(request, response);
    }

}
