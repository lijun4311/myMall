package com.mall.util;

import com.mall.common.RedisPool;
import com.mall.common.consts.propertiesconsts.RedisConsts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.ShardedJedis;

/**
 * @Author lijun
 * @Date 2020-04-28 23:04
 * @Description Jedis分布式redis缓存工具类
 * @Since version-1.0
 */

public class RedisPoolUtil {
    protected final static Logger log = LoggerFactory.getLogger("AsyncLogger");

    /**
     * key设置过期时间
     *
     * @param key    键值
     * @param exTime 过期时间
     * @return 1 成功 0 失败
     */
    public static Long expire(String key, int exTime) {
        ShardedJedis jedis = null;
        Long result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.expire(key, exTime);
        } catch (Exception e) {
            log.error("expire key:{} error", key, e);
            return result;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return result;
    }


    /**
     * 设置key对应字符串value，并且设置key在给定的seconds时间之后超时过期
     *
     * @param key    键
     * @param exTime 过期时间
     * @param value  值
     * @return 状态回复
     */
    public static String setEx(String key, int exTime, String value) {
        ShardedJedis jedis = null;
        String result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.setex(key, exTime, value);
            jedis.close();
        } catch (Exception e) {
            log.error("setex key:{} value:{} error", key, value, e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return result;
    }

    public static String setEx(String key, String value) {
        ShardedJedis jedis = null;
        String result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.setex(key, RedisConsts.REDIS_SESSION_EXTIME, value);
            jedis.close();
        } catch (Exception e) {
            log.error("setex key:{} value:{} error", key, value, e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return result;
    }

    /**
     * @param key   键
     * @param value 值
     * @return 状态回复
     */
    public static String set(String key, String value) {
        ShardedJedis jedis = null;
        String result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.set(key, value);
        } catch (Exception e) {
            log.error("set key:{} value:{} error", key, value, e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return result;
    }


    /**
     * 自动将key对应到value并且返回原来key对应的value
     *
     * @param key   键
     * @param value 值
     * @return 返回之前的旧值，如果之前Key不存在将返回nil
     */
    public static String getSet(String key, String value) {
        ShardedJedis jedis = null;
        String result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.getSet(key, value);
        } catch (Exception e) {
            log.error("getset key:{} value:{} error", key, value, e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return result;
    }


    /**
     * 获得key对应value
     *
     * @param key 键
     * @return 值
     */
    public static String get(String key) {
        ShardedJedis jedis = null;
        String result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.get(key);
        } catch (Exception e) {
            log.error("get key:{} error", key, e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return result;
    }


    /**
     * 删除指定的一批keys，如果删除中的某些key不存在，则直接忽略
     *
     * @param key 键
     * @return 被删除的keys的数量
     */
    public static Long del(String key) {
        ShardedJedis jedis = null;
        Long result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.del(key);
        } catch (Exception e) {
            log.error("del key:{} error", key, e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return result;
    }

    /**
     * 将key设置值为value，如果key不存在，这种情况下等同SET命令。 当key存在时，什么也不做
     *
     * @param key   键
     * @param value 值
     * @return 1如果key被设置了   0如果key没有被设置了
     */
    public static Long setnx(String key, String value) {
        ShardedJedis jedis = null;
        Long result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.setnx(key, value);
        } catch (Exception e) {
            log.error("setnx key:{} value:{} error", key, value, e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return result;
    }

}
