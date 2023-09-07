package com.gracerun.security.authentication.session;

import org.springframework.session.web.http.HttpSessionIdResolver;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 聚合sessionId解析器(支持从请求头获取token与cookie任意一个)
 *
 * @author Tom
 * @version 1.0.0
 * @date 2023/9/6
 */
public class MultiHttpSessionIdResolver implements HttpSessionIdResolver {

    private static final List<HttpSessionIdResolver> RESOLVERS = new ArrayList<>();

    public void add(HttpSessionIdResolver resolver) {
        if (Objects.nonNull(resolver)) {
            RESOLVERS.add(resolver);
        }
    }

    @Override
    public List<String> resolveSessionIds(HttpServletRequest request) {
        for (HttpSessionIdResolver e : RESOLVERS) {
            List<String> ids = e.resolveSessionIds(request);
            if (!CollectionUtils.isEmpty(ids)) {
                return ids;
            }
        }
        return Collections.emptyList();
    }

    @Override
    public void setSessionId(HttpServletRequest request, HttpServletResponse response, String sessionId) {
        for (HttpSessionIdResolver e : RESOLVERS) {
            e.setSessionId(request, response, sessionId);
        }
    }

    @Override
    public void expireSession(HttpServletRequest request, HttpServletResponse response) {
        for (HttpSessionIdResolver e : RESOLVERS) {
            e.expireSession(request, response);
        }
    }
}
