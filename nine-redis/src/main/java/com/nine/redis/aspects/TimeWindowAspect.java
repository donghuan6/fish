package com.nine.redis.aspects;

import com.nine.redis.annotation.TimeWindow;
import com.nine.common.ex.ServiceException;
import com.nine.common.utils.spel.SpelExpressionUtil;
import com.nine.redis.window.TimeWindowLimiter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 时间窗口
 *
 * @author fan
 */
@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class TimeWindowAspect {

    private final StringRedisTemplate stringRedisTemplate;

    @Before(value = "@annotation(timeWindow)")
    public void before(JoinPoint point, TimeWindow timeWindow) {
        String key = SpelExpressionUtil.get(((MethodSignature) point.getSignature()).getMethod(), point.getArgs(),
                timeWindow.keyEL(), String.class);
        if (!new TimeWindowLimiter(stringRedisTemplate, timeWindow.maxPreMinute(), timeWindow.maxPreHour(), timeWindow.maxPreDay())
                .tryAcquire(key)) {
            throw new ServiceException("请求超过限制，稍后再试");
        }
    }
}
