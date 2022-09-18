package com.seepine.secret.interfaces;

import java.time.Duration;
/**
 * 缓存接口
 *
 * @author seepine
 */
public interface Cache {
  /**
   * 获取缓存
   *
   * @param key key
   * @return 值
   */
  Object get(String key);

  /**
   * 设置缓存
   *
   * @param key key
   * @param val val
   */
  void set(String key, Object val);

  /**
   * 移除缓存
   *
   * @param key key
   */
  void remove(String key);

  /**
   * 更新过期事件
   *
   * @param key key
   * @param duration 时长
   */
  void expire(String key, Duration duration);
}
