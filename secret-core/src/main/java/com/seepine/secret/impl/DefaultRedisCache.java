package com.seepine.secret.impl;

import com.seepine.secret.interfaces.Cache;
import com.seepine.secret.util.RedisUtil;

import java.time.Duration;
/**
 * redis缓存
 *
 * @author seepine
 */
public class DefaultRedisCache implements Cache {
  @Override
  public Object get(String key) {
    return RedisUtil.get(key);
  }

  @Override
  public void set(String key, Object val) {
    RedisUtil.set(key, val);
  }

  @Override
  public void remove(String key) {
    RedisUtil.remove(key);
  }

  @Override
  public void expire(String key, Duration duration) {
    RedisUtil.expire(key, duration);
  }
}
