package com.gracerun.security.authentication.core;

import com.gracerun.log.annotation.Logging;
import com.gracerun.security.authentication.bean.LoginRequest;
import com.gracerun.security.authentication.bean.UserToken;
import com.gracerun.security.authentication.constant.LoginConstant;
import com.gracerun.security.authentication.util.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * 用户登录
 *
 * @author Tom
 * @version 1.0.0
 * @date 2023/9/7
 */
@Slf4j
@Logging
@Component
public class LoginAuthenticationProvider implements AuthenticationProvider {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        LoginRequest loginRequest = (LoginRequest) authentication;
        UserToken userToken = selectUserDetail(loginRequest);
        boolean matches;
        if (LoginConstant.PASSWORD.equals(loginRequest.getLoginType())) {
            matches = PasswordUtil.matches(loginRequest.getPassword(), userToken.getPassword());
        } else if (LoginConstant.SMS_CODE.equals(loginRequest.getLoginType())) {
            matches = false;
        } else {
            throw new AuthenticationServiceException("登录类型错误");
        }

        if (!matches) {
//            incrementLoginFailAttempts(userContext.getUserId());
            if (LoginConstant.PASSWORD.equals(loginRequest.getLoginType())) {
                throw new BadCredentialsException("账号或密码错误");
            } else if (LoginConstant.SMS_CODE.equals(loginRequest.getLoginType())) {
                throw new BadCredentialsException("短信验证码错误");
            }
        } else {
            userToken.setAuthenticated(true);
//            userService.clearCache(userContext.getUserId());
        }
        return userToken;
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

    public UserToken selectUserDetail(LoginRequest loginToken) {
        UserToken userToken = new UserToken(Arrays.asList(new SimpleGrantedAuthority("test4")));
        userToken.setUserId("userI1010101");
        userToken.setUsername("test");
        userToken.setPassword(PasswordUtil.encode("test"));
        return userToken;
    }

}
