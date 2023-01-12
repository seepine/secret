package com.seepine.secret.impl;

import com.seepine.secret.interfaces.CacheService;
import com.seepine.secret.util.CacheUtil;
/**
 * 默认缓存实现
 *
 * @author seepine
 * @since 1.0.0
 */
public class DefaultCacheServiceImpl implements CacheService {

  @Override
  public void setCache(String key, Object value, long delayMillisecond) {
    CacheUtil.set(key, value, delayMillisecond);
  }

  @Override
  public Object getCache(String key) {
    return CacheUtil.get(key);
  }

  @Override
  public void remove(String key) {
    CacheUtil.remove(key);
  }
}
