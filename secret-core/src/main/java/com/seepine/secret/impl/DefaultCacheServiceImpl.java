package com.seepine.secret.impl;

import com.seepine.secret.interfaces.CacheService;
import com.seepine.tool.cache.Cache;

/**
 * 默认缓存实现
 *
 * @author seepine
 * @since 1.0.0
 */
public class DefaultCacheServiceImpl implements CacheService {

  @Override
  public void setCache(String key, Object value, long delayMillisecond) {
    Cache.set(key, value, delayMillisecond);
  }

  @Override
  public Object getCache(String key) {
    return Cache.get(key);
  }

  @Override
  public void remove(String key) {
    Cache.remove(key);
  }
}
