package org.idea.livestream.framework.redis.starter.id;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * 使用Redis自定义自增ID生成器
 */
@Component
public class RedisSeqIdHelper {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 开始时间戳
     */
    public static final long BEGIN_TIMESTAMP = 1640995299L;// 2021年1月1日时间戳

    /**
     * 序列号的位数
     */
    public static final int COUNT_BITS = 32;

    @Value("${spring.application.name}")
    private String applicationName;

    /**
     * 1位符号位 + 31位时间戳 + 32位序列号
     *
     * @param keyPrefix
     * @return
     */
    public Long nextId(String keyPrefix) {
        // 1 生成时间戳
        LocalDateTime now = LocalDateTime.now();
        long nowSecond = now.toEpochSecond(ZoneOffset.UTC);
        long timestamp = nowSecond - BEGIN_TIMESTAMP;

        // 2 生成序列号
        // 2.1 获取当前日期，精确到天，每天一个key，方便统计
        String date = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        String seqKey = applicationName + ":icr:" + keyPrefix + ":" + date;
        // 2.2 自增长
        long count = redisTemplate.opsForValue().increment(seqKey, 1);
        redisTemplate.expire(seqKey, 1, TimeUnit.DAYS);
        // 拼接并返回
        return timestamp << COUNT_BITS | count;
    }
}