package com.seepine.secret.impl;

import com.seepine.secret.entity.AuthUser;
import com.seepine.secret.interfaces.AuthCache;
import com.seepine.secret.properties.AuthProperties;
import com.seepine.secret.util.CurrentTimeMillis;
import com.seepine.secret.util.RedisUtil;

import java.util.HashMap;

/**
 * @author seepine
 * @since 0.0.6
 */
public class DefaultAuthCacheImpl implements AuthCache {
  private final boolean hasRedisson;
  private final AuthProperties authProperties;
  private final HashMap<String, AuthUser> spareDb;

  public DefaultAuthCacheImpl(AuthProperties authProperties) {
    this.hasRedisson = !RedisUtil.isShutdown();
    this.authProperties = authProperties;
    spareDb = new HashMap<>();
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T extends AuthUser> T get(String key) {
    if (hasRedisson) {
      return RedisUtil.get(key);
    }
    AuthUser user = spareDb.get(key);
    if (user != null) {
      // if now-lastRefreshTime > timeout
      if (CurrentTimeMillis.now() - user.getRefreshTime() > authProperties.getTimeout()) {
        remove(key);
        return null;
      }
    }
    return (T) user;
  }

  @Override
  public <T extends AuthUser> void set(String key, T val) {
    if (hasRedisson) {
      RedisUtil.set(key, val);
    } else {
      spareDb.put(key, val);
    }
  }

  @Override
  public void remove(String key) {
    if (hasRedisson) {
      RedisUtil.remove(key);
    } else {
      spareDb.remove(key);
    }
  }
}
