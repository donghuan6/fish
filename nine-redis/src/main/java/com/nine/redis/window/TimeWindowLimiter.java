package com.nine.redis.window;

import com.nine.common.ex.ServiceException;
import com.nine.common.utils.resources.ResourcesUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * 时间窗口
 *
 * @author fan
 */
public class TimeWindowLimiter {

    private final StringRedisTemplate stringRedisTemplate;

    // 每分钟5个
    private Integer maxPerMinute = 5;

    // 每小时5个
    private Integer maxPerHour = 20;

    // 每天50个
    private Integer maxPerDay = 50;


    public TimeWindowLimiter(StringRedisTemplate stringRedisTemplate,
                             Integer maxPerMinute,
                             Integer maxPerHour,
                             Integer maxPerDay) {
        this.stringRedisTemplate = Optional.ofNullable(stringRedisTemplate)
                .orElseThrow(() -> new ServiceException("stringRedisTemplate 不能为空"));
        Optional.ofNullable(maxPerMinute).ifPresent(m -> this.maxPerMinute = m);
        Optional.ofNullable(maxPerHour).ifPresent(h -> this.maxPerHour = h);
        Optional.ofNullable(maxPerDay).ifPresent(d -> this.maxPerDay = d);
    }

    // 滑动窗口脚本 time_window.lua
    private static final RedisScript<Long> REDIS_SCRIPT;

    static {
        String lua = ResourcesUtil.readLineAsString("lua", "time_window.lua");
        REDIS_SCRIPT = new DefaultRedisScript<>(Objects.requireNonNull(lua), Long.class);
    }

    // 前缀
    private static final String PREFIX = "time_window:";
    private static final String MINUTE = PREFIX + "minute:";
    private static final String HOUR = PREFIX + "hour:";
    private static final String DAY = PREFIX + "day:";

    /**
     * 判断是否允许
     * 原子化操作，为减少网络开销，确保计数和过期操作的原子性
     *
     * @return true:允许访问 false:禁止访问
     */
    public boolean tryAcquire(String key) {
        if (!StringUtils.hasText(key)) {
            throw new ServiceException("TimeWindowLimiter key 不能为空，key = " + key);
        }
        Long res = stringRedisTemplate.execute(
                REDIS_SCRIPT,
                Arrays.asList(MINUTE + key, HOUR + key, DAY + key),
                String.valueOf(maxPerMinute),
                String.valueOf(maxPerHour),
                String.valueOf(maxPerDay)
        );
        return res != null && res == 1L;
    }
}
