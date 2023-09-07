package com.gracerun.security.authorization.core;

import com.gracerun.security.authorization.bean.RoleUrl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 动态更新权限
 *
 * @author Tom
 * @version 1.0.0
 * @date 2023/9/6
 */
@Slf4j
@Component
public class DynamicSecurityMetadataSource implements FilterInvocationSecurityMetadataSource, MessageListener, ApplicationRunner {

    private FilterInvocationSecurityMetadataSource superMetadataSource;

    private final RolePrivilegeRepository rolePrivilegeRepository;

    private volatile Map<RequestMatcher, Collection<ConfigAttribute>> requestMap;

    public DynamicSecurityMetadataSource(@Autowired(required = false) RolePrivilegeRepository rolePrivilegeRepository) {
        this.rolePrivilegeRepository = rolePrivilegeRepository;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        final HttpServletRequest request = ((FilterInvocation) object).getRequest();
        for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : requestMap.entrySet()) {
            if (entry.getKey().matches(request)) {
                return entry.getValue();
            }
        }
        return superMetadataSource.getAttributes(object);
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        Set<ConfigAttribute> allAttributes = new HashSet<>();
        for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : requestMap.entrySet()) {
            allAttributes.addAll(entry.getValue());
        }
        return allAttributes;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return FilterInvocation.class.isAssignableFrom(aClass);
    }

    public void setSuperMetadataSource(FilterInvocationSecurityMetadataSource superMetadataSource) {
        this.superMetadataSource = superMetadataSource;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        reloadRequestMap();
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("handleMessage:{}", message);
        reloadRequestMap();
    }

    public void reloadRequestMap() {
        Map<RequestMatcher, Collection<ConfigAttribute>> newRequestMap = new HashMap<>(128);
        Map<String, List<String>> allRoleUrlPrivilege = this.findAllRoleUrlPrivilege();
        for (Map.Entry<String, List<String>> entry : allRoleUrlPrivilege.entrySet()) {
            newRequestMap.put(new AntPathRequestMatcher(entry.getKey(), null, true, new UrlPathHelper()),
                    entry.getValue().stream().map(s -> new SecurityConfig(s)).collect(Collectors.toList()));
        }
        requestMap = newRequestMap;
    }

    public Map<String, List<String>> findAllRoleUrlPrivilege() {
        Map<String, List<String>> urlMap = new HashMap<>(128);
        if (Objects.nonNull(rolePrivilegeRepository)) {
            List<RoleUrl> roleUrls = rolePrivilegeRepository.selectAllPrivilegeUrl();
            for (RoleUrl roleUrlVo : roleUrls) {
                if (!StringUtils.hasText(roleUrlVo.getUrl()) || !StringUtils.hasText(roleUrlVo.getRoleCode())) {
                    log.error("{} url or roleCode is empty ", roleUrlVo);
                    continue;
                }

                List<String> roleCodes = urlMap.get(roleUrlVo.getUrl());
                if (Objects.isNull(roleCodes)) {
                    roleCodes = new ArrayList<>();
                    urlMap.put(roleUrlVo.getUrl(), roleCodes);
                }
                roleCodes.add(roleUrlVo.getRoleCode());
                urlMap.put(roleUrlVo.getUrl(), roleCodes);
            }
        }
        urlMap.put("/selectUser", Arrays.asList("test1", "test2", "test3"));
        urlMap.put("/addUser", Arrays.asList("test2"));
        urlMap.put("/updateUser", Arrays.asList("test2"));
        urlMap.put("/deleteUser", Arrays.asList("test3"));
        return urlMap;
    }
}
