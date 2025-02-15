package com.nine.redis.aspects;

import cn.hutool.core.util.IdUtil;
import com.nine.redis.annotation.RedisLock;
import com.nine.common.ex.ServiceException;
import com.nine.common.utils.spel.SpelExpressionUtil;
import com.nine.redis.lock.RedisLockImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 方法级别的分布式锁注解
 * 不具备公平，锁续时
 *
 * @author fan
 */
@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class RedisLockAspect {

    private final RedisLockImpl redisLockImpl;

    /**
     * 环绕通知
     *
     * @param joinPoint
     * @param redisLock
     * @return
     * @throws Throwable
     */
    @Around("@annotation(redisLock)")
    public Object around(ProceedingJoinPoint joinPoint, RedisLock redisLock) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        String keyEL = redisLock.keyEL();
        String key;
        if (StringUtils.hasText(keyEL)) {
            key = SpelExpressionUtil.get(method, joinPoint.getArgs(), keyEL, String.class);
        } else {
            key = String.join(":", method.getName(), String.valueOf(Arrays.hashCode(joinPoint.getArgs())));
        }
        String value = IdUtil.fastSimpleUUID();
        if (Objects.isNull(redisLockImpl.lock(key, value, redisLock.timeOutSeconds()))) {
            throw new ServiceException("加锁失败，稍候重试");
        }
        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            log.error("RedisLockAspect error. redisLock={}, key={}, value={}", redisLock, key, value, e);
            throw new ServiceException(e);
        } finally {
            // 重试解锁
            retryUnlock(redisLock, key, value);
        }
    }

    /**
     * 重试解锁
     */
    private void retryUnlock(RedisLock redisLock, String key, String value) {
        int retryCount = 3;
        boolean unlock = false;
        while (retryCount-- > 0 && !unlock) {
            unlock = redisLockImpl.unlock(key, value);
            if (!unlock) {
                try {
                    // 延时等待，避免立即重试
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    // 恢复中断状态
                    Thread.currentThread().interrupt();
                }
            }
        }
        if (!unlock) {
            log.error("Failed to unlock after multiple attempts. redisLock={}, key={}, value={}", redisLock, key, value);
        }
    }
}
