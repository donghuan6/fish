package com.nine.redis.lock;

import com.nine.common.ex.ServiceException;
import com.nine.common.utils.resources.ResourcesUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * redis 分布式锁
 *
 * @author fan
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RedisLockImpl {

    private final StringRedisTemplate stringRedisTemplate;

    // 加锁
    private static final RedisScript<String> LOCK_SCRIPT;
    // 释放锁
    private static final RedisScript<Long> UNLOCK_SCRIPT;

    static {
        String redisLockLua = ResourcesUtil.readLineAsString("lua", "redis_lock.lua");
        LOCK_SCRIPT = new DefaultRedisScript<>(Objects.requireNonNull(redisLockLua), String.class);

        String redisUnlockLua = ResourcesUtil.readLineAsString("lua", "redis_unlock.lua");
        UNLOCK_SCRIPT = new DefaultRedisScript<>(Objects.requireNonNull(redisUnlockLua), Long.class);
    }

    // 前缀
    public static final String REDIS_LOCK_PREFIX = "redis_lock:";

    public static String getRealRedisKey(String redisKey) {
        if (StringUtils.hasText(redisKey)) {
            return REDIS_LOCK_PREFIX + redisKey;
        }
        return null;
    }

    /**
     * 获取分布式锁，会持续 1s 尝试获取
     *
     * @param redisKey     redis key
     * @param redisValue   redis value
     * @param expireSecond 过期时间（秒）
     * @return null 证明获取锁失败
     */
    public String lock(String redisKey, String redisValue, int expireSecond) {
        if (!StringUtils.hasText(redisKey) || !StringUtils.hasText(redisValue) || expireSecond <= 0) {
            throw new ServiceException("【lock】参数错误");
        }
        // 获取锁的超时时间 1s
        long endSecond = System.currentTimeMillis() + 1000;
        while (System.currentTimeMillis() < endSecond) {
            String res = stringRedisTemplate.execute(
                    LOCK_SCRIPT,
                    Collections.singletonList(getRealRedisKey(redisKey)),
                    redisValue,
                    String.valueOf(expireSecond)
            );
            if (StringUtils.hasText(res)) {
                return res;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                log.error("获取 redis 锁失败，线程中断异常，ThreadName={}, redisKey={}, redisValue={}",
                        Thread.currentThread().getName(), redisKey, redisValue, e);
                Thread.currentThread().interrupt();
            }
        }
        return null;
    }

    /**
     * 释放锁
     *
     * @return true:成功
     */
    public boolean unlock(String redisKey, String redisValue) {
        if (!StringUtils.hasText(redisKey) || !StringUtils.hasText(redisValue)) {
            throw new ServiceException("【unlock】参数错误");
        }
        Long res = stringRedisTemplate.execute(UNLOCK_SCRIPT, Collections.singletonList(getRealRedisKey(redisKey)), redisValue);
        return Objects.nonNull(res) && res == 1L;
    }


}
