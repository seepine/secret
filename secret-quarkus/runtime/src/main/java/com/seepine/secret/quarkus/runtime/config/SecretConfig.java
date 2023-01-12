package com.seepine.secret.quarkus.runtime.config;

import com.seepine.secret.impl.DefaultCacheServiceImpl;
import com.seepine.secret.impl.DefaultTokenServiceImpl;
import com.seepine.secret.interfaces.CacheService;
import com.seepine.secret.interfaces.TokenService;
import com.seepine.secret.properties.AuthProperties;
import io.quarkus.arc.DefaultBean;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author seepine
 */
public class SecretConfig {
  @Inject AuthProperties authProperties;

  @Produces
  @DefaultBean
  @Singleton
  public TokenService tokenService() {
    return new DefaultTokenServiceImpl(authProperties);
  }

  @Produces
  @DefaultBean
  @Singleton
  public CacheService cacheService() {
    return new DefaultCacheServiceImpl();
  }
}
