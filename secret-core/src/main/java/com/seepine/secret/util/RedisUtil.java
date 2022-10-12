package com.seepine.secret.util;

import com.seepine.secret.interfaces.LockApply;
import com.seepine.secret.interfaces.LockApplyAs;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
/**
 * @author seepine
 */
public class RedisUtil {
  private static final RedisUtil REDIS_UTIL = new RedisUtil();
  RedissonClient redissonClient;

  public static void init(RedissonClient redissonClient) {
    REDIS_UTIL.redissonClient = redissonClient;
  }

  /**
   * redis实例是否关闭
   *
   * @return bool
   */
  public static boolean isShutdown() {
    return REDIS_UTIL.redissonClient == null || REDIS_UTIL.redissonClient.isShutdown();
  }

  private static final long DEFAULT_TIME = 7;
  private static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.DAYS;
  private static final String REDIS_LOCK_KEY = "secret_redisson_lock:";

  /**
   * 设置过期时间
   *
   * @param key key
   * @param duration 期限
   */
  public static void expire(String key, Duration duration) {
    REDIS_UTIL.redissonClient.getBucket(key).expire(duration);
  }

  /**
   * 设置过期时间
   *
   * @param key key
   * @param seconds 秒
   */
  public static void expire(String key, long seconds) {
    expire(key, Duration.ofSeconds(seconds));
  }
  /**
   * 获取缓存
   *
   * @param key key
   * @return value
   */
  @SuppressWarnings("unchecked")
  public static <T> T get(String key) {
    try {
      return (T) REDIS_UTIL.redissonClient.getBucket(key).get();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 获取字符串缓存
   *
   * @param key key
   * @return value
   */
  public static String getStr(String key) {
    Object value = get(key);
    return value == null ? null : String.valueOf(value);
  }

  /**
   * 获取整型缓存
   *
   * @param key key
   * @return value
   */
  public static Integer getInt(String key) {
    String value = getStr(key);
    return value == null ? null : Integer.valueOf(value);
  }

  /**
   * 获取长整形缓存
   *
   * @param key key
   * @return value
   */
  public static Long getLong(String key) {
    String value = getStr(key);
    return value == null ? null : Long.valueOf(value);
  }

  /**
   * 移除缓存
   *
   * @param key key
   */
  public static void remove(String key) {
    REDIS_UTIL.redissonClient.getBucket(key).delete();
  }

  /**
   * 判断缓存是否存在
   *
   * @param key key
   * @return boolean
   */
  public static boolean isExists(String key) {
    return REDIS_UTIL.redissonClient.getBucket(key).isExists();
  }

  /**
   * 设置缓存，默认无过期时间
   *
   * @param key key
   * @param value value
   */
  public static void set(String key, Object value) {
    REDIS_UTIL.redissonClient.getBucket(key).set(value);
  }

  /**
   * 设置缓存，使用默认7天过期时间
   *
   * @param key key
   * @param value value
   */
  public static void setWithDefaultExpire(String key, Object value) {
    REDIS_UTIL.redissonClient.getBucket(key).set(value, DEFAULT_TIME, DEFAULT_TIME_UNIT);
  }
  /**
   * 设置缓存
   *
   * @param key key
   * @param value value
   * @param expireTime 过期时间
   * @param timeUnit 时间单位
   */
  public static void set(String key, Object value, long expireTime, TimeUnit timeUnit) {
    REDIS_UTIL.redissonClient.getBucket(key).set(value, expireTime, timeUnit);
  }

  /**
   * 锁定运行
   *
   * @param key 锁值
   * @param apply 执行方法
   */
  public static void sync(Object key, LockApply apply) {
    RLock lock = REDIS_UTIL.redissonClient.getLock(REDIS_LOCK_KEY + key.toString());
    try {
      lock.lock();
      apply.run();
    } finally {
      lock.unlock();
    }
  }
  /**
   * 锁定运行，有返回值
   *
   * @param key 锁值
   * @param apply 执行方法
   * @param <T> 返回值类型
   * @return 返回值
   */
  public static <T> T sync(Object key, LockApplyAs<T> apply) {
    RLock lock = REDIS_UTIL.redissonClient.getLock(REDIS_LOCK_KEY + key.toString());
    try {
      lock.lock();
      return apply.run();
    } finally {
      lock.unlock();
    }
  }
}
