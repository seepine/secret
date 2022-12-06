package com.seepine.secret.util;

import com.seepine.tool.util.ExpireCache;

import java.util.concurrent.TimeUnit;

/**
 * 缓存工具类
 *
 * @author seepine
 * @since 0.0.10
 */
public class CacheUtil {
  private static final CacheUtil CACHE_UTIL = new CacheUtil();
  ExpireCache<Object> expireCache = new ExpireCache<>();

  private CacheUtil() {}
  /**
   * 获取缓存
   *
   * @param key key
   * @return value
   */
  @SuppressWarnings("unchecked")
  public static <T> T get(String key) {
    if (RedisUtil.isShutdown()) {
      return (T) CACHE_UTIL.expireCache.get(key);
    }
    return RedisUtil.get(key);
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
    if (RedisUtil.isShutdown()) {
      CACHE_UTIL.expireCache.remove(key);
    } else {
      RedisUtil.remove(key);
    }
  }
  /**
   * 设置缓存，默认无过期时间
   *
   * @param key key
   * @param value value
   */
  public static void set(String key, Object value) {
    if (RedisUtil.isShutdown()) {
      CACHE_UTIL.expireCache.put(key, value);
    } else {
      RedisUtil.set(key, value);
    }
  }
  /**
   * 设置缓存，默认无过期时间
   *
   * @param key key
   * @param value value
   * @param delayMillisecond 过期时间(毫秒)
   */
  public static void set(String key, Object value, long delayMillisecond) {
    if (RedisUtil.isShutdown()) {
      CACHE_UTIL.expireCache.put(key, value, delayMillisecond);
    } else {
      RedisUtil.set(key, value, delayMillisecond, TimeUnit.MILLISECONDS);
    }
  }
}
