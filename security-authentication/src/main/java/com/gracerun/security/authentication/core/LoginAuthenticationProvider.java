package com.gracerun.security.authentication.core;

import com.gracerun.security.authentication.bean.LoginRequest;
import com.gracerun.security.authentication.bean.UserDetail;
import com.gracerun.security.authentication.constant.LoginTypeConstant;
import com.gracerun.security.authentication.constant.UserStatusConstant;
import com.gracerun.security.authentication.util.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Arrays;
import java.util.Objects;

/**
 * 自定义登录验证
 *
 * @author adc
 * @version 1.0.0
 * @date 2020-10-15
 */
@Slf4j
@Component
public class LoginAuthenticationProvider implements AuthenticationProvider {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        LoginRequest loginToken = (LoginRequest) authentication;
        UserDetail userDetail = selectLoginUser(loginToken);

        boolean matches;
        if (LoginTypeConstant.PASSWORD.equals(loginToken.getLoginType())) {
            matches = PasswordUtil.matches(loginToken.getPassword(), userDetail.getPassword());
        } else if (LoginTypeConstant.SMS_CODE.equals(loginToken.getLoginType())) {
            matches = false;
        } else {
            throw new AuthenticationServiceException("登录类型错误");
        }

        if (!matches) {
//            incrementLoginFailAttempts(userContext.getUserId());
            if (LoginTypeConstant.PASSWORD.equals(loginToken.getLoginType())) {
                throw new BadCredentialsException("账号或密码错误");
            } else if (LoginTypeConstant.SMS_CODE.equals(loginToken.getLoginType())) {
                throw new BadCredentialsException("短信验证码错误");
            }
        } else {
            loginToken.setAuthenticated(true);
//            userService.clearCache(userContext.getUserId());
        }
        return loginToken;
    }

    /**
     * 增加登录错误次数
     *
     * @param userId
     */
//    protected void incrementLoginFailAttempts(String userId) {
//        BoundValueOperations<String, String> valueOps = stringRedisTemplate.boundValueOps(RedisKeyConstant.LOGIN_FAIL_ATTEMPTS + userId);
//        long increment = valueOps.increment();
//        if (increment <= 1) {
//            valueOps.expire(12, TimeUnit.HOURS);
//        }
//        boolean locked = lockedUserCheck(userId, increment);
//        if (locked) {
//            assertUserUnLocked(userId);
//        }
//    }

    /**
     * 规则：
     * 连续错误6次锁定1分钟,
     * 连续错误7次锁定5分钟,
     * 连续错误8次锁定15分钟,
     * 连续错误9次锁定60分钟,
     * 连续错误10次永久锁定
     *
     * @author adc
     * @date 2020-08-14
     * @version 1.0.0
     */
//    public boolean lockedUserCheck(String userId, long count) {
//        log.info("user [{}] password error count [{}]", userId, count);
//        BoundValueOperations<String, String> valueOps = stringRedisTemplate.boundValueOps(RedisKeyConstant.USER_LOCKED + userId);
//        boolean locked = false;
//        if (count == 6) {
//            valueOps.set(UserStatusConstant.LOCKED, Duration.ofMinutes(1));
//            locked = true;
//        } else if (count == 7) {
//            valueOps.set(UserStatusConstant.LOCKED, Duration.ofMinutes(5));
//            locked = true;
//        } else if (count == 8) {
//            valueOps.set(UserStatusConstant.LOCKED, Duration.ofMinutes(15));
//            locked = true;
//        } else if (count == 9) {
//            valueOps.set(UserStatusConstant.LOCKED, Duration.ofMinutes(60));
//            locked = true;
//        } else if (count >= 10) {
//            User t = new User();
//            t.setStatus(UserStatusConstant.LOCKED);
//            LambdaQueryWrapper<User> query = new LambdaQueryWrapper<>();
//            query.eq(User::getUserId, userId);
//            userMapper.update(t, query);
//            userService.clearCache(userId);
//            locked = true;
//        }
//        return locked;
//    }
//
//    protected void assertUserUnLocked(String userId) {
//        BoundValueOperations<String, String> valueOps = stringRedisTemplate.boundValueOps(RedisKeyConstant.USER_LOCKED + userId);
//
//        String status = valueOps.get();
//        if (UserStatusConstant.LOCKED.equals(status)) {
//            Long ttl = valueOps.getExpire();
//            if (Objects.nonNull(ttl) && ttl.longValue() > 0) {
//                String timeMsg;
//                if (ttl / 60 > 0) {
//                    timeMsg = String.format("，请%s分钟后再输入一次", (ttl + (60 - 1)) / 60);
//                } else {
//                    timeMsg = String.format("，请%s秒后再输入一次", ttl);
//                }
//                throw new LockedException("账户已被锁定" + timeMsg);
//            } else {
//                throw new LockedException("账户已被锁定");
//            }
//        }
//    }

    @Override
    public boolean supports(Class<?> authentication) {
        return LoginRequest.class.isAssignableFrom(authentication);
    }

    public UserDetail selectLoginUser(LoginRequest loginToken) {
        UserDetail userContext = new UserDetail();
        userContext.setUserId("testId");
        userContext.setUsername("test");
        userContext.setPassword(PasswordUtil.encode("test"));
        userContext.setRoles(Arrays.asList("test4"));
        loginToken.setDetails(userContext);
        return userContext;
    }
}
