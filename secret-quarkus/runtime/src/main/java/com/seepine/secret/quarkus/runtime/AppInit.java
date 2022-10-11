package com.seepine.secret.quarkus.runtime;

import com.seepine.secret.AuthUtil;
import com.seepine.secret.interfaces.AuthCache;
import com.seepine.secret.interfaces.AuthTokenGen;
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
   * @param authCache authCache
   * @param authTokenGen authTokenGen
   */
  void startup(
      @Observes StartupEvent event,
      AuthProperties authProperties,
      AuthCache authCache,
      AuthTokenGen authTokenGen) {
    AuthUtil.init(authProperties, authCache, authTokenGen);
  }
}
