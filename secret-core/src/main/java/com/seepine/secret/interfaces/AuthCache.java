package com.seepine.secret.interfaces;

import com.seepine.secret.entity.AuthUser;

/**
 * @author seepine
 * @since 0.0.6
 */
public interface AuthCache {
  /**
   * 获取缓存
   *
   * @param key key
   * @return 值
   */
  <T extends AuthUser> T get(String key);

  /**
   * 设置缓存
   *
   * @param key key
   * @param val val
   */
  <T extends AuthUser> void set(String key, T val);

  /**
   * 移除缓存
   *
   * @param key key
   */
  void remove(String key);
}
