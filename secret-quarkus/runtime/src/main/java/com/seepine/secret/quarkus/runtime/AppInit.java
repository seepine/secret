package com.seepine.secret.quarkus.runtime;

import com.seepine.secret.AuthUtil;
import com.seepine.secret.interfaces.CacheService;
import com.seepine.secret.interfaces.TokenService;
import com.seepine.secret.properties.AuthProperties;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.event.Observes;

/**
 * 初始化
 *
 * @author seepine
 */
public class AppInit {
  /**
   * @param event event
   * @param authProperties authProperties
   * @param tokenService tokenService
   * @param cacheService cacheService
   */
  void startup(
      @Observes StartupEvent event,
      AuthProperties authProperties,
      TokenService tokenService,
      CacheService cacheService) {
    AuthUtil.init(authProperties, tokenService, cacheService);
  }
}
