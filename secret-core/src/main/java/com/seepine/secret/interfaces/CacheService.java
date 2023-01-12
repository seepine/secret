package com.seepine.secret.interfaces;

/**
 * @author seepine
 * @since 1.0.0
 */
public interface CacheService {
  /**
   * 设置缓存
   *
   * @param key key
   * @param value 值
   * @param delayMillisecond 有效期
   */
  void setCache(String key, Object value, long delayMillisecond);

  /**
   * 获取缓存
   *
   * @param key key
   * @return 值
   */
  Object getCache(String key);

  /**
   * 移除缓存
   *
   * @param key key
   */
  void remove(String key);
}
