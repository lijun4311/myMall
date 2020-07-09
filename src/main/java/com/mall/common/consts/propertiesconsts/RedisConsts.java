package com.mall.common.consts.propertiesconsts;

import com.mall.util.PropertiesUtil;

/**
 * @author lijun
 * @date 2020-06-16 19:35
 * @description redis 配置常量
 * @error
 * @since version-1.0
 */
public interface RedisConsts {
    /**
     * 存储缓存默认过期时间
     */
    Integer REDIS_SESSION_EXTIME = PropertiesUtil.getRedisConfig("REDIS_SESSION_EXTIME", 30 * 60);
    /**
     * 最大连接数
     */
    Integer MAX_TOTAL = Integer.parseInt(PropertiesUtil.getRedisConfig("MAX_TOTAL", "20"));
    /**
     * 在jedispool中最大的idle状态(空闲的)的jedis实例的个数
     */
    Integer MAX_IDLE = Integer.parseInt(PropertiesUtil.getRedisConfig("MAX_IDLE", "20"));
    /**
     * 在jedispool中最小的idle状态(空闲的)的jedis实例的个数
     */
    Integer MIN_IDLE = Integer.parseInt(PropertiesUtil.getRedisConfig("MIN_IDLE", "20"));
    /**
     * 在borrow一个jedis实例的时候，是否要进行验证操作，如果赋值true。则得到的jedis实例肯定是可以用的。
     */
    Boolean TEST_ON_BORROW = Boolean.parseBoolean(PropertiesUtil.getRedisConfig("TEST_BORROW", "true"));
    /**
     * 在return一个jedis实例的时候，是否要进行验证操作，如果赋值true。则放回jedispool的jedis实例肯定是可以用的。
     */
    Boolean TEST_ON_RETURN = Boolean.parseBoolean(PropertiesUtil.getRedisConfig("TEST_RETURN", "true"));
    /**
     * 缓存ip1
     */
    String REDIS_IP1 = PropertiesUtil.getRedisConfig("IP1");
    /**
     * 缓存端口 1
     */
    Integer REDIS_PORT1 = Integer.parseInt(PropertiesUtil.getRedisConfig("PORT1", "6379"));
    /**
     * 缓存密码1
     */
    String REDIS_PASS1 = PropertiesUtil.getRedisConfig("PASS1");
    /**
     * ip2
     */
    String REDIS_IP2 = PropertiesUtil.getRedisConfig("IP2");
    /**
     * port2
     */
    Integer REDIS_PORT2 = Integer.parseInt(PropertiesUtil.getRedisConfig("PORT2", "6380"));
    /**
     * pass2
     */
    String REDIS_PASS2 = PropertiesUtil.getRedisConfig("PASS2");
}
