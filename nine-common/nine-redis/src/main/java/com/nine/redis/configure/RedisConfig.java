package com.nine.redis.configure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * redis 配置
 *
 * @author fan
 * 2024/5/30
 */
@EnableCaching
@Configurable
public class RedisConfig implements CachingConfigurer {

    /**
     * 配置 RedisTemplate，用于操作 Redis 数据库。
     * 该方法创建并配置一个 RedisTemplate 实例，用于存储和检索对象。
     *
     * @param redisConnectionFactory Redis连接工厂，用于创建Redis连接。
     * @param objectMapper           Jackson 的 ObjectMapper 实例，用于序列化和反序列化 Java 对象。
     * @return 配置好的 RedisTemplate 实例，可用于存储和检索对象。
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory,
                                                       ObjectMapper objectMapper) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        // 使用 StringRedisSerializer 对键进行序列化，确保键的统一编码格式。
        // 使用 string 序列化器
        template.setKeySerializer(new StringRedisSerializer());
        // 对哈希键也使用StringRedisSerializer进行序列化，保持一致性。
        template.setHashKeySerializer(new StringRedisSerializer());

        // 使用 GenericJackson2JsonRedisSerializer 对值进行序列化，支持 Java 对象的存储和检索。
        // 使用 jackson 序列化器
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);
        template.setValueSerializer(genericJackson2JsonRedisSerializer);
        // 对哈希值同样使用该序列化器，确保哈希中的对象也能被正确序列化和反序列化。
        template.setHashValueSerializer(genericJackson2JsonRedisSerializer);

        return template;
    }


    /**
     * 配置 RedisCacheManager，用于管理缓存。
     * 可以使用类似注解 @Cacheable
     *
     * @param redisConnectionFactory 连接Redis的工厂，用于构建 RedisCacheManager。
     * @return RedisCacheManager实例，用于缓存管理。
     * <p>
     * 此方法配置了缓存的默认设置，包括缓存名称前缀、过期时间、键和值的序列化方式。
     * 它使用了非锁定的 RedisCacheWriter 来避免缓存争用，并设置了默认的缓存配置。
     */
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        // 配置缓存的基本设置，包括设置缓存名称前缀为 "redis-cache"，过期时间为 1 小时。
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .prefixCacheNameWith("redis-cache")
                // 设置缓存过期时间为 1 小时
                .entryTtl(Duration.ofHours(1))
                // 配置键的序列化方式为 StringRedisSerializer。
                .serializeKeysWith(RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                // 配置值的序列化方式为 GenericJackson2JsonRedisSerializer，用于将 Java 对象序列化为JSON存储。
                .serializeValuesWith(RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()));

        // 使用非锁定的 RedisCacheWriter 和配置的 redisCacheConfiguration 构建 RedisCacheManager。
        return RedisCacheManager.builder(RedisCacheWriter
                        .nonLockingRedisCacheWriter(redisConnectionFactory))
                .cacheDefaults(redisCacheConfiguration)
                .build();
    }


}
