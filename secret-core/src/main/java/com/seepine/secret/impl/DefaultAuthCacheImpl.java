package com.seepine.secret.impl;

import com.seepine.secret.entity.AuthUser;
import com.seepine.secret.interfaces.AuthCache;
import com.seepine.secret.properties.AuthProperties;
import com.seepine.secret.util.CacheUtil;

/**
 * @author seepine
 * @since 0.0.6
 */
public class DefaultAuthCacheImpl implements AuthCache {
  private final AuthProperties authProperties;

  public DefaultAuthCacheImpl(AuthProperties authProperties) {
    this.authProperties = authProperties;
  }

  @Override
  public <T extends AuthUser> T get(String key) {
    return CacheUtil.get(key);
  }

  @Override
  public <T extends AuthUser> void set(String key, T val) {
    CacheUtil.set(key, val, authProperties.getTimeout() * 1000);
  }

  @Override
  public void remove(String key) {
    CacheUtil.remove(key);
  }
}
