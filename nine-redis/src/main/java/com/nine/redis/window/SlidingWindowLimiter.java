package com.nine.redis.window;

import com.nine.common.ex.ServiceException;
import com.nine.common.utils.resources.ResourcesUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

/**
 * 滑动窗口限流
 *
 * @author fan
 */
public class SlidingWindowLimiter {

    private final StringRedisTemplate stringRedisTemplate;
    // 最大请求数量
    private Integer maxCount = 10;
    // 窗口大小（秒）
    private Integer windowSize = 20;

    public SlidingWindowLimiter(StringRedisTemplate stringRedisTemplate,
                                Integer maxCount, Integer windowSize) {
        this.stringRedisTemplate = Optional.ofNullable(stringRedisTemplate)
                .orElseThrow(() -> new ServiceException("stringRedisTemplate 不能为空"));
        Optional.ofNullable(maxCount).ifPresent(m -> this.maxCount = m);
        Optional.ofNullable(windowSize).ifPresent(m -> this.windowSize = m);
    }

    // 滑动窗口脚本 sliding-window.lua
    private static final RedisScript<Long> REDIS_SCRIPT;

    static {
        String lua = ResourcesUtil.readLineAsString("lua", "sliding_window.lua");
        REDIS_SCRIPT = new DefaultRedisScript<>(Objects.requireNonNull(lua), Long.class);
    }

    // 前缀
    public static final String PREFIX = "sliding_window:";

    public static String getRealKey(String key) {
        return PREFIX + key;
    }

    /**
     * 判断是否允许
     * 原子化操作，为减少网络开销，确保计数和过期操作的原子性
     *
     * @return true:允许访问 false:禁止访问
     */
    public boolean tryAcquire(String key) {
        if (!StringUtils.hasText(key)) {
            throw new ServiceException("SlidingWindowLimiter key 不能为空，key = " + key);
        }
        Long res = stringRedisTemplate.execute(
                REDIS_SCRIPT,
                Collections.singletonList(getRealKey(key)),
                String.valueOf(maxCount),
                String.valueOf(windowSize),
                String.valueOf(System.currentTimeMillis() / 1000));
        return res != null && res == 1L;
    }

}

