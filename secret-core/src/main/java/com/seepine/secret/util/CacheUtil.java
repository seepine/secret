package com.seepine.secret.util;

import com.seepine.tool.function.FunctionN;
import com.seepine.tool.util.ExpireCache;

/**
 * 内存缓存工具类
 *
 * @author seepine
 * @since 0.0.10
 */
public class CacheUtil {
  protected static final CacheUtil CACHE_UTIL = new CacheUtil();
  ExpireCache<Object> expireCache = new ExpireCache<>();

  protected CacheUtil() {}

  /**
   * 获取缓存
   *
   * @param key key
   * @return value
   */
  @SuppressWarnings("unchecked")
  public static <T> T get(String key) {
    return (T) CACHE_UTIL.expireCache.get(key);
  }
  /**
   * 获取缓存
   *
   * @param key key
   * @return value
   */
  @SuppressWarnings("unchecked")
  public static <T> T get(String key, FunctionN<Object> func, long delayMillisecond) {
    return Lock.sync(key, () -> (T) CACHE_UTIL.expireCache.get(key, func, delayMillisecond));
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
    CACHE_UTIL.expireCache.remove(key);
  }

  /**
   * 设置缓存，默认无过期时间
   *
   * @param key key
   * @param value value
   */
  public static void set(String key, Object value) {
    CACHE_UTIL.expireCache.put(key, value);
  }

  /**
   * 设置缓存
   *
   * @param key key
   * @param value value
   * @param delayMillisecond 过期时间(毫秒)
   */
  public static void set(String key, Object value, long delayMillisecond) {
    CACHE_UTIL.expireCache.put(key, value, delayMillisecond);
  }
}
