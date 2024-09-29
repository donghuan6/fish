package com.nine.redis.aspects;

import com.nine.redis.annotation.SlidingWindow;
import com.nine.common.ex.ServiceException;
import com.nine.common.utils.spel.SpelExpressionUtil;
import com.nine.redis.window.SlidingWindowLimiter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 滑动窗口
 *
 * @author fan
 */
@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class SlidingWindowAspect {

    private final StringRedisTemplate stringRedisTemplate;

    @Before(value = "@annotation(slidingWindow)")
    public void before(JoinPoint point, SlidingWindow slidingWindow) {
        String key = SpelExpressionUtil.get(((MethodSignature) point.getSignature()).getMethod(), point.getArgs(), slidingWindow.keyEL(), String.class);
        if (!new SlidingWindowLimiter(stringRedisTemplate, slidingWindow.maxCount(), slidingWindow.windowSize())
                .tryAcquire(key)) {
            throw new ServiceException("请求过于频繁，稍后再试");
        }
    }


}
