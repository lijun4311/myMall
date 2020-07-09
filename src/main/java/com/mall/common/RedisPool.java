package com.mall.common;

import com.google.common.collect.Lists;
import com.mall.common.consts.propertiesconsts.RedisConsts;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.util.Hashing;
import redis.clients.jedis.util.Sharded;

import java.util.List;

/**
 * @Author lijun
 * @Date 2020-05-19 19:34
 * @Description redis缓存池
 * @Since version-1.0
 */
public class RedisPool {
    private static ShardedJedisPool shardedJedisPool;

    /**
     * 初始化
     */
    private static void initPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(RedisConsts.MAX_TOTAL);
        config.setMaxIdle(RedisConsts.MAX_IDLE);
        config.setMinIdle(RedisConsts.MIN_IDLE);
        config.setTestOnBorrow(RedisConsts.TEST_ON_BORROW);
        config.setTestOnReturn(RedisConsts.TEST_ON_RETURN);
        //连接耗尽的时候，是否阻塞，false会抛出异常，true阻塞直到超时。默认为true
        config.setBlockWhenExhausted(true);
        //pool = new JedisPool(config, redisIp, redisPort, 1000 * 2, redisPass);
        JedisShardInfo info1 = new JedisShardInfo(RedisConsts.REDIS_IP1, RedisConsts.REDIS_PORT1, 1000 * 2);
        info1.setPassword(RedisConsts.REDIS_PASS1);
        JedisShardInfo info2 = new JedisShardInfo(RedisConsts.REDIS_IP2, RedisConsts.REDIS_PORT2, 1000 * 2);
        info2.setPassword(RedisConsts.REDIS_PASS2);
        List<JedisShardInfo> list = Lists.newArrayList(info1, info2);
        shardedJedisPool = new ShardedJedisPool(config, list, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);
    }


    static {
        initPool();
    }

    /**
     * 获得连接
     *
     * @return 分片连接
     */
    public static ShardedJedis getJedis() {

        return shardedJedisPool.getResource();

    }

    /**
     * 关闭连接池
     */
    public static void close() {
        shardedJedisPool.close();

    }
}
